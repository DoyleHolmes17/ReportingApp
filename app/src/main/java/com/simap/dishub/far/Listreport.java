
package com.simap.dishub.far;

import java.util.HashMap;
import java.util.Map;

public class Listreport {

    private String id;
    private String noteInfra;
    private String reportStatus;
    private String areaInfra;
    private String alamatInfra;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The noteInfra
     */
    public String getNoteInfra() {
        return noteInfra;
    }

    /**
     *
     * @param noteInfra
     * The note_infra
     */
    public void setNoteInfra(String noteInfra) {
        this.noteInfra = noteInfra;
    }

    /**
     *
     * @return
     * The reportStatus
     */
    public String getReportStatus() {
        return reportStatus;
    }

    /**
     *
     * @param reportStatus
     * The report_status
     */
    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    /**
     *
     * @return
     * The areaInfra
     */
    public String getAreaInfra() {
        return areaInfra;
    }

    /**
     *
     * @param areaInfra
     * The area_infra
     */
    public void setAreaInfra(String areaInfra) {
        this.areaInfra = areaInfra;
    }

    /**
     *
     * @return
     * The alamatInfra
     */
    public String getAlamatInfra() {
        return alamatInfra;
    }

    /**
     *
     * @param alamatInfra
     * The alamat_infra
     */
    public void setAlamatInfra(String alamatInfra) {
        this.alamatInfra = alamatInfra;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}