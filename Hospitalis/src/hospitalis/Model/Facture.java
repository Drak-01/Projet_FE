/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 *
 * @author badra
 */
public class Facture {
    //
    private Connection connection;
    private String idFacture;
    private String dateFacture;
    private List<String> listeTraitements; 
    private Double montant;
    private String nom;
    private String details;
    private String status;
    private static final String attentStatus = "Attent";
    private static final String payeFacture = "payé";
    private List<String> listeTraitementsEffectues;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    public String getDetails(){
        return this.details;
    }
    public void setIdFacture(String idFacture) {
        this.idFacture = idFacture;
    }
    

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

  
    public void setDetails(String details) {
        this.details = details;
    }


    public List<String> getListeTraitements() {
        return listeTraitements;
    }

    public void setListeTraitements(List<String> listeTraitements) {
        this.listeTraitements = listeTraitements;
    }
    //Setter And Getter

    public String getIdFacture() {
        return idFacture;
    }


    public String getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }
    
    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
   
   
    // Constructeurs
    public Facture(Connection connection){
        this.connection = connection;
        listeTraitements = new ArrayList<>();
    }
    
    // Methodes 

    private String genererCodeFacture() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        return "FACT" + dateStr; // Vous pouvez ajouter un préfixe pour identifier les factures
    }
    // Creation de la facture 
    public boolean creeFacture() {
        String queryFacture = "INSERT INTO Facture (num_facture, details, montant, date, listeTraitement, status) VALUES (?, ?, ?, ?, ?, ?)";
        idFacture = genererCodeFacture();
        System.out.println("Facture: " + idFacture);

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryFacture)) {
            preparedStatement.setString(1, idFacture);
            preparedStatement.setString(2, details);
            preparedStatement.setDouble(3, montant);
            preparedStatement.setString(4, dateFacture);
            preparedStatement.setString(5, String.join("\n", listeTraitementsEffectues));
            preparedStatement.setString(6, attentStatus);

            preparedStatement.executeUpdate();

            System.out.println("Facture ajoutée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
           return false;
        }
        return true;
    }
    //
    public List<String> getListeTraitementsEffectues() {
        return listeTraitementsEffectues;
    }

    public void setListeTraitementsEffectues(List<String> listeTraitementsEffectues) {
        this.listeTraitementsEffectues = listeTraitementsEffectues;
    }
    
    //list de traitement effactue
    public void ajouterTraitement(String traitement) {
        listeTraitements.add(traitement);
    }
    
    public List<Traitement> getAllTraitement(String type) {
        List<Traitement> listTraitement = new ArrayList<>();
        String query = "SELECT nom, prix FROM traitement WHERE type=?"; // Ajustez la requête SQL en fonction de votre base de données

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    double montant = resultSet.getDouble("prix");
                    listTraitement.add(new Traitement(nom, montant));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listTraitement;
    }
    public static List<Facture> getAllFacture(Connection connection) {
        List<Facture> factureList = new ArrayList<>();
        String query = "SELECT * FROM facture WHERE status=?";

        try (PreparedStatement statement = connection.prepareStatement(query);) {
            
            statement.setString(1, attentStatus);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                Facture facture = new Facture(connection);
                
                facture.setIdFacture(resultSet.getString("num_Facture"));
                facture.setDetails(resultSet.getString("details"));
                facture.setMontant(Double.parseDouble(resultSet.getString("montant")));
                facture.setDateFacture(resultSet.getString("date"));
                facture.setStatus(resultSet.getString("status"));
                
                factureList.add(facture);
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return factureList;
    }
    public static List<Facture> getAllFacturePayer(Connection connection) {
        List<Facture> factureList = new ArrayList<>();
        String query = "SELECT * FROM FacturesPayees ";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Facture facture = new Facture(connection);
                
                facture.setIdFacture(resultSet.getString("num_Facture"));
                facture.setDetails(resultSet.getString("details"));
                facture.setMontant(Double.parseDouble(resultSet.getString("montant")));
                facture.setDateFacture(resultSet.getString("date"));
                facture.setStatus(resultSet.getString("status"));
                
                factureList.add(facture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return factureList;
    }
    public boolean paiementFacture() {
        try {
            // Change le statut à "payé" dans la table Facture
            String query = "UPDATE Facture SET status=? WHERE num_facture=?";

            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setString(1, payeFacture);
            pstmt.setString(2, this.idFacture);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 1) {
              
                // Réinsérer la ligne mise à jour dans la vue FacturesPayees
                String reinsertQuery = "INSERT INTO FacturesPayees (num_facture, date = ? )SELECT * FROM Facture WHERE num_facture=?";
                PreparedStatement reinsertStmt = connection.prepareStatement(reinsertQuery);
                reinsertStmt.setString(2, this.idFacture);
                reinsertStmt.setString(1, this.dateFacture);
                
                reinsertStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean chercherFacturer(){
        String queryCherche = "SELECT * FROM facture WHERE num_facture=? AND status=?";

        try (PreparedStatement statement = connection.prepareStatement(queryCherche)) {
            statement.setString(1, this.idFacture);
            statement.setString(2, attentStatus);
            
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()){   
                    setDetails(resultSet.getString("details"));
                    setMontant(Double.parseDouble(resultSet.getString("montant")));
                       
                }else{
                    return false;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true; // Si la facture est trouvée avec succès
    }

    public boolean chercherFacturerAll(){
        String queryCherche = "SELECT * FROM facture WHERE num_facture=?";

        try (PreparedStatement statement = connection.prepareStatement(queryCherche)) {
            statement.setString(1, this.idFacture);
            //statement.setString(2, attentStatus);
            
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()){   
                    setDetails(resultSet.getString("details"));
                    setMontant(Double.parseDouble(resultSet.getString("montant")));
                       
                }else{
                    return false;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true; // Si la facture est trouvée avec succès
    }
    
    
    /*
    public List<String> getAllTraitement(String type) {
        List<String> listTraitement = new ArrayList<>();
        String query = "SELECT nom, prix FROM traitement WHERE type = ?"; // Assurez-vous d'utiliser les bons noms de colonnes

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    double montant = resultSet.getDouble("prix");
                    // Concaténez le nom et le montant dans une seule chaîne
                    String traitement = nom + " : " + montant + " DHS";
                    listTraitement.add(traitement);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listTraitement;
    }*/

    public List<String> getTraitementsForFacture(String numFacture) {
        List<String> traitements = new ArrayList<>();
        String query = "SELECT listeTraitement FROM Facture WHERE num_facture = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, numFacture);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String details = resultSet.getString("listeTraitement");
                    // Supposons que les traitements sont stockés dans la colonne "details" sous forme de chaîne séparée par des virgules
                    String[] traitementsArray = details.split(",");
                    traitements.addAll(Arrays.asList(traitementsArray));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return traitements;
    }
    
}
