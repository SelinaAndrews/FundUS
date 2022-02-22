package de.andrews.digsitevisualization.repository;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import de.andrews.digsitevisualization.repository.MeasurementRepository.DataList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelDriver {
    private static final Logger logger = LoggerFactory.getLogger(ExcelDriver.class);

    public static DataList getData(String filePath) throws IOException, InvalidFormatException {
        logger.info("getData aufgerufen");
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        logger.info("workbook erstellt");
        Sheet sheet = workbook.getSheetAt(0);
        logger.info("sheet 0 geladen");

        List<HashMap<String,String>> dataList = new ArrayList<>();

        Iterator<Row> rowIterator = sheet.rowIterator();
        Row firstRow = null;
        HashMap<String, Integer> columnOccurance= new HashMap<>();
        ArrayList<String> metaData = new ArrayList<>();
        logger.info("starting iteration over workbook");
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            if (row.getRowNum()==0) {
                firstRow = row;
                firstRow.forEach(c -> {
                    metaData.add(c.getStringCellValue());
                    columnOccurance.put(c.getStringCellValue(),0);
                });
            } else if (firstRow != null) {
                HashMap<String,String> ms = new HashMap<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String str1 = firstRow.getCell(cell.getColumnIndex()).toString();
                    String str2 = cell.toString();
                    if (!str2.isEmpty()) {
                        ms.put(str1, str2);
                        columnOccurance.put(str1,columnOccurance.get(str1)+1);
                    }
                    //ms.put(str1, str2);
                }
                if (ms.size() > 0) {
                    dataList.add(ms);
                }
            }
        }
        columnOccurance.forEach((a, b) -> {
            if (b <= 0) {
                metaData.remove(a);
            }
        });
        logger.info("iteration über workbook abgeschlossen");
        return new DataList(dataList, metaData);
    }
}
