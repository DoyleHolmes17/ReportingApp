package com.simap.dishub.far;

        import com.google.gson.annotations.SerializedName;

/**
 * Created by Denny on 11/09/2016.
 */

public class DataObject {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public DataObject(){}
    public DataObject(String name) {
        this.name = name;
//        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

