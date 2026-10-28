/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuFinance;

import hospitalis.Interface.componentFi.Menu2;
import hospitalis.Model.Facture;
import hospitalis.Model.Rapport;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class Menu2Controleur implements MouseListener {
    
    private Connection connection;
    private Menu2 menu2;
    private Rapport rapport;
    private static Menu2Controleur instanceM2;
    private Facture facture;
    
    
    public Menu2Controleur(Connection connection , Menu2 menu2){
        this.connection = connection;
        this.menu2 =menu2;
        
        this.menu2.btchercher.addMouseListener(this);
    }

     public static Menu2Controleur getInstance(Connection connection,Menu2 menu2){
        if (instanceM2 == null) {
            synchronized (Menu2Controleur.class) {
                if (instanceM2 == null) {
                    instanceM2 = new Menu2Controleur(connection,menu2);
                    //System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM2;
     }
     public void afficherMenu(){
        if (menu2.isVisible()) {
            menu2.setVisible(false);
        } else {
            menu2.setVisible(true);
        }
        
        updateTable();
    }
    

    @Override
    public void mouseClicked(MouseEvent e) { 
        if (e.getSource() == menu2.btchercher) {
            chercherFacture();
        }
    }
    
    public void chercherFacture(){
        try {
            
            facture = new Facture(connection);
            facture.setIdFacture(menu2.numerFac.getText());
            
            
            if (menu2.numerFac.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
            }

            boolean success = facture.chercherFacturerAll();
            if (success) {
                JOptionPane.showMessageDialog(null, "Facture trouvée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Utiliser StringBuilder pour créer les détails ligne par ligne
                StringBuilder detailsBuilder = new StringBuilder();
                detailsBuilder.append("Détails de la facture:\n");
                detailsBuilder.append(facture.getDetails()).append("\n");
                detailsBuilder.append("Montant: ").append(facture.getMontant()).append(" DHS\n");

                menu2.details.setText(detailsBuilder.toString());
                EffacerChamps();
            } else {
                JOptionPane.showMessageDialog(null, "Facture Nom Trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void chargementDeFacturePayer() {
        List<Facture> factureList = Facture.getAllFacturePayer(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Numéro Facture", "Détails", "Montant", "Date", "Status"}, 
            0
        );
        for (Facture facture : factureList) {
            model.addRow(new Object[]{
                facture.getIdFacture(),
                facture.getDetails(),
                facture.getMontant(),
                facture.getDateFacture(),
                facture.getStatus(),
            });
        }
        menu2.jTable1.setModel(model);
    }
    private void chargementDeFactureImPayer() {
        List<Facture> factureList = Facture.getAllFacture(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Numéro Facture", "Détails", "Montant", "Date", "Status"}, 
            0
        );
        for (Facture facture : factureList) {
            model.addRow(new Object[]{
                facture.getIdFacture(),
                facture.getDetails(),
                facture.getMontant(),
                facture.getDateFacture(),
                facture.getStatus(),
            });
        }
        menu2.jTable2.setModel(model);
    }
    private void updateTable() {
        chargementDeFacturePayer();
        chargementDeFactureImPayer();
    }
    
    private void EffacerChamps(){
        menu2.numerFac.setText("");
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
