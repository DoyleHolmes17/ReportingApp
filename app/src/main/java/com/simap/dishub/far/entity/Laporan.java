package com.simap.dishub.far.entity;

/**
 * Created by Aviroez on 20/08/2015.
 */
public class Laporan {
    private String status;
    private String message;
    private String id;
    private String id_user;
    private String rep_number;
    private String id_category_infra;
    private String id_area_infra;
    private String status_report;
    private String rep_date;
    private String note_infra;
    private String lokasi_infra;
    private String alamat_infra;
    private String category_infra;
    private String sub_category_infra;
    private String tanggal_pelaporan;
    private String img_pelapor;
    private String pelaporan_via;
    private String id_laporan;

    public String getId_laporan() {
        return id_laporan;
    }

    public void setId_laporan(String id_laporan) {
        this.id_laporan = id_laporan;
    }

    public String getPelaporan_via() {
        return pelaporan_via;
    }

    public void setPelaporan_via(String pelaporan_via) {
        this.pelaporan_via = pelaporan_via;
    }

    public String getImg_pelapor() {
        return img_pelapor;
    }

    public void setImg_pelapor(String img_pelapor) {
        this.img_pelapor = img_pelapor;
    }

    public String getTanggal_pelaporan() {
        return tanggal_pelaporan;
    }

    public void setTanggal_pelaporan(String tanggal_pelaporan) {
        this.tanggal_pelaporan = tanggal_pelaporan;
    }

    public String getId_category_infra() {
        return id_category_infra;
    }

    public void setId_category_infra(String id_category_infra) {
        this.id_category_infra = id_category_infra;
    }

    public String getId_area_infra() {
        return id_area_infra;
    }

    public void setId_area_infra(String id_area_infra) {
        this.id_area_infra = id_area_infra;
    }

    public String getStatus_report() {
        return status_report;
    }

    public void setStatus_report(String status_report) {
        this.status_report = status_report;
    }

    public String getRep_date() {
        return rep_date;
    }

    public void setRep_date(String rep_date) {
        this.rep_date = rep_date;
    }

    public String getNote_infra() {
        return note_infra;
    }

    public void setNote_infra(String note_infra) {
        this.note_infra = note_infra;
    }

    public String getLokasi_infra() {
        return lokasi_infra;
    }

    public void setLokasi_infra(String lokasi_infra) {
        this.lokasi_infra = lokasi_infra;
    }

    public String getAlamat_infra() {
        return alamat_infra;
    }

    public void setAlamat_infra(String alamat_infra) {
        this.alamat_infra = alamat_infra;
    }

    public String getCategory_infra() {
        return category_infra;
    }

    public void setCategory_infra(String category_infra) {
        this.category_infra = category_infra;
    }

    public String getSub_category_infra() {
        return sub_category_infra;
    }

    public void setSub_category_infra(String sub_category_infra) {
        this.sub_category_infra = sub_category_infra;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getRep_number() {
        return rep_number;
    }

    public void setRep_number(String rep_number) {
        this.rep_number = rep_number;
    }

    @Override
    public String toString() {
        return rep_number;
    }

    public Laporan() {

    }
}