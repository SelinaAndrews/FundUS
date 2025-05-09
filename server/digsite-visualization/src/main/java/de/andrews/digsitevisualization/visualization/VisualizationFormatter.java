package de.andrews.digsitevisualization.visualization;

import de.andrews.digsitevisualization.SessionData;
import de.andrews.digsitevisualization.calculation.MeasurementGroupIdentifier;
import de.andrews.digsitevisualization.calculation.SurfaceCalculator;
import de.andrews.digsitevisualization.data.*;
import de.andrews.digsitevisualization.repository.Measurement;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VisualizationFormatter {

    Logger logger = LoggerFactory.getLogger(VisualizationFormatter.class);

    private SessionData sessionData;

    private final String topo = "TOPO";
    private String info = "";

    /** Create a Visualization object with which to send data to the visualization application. **/
    public Visualization formatVisualization(List<Measurement> originalMeasurements, SessionData sessionData) {
        this.sessionData = sessionData;

        HashMap<String, String> metaData = new HashMap<>();
        metaData.put("path", this.sessionData.getDatabase());
        metaData.put("table", this.sessionData.getTable());
        metaData.put("digsite", this.sessionData.getDigsite());
        metaData.put("year", this.sessionData.getYear());
        metaData.put("xLow", this.sessionData.getxLow());
        metaData.put("xHigh", this.sessionData.getxHigh());
        metaData.put("yLow", this.sessionData.getyLow());
        metaData.put("yHigh", this.sessionData.getyHigh());
        metaData.put("zLow", this.sessionData.getzLow());
        metaData.put("zHigh", this.sessionData.getzHigh());

        logger.info("mapping visualization to jsonobject");
        List<Measurement> measurements = limitAxes(originalMeasurements);

        info = "Fund- und Oberflächendaten korrekt abgerufen";


        logger.info("formatSurfaces");
        List<Surface> surfaces = formatSurfaces(measurements);
        logger.info("formatGroupFindings");
        List<GroupFinding> groupFindings = formatGroupFindings(measurements);

        List<SingularFinding> singularFindings = new ArrayList<>();

        logger.info("formatSingularFindings");
        for (Measurement measurement : measurements) {
            if (measurement.getEF() && !measurement.getTOPO()) {
                SingularFinding singularFinding = formatSingularFinding(measurement);
                singularFindings.add(singularFinding);
                measurement.setType(Measurement.Type.SINGLEFIND);
            }
        }

        Visualization visualization = new Visualization();
        visualization.setInfo(info);
        visualization.setSingles(singularFindings);
        visualization.setBuckets(groupFindings);
        visualization.setSurfaces(surfaces);
        visualization.setMetaData(metaData);


        logger.info("formatting all data finished");
        this.sessionData.setCurrentMeasurements(measurements);
        return visualization;
    }

    /** Calculate a profile surface from the points given. **/
    public Surface formatProfileSurface(Profile profile) throws NotEnoughPointsException {
        SurfaceCalculator surfaceCalculator = new SurfaceCalculator();

        Surface surface = new Surface();
        surface.setLayer(profile.getLayer());
        surface.setVertices(profile.getVertices());

        surface.setConnections(surfaceCalculator.calculateSurface(profile.getVertices(), true));

        return  surface;
    }

    /** Filter the measurements to only include measurements within the axis limits given. **/
    private List<Measurement> limitAxes(List<Measurement> originalMeasurements) {
        List<Measurement> limitedMeasurements = new ArrayList<>(originalMeasurements);
        limitedMeasurements.removeIf(m -> m.getX() == null);
        limitedMeasurements.removeIf(m -> m.getY() == null);
        limitedMeasurements.removeIf(m -> m.getZ() == null);
        if(!sessionData.getxLow().equals("")) {
            double xLow = Double.parseDouble(sessionData.getxLow().replace(",", "."));
            logger.info("Removing all measurements with x lower than " + xLow);
            limitedMeasurements.removeIf(m -> Double.parseDouble(m.getX().replace(",", ".")) < xLow);
        }
        if(!sessionData.getxHigh().equals("")) {
            double xHigh = Double.parseDouble(sessionData.getxHigh().replace(",", "."));
            logger.info("Removing all measurements with x higher than " + xHigh);
            limitedMeasurements.removeIf(m -> Double.parseDouble(m.getX().replace(",", ".")) > xHigh);
        }
        if(!sessionData.getyLow().equals("")) {
            double yLow = Double.parseDouble(sessionData.getyLow().replace(",", "."));
            logger.info("Removing all measurements with y lower than " + yLow);
            limitedMeasurements.removeIf(m -> Double.parseDouble(m.getY().replace(",", ".")) < yLow);
        }
        if(!sessionData.getyHigh().equals("")) {
            double yHigh = Double.parseDouble(sessionData.getyHigh().replace(",", "."));
            logger.info("Removing all measurements with y higher than " + yHigh);
            limitedMeasurements.removeIf(m -> Double.parseDouble(m.getY().replace(",", ".")) > yHigh);
        }
        if(!sessionData.getzLow().equals("")) {
            double zLow = Double.parseDouble(sessionData.getzLow().replace(",", "."));
            logger.info("Removing all measurements with z lower than " + zLow);
            limitedMeasurements.removeIf(m -> Double.parseDouble(m.getZ().replace(",", ".")) < zLow);
        }
        if(!sessionData.getzHigh().equals("")) {
            double zHigh = Double.parseDouble(sessionData.getzHigh().replace(",", "."));
            logger.info("Removing all measurements with z higher than " + zHigh);
            limitedMeasurements.removeIf(m -> Double.parseDouble(m.getZ().replace(",", ".")) > zHigh);
        }
        return limitedMeasurements;
    }

    /** Create a finding number for each single or group find that matches the following pattern:
     *  for single findings: [digsite][year of dig].[unit].[finding number]
     *  for group findings: [digsite][year of dig].[unit][sub unit].[finding number].[sub finding number]
     * **/
    public String formatFindingNumber(Measurement measurement, Measurement.Type type) {

        StringBuilder findingNumber = new StringBuilder();

        if (type == Measurement.Type.SINGLEFIND || type == Measurement.Type.SURFACE) {
            findingNumber.append(sessionData.getDigsite());
            findingNumber.append(sessionData.getYear());
            findingNumber.append(".");
            findingNumber.append(String.join(".",measurement.getFindingNumberParts()));

        } else if (type == Measurement.Type.GROUPFIND) {
            findingNumber.append(sessionData.getDigsite());
            findingNumber.append(sessionData.getYear());
            findingNumber.append(".");
            findingNumber.append(String.join(".",measurement.getGroupFindingNumberParts()));
        }
        String findingNumberStr = findingNumber.toString();
        measurement.setFindingNumber(findingNumberStr);
        return findingNumberStr;
    }

    /** Format the information for a single finding for the visualization application. **/
    private SingularFinding formatSingularFinding(Measurement measurement) {

        SingularFinding finding = new SingularFinding();

        double x = Double.parseDouble(measurement.getX().replace(",", "."));
        double y = Double.parseDouble(measurement.getY().replace(",", "."));
        double z = Double.parseDouble(measurement.getZ().replace(",", "."));

        finding.setVertex(new Vertex(x, y, z));
        finding.setLayer(getLayer(measurement.getGH()));

        finding.setGeologicalHorizon(measurement.getGH());
        finding.setIdentification(measurement.getBEST());
        finding.setBasicForm(measurement.getGF());
        finding.setDefinition(measurement.getDEF());
        finding.setComment(measurement.getBEMERK());
        finding.setWorked(measurement.getBEARBEITET());
        finding.setImage(""); // TODO: Implement image paths

        finding.setUnmappedData(measurement.getRemainingData());

        finding.setId(formatFindingNumber(measurement, Measurement.Type.SINGLEFIND));

        return finding;
    }

    /** Calculate the layer number from the geological horizon information from the database. **/
    private int getLayer(String geologicalHorizon) {

        if (geologicalHorizon != null) {
            if (geologicalHorizon.contains("OF") || geologicalHorizon.contains("of")) {
                // Set GH to 0 for OF = Oberflächenfund
                logger.info("Geological horizon " + geologicalHorizon + " has been interpreted as surface level finding and given the layer 0");
                return 0;
            } else if (geologicalHorizon.matches("^([^0-9]*)$")) {
                // Set GH to -1 for anything that is neither an OF nor contains a number
                logger.info("Geological horizon " + geologicalHorizon + " could not be interpreted as a number and has been given the layer -1");
                info = "Warnung: Mindestens ein Element wurde zu GH -1 zugeordnet, da der Eintrag GH nicht eindeutig war.";
                return -1;
            } else {
                // Set GH to the first number found in the field
                Pattern pattern = Pattern.compile("^([0-9]*)");
                Matcher matcher = pattern.matcher(geologicalHorizon);
                matcher.find();
                int layer = Integer.parseInt(matcher.group());
                //logger.info("Geological horizon " + geologicalHorizon + " has been parsed and identified as layer " + layer);
                return layer;
            }
        } else {
            logger.warn("Empty geological horizon found.");
            info = "Warnung: Mindestens ein Element wurde zu GH -1 zugeordnet, da der Eintrag GH nicht eindeutig war.";
            return -1;
        }

    }

    /** Format the information for all surfaces for the visualization application. **/
    private List<Surface> formatSurfaces(List<Measurement> allMeasurements) {
        List<Surface> surfaces = new ArrayList<>();

        //Filter the measurements to only select surface data
        List<Measurement> topoMeasurements = new ArrayList<>();
        for (Measurement measurement : allMeasurements) {
            if (measurement.getTOPO()) {
                measurement.setType(Measurement.Type.SURFACE);
                formatFindingNumber(measurement, Measurement.Type.SURFACE);
                topoMeasurements.add(measurement);
            }
        }

        Set<MeasurementGroupIdentifier> measurementGroupIdentifiers = getMeasurementGroupIdentifiers(topoMeasurements);

        SurfaceCalculator surfaceCalculator = new SurfaceCalculator();

        //For each group of surface measurements, calculate a new surface object
        for (MeasurementGroupIdentifier s : measurementGroupIdentifiers) {
            List<Vertex> pointList = new ArrayList<>();
            String geologicalHorizon = "";
            int index = 0;
            String surfaceNumber = sessionData.getDigsite() + sessionData.getYear() + "." + String.join(".", s.getBestId());
            for (Measurement m : topoMeasurements) {
                if (m.getBestId().equals(s.getBestId())) {
                    double x = Double.parseDouble(m.getX().replace(",", "."));
                    double y = Double.parseDouble(m.getY().replace(",", "."));
                    double z = Double.parseDouble(m.getZ().replace(",", "."));
                    pointList.add(new Vertex(index,x,y,z));
                    index++;
                    geologicalHorizon = m.getGH();
                }
            }
            if (pointList.size() >= 3) {
                Surface surface = new Surface();
                surface.setLayer(getLayer(geologicalHorizon));
                surface.setVertices(pointList);
                surface.setId(surfaceNumber);

                try {
                    surface.setConnections(surfaceCalculator.calculateSurface(pointList, false));
                } catch (NotEnoughPointsException e) {
                    e.printStackTrace();
                }

                surfaces.add(surface);
            } else {
                logger.warn("Measurement group with identification " + s.getBestId() + " contained less than three points and was not rendered.");
            }
        }

        return surfaces;
    }

    /** Format the information for all group findings for the visualization application. **/
    private List<GroupFinding> formatGroupFindings(List<Measurement> allMeasurements) {
        List<GroupFinding> groupFindings = new ArrayList<>();

        //Filter the measurements to exclude single finds and surfaces
        List<Measurement> groupMeasurements = new ArrayList<>();
        for (Measurement measurement : allMeasurements) {
            boolean singlefind = measurement.getEF();
            boolean topo = measurement.getTOPO();
            if (!singlefind && !topo) {
                groupMeasurements.add(measurement);
                measurement.setType(Measurement.Type.GROUPFIND);
            }
        }

        Set<MeasurementGroupIdentifier> measurementGroupIdentifiers = getMeasurementGroupIdentifiers(groupMeasurements);

        //For each group measurement create a new group finding objects with as many sub group findings as applicable
        for (MeasurementGroupIdentifier s : measurementGroupIdentifiers) {
            GroupFinding groupFinding = new GroupFinding();

            Vertex centerPosition = new Vertex();
            String layer = "";
            List<SubGroupFinding> subGroupFindings = new ArrayList<>();

            for (Measurement m : groupMeasurements) {
                if (m.getBestId().equals(s.getBestId())) {
                    layer = m.getGH();
                    SubGroupFinding subGroupFinding = new SubGroupFinding();
                    double x = Double.parseDouble(m.getX().replace(",", "."));
                    double y = Double.parseDouble(m.getY().replace(",", "."));
                    double z = Double.parseDouble(m.getZ().replace(",", "."));
                    centerPosition.setX(x);
                    centerPosition.setY(y);
                    centerPosition.setZ(z);

                    subGroupFinding.setId(formatFindingNumber(m, Measurement.Type.GROUPFIND));
                    subGroupFinding.setIdentification(m.getBEST());
                    subGroupFinding.setBasicForm(m.getGF());
                    subGroupFinding.setDefinition(m.getDEF());
                    subGroupFinding.setImage("");
                    subGroupFinding.setComment(m.getBEMERK());
                    subGroupFinding.setWorked(m.getBEARBEITET());
                    subGroupFinding.setGeologicalHorizon(layer);

                    subGroupFinding.setUnmappedData(m.getRemainingData());

                    subGroupFindings.add(subGroupFinding);
                }
            }

            groupFinding.setComposition(subGroupFindings);
            groupFinding.setLayer(getLayer(layer));
            groupFinding.setVertex(centerPosition);

            groupFindings.add(groupFinding);

        }

        return groupFindings;
    }

    /** Group measurements by creating identifiers made from unit and ID for group findings and surfaces. **/
    private Set<MeasurementGroupIdentifier> getMeasurementGroupIdentifiers(List<Measurement> measurements) {
        Set<MeasurementGroupIdentifier> measurementGroupIdentifiers = new HashSet<>();

        for (Measurement m : measurements) {
            boolean isRegistered = false;
            //Create a group identifier from unit and ID
            MeasurementGroupIdentifier identifier = new MeasurementGroupIdentifier(m.getBestId());
            for (MeasurementGroupIdentifier s : measurementGroupIdentifiers) {
                //Check if the identifier created from the measurement has already been registered
                if (s.equals(identifier)) {
                    isRegistered = true;
                    break;
                }
            }
            if (!isRegistered) {
                //Register a new identifier that has not been registered before
                measurementGroupIdentifiers.add(identifier);
            }
        }
        return measurementGroupIdentifiers;
    }

    /**
     * for testing purposes
     * @param sessionData
     */
    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }
}
