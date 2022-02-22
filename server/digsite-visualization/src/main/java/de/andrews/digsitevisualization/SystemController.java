package de.andrews.digsitevisualization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.andrews.digsitevisualization.calculation.query.QueryHandler;
import de.andrews.digsitevisualization.calculation.query.QueryParsingException;
import de.andrews.digsitevisualization.data.Profile;
import de.andrews.digsitevisualization.data.Surface;
import de.andrews.digsitevisualization.repository.Measurement;
import de.andrews.digsitevisualization.repository.MeasurementMapping;
import de.andrews.digsitevisualization.repository.MeasurementRepository;
import de.andrews.digsitevisualization.visualization.Visualization;
import de.andrews.digsitevisualization.visualization.VisualizationFormatter;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import de.andrews.digsitevisualization.repository.MeasurementRepository.DataList;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SystemController {

    Logger logger = LoggerFactory.getLogger(SystemController.class);
    Process process;

    @Autowired
    public SessionData sessionData;
    @Autowired
    public MeasurementRepository measurementRepository;
    @Autowired
    public VisualizationFormatter visualizationFormatter;

    @GetMapping("/")
    public String sessionDataForm(Model model) {
        model.addAttribute("sessiondata", new SessionData());
        return "index";
    }

    @RequestMapping(value="/mapping", method = RequestMethod.POST)
    public String startTool(@ModelAttribute("sessiondata") SessionData sessiondata,
                            @ModelAttribute MeasurementMapping mapping,
                            Model model) {
        model.addAttribute("mapping", mapping);
        model.addAttribute("mappingFilePath", "");
        this.sessionData = sessiondata;

        String database = sessiondata.getDatabase();
        this.sessionData.setDatabase(this.formatFilePathString(database));
        try {
            //Request all measurements from the database
            DataList dataList = measurementRepository.findAll(sessionData.getDatabase(), sessionData.getTable(), sessionData.getSeparator());
            sessionData.setDataList(dataList);
        } catch (InvalidFormatException | IOException e) {
            model.addAttribute("exception", e);
            logger.error("Visualization executable could not be started.", e);
            e.printStackTrace();
            return "error";
        } catch (Exception e) {
            model.addAttribute("exception", e);
            logger.error("Error in database connection.", e);
            e.printStackTrace();
            return "error";
        }
        return "mapping";
    }

    @RequestMapping(value = "/startup", method = RequestMethod.POST, params="action=downloadMapping")
    public ResponseEntity<Resource> downloadFileWithGet(
            @ModelAttribute("sessiondata") SessionData sessiondata,
            @ModelAttribute("mapping") MeasurementMapping mapping,
            @RequestParam(required = false, defaultValue = "") String mappingFilePath) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(mapping);

        ByteArrayResource resource = new ByteArrayResource(jsonString.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mapping.json");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @RequestMapping(value = "/startup", method = RequestMethod.POST, params="action=startup")
    public String map(@ModelAttribute("sessiondata") SessionData sessiondata,
                      @ModelAttribute("mapping") MeasurementMapping mapping,
                      @RequestParam(required = false, defaultValue = "") String mappingFilePath,
                      Model model) {



        DataList dataList = this.sessionData.getDataList();
        try {
            if (!mappingFilePath.isEmpty()) {
                String formattedPath  = this.formatFilePathString(mappingFilePath);
                File file = new File(formattedPath);
                ObjectMapper mapper = new ObjectMapper();
                mapping = mapper.readValue(file, new TypeReference<MeasurementMapping>() {});
            }
            this.sessionData.setMapping(mapping);
        } catch (IOException e) {
            model.addAttribute("exception", e);
            logger.error("There was something wrong with the mapping.", e);
            e.printStackTrace();
            return "error";
        }

        try {
            List<Measurement> measurements = measurementRepository.mapDataToMeasurement(dataList.getDataList(), this.sessionData.getMapping());

            System.out.println("Number of measurements: " + measurements.size());

            visualizationFormatter.formatVisualization(measurements, this.sessionData);

            //Get presets for axis limitations from the web surface
            if(!this.sessionData.getxHigh().isEmpty()
                    || !this.sessionData.getxLow().isEmpty()
                    || !this.sessionData.getyHigh().isEmpty()
                    || !this.sessionData.getyLow().isEmpty()
                    || !this.sessionData.getzHigh().isEmpty()
                    || !this.sessionData.getzLow().isEmpty())
            {
                this.sessionData.setPreset(true);
                System.out.println(this.sessionData.getxLow());
            } else {
                this.sessionData.setPreset(false);
            }

            //Start the visualization software
            process = new ProcessBuilder("DigsiteVisualization.exe").start();

        } catch (IOException e) {
            model.addAttribute("exception", e);
            logger.error("Visualization executable could not be started.", e);
            e.printStackTrace();
            return "error";
        } catch (Exception e) {
            model.addAttribute("exception", e);
            logger.error("Error in database connection.", e);
            e.printStackTrace();
            return "error";
        }

        model.addAttribute("runningSession", this.sessionData);
        return "running";
    }

    @RequestMapping(value="/shutdown", method = RequestMethod.GET)
    public void stopTool(@ModelAttribute("sessiondata") SessionData sessiondata) {
        System.exit(0);
    }

    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<String> dataRequest(@RequestParam(required = false, defaultValue = "") String x1,
                                              @RequestParam(required = false, defaultValue = "") String x2,
                                              @RequestParam(required = false, defaultValue = "") String y1,
                                              @RequestParam(required = false, defaultValue = "") String y2,
                                              @RequestParam(required = false, defaultValue = "") String z1,
                                              @RequestParam(required = false, defaultValue = "") String z2) {
        logger.info("requesting data");
        try {
            //For a startup with presets from the website ignore the default values
            if (sessionData.isPreset()) {
                sessionData.setPreset(false);
            } else {
                sessionData.setAxisLimitations(x1, x2, y1, y2, z1, z2);
            }
        } catch (IOException e) {
            logger.error("Invalid axis limitation input.", e);
            e.printStackTrace();
            return new ResponseEntity<>("{ \"info\": \"Warnung: Die eingegebenen Daten für die Achsenbeschränkung sind ungültig!\"}", HttpStatus.BAD_REQUEST);
        }

        try {
            logger.info("getting all data");
            DataList dataList = measurementRepository.findAll(sessionData.getDatabase(), sessionData.getTable(), sessionData.getSeparator());
            this.sessionData.setDataList(dataList);

            logger.info("mapping all data");
            MeasurementMapping mapping = sessionData.getMapping();
            List<Measurement> measurements = measurementRepository.mapDataToMeasurement(dataList.getDataList(), mapping);

            logger.info("formatting all data");
            Visualization visualization = visualizationFormatter.formatVisualization(measurements, sessionData);

            System.out.println(visualization.getSurfaces().size());

            logger.info("mapping visualization to jsonobject");
            String jsonString = visualization.generateJSON();

            return new ResponseEntity<>(jsonString, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error("Error in parsing the JSON response.", e);
            e.printStackTrace();
            return new ResponseEntity<>("{ \"info\": \"Warnung: Fehler bei der JSON-Datenverarbeitung!\"}", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NumberFormatException e) {
            logger.error("Invalid data in database, could not parse coordinates to numbers.", e);
            e.printStackTrace();
            return new ResponseEntity<>("{ \"info\": \"Warnung: Koordinaten konnten nicht konvertiert werden! Bitte Eingaben und/oder Datenbank prüfen.\"}", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error in database connection.", e);
            e.printStackTrace();
            return new ResponseEntity<>("{ \"info\": \"Warnung: Fehler bei der Verbindung zur Datenbank!\"}", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/ruleIDs/{base64EncodedQuery}")
    public ResponseEntity<String> getMeasurementsByFilter(@PathVariable String base64EncodedQuery) throws JsonProcessingException {
        if (sessionData.getDatabase().isEmpty() || sessionData.getCurrentMeasurements() == null) {
           return new ResponseEntity<>("{ \"info\": \"Warnung: Fehler bei der Verbindung zur Datenbank!\"}", HttpStatus.SERVICE_UNAVAILABLE);
        }
        String decodedQuery = new String(Base64.decodeBase64(base64EncodedQuery));
        logger.info("/data called with decoded parameter: " + decodedQuery);
        String[] singleIds;
        String[] groupIds;
        String[] surfaceIds;

        List<Measurement> singleMeasurements = sessionData.getCurrentMeasurements().stream()
                .filter(ms -> ms.getType() == Measurement.Type.SINGLEFIND)
                .collect(Collectors.toList());
        List<Measurement> groupMeasurements = sessionData.getCurrentMeasurements().stream()
                .filter(ms -> ms.getType() == Measurement.Type.GROUPFIND)
                .collect(Collectors.toList());
        List<Measurement> surfaceMeasurements = sessionData.getCurrentMeasurements().stream()
                .filter(ms -> ms.getType() == Measurement.Type.SURFACE)
                .collect(Collectors.toList());
        try {
            singleIds = QueryHandler.applyQuery(decodedQuery, singleMeasurements);
            groupIds = QueryHandler.applyQuery(decodedQuery, groupMeasurements);
            surfaceIds = QueryHandler.applyQuery(decodedQuery, surfaceMeasurements);
        } catch (QueryParsingException e) {
            logger.info(HttpStatus.BAD_REQUEST + ": An error occured calling the 'ruleIDs' endpoint: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String jsonString = toJSON(singleIds, groupIds, surfaceIds);

        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }

    private String toJSON(String[] singleIds, String[] groupIds, String[] surfaceIds) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode singleFindsJsonArray = mapper.createArrayNode();
        for (String singleId : singleIds) { singleFindsJsonArray.add(singleId); }
        ArrayNode groupFindsJsonArray = mapper.createArrayNode();
        for (String groupId : groupIds) { groupFindsJsonArray.add(groupId); }
        ArrayNode surfacesJsonArray = mapper.createArrayNode();
        Arrays.stream(surfaceIds).distinct().forEach(surfacesJsonArray::add);

        ObjectNode ids = mapper.createObjectNode();
        ids.set("singleFinds", singleFindsJsonArray);
        ids.set("groupFinds", groupFindsJsonArray);
        ids.set("surfaces", surfacesJsonArray);
        return mapper.writeValueAsString(ids);
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ResponseEntity<Surface> reconstructProfile(@RequestBody Profile profile) {
        try {
            Surface surface = visualizationFormatter.formatProfileSurface(profile);
            return new ResponseEntity<>(surface, HttpStatus.OK);
        } catch (NotEnoughPointsException e) {
            logger.error("Invalid number of points, requires at least three points.", e);
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Not enough points for reconstruction. Requires at least three points.", e);
        }
    }

    private String formatFilePathString(String database) {
        //Remove leading and trailing quotation marks from the database string
        if (database.startsWith("\"")) {
            database = (database.substring(1));
        }
        if (database.endsWith("\"")) {
            database = (database.substring(0,database.length()-1));
        }
        return database;
    }

}
