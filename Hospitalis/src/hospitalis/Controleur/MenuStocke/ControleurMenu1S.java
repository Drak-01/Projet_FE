/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuStocke;

import hospitalis.Interface.componentSt.Menu1S;
import hospitalis.Model.Stock;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 *
 * @author badra
 */
public class ControleurMenu1S implements MouseListener, ListSelectionListener {
   //
    private Connection connection;
    private Menu1S menu1;
    private Stock stock;
    private ResultatRecherche resultat;
    private static ControleurMenu1S instance;
    //
    public ControleurMenu1S(Connection connection, Menu1S menu) {
        this.connection = connection;
        this.menu1 = menu;
        this.resultat = ResultatRecherche.getInstance();
        
        System.out.println("Appe creation menuCont1");
        
        this.menu1.btadd.addMouseListener(this);
        
        this.menu1.btChercher.addMouseListener(this);
        
        resultat.ferme.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultat.setVisible(false);
                 EffacerChamps();
            }
        });
        
    }
    //
    //Contrôle la creation des instances lors de l'arriver pour ne pas créer a chaque fois une autre instance ( surcharge).
    public static ControleurMenu1S getInstance(Connection connection, Menu1S menu) {
        
        if (instance == null) {
            synchronized (ControleurMenu1S.class) {
                if (instance == null) {
                    instance = new ControleurMenu1S(connection,menu);
                    System.out.println("Appel a l'instance de comtr me4444n");
                }
            }
        }
        return instance;
    }
    ///
    public void afficherMenu() {
        if (menu1.isVisible()) {
            menu1.setVisible(false);
        } else {
            menu1.setVisible(true);
        }
        updateTable();
    }
    //**************************************************************************

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == menu1.btadd){        
            handleAddStock();
        }else if (e.getSource() == menu1.btChercher){
            ChercheArticle();
        }
    }
    
    //////////
    private void handleAddStock() {
        try {
            String dateStr = formatDate(menu1.sdate.getDate());
            stock = new Stock(connection);
            stock.setNom(menu1.ipArt.getText());
            stock.setType((String) menu1.jtype.getSelectedItem());
            stock.setQuantite(Long.parseLong(menu1.ipQuant.getText()));
            stock.setDate(dateStr);
            
            if (menu1.ipArt.getText().isEmpty() || Long.parseLong(menu1.ipQuant.getText())< 0 || menu1.sdate == null ) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
            }

            boolean success = stock.ajouterStocke(connection);
            if (success) {
                JOptionPane.showMessageDialog(null, "Stock ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                EffacerChamps();
                updateTable(); // Mise à jour de la table après ajout
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de Stock.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
        menu1.ipArt.setText("");
        menu1.ipQuant.setText("");
        menu1.sdate.setDate(null);
        menu1.chercher.setText("");
        //menu1A.buttonGroup1.clearSelection();
        menu1.jtype.setSelectedIndex(0);
    }
    
    //Cette methodes mes permet de chercher les données et afficher
    private void chargementDeStocke() {
        List<Stock> stockList = Stock.getAllStock(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Article", "Type", "Quantité", "Date"}, 
            0
        );
        for (Stock stock : stockList) {
            model.addRow(new Object[]{
                stock.getNom(),
                stock.getType(),
                stock.getQuantite(),
                stock.getDate(),
            });
        }
        menu1.jtables.setModel(model);
    }
    

    private void updateTable() {
        chargementDeStocke();
        EffacerChamps();
    }
    //
    @Override
    public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = menu1.jtables.getSelectedRow();
                if (selectedRow >= 0) {
                    TableModel model = menu1.jtables.getModel();
                    menu1.ipArt.setText(model.getValueAt(selectedRow, 0).toString());
                    menu1.jtype.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
                    menu1.ipQuant.setText(model.getValueAt(selectedRow, 2).toString());

                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(selectedRow, 3).toString());
                        menu1.sdate.setDate(date);
                    } catch (ParseException ex) {
                        Logger.getLogger(ControleurMenu1S.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
    }
    private void ChercheArticle() {
        if (menu1.chercher.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir le champ de recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Sortir de la méthode si le champ est vide
        }

        System.out.println("Recherche en cours...");
        List<Stock> listeArticleTrouves = Stock.chercheArticle(connection, menu1.chercher.getText());

        if (!listeArticleTrouves.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) resultat.jtables.getModel();

            // Effacer les anciennes lignes
            model.setRowCount(0);

            // Ajouter les nouvelles lignes
            for (Stock stock : listeArticleTrouves) {
                model.addRow(new Object[]{
                    stock.getNom(),
                    stock.getType(),
                    stock.getQuantite(),
                    stock.getDate()
                });
            }
            resultat.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Aucun article trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    ///-------------------------------------------------------------------------
    
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
