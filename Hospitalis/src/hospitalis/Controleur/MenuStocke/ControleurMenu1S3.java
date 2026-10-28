/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuStocke;

import hospitalis.Interface.componentSt.Menu1S3;
import hospitalis.Model.Service;
import hospitalis.Model.Stock;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author badra
 */
public class ControleurMenu1S3 implements MouseListener {
    //
    private Connection connection;
    private final Menu1S3 menu;
    private Stock stock;
    private static ControleurMenu1S3 instance;
   
    public ControleurMenu1S3(Connection connection, Menu1S3 menu) {
        this.connection = connection;
        this.menu = menu;
        System.out.println("Appel creation menuStocke1 ");
        
        this.menu.btLivre.addMouseListener(this);
        //chargementDesLivraisons();
    }

    public static ControleurMenu1S3 getInstance(Connection connection, Menu1S3 menu) {
        if (instance == null) {
            synchronized (ControleurMenu1S3.class) {
                if (instance == null) {
                    instance = new ControleurMenu1S3(connection, menu);
                    System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instance;
    }

    public void afficherMenu() {
        if (menu.isVisible()) {
            menu.setVisible(false);
        } else {
            menu.setVisible(true);
        }
        updateTable();
    }

    private void loadServiceTypes() {
        List<String> serviceTypes = Service.getAllServiceTypes(connection);
        Set<String> uniqueServiceTypes = new HashSet<>(serviceTypes);

        // Efface la JComboBox avant d'ajouter de nouvelles valeurs pour éviter les doublons
        menu.service.removeAllItems();

        // Ajouter les types de services uniques à la JComboBox
        for (String serviceType : uniqueServiceTypes) {
            menu.service.addItem(serviceType);
        }
    }

    private void loadArticleNames() {
        List<String> articleList = Stock.getArticleList(connection);
        Set<String> uniqueArticles = new HashSet<>(articleList);

        // Efface la JComboBox avant d'ajouter de nouvelles valeurs pour éviter les doublons
        menu.Article.removeAllItems();

        // Ajouter les articles uniques à la JComboBox
        for (String article : uniqueArticles) {
            menu.Article.addItem(article);
        }
    }
    ///
    private void handleLivraison() {
        try {
            
            if (menu.ipArtQua_Livre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
            }
            String dateStr = formatDate(menu.jdateLivre.getDate());
            stock = new Stock(connection);
            Service service = new Service(connection);
            service.setNomService((String) menu.service.getSelectedItem());
            stock.setCodeService(service.getServiceCode());           
            stock.setNom((String) menu.Article.getSelectedItem());
            stock.setQuantite(Long.parseLong(menu.ipArtQua_Livre.getText()));
            stock.setDate(dateStr);
            

            boolean success = stock.stockLivraison(connection);
            if (success) {
                JOptionPane.showMessageDialog(null, "Succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                EffacerChamps();
                updateTable(); // Mise à jour de la table après ajout
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de livraison.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
    }
    private String formatDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    private void EffacerChamps() {
        menu.ipArtQua_Livre.setText("");
        menu.jdateLivre.setDate(null);
        //menu1A.buttonGroup1.clearSelection();
        menu.service.setSelectedIndex(0);        
        menu.Article.setSelectedIndex(0);

    }
    ///*
    
    private void chargementDesLivraisons() {
        List<Stock> livraisons = Stock.getListeLivraison(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Service", " Article", "Quantité", "Date"}, 
            0
        );

        for (Stock livraison : livraisons) {
            model.addRow(new Object[]{
                livraison.getNom(),
                livraison.getServiceLivre(),
                livraison.getQuantite(),
                livraison.getDate(),
            });
        }

        menu.tbenregistre.setModel(model);
    }
     private void updateTable() {
        chargementDesLivraisons();
        loadServiceTypes();
        loadArticleNames();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btLivre) {
            handleLivraison();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}