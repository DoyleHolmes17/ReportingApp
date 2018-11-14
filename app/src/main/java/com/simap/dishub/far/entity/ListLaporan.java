package com.simap.dishub.far.entity;

/**
 * Created by Aviroez on 20/08/2015.
 */
public class ListLaporan {
    private String status;
    private String message;
    private String jumlah_pelapor;
    private String lokasi_infra;
    private String alamat_infra;
    private String category_infra;
    private String sub_category_infra;
    private String wilayah_infra;
    private String tanggal_pelaporan;
    private String note_infra;
    private String status_report;
    private String id_reportstatus;
    private String id_category_infra;
    private String id_area_infra;
    private String note_admin;
    private String id_userteknis;
    private String end_teknisi_confir;
    private String end_teknis;
    private String updated_id;
    private String mulai_pengerjaan;
    private String selesai_pengerjaan;
    private String lama_pengerjaan;
    private String nama_teknisi;
    private String updated_at;
    private String laporan_close;
    private String total_downtime;
    private String img_pelapor;
    private String img_konfirmasi;

    public String getImg_pelapor() {
        return img_pelapor;
    }

    public void setImg_pelapor(String img_pelapor) {
        this.img_pelapor = img_pelapor;
    }

    public String getImg_konfirmasi() {
        return img_konfirmasi;
    }

    public void setImg_konfirmasi(String img_konfirmasi) {
        this.img_konfirmasi = img_konfirmasi;
    }

    public String getLaporan_close() {
        return laporan_close;
    }

    public void setLaporan_close(String laporan_close) {
        this.laporan_close = laporan_close;
    }

    public String getTotal_downtime() {
        return total_downtime;
    }

    public void setTotal_downtime(String total_downtime) {
        this.total_downtime = total_downtime;
    }

    public String getMulai_pengerjaan() {
        return mulai_pengerjaan;
    }

    public void setMulai_pengerjaan(String mulai_pengerjaan) {
        this.mulai_pengerjaan = mulai_pengerjaan;
    }

    public String getSelesai_pengerjaan() {
        return selesai_pengerjaan;
    }

    public void setSelesai_pengerjaan(String selesai_pengerjaan) {
        this.selesai_pengerjaan = selesai_pengerjaan;
    }

    public String getLama_pengerjaan() {
        return lama_pengerjaan;
    }

    public void setLama_pengerjaan(String lama_pengerjaan) {
        this.lama_pengerjaan = lama_pengerjaan;
    }

    public String getNama_teknisi() {
        return nama_teknisi;
    }

    public void setNama_teknisi(String nama_teknisi) {
        this.nama_teknisi = nama_teknisi;
    }

    public String getEnd_teknisi_confir() {
        return end_teknisi_confir;
    }

    public void setEnd_teknisi_confir(String end_teknisi_confir) {
        this.end_teknisi_confir = end_teknisi_confir;
    }

    public String getEnd_teknis() {
        return end_teknis;
    }

    public void setEnd_teknis(String end_teknis) {
        this.end_teknis = end_teknis;
    }

    public String getUpdated_id() {
        return updated_id;
    }

    public void setUpdated_id(String updated_id) {
        this.updated_id = updated_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getNote_admin() {
        return note_admin;
    }

    public void setNote_admin(String note_admin) {
        this.note_admin = note_admin;
    }

    public String getId_userteknis() {
        return id_userteknis;
    }

    public void setId_userteknis(String id_userteknis) {
        this.id_userteknis = id_userteknis;
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

    public String getJumlah_pelapor() {
        return jumlah_pelapor;
    }

    public void setJumlah_pelapor(String jumlah_pelapor) {
        this.jumlah_pelapor = jumlah_pelapor;
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

    public String getWilayah_infra() {
        return wilayah_infra;
    }

    public void setWilayah_infra(String wilayah_infra) {
        this.wilayah_infra = wilayah_infra;
    }

    public String getTanggal_pelaporan() {
        return tanggal_pelaporan;
    }

    public void setTanggal_pelaporan(String tanggal_pelaporan) {
        this.tanggal_pelaporan = tanggal_pelaporan;
    }

    public String getNote_infra() {
        return note_infra;
    }

    public void setNote_infra(String note_infra) {
        this.note_infra = note_infra;
    }

    public String getStatus_report() {
        return status_report;
    }

    public void setStatus_report(String status_report) {
        this.status_report = status_report;
    }

    public String getId_reportstatus() {
        return id_reportstatus;
    }

    public void setId_reportstatus(String id_reportstatus) {
        this.id_reportstatus = id_reportstatus;
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

    @Override
    public String toString() {
        return status;
    }

    public ListLaporan() {

    }
}