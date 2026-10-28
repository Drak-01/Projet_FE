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
public class Chambre {
    //Attributs
    private int numChambre;
    private final Connection connection;
    private String type;
    private String disponibilite;
    private int nombreLits;
    private String categorie;

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String Categorie) {
        this.categorie = Categorie;
    }
    private final  String dispoOui = "Disponible";// par défaut la chambre est disponible


    public int getNumChambre() {
        return numChambre;
    }

    public void setNumChambre(int numChambre) {
        this.numChambre = numChambre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public int getNombreLits() {
        return nombreLits;
    }

    public void setNombreLits(int nombreLits) {
        this.nombreLits = nombreLits;
    }
    
    //private Patient patient;
    
    // Constructeurs
    public Chambre(Connection connection) {
        this.connection = connection;
    }
    // Methode
    public boolean supprimerChambre(Connection connection){
        try {
                String query = "DELETE FROM chambre WHERE num_Chambre = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, numChambre);
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                return false;
        }
    }  
    //-------------------------------
    public static List<Chambre> getAllChambre(Connection connection) {
        List<Chambre> chambreList = new ArrayList<>();
        String query = "SELECT * FROM chambre";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Chambre chambre = new Chambre(connection);
                
                chambre.setNumChambre(resultSet.getInt("num_chambre"));
                chambre.setType(resultSet.getString("type"));
                chambre.setDisponibilite(resultSet.getString("disponibilite"));
                chambre.setCategorie(resultSet.getString("categorie"));
                               
                chambreList.add(chambre);
            }
        } catch (SQLException e) {
        
        }

        return chambreList;
    }
    //********************************************
    public boolean modifierChambre(Connection connection) {
        try {
            //Change le type_service a nom
            String query = "UPDATE chambre SET type=? ,categorie = ? Where num_chambre=? ";
            
            PreparedStatement pstmt = connection.prepareStatement(query);
            
            
            pstmt.setString(1, this.type);
            pstmt.setString(2, this.categorie);
            pstmt.setInt(3, this.numChambre);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    //-----------------------------------------------------------------------
    public boolean ajouterChambre(Connection connection) {
        boolean chambreExists = false;
        String checkQuery = "SELECT COUNT(*) FROM Chambre WHERE num_chambre = ?";

        // Vérifier si la chambre existe déjà
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, numChambre);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    chambreExists = resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            return false; // If there's an error in checking, return false
        }

        if (chambreExists) {
            return false;
        } else {
            // Insérer la nouvelle chambre
            String insertQuery = "INSERT INTO Chambre (num_chambre, type, disponibilite, categorie) VALUES (?, ?, ?, ?)";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, numChambre);
                insertStatement.setString(2, type);
                insertStatement.setString(3, dispoOui);
                insertStatement.setString(4, categorie);

                insertStatement.executeUpdate();

                System.out.println("Chambre ajoutée avec succès");
                return true; 
            } catch (SQLException e) {
                return false; 
            }
        }
    }
    public static List<Chambre> chercherChambre(Connection connection, String motCherche){
        List<Chambre> listChambre = new ArrayList<>();
        String sqlQuery = "SELECT * FROM chambre WHERE num_chambre LIKE ? OR categorie LIKE ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            String cherche = "%" + motCherche + "%";

            preparedStatement.setString(1, cherche);
            preparedStatement.setString(2, cherche);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Chambre chambre = new Chambre(connection); 
                
                chambre.setNumChambre(Integer.parseInt(resultSet.getString("num_chambre")));
                chambre.setType(resultSet.getString("type"));
                chambre.setDisponibilite(resultSet.getString("disponibilite"));
                chambre.setCategorie(resultSet.getString("categorie"));
                
                listChambre.add(chambre);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listChambre;
    }
    //*********************************************
    public static List<String> getAllTypesChambre(Connection connection) {
        List<String> chambreTypes = new ArrayList<>();
        String query = "SELECT DISTINCT type FROM chambre";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                chambreTypes.add(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chambreTypes;
    }
    public static List<String> getAllCategorieChambre(Connection connection) {
        List<String> chambreCategorie = new ArrayList<>();
        String query = "SELECT DISTINCT categorie FROM chambre";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                chambreCategorie.add(resultSet.getString("categorie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chambreCategorie;
    }
    // Affecter un patient ///
    public boolean updateDisponibilite(int num,String dispo){
        String query = "UPDATE Chambre SET disponibilite = ? WHERE num_chambre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, dispo);
            stmt.setInt(2, num);
            
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    //
    public  boolean isDisponible(int numChambre) { /// Numero Chambre
        String query = "SELECT disponibilite FROM Chambre WHERE num_chambre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, numChambre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String disponibilite = rs.getString("disponibilite");
                if("Disponible".equals(disponibilite)){
                      Chambre chambre = new Chambre(connection);
                      chambre.setNumChambre(rs.getInt("num_chambre"));
                     return true;
                }  
            }
        }catch (SQLException e){
        }  
        return false;
    }
    public  boolean isDisponibleTexte(String type, String categorie) { /// Numero Chambre
        String query = "SELECT * FROM Chambre WHERE type = ? AND categorie = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, type);
            stmt.setString(2, categorie);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String disponibilite = rs.getString("disponibilite");
                if("Disponible".equals(disponibilite)){
                      Chambre chambre = new Chambre(connection);
                      chambre.setNumChambre(rs.getInt("num_chambre"));
                     return true;
                }                
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
