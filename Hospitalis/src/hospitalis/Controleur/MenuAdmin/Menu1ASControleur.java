/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuAdmin;

import hospitalis.Interface.componentAD.Menu1AS;
import hospitalis.Model.Service;
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
public class Menu1ASControleur implements MouseListener, ListSelectionListener {
     
    private Connection connection;
    private Menu1AS menu;
    private Service service;
    private static Menu1ASControleur instanceM2;
    //-----------------
    
    public Menu1ASControleur(Connection connection, Menu1AS menu) {
        this.connection = connection;
        this.menu = menu;
        
        System.out.println("Appe creation menuCont1");
        
        this.menu.btajouter.addMouseListener(this);
        this.menu.btsupprimer.addMouseListener(this);
        this.menu.btmodifier.addMouseListener(this);
        this.menu.jtables.getSelectionModel().addListSelectionListener(this);
        //chargementDeService();
        
    }
    
    //Contrôle la creation des instances lors de l'arriver pour ne pas créer a chaque fois une autre instance ( surcharge).
    public static Menu1ASControleur getInstance(Connection connection, Menu1AS menu) {
        if (instanceM2 == null) {
            synchronized (Menu1AControleur.class) {
                if (instanceM2 == null) {
                    instanceM2 = new Menu1ASControleur(connection,menu);
                    System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM2;
    }

    //------------------------------------------------------------------------------------
    public void setMenu(Menu1AS menu1A) {
        this.menu= menu1A;
    }
     //------------------------------------------------------------------------------------
    // Permet d'affiche a l'ecran et de controle l'ouverture
    public void afficherMenu(){
       if(menu.isVisible()){
            menu.setVisible(false);
        }else{
            menu.setVisible(true);
        }
       updateTable();
    }
        //------------------------------------------------------------------------------------
    
    
    //Suivie des Clicked de
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.btajouter) {
            handleAddService();
        } else if (e.getSource() == menu.btsupprimer) {
            handleDeleteService();
        } else if (e.getSource() == menu.btmodifier) {
           
            try {
                handleModifyService();
            } catch (ParseException ex) {
                Logger.getLogger(Menu1AControleur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //--------------------------------------------------------------------------
    private void handleAddService() {
        try {
            if (menu.NomService.getText().isEmpty() || menu.jdes.getText().isEmpty() ) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return; // Sortir de la méthode si les champs sont vides
            }
            Service service = new Service(connection);

            service.setCodeService(Integer.parseInt(menu.codeService.getText()));// Il faut convertire 
            service.setNomService(menu.NomService.getText());
            service.setDesService(menu.jdes.getText());

            boolean success = service.ajouterService(connection);
            if (success) {
                JOptionPane.showMessageDialog(null, "Service ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
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
    //***************************************************************************************
    private void handleModifyService() throws ParseException {
        int selectedRow = menu.jtables.getSelectedRow();
        if (selectedRow >= 0) {
            int codeService = (int) menu.jtables.getValueAt(selectedRow, 0);
            
            service = new Service(connection);
            service.setCodeService(codeService);
            service.setNomService(menu.NomService.getText());
            service.setDesService(menu.jdes.getText());

            try {
                // Récupération des données actuelles du tableau

                int currentCodeService = (int) menu.jtables.getValueAt(selectedRow, 0);
                String currentNom = (String) menu.jtables.getValueAt(selectedRow, 1);
                String currentDes = (String) menu.jtables.getValueAt(selectedRow, 2);

                // Comparaison avec les valeurs actuelles et mise à jour si nécessaire
                if (codeService != currentCodeService) {
                    service.setCodeService(codeService);
                }
                if (!menu.NomService.getText().equals(currentNom)) {
                    service.setNomService(menu.NomService.getText());
                }
                if (!menu.jdes.getText().equals(currentDes)) {
                    service.setDesService(menu.jdes.getText());
                }
                // Répétez le processus pour les autres champs

                boolean success = service.modifierService(connection);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Service modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    updateTable(); // Mise à jour de la table après modification
                    EffacerChamps();
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la modification de Service", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un Service à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    //***************************************************************************************
    private void handleDeleteService() {
        int selectedRow = menu.jtables.getSelectedRow();
        if (selectedRow >= 0) {
            int codeService = (int) menu.jtables.getValueAt(selectedRow, 0);

            // Afficher la boîte de dialogue de confirmation
            int response = JOptionPane.showConfirmDialog(null, 
                "Êtes-vous sûr de vouloir supprimer le service avec le code : " + codeService + " ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                service = new Service(connection);
                service.setCodeService(codeService);

                boolean success = service.supprimerService(connection);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Service supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    updateTable(); // Mise à jour de la table après suppression
                    EffacerChamps();
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du service.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un service à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    //***************************************************************************************
    
    private void EffacerChamps() {
        menu.codeService.setText("");
        menu.NomService.setText("");
        menu.jdes.setText("");
    }
    //***************************************************************************************
    private void updateTable() {
        chargementDeService();
        EffacerChamps();
    }
   //***************************************************************************************
    private void chargementDeService() {
        List<Service> serviceList = Service.getAllService(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Code Service", "Nom", "Description"}, 
            0
        );
        for (Service service: serviceList) {
            model.addRow(new Object[]{
                service.getCodeService(),
                service.getNomService(),
                service.getDesService(),
            });
        }
        menu.jtables.setModel(model);
    }
    //**************************************************************************************
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
                menu.codeService.setText(model.getValueAt(selectedRow, 0).toString());
                menu.NomService.setText(model.getValueAt(selectedRow, 1).toString());
                menu.jdes.setText(model.getValueAt(selectedRow, 2).toString());               
            }
        }
    }

    
}
