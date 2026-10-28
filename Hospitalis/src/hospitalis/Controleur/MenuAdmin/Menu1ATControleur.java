/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuAdmin;

import hospitalis.Interface.componentAD.Menu1AT;
import hospitalis.Model.Traitement;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.text.ParseException;
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
public class Menu1ATControleur implements MouseListener, ListSelectionListener {
    //
    private Connection connection;
    private Menu1AT menu;
    private Traitement traitement;
    private static Menu1ATControleur instanceM3;
    //-----------------
    
    public Menu1ATControleur(Connection connection, Menu1AT menu) {
        this.connection = connection;
        this.menu = menu;
        
        System.out.println("Appe creation menuCont1");
        
        this.menu.btajouter.addMouseListener(this);
        this.menu.btsupprimer.addMouseListener(this);
        this.menu.btmodifier.addMouseListener(this);
        this.menu.jtables.getSelectionModel().addListSelectionListener(this);
        //chargementDeTraitement();
    
    }
    //Contrôle la creation des instances lors de l'arriver pour ne pas créer a chaque fois une autre instance ( surcharge).
    public static Menu1ATControleur getInstance(Connection connection, Menu1AT menu) {
        if (instanceM3 == null) {
            synchronized (Menu1AControleur.class) {
                if (instanceM3== null) {
                    instanceM3 = new Menu1ATControleur(connection,menu);
                    System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM3;
    }
    public void setMenu(Menu1AT menu1A) {
        this.menu = menu1A;
    }
    public void afficherMenu(){// me permet de pas avoir de menu chaque fois
       if(menu.isVisible()){
            menu.setVisible(false);
        }else{
            menu.setVisible(true);
        }  
       updateTable();
    }
    //Methode 
    ///-------------------------------------------------------------------------
    private void handleAddTraitement() {
    try {
        if (menu.ipcodeT.getText().isEmpty() || menu.jdes.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
        }
        Traitement traitement = new Traitement(connection);
        
        traitement.setCodeTraitement(Integer.parseInt(menu.ipcodeT.getText()));// Il faut convertire 
        traitement.setNomTraitement(menu.ipNom.getText());
        traitement.setDesTraitement(menu.jdes.getText());
        traitement.setType(menu.type.getText());
        traitement.setPrix(Double.parseDouble(menu.type.getText()));
        System.out.println();
        
        boolean success = traitement.ajouterTraitement(connection);
        if (success) {
            JOptionPane.showMessageDialog(null, "Traitement ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            EffacerChamps();
            updateTable();
            // Mise à jour de la table après ajout
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de Service.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    ///-------------------------------------------------------------------------
    private void handleModifyTraitement() throws ParseException {
        int selectedRow = menu.jtables.getSelectedRow();
        if (selectedRow >= 0) {
            int codeTraitement = (int) menu.jtables.getValueAt(selectedRow, 0);
            
            traitement = new Traitement(connection);
            
            traitement.setCodeTraitement(codeTraitement);
            traitement.setNomTraitement(menu.ipNom.getText());
            traitement.setDesTraitement(menu.jdes.getText());
            traitement.setPrix(Double.parseDouble(menu.type.getText()));

            try {
                // Récupération des données actuelles du tableau

                int currentCodeTraitement = (int) menu.jtables.getValueAt(selectedRow, 0);               
                String currentNom = (String) menu.jtables.getValueAt(selectedRow, 1);
                String currentDes = (String) menu.jtables.getValueAt(selectedRow, 2);
                Double currentPrix = (Double) menu.jtables.getValueAt(selectedRow, 3);

                // Comparaison avec les valeurs actuelles et mise à jour si nécessaire
                if (codeTraitement != currentCodeTraitement) {
                    traitement.setCodeTraitement(codeTraitement);
                }
                if (!menu.ipNom.getText().equals(currentNom)) {
                    traitement.setNomTraitement(menu.ipNom.getText());
                }
                if (!menu.jdes.getText().equals(currentDes)) {
                    traitement.setDesTraitement(menu.jdes.getText());
                }
                if (Double.parseDouble(menu.type.getText()) != currentPrix) {
                    traitement.setPrix(Double.parseDouble(menu.type.getText()));
                }
                // Répétez le processus pour les autres champs

                boolean success = traitement.modifierTraitement(connection);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Traitement modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    updateTable(); // Mise à jour de la table après modification
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la modification de Traitement", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification de Traitement  : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un Service à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    //***************************************************************************************
    private void handleDeleteTraitement() {
        int selectedRow = menu.jtables.getSelectedRow();
        if (selectedRow >= 0) {
            int codeTraitement = (int) menu.jtables.getValueAt(selectedRow, 0);

            // Afficher la boîte de dialogue de confirmation
            int response = JOptionPane.showConfirmDialog(null, 
                "Êtes-vous sûr de vouloir supprimer le traitement avec le code : " + codeTraitement + " ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                traitement = new Traitement(connection);
                traitement.setCodeTraitement(codeTraitement);

                boolean success = traitement.supprimerTraitement(connection);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Traitement supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    updateTable(); // Mise à jour de la table après suppression
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du traitement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un traitement à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    //***************************************************************************************
     private void chargementDeTraitement() {
        List<Traitement> traitementList = Traitement.getAllTraitement(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Code Service","Type", "Nom", "Description","Prix"}, 
            0
        );
        for (Traitement traitement: traitementList) {
            model.addRow(new Object[]{
                traitement.getCodeTraitement(),
                traitement.getType(),
                traitement.getNomTraitement(),
                traitement.getDesTraitement(),
                traitement.getPrix(),
            });
        }
        menu.jtables.setModel(model);
    }
    //**************************************************************************************
    //***************************************************************************************
    
    private void EffacerChamps() {
        menu.ipcodeT.setText("");
        menu.ipNom.setText("");
        menu.jdes.setText("");
        menu.type.setText("");
        menu.ipPrix1.setText("");

    }
    //***************************************************************************************
    private void updateTable() {
       chargementDeTraitement();
       EffacerChamps();
    }
   //***************************************************************************************

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btajouter) {
            handleAddTraitement();
        } else if (e.getSource() == menu.btsupprimer) {
            handleDeleteTraitement();
        } else if (e.getSource() == menu.btmodifier) {
           
            try {
                handleModifyTraitement();
            } catch (ParseException ex) {
                Logger.getLogger(Menu1AControleur.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = menu.jtables.getSelectedRow();
            if (selectedRow >= 0) {
                TableModel model = menu.jtables.getModel();
                menu.ipcodeT.setText(model.getValueAt(selectedRow, 0).toString());
                menu.ipNom.setText(model.getValueAt(selectedRow, 2).toString());
                menu.ipPrix1.setText(model.getValueAt(selectedRow, 4).toString());
                menu.jdes.setText(model.getValueAt(selectedRow, 3).toString()); 
                menu.type.setText(model.getValueAt(selectedRow, 1).toString());               
            }
        }
    }
    
}
