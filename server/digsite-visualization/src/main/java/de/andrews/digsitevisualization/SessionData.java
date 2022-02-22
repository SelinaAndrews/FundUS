package de.andrews.digsitevisualization;

import de.andrews.digsitevisualization.repository.Measurement;
import de.andrews.digsitevisualization.repository.MeasurementMapping;
import org.springframework.stereotype.Component;
import de.andrews.digsitevisualization.repository.MeasurementRepository.DataList;

import java.io.IOException;
import java.util.List;

@Component
public class SessionData {

    private List<Measurement> currentMeasurements = null;
    private DataList dataList;
    private MeasurementMapping mapping;

    private String database = "";
    private String table = "";
    private String digsite = "";
    private String year = "";
    /* Axis limits to display */
    private String xLow = "";
    private String xHigh = "";
    private String yLow = "";
    private String yHigh = "";
    private String zLow = "";
    private String zHigh = "";

    private String separator;

    private boolean preset; //Using axis limits from website - yes/no

    /** For each pair of limitations of an axis, check which one is higher. Set this as the high value of this axis and the other as the low value. **/
    public void setAxisLimitations(String x1, String x2, String y1, String y2, String z1, String z2) throws IOException {
        try {
            //Check if x values are both present
            if (!x1.isEmpty() && !x2.isEmpty()) {
                if (Double.parseDouble(x1.replace(",", ".")) <= Double.parseDouble(x2.replace(",", "."))) {
                    //x2 is the higher, x1 the lower value OR both values are the same
                    this.setxLow(x1);
                    this.setxHigh(x2);
                } else {
                    //x1 is the higher, x2 the lower value
                    this.setxLow(x2);
                    this.setxHigh(x1);
                }
            } else {
                this.setxLow(x1);
                this.setxHigh(x2);
            }
            //Check if y values are both present
            if (!y1.isEmpty() && !y2.isEmpty()) {
                if (Double.parseDouble(y1.replace(",", ".")) <= Double.parseDouble(y2.replace(",", "."))) {
                    //y2 is the higher, y1 the lower value OR both values are the same
                    this.setyLow(y1);
                    this.setyHigh(y2);
                } else {
                    //y1 is the higher, y2 the lower value
                    this.setyLow(y2);
                    this.setyHigh(y1);
                }
            } else {
                this.setyLow(y1);
                this.setyHigh(y2);
            }
            //Check if z values are both present
            if (!z1.isEmpty() && !z2.isEmpty()) {
                if (Double.parseDouble(z1.replace(",", ".")) <= Double.parseDouble(z2.replace(",", "."))) {
                    //z2 is the higher, z1 the lower value OR both values are the same
                    this.setzLow(z1);
                    this.setzHigh(z2);
                } else {
                    //z1 is the higher, z2 the lower value
                    this.setzLow(z2);
                    this.setzHigh(z1);
                }
            } else {
                this.setzLow(z1);
                this.setzHigh(z2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Invalid axis limitations.");
        }
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public MeasurementMapping getMapping() {
        return mapping;
    }

    public void setMapping(MeasurementMapping mapping) {
        this.mapping = mapping;
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public List<Measurement> getCurrentMeasurements() {
        return currentMeasurements;
    }

    public void setCurrentMeasurements(List<Measurement> currentMeasurements) {
        this.currentMeasurements = currentMeasurements;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDigsite() {
        return digsite;
    }

    public void setDigsite(String digsite) {
        this.digsite = digsite;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getxLow() {
        return xLow;
    }

    public void setxLow(String xLow) {
        this.xLow = xLow;
    }

    public String getxHigh() {
        return xHigh;
    }

    public void setxHigh(String xHigh) {
        this.xHigh = xHigh;
    }

    public String getyLow() {
        return yLow;
    }

    public void setyLow(String yLow) {
        this.yLow = yLow;
    }

    public String getyHigh() {
        return yHigh;
    }

    public void setyHigh(String yHigh) {
        this.yHigh = yHigh;
    }

    public String getzLow() {
        return zLow;
    }

    public void setzLow(String zLow) {
        this.zLow = zLow;
    }

    public String getzHigh() {
        return zHigh;
    }

    public void setzHigh(String zHigh) {
        this.zHigh = zHigh;
    }

    public boolean isPreset() {
        return preset;
    }

    public void setPreset(boolean preset) {
        this.preset = preset;
    }
}
