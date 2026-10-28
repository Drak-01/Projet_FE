/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Model;

import hospitalis.Controleur.MenuStocke.StockObserable;
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
public class Stock  extends StockObserable {
    //Attributs
     private String nom;
    private String type;
    private long quantite;
    private String date;
    private String serviceLivre;
    private int codeService;

    public void setCodeService(int codeService) {
        this.codeService = codeService;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }
    private int idArticle;

    public int getCodeService() {
        return codeService;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setServiceLivre(String serviceLivre) {
        this.serviceLivre = serviceLivre;
    }

    public String getServiceLivre() {
        return serviceLivre;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    private Connection connection;

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setQuantite(long quantite) {
        this.quantite = quantite;
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }

    public long getQuantite() {
        return quantite;
    }
    
    
    //Constructor
    public Stock(Connection connection){
        this.connection = connection;
    }
    //Methode
    //Ajoute de nouvelle stocke
    public boolean ajouterStocke (Connection connection) {
        // Insérer le nouvel aJOUTE si le matricule n'existe pas
            String insertQuery = "INSERT INTO Stock (article, type, quantite, date_entre) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, this.nom);
                preparedStatement.setString(2, this.type);
                preparedStatement.setLong(3, this.quantite);
                preparedStatement.setString(4, this.date);
                preparedStatement.executeUpdate();
                
                getAllStock(connection);
                System.out.println("Stocke ajouté avec succès");
                notifyObservers();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
                }
        return true;
    }
    //La liste de stocke
    public static List<Stock> getAllStock(Connection connection) {
        List<Stock> stockeList = new ArrayList<>();
        String query = "SELECT article, type, quantite, date_entre FROM  Stock";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Stock stock = new Stock(connection);
                stock.setNom(resultSet.getString("article"));
                stock.setType(resultSet.getString("type"));
                stock.setQuantite(resultSet.getLong("quantite"));
                stock.setDate(resultSet.getString("date_entre"));
               
                stockeList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockeList;
    }
    public static List<Stock> getNiveau(Connection connection) {
        List<Stock> stockeList = new ArrayList<>();
        String query = "SELECT article, type, SUM(quantite) AS total_quantite, MAX(date_entre) AS last_date_entre " +
                       "FROM Stock " +
                       "GROUP BY article, type " +
                       "ORDER BY total_quantite";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Stock stock = new Stock(connection);
                stock.setNom(resultSet.getString("article"));
                stock.setType(resultSet.getString("type"));
                stock.setQuantite(resultSet.getLong("total_quantite"));
                stock.setDate(resultSet.getString("last_date_entre"));

                stockeList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockeList;
    }
    public static List<Stock> getListeLivraison(Connection connection) {
        List<Stock> listeLivraison = new ArrayList<>();
        String query = "SELECT sl.code_service, s.type_service, sl.id_article, st.article, sl.quantite, sl.date " +
                       "FROM Stock_Livre sl " +
                       "JOIN Service s ON sl.code_service = s.code_service " +
                       "JOIN Stock st ON sl.id_article = st.id_article";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Stock stock = new Stock(connection);
                stock.setServiceLivre(resultSet.getString("type_service"));
                stock.setNom(resultSet.getString("article"));
                stock.setQuantite(resultSet.getLong("quantite"));
                stock.setDate(resultSet.getString("date"));

                listeLivraison.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("APPPEL LISTE LIVR");
        return listeLivraison;
    }
    ///
    public static List<String> getArticleList(Connection connection) {
        List<String> articleList = new ArrayList<>();
        String query = "SELECT DISTINCT article FROM Stock";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                articleList.add(resultSet.getString("article"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articleList;
    }
    //
    public boolean stockLivraison(Connection connection) {
        String queryLivraison = "INSERT INTO Stock_Livre (code_service, id_article, quantite, date) VALUES (?, ?, ?, ?)";
        String queryMisJourtock = "UPDATE Stock SET quantite = quantite - ? WHERE id_article = ?";
        String queryIDArticle = "SELECT DISTINCT id_article FROM Stock WHERE article = ?";
        String queryQuantiteStock = "SELECT quantite FROM Stock WHERE id_article = ?";

        int idArticle = -1;
        long quantiteStock = 0;

        try (PreparedStatement preS = connection.prepareStatement(queryIDArticle)) {
            preS.setString(1, this.nom);
            try (ResultSet resultSet = preS.executeQuery()) {
                if (resultSet.next()) {
                    idArticle = resultSet.getInt("id_article");
                } else {
                    System.out.println("Article non trouvé : " + this.nom);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Obtenir la quantité actuelle du stock
        try (PreparedStatement preSQuantite = connection.prepareStatement(queryQuantiteStock)) {
            preSQuantite.setInt(1, idArticle);
            try (ResultSet resultSet = preSQuantite.executeQuery()) {
                if (resultSet.next()) {
                    quantiteStock = resultSet.getLong("quantite");
                    if (quantiteStock < this.quantite) {
                        System.out.println("Quantité insuffisante pour l'article : " + this.nom);
                        return false;
                    }
                } else {
                    System.out.println("Article non trouvé pour vérifier la quantité : " + this.nom);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Mettre à jour la quantité du stock
        try (PreparedStatement updateStatement = connection.prepareStatement(queryMisJourtock)) {
            updateStatement.setLong(1, this.quantite);
            updateStatement.setInt(2, idArticle);
            int rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Article non trouvé ou quantité insuffisante");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Insérer dans Stock_Livre
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryLivraison)) {
            preparedStatement.setInt(1, this.codeService);
            preparedStatement.setInt(2, idArticle);
            preparedStatement.setLong(3, this.quantite);
            preparedStatement.setString(4, this.date);
            preparedStatement.executeUpdate();

            getAllStock(connection);
            System.out.println("Stock ajouté avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    // -------------------------------------------------------------------------
    public static List<Stock> chercheArticle(Connection connection, String motChercher) {
        List<Stock> listeArticle = new ArrayList<>();
        String query = "SELECT article, type, quantite, date_entre FROM Stock WHERE article LIKE ? OR type LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String recherche = "%" + motChercher + "%";
            statement.setString(1, recherche);
            statement.setString(2, recherche);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Stock stock = new Stock(connection);

                    stock.setNom(resultSet.getString("article"));
                    stock.setType(resultSet.getString("type"));
                    stock.setQuantite(resultSet.getLong("quantite"));
                    stock.setDate(resultSet.getString("date_entre")); // Corrigez le nom de la colonne ici

                    listeArticle.add(stock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listeArticle;
    }
  
}

 