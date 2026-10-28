/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author badra
 */
public class Patient {
    private int id_patient; // ID du patient
    private String nom; // Nom du patient
    private String prenom; // Prénom du patient
    private String dateNaissance; // Date de naissance du patient
    private String etat_civil; // État civil du patient
    private long telephone; // Numéro de téléphone du patient
    private String sexe; // Sexe du patient
    private String cni;
    private String med;
    private Connection connection;

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    
    // Getters et setters
    public int getId_patient() {
        return id_patient;
    }

    public void setId_patient(int id_patient) {
        this.id_patient = id_patient;
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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEtat_civil() {
        return etat_civil;
    }

    public void setEtat_civil(String etat_civil) {
        this.etat_civil = etat_civil;
    }

    public long getTelephone() {
        return telephone;
    }

    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    // Méthode pour afficher les détails du patient
    // Constructeur
    public Patient(int id_patient, String nom, String prenom, String dateNaissance, String etat_civil, long telephone, String sexe) {
        this.id_patient = id_patient;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.etat_civil = etat_civil;
        this.telephone = telephone;
        this.sexe = sexe;
    }
    public Patient(Connection connection){
        this.connection = connection;
    }
    // pour la Facturation 
    public Patient(){
        this.nom = "inconnue";
        this.prenom = "inconnue";
        this.dateNaissance = "inconnue";
        this.cni = "inconnue";
    }
    
    public String detailsFacturePatient() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nom: ").append(nom).append("\n");
        sb.append("Prénom: ").append(prenom).append("\n");
        sb.append("Date de Naissance: ").append(dateNaissance).append("\n");
        sb.append("CNI: ").append(cni).append("\n\n");
        sb.append("Nom: ").append(med).append("\n");
        return sb.toString();
    }
    
    //Permet de vérifier si la patient est enregistre ou pas
    public boolean verificationPatient(String nom, String prenom, String dateNaissance) {
        String query = "SELECT * FROM patient WHERE nom = ? AND prenom = ? AND dateNais = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, dateNaissance);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setId_patient(resultSet.getInt("id"));
                    id_patient = resultSet.getInt("id");
                    System.out.println("id pate"+ id_patient);
                    patient.setNom(resultSet.getString("nom"));
                    patient.setPrenom(resultSet.getString("prenom"));
                    patient.setDateNaissance(resultSet.getString("dateNais"));
                    patient.setSexe(resultSet.getString("sexe"));
                    return true; // Patient trouvé
                } else {
                    return false; // Aucun patient trouvé
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

