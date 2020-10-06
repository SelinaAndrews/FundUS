package de.andrews.digsitevisualization.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MeasurementRepository {

    Logger logger = LoggerFactory.getLogger(MeasurementRepository.class);

    /**  **/
    public List<Measurement> findAll(String databaseUrl, String tableName) throws SQLException {

        //Build database connection URL
        String formattedDatabaseUrl = databaseUrl.replace("\\\\", "/");
        String connectionUrl = "jdbc:ucanaccess://" + formattedDatabaseUrl;

        //Connect to Access database
        try (Connection conn =
                     DriverManager.getConnection(connectionUrl)) {

            //Build and execute SQL query
            String sql = "SELECT * FROM " + tableName;
            PreparedStatement prep = conn.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();

            List<Measurement> measurementList = new ArrayList<>();

            //Get all measurements from the database and add them to the list
            while (rs.next()) {
                Measurement ms = new Measurement(
                        rs.getString("UNIT"),
                        rs.getLong("ID"),
                        rs.getLong("SUFFIX"),
                        rs.getString("X"),
                        rs.getString("Y"),
                        rs.getString("Z"),
                        rs.getString("GH"),
                        rs.getString("AH"),
                        rs.getString("BEST"),
                        rs.getString("GF"),
                        rs.getString("DEF"),
                        rs.getString("BEMERK"),
                        rs.getBoolean("BEARBEITET"),
                        rs.getBoolean("EF")
                );
                measurementList.add(ms);
            }

            return measurementList;

        } catch (SQLException e) {
            logger.error("Could not retrieve data from database: " + e.getMessage(), e);
            e.printStackTrace();
        }

        throw new SQLException("Could not establish a connection to the database.");
    }

}
