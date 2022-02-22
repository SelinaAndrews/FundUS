package de.andrews.digsitevisualization.repository;

import net.ucanaccess.jdbc.UcanaccessSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import de.andrews.digsitevisualization.repository.MeasurementRepository.DataList;

public class JDBCDriver {
    static Logger logger = LoggerFactory.getLogger(JDBCDriver.class);

    private static DataList getData(String connectionUrl, String tableName, Properties props) throws SQLException {

        try (Connection conn =
            DriverManager.getConnection(connectionUrl, props)) {

            //Build and execute SQL query
            String sql = "SELECT * FROM " + tableName;
            PreparedStatement prep = conn.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();
            ResultSetMetaData rsMd = rs.getMetaData();
            ArrayList<String> columns = new ArrayList<>(rsMd.getColumnCount());
            HashMap<String, Integer> columnOccurance= new HashMap<>();
            for(int i = 1; i <= rsMd.getColumnCount(); i++){
                String columnName = rsMd.getColumnName(i);
                columns.add(columnName);
                columnOccurance.put(columnName,0);
            }
            List<HashMap<String,String>> dataList = new ArrayList<>();

            //Get all measurements from the database and add them to the list
            while(rs.next()){
                HashMap<String,String> row = new HashMap<>(columns.size());
                for(String col : columns) {
                    String value = "";
                    try {
                        if (!col.isEmpty()) {
                            value = rs.getObject(col) != null ? rs.getObject(col).toString() : "";
                        } else {
                            value = "";
                        }
                    } catch (SQLException sqlE) {
                        logger.error("Could not parse data: " + sqlE);
                    }
                    if(value != null && !value.equals("")) {
                        row.put(col, value);
                        columnOccurance.put(col,columnOccurance.get(col)+1);
                    }
                }
                dataList.add(row);
            }
            rs.close();
            prep.close();
            conn.close();
            columnOccurance.forEach((a, b) -> {
                if (b <= 0) {
                    columns.remove(a);
                }
            });
            return new DataList(dataList, columns);

        } catch (SQLException e) {
            logger.error("Could not retrieve data from database: " + e.getMessage(), e);
            e.printStackTrace();
            throw new SQLException("Could not establish a connection to the database.");
        }
    }

    public static DataList getACCDBData(String databaseUrl, String tableName) throws SQLException {

        String connectionUrl = "jdbc:ucanaccess://" + databaseUrl;

        Properties props = new Properties();
        return getData(connectionUrl, tableName, props);
    }
    public static DataList getCSVData(String databaseUrl, String separator) throws SQLException {
        String urlOfFolder = extractFolderUrl(databaseUrl);
        String tableName = extractFilename(databaseUrl);
        String connectionUrl = "jdbc:relique:csv:" + urlOfFolder;
        Properties props = new Properties();
        if (separator == null || separator.isEmpty()) {
            separator = ",";
        }
        props.put("separator", separator);
        return getData(connectionUrl, tableName, props);
    }

    public static String extractFolderUrl(String path) throws StringIndexOutOfBoundsException {
        try {
            int indexOfFileName = path.lastIndexOf("\\");
            return path.substring(0,indexOfFileName);
        } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new StringIndexOutOfBoundsException("There was a problem with the path'" + path + "'");
        }
    }

    public static String extractFilename(String path) {
        try {
            int indexOfFileName = path.lastIndexOf("\\");
            int indexOfFileSuffix = path.lastIndexOf(".");
            return path.substring(indexOfFileName+1, indexOfFileSuffix);
        } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new StringIndexOutOfBoundsException("There was a problem with the path'" + path + "'");
        }
    }
}
