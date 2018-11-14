package com.simap.dishub.far;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelReport {

    private ArrayList<Listreport> listreport = new ArrayList<Listreport>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The listreport
     */
    public ArrayList<Listreport> getListreport() {
        return listreport;
    }

    /**
     *
     * @param listreport
     * The listreport
     */
    public void setListreport(ArrayList<Listreport> listreport) {
        this.listreport = listreport;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}