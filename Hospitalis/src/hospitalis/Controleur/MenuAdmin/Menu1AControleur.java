/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuAdmin;

import hospitalis.Interface.componentAD.Menu1A;
import hospitalis.Model.Service;
import hospitalis.Model.Utilisateurs;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.crypto.Cipher;

public class Menu1AControleur implements MouseListener , ListSelectionListener, ItemListener {
    private Connection connection;
    private Menu1A menu1A;
    private Utilisateurs users;
    private ResultRecherche resultat;
    private static Menu1AControleur instanceM1;
    
    public Menu1AControleur(Connection connection, Menu1A menu1A) {
        this.connection = connection;
        this.menu1A = menu1A;
        this.resultat = ResultRecherche.getInstance();
        
        
        System.out.println("Appe creation menuCont1");
        this.menu1A.btajouter.addMouseListener(this);
        this.menu1A.btsupprimer.addMouseListener(this);
        this.menu1A.btmodifier.addMouseListener(this);
        this.menu1A.btChercher.addMouseListener(this);
        resultat.ferme.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultat.setVisible(false);
                 EffacerChamps();
            }
        });
        //this.menu1A.jtables.getSelectionModel().addListSelectionListener(this);
        chargementDeUsers();
        //loadServiceTypes();
    }
    
    //Contrôle la creation des instances lors de l'arriver pour ne pas créer a chaque fois une autre instance ( surcharge).
    public static Menu1AControleur getInstance(Connection connection, Menu1A menu1A) {
        if (instanceM1 == null) {
            synchronized (Menu1AControleur.class) {
                if (instanceM1 == null) {
                    instanceM1 = new Menu1AControleur(connection,menu1A);
                    System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM1;
    }


    public void setMenu(Menu1A menu1A) {
        this.menu1A = menu1A;
        if (this.menu1A == null) {
            System.out.println("Le menu est null");
        } else {
            System.out.println("Le menu n'est pas null");
            this.menu1A.btajouter.addMouseListener(this);
            this.menu1A.btsupprimer.addMouseListener(this);
            this.menu1A.btmodifier.addMouseListener(this);
        }
    }
   
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu1A.btajouter) {
            handleAddUser();
        } else if (e.getSource() == menu1A.btsupprimer) {
            handleDeleteUser();
        } else if (e.getSource() == menu1A.btmodifier) {
            try {
                handleModifyUser();
            } catch (ParseException ex) {
                Logger.getLogger(Menu1AControleur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if (e.getSource() == menu1A.btChercher){
            chercherUser();
        }
         EffacerChamps();
    }
    //*******************************

    /**
     *
     * @param e
     * @throws ParseException
     */

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = menu1A.jtables.getSelectedRow();
            if (selectedRow >= 0) {
                TableModel model = menu1A.jtables.getModel();
                menu1A.inputMatric.setText(model.getValueAt(selectedRow, 0).toString());
                menu1A.inputNom.setText(model.getValueAt(selectedRow, 1).toString());
                menu1A.inputPrenom.setText(model.getValueAt(selectedRow, 2).toString());

                try {
                    Date dateNaissance = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(selectedRow, 3).toString());
                    menu1A.inputNaiss.setDate(dateNaissance);
                } catch (ParseException ex) {
                    Logger.getLogger(Menu1AControleur.class.getName()).log(Level.SEVERE, null, ex);
                }

                menu1A.inputPass.setText(model.getValueAt(selectedRow, 4).toString());
                menu1A.inputTel.setText(model.getValueAt(selectedRow, 5).toString());
                menu1A.inputSpe.setText(model.getValueAt(selectedRow, 6).toString());
                menu1A.inputMai.setText(model.getValueAt(selectedRow, 7).toString());
                menu1A.inputRole.setSelectedItem(model.getValueAt(selectedRow, 8).toString());

                String sexe = model.getValueAt(selectedRow, 9).toString();
                if ("Homme".equals(sexe)) {
                    menu1A.boutonHomme.setSelected(true);
                } else {
                    menu1A.boutonFemme.setSelected(true);
                }
            }
        }
    }

    //-----------------455----

    private void handleAddUser() {
    try {
        
        
        if (menu1A.inputMai.getText().isEmpty() || menu1A.inputNom.getText().isEmpty() || menu1A.inputPrenom.getText().isEmpty() || menu1A.inputPass.getText().isEmpty() ) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
        }
        String dateNaissanceStr = formatDate(menu1A.inputNaiss.getDate());
        users = new Utilisateurs(connection);
        users.setMatricule(menu1A.inputMatric.getText());
        users.setNom(menu1A.inputNom.getText());
        users.setPrenom(menu1A.inputPrenom.getText());
        users.setDateNaissance(dateNaissanceStr);
        users.setPassword(new String(menu1A.inputPass.getPassword()));//LE Cryptage de la password 
        users.setTelephone(Long.parseLong(menu1A.inputTel.getText())); // Utilisez long ici
        users.setSpecialite(menu1A.inputSpe.getText());
        users.setEmail(menu1A.inputMai.getText());
        users.setRole((String) menu1A.inputRole.getSelectedItem());
        users.setSexe(menu1A.boutonHomme.isSelected() ? "Homme" : "Femme");

        boolean success = users.ajouterCompte(connection);
        if (success) {
            JOptionPane.showMessageDialog(null, "Utilisateur ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            EffacerChamps();
            updateTable(); // Mise à jour de la table après ajout
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
        menu1A.cherche.setText("");
        menu1A.inputMatric.setText("");
        menu1A.inputNom.setText("");
        menu1A.inputPrenom.setText("");
        menu1A.inputPass.setText("");
        menu1A.inputTel.setText("");
        menu1A.inputSpe.setText("");
        menu1A.inputMai.setText("");
        menu1A.inputNaiss.setDate(null);
        //menu1A.buttonGroup1.clearSelection();
        menu1A.inputRole.setSelectedIndex(0);
    }
    public void afficherMenu() {
        if (menu1A.isVisible()) {
            menu1A.setVisible(false);
        } else {
            menu1A.setVisible(true);
        }
         EffacerChamps();
    }
    private void handleDeleteUser() {
        int selectedRow = menu1A.jtables.getSelectedRow();
        if (selectedRow >= 0) {
            String matricule = (String) menu1A.jtables.getValueAt(selectedRow, 0);
            int response = JOptionPane.showConfirmDialog(null, 
                "Êtes-vous sûr de vouloir supprimer l'utilisateur avec le matricule : " + matricule + " ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                users = new Utilisateurs(connection);
                users.setMatricule(matricule);

                boolean success = users.supprimerCompte(connection);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Utilisateur supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    EffacerChamps();
                    updateTable(); // Mise à jour de la table après suppression
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de l'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } // Si la réponse est NO, ne faites rien
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un utilisateur à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        EffacerChamps();
    }

    private void handleModifyUser() throws ParseException {
        // Créer une nouvelle instance de Utilisateurs avec les informations saisies
        users = new Utilisateurs(connection);

        // Récupération des données saisies dans les champs de saisie
        String matricule = menu1A.inputMatric.getText();
        String nom = menu1A.inputNom.getText();
        String prenom = menu1A.inputPrenom.getText();
        String dateNaissanceStr = formatDate(menu1A.inputNaiss.getDate());
        String password = new String(menu1A.inputPass.getPassword());
        String specialite = menu1A.inputSpe.getText();
        String email = menu1A.inputMai.getText();
        String role = (String) menu1A.inputRole.getSelectedItem();
        String sexe = menu1A.boutonHomme.isSelected() ? "Homme" : "Femme";

        // Vérifie si le champ de téléphone n'est pas vide avant de le convertir en Long
        String telephoneStr = menu1A.inputTel.getText();
        long telephone = 0;
        if (!telephoneStr.isEmpty()) {
            try {
                telephone = Long.parseLong(telephoneStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Erreur: Le numéro de téléphone doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Définir les informations dans l'objet Utilisateurs
        users.setMatricule(matricule);
        users.setNom(nom);
        users.setPrenom(prenom);
        users.setDateNaissance(dateNaissanceStr);
        users.setPassword(password);
        users.setSpecialite(specialite);
        users.setEmail(email);
        users.setRole(role);
        users.setSexe(sexe);
        users.setTelephone(telephone);

        try {
            // Modifier l'utilisateur dans la base de données
            boolean success = users.modifierCompte(connection);
            if (success) {
                JOptionPane.showMessageDialog(null, "Utilisateur modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                EffacerChamps();
                updateTable(); // Mise à jour de la table après modification
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification de l'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        //EffacerChamps();
    }
        
    //Cette methodes mes permet de chercher les données et afficher
    private void chargementDeUsers() {
        List<Utilisateurs> userList = Utilisateurs.getAllUsers(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Matricule", "Nom", "Prenom", "Date de Naissance", "Mot de Passe", "Telephone", "Specialite", "Email", "Role", "Sexe"}, 
            0
        );
        for (Utilisateurs user : userList) {
            model.addRow(new Object[]{
                user.getMatricule(),
                user.getNom(),
                user.getPrenom(),
                user.getDateNaissance(),
                user.getPassword(),
                user.getTelephone(),
                user.getSpecialite(),
                user.getEmail(),
                user.getRole(),
                user.getSexe()
            });
        }
        menu1A.jtables.setModel(model);
    }

    private void updateTable() {
        chargementDeUsers();
        //loadServiceTypes();
    }
    //-----------------------------------------------------
    private void chercherUser() {
        if (menu1A.cherche.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous le champs cherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Sortir de la méthode si les champs sont vides
        }        
        //users = new Utilisateurs(connection);
        
        List<Utilisateurs> utilisateursTrouves = Utilisateurs.rechercherUtilisateurs(connection, menu1A.cherche.getText());

        if (!utilisateursTrouves.isEmpty()) {
            // Effacer le modèle actuel de la table de recherche
            DefaultTableModel model = (DefaultTableModel) resultat.jtblRecherche.getModel();
            model.setRowCount(0);

            // Remplir le modèle avec les résultats de la recherche
            for (Utilisateurs utilisateur : utilisateursTrouves) {
                model.addRow(new Object[]{
                    utilisateur.getMatricule(),
                    utilisateur.getNom(),
                    utilisateur.getPrenom(),
                    utilisateur.getDateNaissance(),
                    utilisateur.getPassword(),
                    utilisateur.getTelephone(),
                    utilisateur.getSpecialite(),
                    utilisateur.getEmail(),
                    utilisateur.getRole(),
                    utilisateur.getSexe()
                });
            }
            
            resultat.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Aucun utilisateur trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        //EffacerChamps();
    }
    
    //Liste des services
    /*
    private void loadServiceTypes() {
        List<String> serviceTypes = Service.getAllServiceTypes(connection);
        Set<String> uniqueServiceTypes = new HashSet<>(serviceTypes);

        // Efface la JComboBox avant d'ajouter de nouvelles valeurs pour éviter les doublons
        menu1A.service.removeAllItems();

        // Ajouter les types de services uniques à la JComboBox
        for (String serviceType : uniqueServiceTypes) {
            menu1A.service.addItem(serviceType);
        }
    }*/
    private void chargementDisplay(String mot){
        List<Utilisateurs> userList = Utilisateurs.getAllUsers(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Matricule", "Nom", "Prenom", "Date de Naissance", "Mot de Passe", "Telephone", "Specialite", "Email", "Role", "Sexe"},
            0
        );
        for (Utilisateurs user : userList) {
            model.addRow(new Object[]{
                user.getMatricule(),
                user.getNom(),
                user.getPrenom(),
                user.getDateNaissance(),
                user.getPassword(),
                user.getTelephone(),
                user.getSpecialite(),
                user.getEmail(),
                user.getRole(),
                user.getSexe()
            });
        }
        menu1A.jtables.setModel(model);
    }
    
    //-----------------------------------------------------
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            String selectItem = (String) menu1A.display.getSelectedItem();
            chargementDisplay(selectItem);
        }
    }
}
