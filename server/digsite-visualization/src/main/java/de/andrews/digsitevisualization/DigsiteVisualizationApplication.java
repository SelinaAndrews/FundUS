package de.andrews.digsitevisualization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class DigsiteVisualizationApplication {

	static Logger logger = LoggerFactory.getLogger(DigsiteVisualizationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DigsiteVisualizationApplication.class, args);

		//Open web interface in preferred browser
		if(Desktop.isDesktopSupported()){
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI("http://localhost:2020"));
				logger.info("Opened website via Desktop.");
			} catch (IOException | URISyntaxException e) {
				logger.error("Desktop was unable to open the website.");
				e.printStackTrace();
			}
		} else {
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:2020");
				logger.info("Opened website via Runtime.");
			} catch (IOException e) {
				logger.error("Runtime was unable to open the website.");
				e.printStackTrace();
			}
		}
	}

}
