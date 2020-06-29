package de.andrews.digsitevisualization;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SessionData {

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
    private boolean preset;

    public void setAxisLimitations(String x1, String x2, String y1, String y2, String z1, String z2) throws IOException {
        try {
            if (!x1.isEmpty() && !x2.isEmpty()) {
                if (Double.parseDouble(x1.replace(",", ".")) <= Double.parseDouble(x2.replace(",", "."))) {
                    this.setxLow(x1);
                    this.setxHigh(x2);
                } else {
                    this.setxLow(x2);
                    this.setxHigh(x1);
                }
            } else {
                this.setxLow(x1);
                this.setxHigh(x2);
            }

            if (!y1.isEmpty() && !y2.isEmpty()) {
                if (Double.parseDouble(y1.replace(",", ".")) <= Double.parseDouble(y2.replace(",", "."))) {
                    this.setyLow(y1);
                    this.setyHigh(y2);
                } else {
                    this.setyLow(y2);
                    this.setyHigh(y1);
                }
            } else {
                this.setyLow(y1);
                this.setyHigh(y2);
            }

            if (!z1.isEmpty() && !z2.isEmpty()) {
                if (Double.parseDouble(z1.replace(",", ".")) <= Double.parseDouble(z2.replace(",", "."))) {
                    this.setzLow(z1);
                    this.setzHigh(z2);
                } else {
                    this.setzLow(z2);
                    this.setzHigh(z1);
                }
            } else {
                this.setyLow(y1);
                this.setyHigh(y2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Invalid axis limitations.");
        }
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
