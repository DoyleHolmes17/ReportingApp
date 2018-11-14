package com.simap.dishub.far.entity;

/**
 * Created by MACBOOK on 12/11/2017.
 */

public class Via {
    private String id_pelaporan_via;
    private String nama_pelaporan_via;
    private String status;
    private String message;

    public String getId_pelaporan_via() {
        return id_pelaporan_via;
    }

    public void setId_pelaporan_via(String id_pelaporan_via) {
        this.id_pelaporan_via = id_pelaporan_via;
    }

    public String getNama_pelaporan_via() {
        return nama_pelaporan_via;
    }

    public void setNama_pelaporan_via(String nama_pelaporan_via) {
        this.nama_pelaporan_via = nama_pelaporan_via;
    }

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


    @Override
    public String toString() {
        return nama_pelaporan_via;
    }
}
