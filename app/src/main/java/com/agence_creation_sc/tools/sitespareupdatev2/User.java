package com.agence_creation_sc.tools.sitespareupdatev2;

/**
 * Created by Stéf on 04/11/2015.
 */
public class User {
    private int id;

    private String nom;

    private String prenom;

    private String userrt;

    private String pswdrt;

    private String email;

    private String password;



    public User() {

    }

    public User(String nom, String prenom,String email,String password ,String userrt,String pswdrt) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.userrt = userrt;
        this.pswdrt = pswdrt;
        this.email = email;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUserrt() {
        return userrt;
    }

    public void setUserrt(String userrt) {this.userrt = userrt; }

    public String getPswdrt() {
        return pswdrt;
    }

    public void setPswdrt(String pswdrt) {this.pswdrt = pswdrt; }

    public String getEmail() {return email; }

    public void setEmail(String email) {this.email = email; }

    public String getPassword() {return password; }

    public void setPassword(String password) {this.password = password; }



    @Override
    public String toString() {
        return  "[USER] " + id +" " +nom +" " + prenom +" enregistré avec succès";
    }

}

