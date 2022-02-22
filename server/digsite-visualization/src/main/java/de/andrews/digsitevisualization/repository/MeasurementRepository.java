package de.andrews.digsitevisualization.repository;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class MeasurementRepository {

    Logger logger = LoggerFactory.getLogger(MeasurementRepository.class);

    /**  **/
    public DataList findAll(String databaseUrl, String tableName, String separator) throws IOException, InvalidFormatException, SQLException {

        String database_type = extractDatabaseType(databaseUrl);
        String formattedDatabaseUrl = databaseUrl.replace("\\\\", "/");
        DataList dataList;

        switch (database_type) {
            case "xlsx":
                dataList = ExcelDriver.getData(formattedDatabaseUrl);
                break;
            case "csv":
                dataList = JDBCDriver.getCSVData(formattedDatabaseUrl, separator);
                break;
            case "accdb":
                dataList = JDBCDriver.getACCDBData(formattedDatabaseUrl, tableName);
                break;
            default:
                throw new IOException("File format not supported");
        }
        return dataList;
    }

    public List<Measurement> mapDataToMeasurement(List<HashMap<String,String>> dataList, MeasurementMapping mapping) {

        List<Measurement> msList = new ArrayList<>();
        for (HashMap<String,String> date : dataList) {

            Measurement ms = new Measurement(date, mapping);
            msList.add(ms);

        }
        return msList;
    }


    private String extractDatabaseType(String databaseUrl) throws IOException {
        String database_type = null;
        int index = databaseUrl.lastIndexOf(".");
        if (index >= 0) {
            database_type = databaseUrl.substring(index+1).toLowerCase();
        } else {
            throw new IOException("File format not supported");
        }
        return database_type;
    }
    public static class DataList {
        private List<HashMap<String,String>> dataList;
        private ArrayList<String> columns;
        public DataList(List<HashMap<String,String>> dataList, ArrayList<String> columns) {
            this.dataList = dataList;
            this.columns = columns;
        }

        public List<HashMap<String, String>> getDataList() {
            return dataList;
        }
        public ArrayList<String> getColumns() {
            return columns;
        }
    }
}
