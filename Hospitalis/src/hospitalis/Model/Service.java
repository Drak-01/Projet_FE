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
 *             //Change  type_service a nom

 */
public class Service {
    //Attributs
    private int codeService;
    private String nomService;
    private String desService;
    private Connection connection;
    
    //Constructeurs
    public Service(Connection connection){
        this.connection = connection;
    }
    
    //Setter et les Getters
    public void setCodeService(int codeService) {
        this.codeService = codeService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public void setDesService(String DesService) {
        this.desService = DesService;
    }
    
    public int getCodeService() {
        return codeService;
    }

    public String getNomService() {
        return nomService;
    }

    public String getDesService() {
        return desService;
    }
    
    //Methodes
    //Ajouter de nouvelle service s'il existe pas besoin d'ajouter 
    public boolean ajouterService(Connection connection) {
        System.out.println("Appel a jouter utilisateur");
        boolean codeServiceExiste = false;
        String verifiSql = "SELECT COUNT(*) FROM service WHERE code_service =?";

        // Vérifier si le matricule existe déjà
        try (PreparedStatement verifi = connection.prepareStatement(verifiSql)) {
            verifi.setInt(1, codeService);
            try (ResultSet resultSet = verifi.executeQuery()) {
                if (resultSet.next()) {
                    codeServiceExiste = resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (codeServiceExiste) {
            
            System.out.println("Le Service existe déjà dans la base de données.");
            return false;
        } else {
            // Insérer le nouvel utilisateur si le matricule n'existe pas
            String insertQuery = "INSERT INTO service (code_service, type_service, description) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, codeService);
                preparedStatement.setString(2, nomService);
                preparedStatement.setString(3,desService );

                preparedStatement.executeUpdate();

                System.out.println("Utilisateur ajouté avec succès");
            } catch (SQLException e) {
                e.printStackTrace();
                }
            //return true;

        }
        return true;
    }
    // Modifier la Description de service
    public boolean modifierService(Connection connection) {
        try {
            //Change le type_service a nom
            String query = "UPDATE service SET type_service=? ,description = ? Where code_service=? ";
            
            PreparedStatement pstmt = connection.prepareStatement(query);
            
            
            pstmt.setString(1, this.nomService);
            pstmt.setString(2, this.desService);
            pstmt.setInt(3, this.codeService);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Supprimer Service
    public boolean supprimerService(Connection connection){
        try {
                String query = "DELETE FROM service WHERE code_service = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, codeService);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
        }
    }  
    //--------------------------------------------------------------------------
    //Récuperation des données de la base de
    public static List<Service> getAllService(Connection connection) {
        List<Service> serviceList = new ArrayList<>();
        String query = "SELECT code_service, type_service, description FROM service";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Service service = new Service(connection);
                service.setCodeService(resultSet.getInt("code_service"));
                service.setNomService(resultSet.getString("type_Service"));
                service.setDesService(resultSet.getString("description"));
                
                serviceList.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return serviceList;
    }
    ///
    public int getServiceCode() {
        String queryCodeService = "SELECT DISTINCT code_service FROM Service WHERE type_service = ?";
        int codeService = -1;
        // Rechercher le code de service à partir du type de service
        try (PreparedStatement statement = connection.prepareStatement(queryCodeService)) {
            statement.setString(1, nomService);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                   codeService = resultSet.getInt("code_service");
                } else {
                    System.out.println("Type de service non trouvé : " + nomService);
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return codeService;
    }
    //
    public static List<String> getAllServiceTypes(Connection connection) {
        List<String> serviceTypes = new ArrayList<>();
        String query = "SELECT DISTINCT type_service FROM service";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                serviceTypes.add(resultSet.getString("type_service"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return serviceTypes;
    }
}
