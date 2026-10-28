/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuFinance;

import hospitalis.Interface.componentFi.Menu3;
import hospitalis.Model.Facture;
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
 * @author pc
 */
public class Menu3Controleur implements MouseListener{
    
    private Facture facture;
    private final Connection connection;
    private final Menu3 menu3;
    private static Menu3Controleur instanceM3;
    
    public Menu3Controleur(Connection connection, Menu3 menu3) {
        this.connection = connection;
        this.menu3 = menu3;
        this.menu3.btpayer.addMouseListener(this);
        this.menu3.chercher.addMouseListener(this);
       

    }
    public static Menu3Controleur getInstance(Connection connection, Menu3 menu3) {
        if (instanceM3 == null) {
            synchronized (Menu3Controleur.class) {
                if (instanceM3 == null) {
                    instanceM3 = new Menu3Controleur(connection,menu3);
                   // System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM3;
    }
    
    public void afficherMenu(){
        if (menu3.isVisible()) {
            menu3.setVisible(false);
        } else {
            menu3.setVisible(true);
        }
        menu3.jdetails.setText("");
        updateTable();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu3.btpayer) {
            payerFacture();
        } else if (e.getSource() == menu3.chercher) {
            chercherFacture();
        }
    }
  
    public void payerFacture() {
        try {
            if (menu3.numerFac.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
            }

            String dateStr = formatDate(new Date());

            facture = new Facture(connection);
            facture.setIdFacture(menu3.numerFac.getText());
            facture.setDateFacture(dateStr);

            boolean success = facture.paiementFacture();
            System.out.println("ture+++"+ success);
            if (success) {
                JOptionPane.showMessageDialog(null, "Paiement effectué avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                effacerChamps();
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur de paiement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void chercherFacture(){        
            
            if (menu3.numerFac.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
            }
            facture = new Facture(connection);
            facture.setIdFacture(menu3.numerFac.getText());

            boolean success = facture.chercherFacturer();
            if (success) {
                JOptionPane.showMessageDialog(null, "Facture Trouvé avec ", "Succès", JOptionPane.INFORMATION_MESSAGE);
                StringBuilder detailsBuilder = new StringBuilder();
                detailsBuilder.append("Détails de la facture:\n");
                detailsBuilder.append(facture.getDetails()).append("\n");
                detailsBuilder.append("Montant: ").append(facture.getMontant()).append(" DHS\n");

                menu3.jdetails.setText(detailsBuilder.toString());
                effacerChamps();
            } else {
                JOptionPane.showMessageDialog(null, "Facture Nom Trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
    }
    
    public void effacerChamps(){
        menu3.numerFac.setText("");
       //
    }
    
    private String formatDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    //************************************
    private void updateTable() {
        chargementDeFacturePayer();
    }
    
    //-----------------------------------
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
        menu3.table.setModel(model);
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
