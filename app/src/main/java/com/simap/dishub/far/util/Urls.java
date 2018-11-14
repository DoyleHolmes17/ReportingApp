package com.simap.dishub.far.util;

/**
 * Created by Aviroez on 12/02/2015.
 */
public interface Urls {
    String URL = "url";
    String URI = "uri";

//    String urlmas = "http://182.253.200.184/dishub/apis/master/"; //Jkt
//    String urlauth = "http://182.253.200.184/dishub/apis/auth/"; //Jkt
//    String urllaporan = "http://182.253.200.184/dishub/apis/laporan/"; //Jkt
//    String urlgambar = "http://182.253.200.184/dishub/files/thumbnail/"; //Jkt

    String urlmas = "http://103.255.15.34/apis/master/"; //dishub serv
    String urlauth = "http://103.255.15.34/apis/auth/"; //dishub serv
    String urllaporan = "http://103.255.15.34/apis/laporan/"; //dishub serv
    String urlgambar = "http://103.255.15.34/files/thumbnail/"; //dishub serv

    //USER
    String area = "area";
    String login = "login";
    String register = "register";
    String category = "category";
    String pelaporanvia = "pelaporanvia";
    String inputlaporan = "inputlaporan";
    String inputlaporanadmin = "inputlaporanadmin";
    String laporanuser = "laporanuser";

    //Teknisi
    String laporanteknisifollowup = "laporanteknisifollowup";
    String updatefollowupteknisi = "updatefollowupteknisi";
    String ceklaporanteknisifollowup = "ceklaporanteknisifollowup";
    String updatefollowupteknisikonfir = "updatefollowupteknisikonfir";

    //Admin
    String userteknisi = "userteknisi";
    String reportstatusmasuk = "reportstatusmasuk";
    String laporanusermasuk = "laporanusermasuk";
    String updatelaporanmasukadmin = "updatelaporanmasukadmin";
    String laporanuserpenugasan = "laporanuserpenugasan";
    String updatepenugasanteknisi = "updatepenugasanteknisi";
    String reportstatusprogress = "reportstatusprogress";
    String laporanteknisiprogress = "laporanteknisiprogress";
    String updatelaporanprogress = "updatelaporanprogress";
    String reportstatusconfirmation = "reportstatusconfirmation";
    String laporanteknisiconfirmation = "laporanteknisiconfirmation";
    String updatelaporanconfirmation = "updatelaporanconfirmation";
    String laporanteknisiclose = "laporanteknisiclose";

    String upload_file = "upload_file";
    String notiftopic = "notiftopic1";
    String groupuser = "groupuser";
    String adduser = "adduser";
    String forgotpassword = "forgotpassword";
    String cancellaporan  = "cancellaporan ";

}