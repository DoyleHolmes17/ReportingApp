package com.simap.dishub.far;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Denny on 11/09/2016.
 */

public class DataObject2 {
    @SerializedName("name")
    private String name;
    public DataObject2(){}
    public DataObject2(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

