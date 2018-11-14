package com.simap.dishub.far.entity;

/**
 * Created by MACBOOK on 26/10/2017.
 */

public class Group {
    private String status;
    private String message;
    private String id_group;
    private String nama_group;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_group() {
        return id_group;
    }

    public void setId_group(String id_group) {
        this.id_group = id_group;
    }

    public String getNama_group() {
        return nama_group;
    }

    public void setNama_group(String nama_group) {
        this.nama_group = nama_group;
    }

    @Override
    public String toString() {
        return nama_group;
    }
}
