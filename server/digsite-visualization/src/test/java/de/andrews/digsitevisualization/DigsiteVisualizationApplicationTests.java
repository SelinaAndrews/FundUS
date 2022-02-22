package de.andrews.digsitevisualization;
import de.andrews.digsitevisualization.repository.JDBCDriver;
import de.andrews.digsitevisualization.repository.MeasurementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
class DigsiteVisualizationApplicationTests {


	@Test
    @Disabled
	void drivers() throws Exception {
	    MeasurementRepository mR = new MeasurementRepository();
        MeasurementRepository.DataList csvTestData = mR.findAll("C:\\Users\\hdm\\Desktop\\FundUs\\testDBs\\klein.csv","klein", null);
        MeasurementRepository.DataList excelTestData = mR.findAll("C:\\Users\\hdm\\Desktop\\FundUs\\testDBs\\klein.xlsx","xlsx", null);
        Assertions.assertEquals(excelTestData.getDataList().size(), csvTestData.getDataList().size());
	}

	@Test
    void parseCsvUrl() {
	    String testUrl1 = "C:\\Users\\User\\DBs\\klein.csv";
        String result1 = JDBCDriver.extractFolderUrl(testUrl1);
        Assertions.assertEquals(result1, "C:\\Users\\User\\DBs");

        String testUrl2 = "C:\\Users\\User\\DBs\\not_a_csv.xlxs";
        String result2 = JDBCDriver.extractFolderUrl(testUrl2);
        Assertions.assertEquals(result2, result2);
    }
}
