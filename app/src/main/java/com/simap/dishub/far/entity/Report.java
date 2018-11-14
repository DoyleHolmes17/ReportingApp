package com.simap.dishub.far.entity;

/**
 * Created by Aviroez on 20/08/2015.
 */
public class Report {
    private String status;
    private String message;
    private String id_report_status;
    private String nama_report_status;

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

    public String getId_report_status() {
        return id_report_status;
    }

    public void setId_report_status(String id_report_status) {
        this.id_report_status = id_report_status;
    }

    public String getNama_report_status() {
        return nama_report_status;
    }

    public void setNama_report_status(String nama_report_status) {
        this.nama_report_status = nama_report_status;
    }

    @Override
    public String toString() {
        return nama_report_status;
    }

    public Report() {

    }
}