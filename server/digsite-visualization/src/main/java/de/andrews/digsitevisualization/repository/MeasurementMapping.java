package de.andrews.digsitevisualization.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Component
public class MeasurementMapping {

    String xCoord, yCoord, zCoord;

    List<String> findingNumberParts, groupFindingNumberParts;

    String GH, AH, GF, DEF, BEMERK, BEARBEITET;

    List<String> bestId, topoKeys;
    String best;

    List<String> EFKeys;
    String EFColumn;
    boolean efBool = false;
    boolean topoBool = false;

    List<String> notMapped = new ArrayList<>();

    public boolean isEfBool() {
        return efBool;
    }

    public void setEfBool(boolean efBool) {
        this.efBool = efBool;
    }

    public boolean isTopoBool() {
        return topoBool;
    }

    public void setTopoBool(boolean topoBool) {
        this.topoBool = topoBool;
    }


    public List<String> getEFKeys() {
        return EFKeys;
    }

    public void setEFKeys(List<String> EFKeys) {
        this.EFKeys = EFKeys;
    }

    public String getEFColumn() {
        return EFColumn;
    }

    public void setEFColumn(String EFColumn) {
        this.EFColumn = EFColumn;
    }

    public MeasurementMapping() {
    }

    public String getX() {
        return xCoord;
    }

    public void setX(String xCoord) {
        this.xCoord = xCoord;
    }

    public String getY() {
        return yCoord;
    }

    public void setY(String yCoord) {
        this.yCoord = yCoord;
    }

    public String getZ() {
        return zCoord;
    }

    public void setZ(String zCoord) {
        this.zCoord = zCoord;
    }

    public List<String> getFindingNumberParts() {
        return findingNumberParts;
    }

    public void setFindingNumberParts(List<String> findingNumberParts) {
        this.findingNumberParts = findingNumberParts;
    }

    public List<String> getGroupFindingNumberParts() {
        return groupFindingNumberParts;
    }

    public void setGroupFindingNumberParts(List<String> groupFindingNumberParts) {
        this.groupFindingNumberParts = groupFindingNumberParts;
    }

    public String getGH() {
        return GH;
    }

    public void setGH(String GH) {
        this.GH = GH;
    }

    public String getAH() {
        return AH;
    }

    public void setAH(String AH) {
        this.AH = AH;
    }

    public String getGF() {
        return GF;
    }

    public void setGF(String GF) {
        this.GF = GF;
    }

    public String getDEF() {
        return DEF;
    }

    public void setDEF(String DEF) {
        this.DEF = DEF;
    }

    public String getBEMERK() {
        return BEMERK;
    }

    public void setBEMERK(String BEMERK) {
        this.BEMERK = BEMERK;
    }

    public String getBEARBEITET() {
        return BEARBEITET;
    }

    public void setBEARBEITET(String BEARBEITET) {
        this.BEARBEITET = BEARBEITET;
    }

    public String getBEST() {
        return best;
    }

    public void setBEST(String best) {
        this.best = best;
    }

    public List<String> getTopoKeys() {
        return topoKeys;
    }

    public void setTopoKeys(List<String> topoKeys) {
        this.topoKeys = topoKeys;
    }

    public List<String> getBestId() {
        return bestId;
    }

    public void setBestId(List<String> bestId) {
        this.bestId = bestId;
    }

    public List<String> getNotMapped() {
        return notMapped;
    }

    public void setNotMapped(List<String> notMapped) {
        this.notMapped = notMapped;
    }

}
