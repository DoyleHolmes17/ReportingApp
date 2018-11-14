package com.simap.dishub.far.entity;

/**
 * Created by Aviroez on 20/08/2015.
 */
public class Area {
    private String id_area;
    private String area_infra;
    private String alamat_infra;
    private String id_wilayah;
    private String id;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_wilayah() {
        return id_wilayah;
    }

    public void setId_wilayah(String id_wilayah) {
        this.id_wilayah = id_wilayah;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getId_area() {
        return id_area;
    }

    public void setId_area(String id_area) {
        this.id_area = id_area;
    }

    public String getArea_infra() {
        return area_infra;
    }

    public void setArea_infra(String area_infra) {
        this.area_infra = area_infra;
    }

    public String getAlamat_infra() {
        return alamat_infra;
    }

    public void setAlamat_infra(String alamat_infra) {
        this.alamat_infra = alamat_infra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String toString() {
        return (area_infra + ", " + alamat_infra);
    }
}