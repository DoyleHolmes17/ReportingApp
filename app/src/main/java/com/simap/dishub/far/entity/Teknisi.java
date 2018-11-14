package com.simap.dishub.far.entity;

/**
 * Created by Aviroez on 20/08/2015.
 */
public class Teknisi {
    private String status;
    private String message;
    private String id_user_teknis;
    private String nama_teknisi;

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

    public String getId_user_teknis() {
        return id_user_teknis;
    }

    public void setId_user_teknis(String id_user_teknis) {
        this.id_user_teknis = id_user_teknis;
    }

    public String getNama_teknisi() {
        return nama_teknisi;
    }

    public void setNama_teknisi(String nama_teknisi) {
        this.nama_teknisi = nama_teknisi;
    }

    @Override
    public String toString() {
        return nama_teknisi;
    }
}