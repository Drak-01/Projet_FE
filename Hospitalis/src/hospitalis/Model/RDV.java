/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Model;

import java.sql.Connection;

/**
 *
 * @author badra
 */
public class RDV {
    private String numRDV; // Numéro du rendez-vous
    private String typeRDV; // Type de rendez-vous
    private String date; // Date du rendez-vous
    private String heure; // Heure du rendez-vous
    private String statut; // Statut du rendez-vous
    private String matriculeMed; // Matricule du médecin associé au rendez-vous
    private Connection connection;
    // Constructeur
    public RDV(String numRDV, String typeRDV, String date, String heure, String statut, String matriculeMed) {
        this.numRDV = numRDV;
        this.typeRDV = typeRDV;
        this.date = date;
        this.heure = heure;
        this.statut = statut;
        this.matriculeMed = matriculeMed;
    }
    public RDV (Connection connection){
        this.connection = connection;
    }
    // Getters et setters
    public String getNumRDV() {
        return numRDV;
    }
    public void setNumRDV(String numRDV) {
        this.numRDV = numRDV;
    }
    public String getTypeRDV() {
        return typeRDV;
    }

    public void setTypeRDV(String typeRDV) {
        this.typeRDV = typeRDV;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMatriculeMed() {
        return matriculeMed;
    }

    public void setMatriculeMed(String matriculeMed) {
        this.matriculeMed = matriculeMed;
    }
    public void setConnection(Connection connection){
        this.connection=connection;
    }
    public Connection getConnection(){
        return connection;
    }
}

