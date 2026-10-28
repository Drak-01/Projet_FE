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
public class Traitement {
    //Attributs
    private Connection connection;
    private String nomTraitement;
    private double prix;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    private String desTraitement;
    private int codeTraitement;

    public void setCodeTraitement(int codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    public void setNomTraitement(String nomTraitement) {
        this.nomTraitement = nomTraitement;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setDesTraitement(String desTraitement) {
        this.desTraitement = desTraitement;
    }
    
    public int getCodeTraitement() {
        return codeTraitement;
    }

    public String getNomTraitement() {
        return nomTraitement;
    }

    public double getPrix() {
        return prix;
    }

    public String getDesTraitement() {
        return desTraitement;
    }
    //----------------------------
    
    //Constructor
    public Traitement(Connection connection){
        this.connection = connection;
    }
    public Traitement(String nom, Double montant){
        this.prix = montant;
        this.nomTraitement = nom;
    }
    //Methodes
    //Ajouter de traitement
    public boolean ajouterTraitement(Connection connection) {
        System.out.println("Appel a jouter Traitement");
        boolean codeTraitementExiste = false;
        String verifiSql = "SELECT COUNT(*) FROM traitement WHERE codeTraitement =?";

        // Vérifier si le matricule existe déjà
        try (PreparedStatement verifi = connection.prepareStatement(verifiSql)) {
            verifi.setInt(1, codeTraitement);
            try (ResultSet resultSet = verifi.executeQuery()) {
                if (resultSet.next()) {
                    codeTraitementExiste = resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (codeTraitementExiste) {
            
            System.out.println("Le Service existe déjà dans la base de données.");
            return false;
        } else {
            // Insérer le nouvel utilisateur si le matricule n'existe pas
            String insertQuery = "INSERT INTO traitement(codeTraitement,type, nom, description, prix) VALUES (?, ?, ?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
               
                preparedStatement.setInt(1, this.codeTraitement);
                preparedStatement.setString(2, this.type);  
                preparedStatement.setString(3, this.nomTraitement);
                preparedStatement.setString(4,this.desTraitement );
                preparedStatement.setDouble(5, this.prix );
                System.out.println(this.prix);

                preparedStatement.executeUpdate();

                System.out.println("Tratement ajouté avec succès");
                
            } catch (SQLException e) {
                e.printStackTrace();
                }
            //return true;

        }
        return true;
    }
    // Modifier la Description de service
    public boolean modifierTraitement(Connection connection) {
        try {
            //Change le type_service a nom
            String query = "UPDATE traitement SET nom=? ,description= ?, prix =? Where codeTraitement =?";
            
            PreparedStatement pstmt = connection.prepareStatement(query);
            
            pstmt.setString(1, this.nomTraitement);
            pstmt.setString(2, this.desTraitement);
            pstmt.setDouble(3, this.prix);
            pstmt.setInt(4,this.codeTraitement);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Supprimer Service
    public boolean supprimerTraitement(Connection connection){
        try {
                String query = "DELETE FROM traitement WHERE codeTraitement = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, codeTraitement);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
        }
    }  
    //--------------------------------------------------------------------------
    //Récuperation des données de la base de
    public static List<Traitement> getAllTraitement (Connection connection) {
        List<Traitement> traitementList = new ArrayList<>();
        String query = "SELECT codeTraitement,type, nom, description, prix FROM traitement";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Traitement traitement = new Traitement(connection);
                
                traitement.setCodeTraitement(resultSet.getInt("codeTraitement"));
                traitement.setType(resultSet.getString("type"));
                traitement.setNomTraitement(resultSet.getString("nom"));
                traitement.setDesTraitement(resultSet.getString("description"));
                traitement.setPrix(resultSet.getDouble("prix"));

                
                traitementList.add(traitement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return traitementList;
    }
    ///
    public static List<String> getAllTraitementTypes(Connection connection) {
        List<String> traitementTypes = new ArrayList<>();
        String query = "SELECT DISTINCT type FROM traitement";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                traitementTypes.add(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return traitementTypes;
    }
    
}
