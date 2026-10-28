/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author badra
 */
public class Hospitatlisation {
    private int idPatient;
    private int numChambre;
    private String dateAdmission;
    private String motif;
    private Connection connection;
    private String dateSortie;
    private int numHospital;


    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getNumChambre() {
        return numChambre;
    }

    public void setNumChambre(int numChambre) {
        this.numChambre = numChambre;
    }

    public String getDateAdmission() {
        return dateAdmission;
    }

    public void setDateAdmission(String dateAdmission) {
        this.dateAdmission = dateAdmission;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
    }

    public int getNumHospital() {
        return numHospital;
    }

    public void setNumHospital(int numHospital) {
        this.numHospital = numHospital;
    }
    
    public Hospitatlisation(Connection connection) {
        this.connection = connection;
    }


    public static boolean  insertHospi(Connection connection, int idPatient, int numChambre, String dateAdmission) {
        String query = "INSERT INTO Hospitalisation (id_patient, num_chambre, date_admission) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPatient);
            stmt.setInt(2, numChambre);
            stmt.setString(3, dateAdmission);
            
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
    public static boolean hospitUpdate(Connection connection, int numChambre, String dateSortie) {
        String sql = "UPDATE Hospitalisation SET date_sortie = ? WHERE num_chambre = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, dateSortie);
            preparedStatement.setInt(2, numChambre);

            int rowsUpdated = preparedStatement.executeUpdate();
             // Retourne true si au moins une ligne a été mise à jour
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Retourne false en cas d'erreur
        }
        return true;
    }
    
    public static List<Hospitatlisation> getAllHospitatlisation(Connection connection) {
        List<Hospitatlisation> hospiList = new ArrayList<>();
        String query = "SELECT id_hospitalisation,id_patient,num_chambre,date_admission,date_sortie FROM hospitalisation";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Hospitatlisation hospit = new Hospitatlisation(connection);
               
                hospit.setNumHospital(resultSet.getInt("id_hospitalisation"));
                hospit.setIdPatient(resultSet.getInt("id_patient"));
                hospit.setNumChambre(resultSet.getInt("num_chambre"));
                hospit.setDateAdmission(resultSet.getString("date_admission"));
                hospit.setDateSortie(resultSet.getString("date_sortie"));
              
                hospiList.add(hospit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospiList;
    }
}