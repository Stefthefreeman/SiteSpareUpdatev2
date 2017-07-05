package com.agence_creation_sc.tools.sitespareupdatev2;

/**
 * Created by Stéf on 04/11/2015.
 */
public class Contact {
    private int id;

    private String snok;

    private String pnhs;

    private String snhs;

    private String qrok;

    private String qrhs;

    private double longitude;

    private double latitude;

    private String timestamps;

    private String ticket;

    public Contact() {

    }

    public Contact(String snok, String pnhs, String snhs,String qrok,String qrhs,double longitude,double latitude,String timestamps,
                   String ticket) {
        super();
        this.snok = snok;
        this.pnhs = pnhs;
        this.snhs = snhs;
        this.qrok = qrok;
        this.qrhs = qrhs;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamps = timestamps;
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSnok() {
        return snok;
    }

    public void setSnok(String snok) {
        this.snok = snok;
    }

    public String getPnhs() {
        return pnhs;
    }

    public void setPnhs(String pnhs) {
        this.pnhs = pnhs;
    }

    public String getSnhs() {
        return snhs;
    }

    public void setSnhs(String snhs) {this.snhs = snhs; }

    public String getQrok() {
        return qrok;
    }

    public void setQrok(String qrok) {this.qrok = qrok; }

    public String getQrhs() {
        return qrhs;
    }

    public void setQrhs(String qrhs) {this.qrhs = qrhs; }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {this.longitude = longitude; }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {this.latitude = latitude; }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {this.timestamps = timestamps; }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {this.ticket = ticket; }



    @Override
    public String toString() {
        return  "[OPERATION]" + ticket +" sauvegardée";
    }

}
