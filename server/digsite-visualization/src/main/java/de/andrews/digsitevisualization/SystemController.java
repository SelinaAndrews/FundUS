package de.andrews.digsitevisualization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.andrews.digsitevisualization.data.Profile;
import de.andrews.digsitevisualization.data.Surface;
import de.andrews.digsitevisualization.repository.Measurement;
import de.andrews.digsitevisualization.repository.MeasurementRepository;
import de.andrews.digsitevisualization.visualization.Visualization;
import de.andrews.digsitevisualization.visualization.VisualizationFormatter;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class SystemController {

    Logger logger = LoggerFactory.getLogger(SystemController.class);

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

    @RequestMapping(value="/startup", method = RequestMethod.POST)
    public String startTool(@ModelAttribute("sessiondata") SessionData sessiondata) {
        this.sessionData = sessiondata;

        //Remove leading and trailing quotation marks from the database string
        if (sessiondata.getDatabase().startsWith("\"")) {
            sessiondata.setDatabase(sessiondata.getDatabase().substring(1));
        }
        if (sessiondata.getDatabase().endsWith("\"")) {
            sessiondata.setDatabase(sessiondata.getDatabase().substring(0,sessiondata.getDatabase().length()-1));
        }

        try {
            //Request all measurements from the database
            List<Measurement> measurements = measurementRepository.findAll(sessionData.getDatabase(), sessionData.getTable());
            System.out.println("Number of measurements: " + measurements.size());

            visualizationFormatter.formatVisualization(measurements, sessionData);

            //Get presets for axis limitations from the web surface
            if(!sessionData.getxHigh().isEmpty()
                    || !sessionData.getxLow().isEmpty()
                    || !sessionData.getyHigh().isEmpty()
                    || !sessionData.getyLow().isEmpty()
                    || !sessiondata.getzHigh().isEmpty()
                    || !sessiondata.getzLow().isEmpty())
            {
                sessiondata.setPreset(true);
                System.out.println(sessiondata.getxLow());
            } else {
                sessiondata.setPreset(false);
            }

            //Start the visualization software
            Process process = new ProcessBuilder("DigsiteVisualization.exe").start();

        } catch (SQLException e) {
            logger.error("Error in database connection.", e);
            e.printStackTrace();
            return "database-error";
        } catch (IOException e) {
            logger.error("Visualization executable could not be started.", e);
            e.printStackTrace();
            return "visualization-error";
        }
        return "running";
    }

    @RequestMapping(value="/shutdown", method = RequestMethod.POST)
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
            List<Measurement> measurements = measurementRepository.findAll(sessionData.getDatabase(), sessionData.getTable());

            Visualization visualization = visualizationFormatter.formatVisualization(measurements, sessionData);

            System.out.println(visualization.getSurfaces().size());

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(visualization);

            return new ResponseEntity<>(jsonString, HttpStatus.OK);
        } catch (SQLException e) {
            logger.error("Error in database connection.", e);
            e.printStackTrace();
            return new ResponseEntity<>("{ \"info\": \"Warnung: Fehler bei der Verbindung zur Datenbank!\"}", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (JsonProcessingException e) {
            logger.error("Error in parsing the JSON response.", e);
            e.printStackTrace();
            return new ResponseEntity<>("{ \"info\": \"Warnung: Fehler bei der JSON-Datenverarbeitung!\"}", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NumberFormatException e) {
            logger.error("Invalid data in database, could not parse coordinates to numbers.", e);
            e.printStackTrace();
            return new ResponseEntity<>("{ \"info\": \"Warnung: Koordinaten konnten nicht konvertiert werden! Bitte Eingaben und/oder Datenbank prüfen.\"}", HttpStatus.BAD_REQUEST);
        }
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

}
