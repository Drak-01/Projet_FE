/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuMedecin;

import hospitalis.Controleur.Authentification;
import hospitalis.Interface.componentMe.Menu4;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author pc
 */
public class Menu4Controleur implements MouseListener{
    
    
    private  Connection connection;
    private Menu4 menu4;
    private static Menu4Controleur instanceM4;
    private  Menu1Controleur menu1c;
    private int patientId;
    public Menu4Controleur(Connection connection, Menu4 menu4, Menu1Controleur menu1Controleur) {
        this.connection = connection;
        this.menu4 = menu4;
        this.menu1c = menu1Controleur; // Initialisation du champ Menu1Controleur
        this.menu4.btajout.addMouseListener(this);
        this.menu4.btsupp.addMouseListener(this);
        fetchDataToTable();
    }
    public static Menu4Controleur getInstance(Connection connection,Menu4 menu4 ,Menu1Controleur in){
        if (instanceM4 == null) {
            synchronized (Menu4Controleur.class) {
                if (instanceM4 == null) {
                    instanceM4 = new Menu4Controleur(connection,menu4,in);
                }
            }
        }
        return instanceM4;       
    }   
     public void afficherMenu(){
        if (menu4.isVisible()) {
            menu4.setVisible(false);
        } else {
            menu4.setVisible(true);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu4.btajout) {
            handleAddPatient();
        } else if (e.getSource() == menu4.btsupp) {
            handleDeletePatient();
        } else if (e.getSource() == menu4.btmodi) {
            handleModifyPatient();
        }
    }   
    public void handleAddPatient() {
       
    if (menu4.inputdatenais.getDate() == null || menu4.inputnompatient.getText().isEmpty() || menu4.inputetatcivil.getSelectedItem() == null 
        || menu4.inputlien.getText().isEmpty() || menu4.inputadresse.getText().isEmpty() || menu4.inputassureur.getText().isEmpty()
        || menu4.inputtelephone.getText().isEmpty() || menu4.inputpoids.getText().isEmpty() || menu4.inputprofession.getText().isEmpty()
        || menu4.inputsanguin.getSelectedItem() == null || menu4.inputtaille.getText().isEmpty()) {
        
        JOptionPane.showMessageDialog(menu4, "Veuillez remplir tous les champs", "Champs vides", JOptionPane.ERROR_MESSAGE);
    } else {
        try {
            String datenais = formatDate(menu4.inputdatenais.getDate());
            String nom = menu4.inputnompatient.getText();
            String prenom = menu4.inputprenompatient.getText();
            String lien = menu4.inputlien.getText();
            String assureur = menu4.inputassureur.getText();
            String adresse = menu4.inputadresse.getText();
            String profession = menu4.inputprofession.getText();
            String taille = menu4.inputtaille.getText();
            String poids = menu4.inputpoids.getText();
            String sanguin = (String) menu4.inputsanguin.getSelectedItem();
            String etatcivil = (String) menu4.inputetatcivil.getSelectedItem();
            String telephone = menu4.inputtelephone.getText();

            String sql = "INSERT INTO patient_med (id, dateNais, nom, prenom, sexe, etatcivil, telephone, lien, adresse, assureur, profession, taille, poids, sanguin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                patientId = genererPatientId();
                preparedStatement.setInt(1, patientId);
                preparedStatement.setString(2, datenais);
                preparedStatement.setString(3, nom);
                preparedStatement.setString(4, prenom);
                if (menu4.inputsexeH.isSelected()) {
                    preparedStatement.setString(5, "Masculin");
                } else if (menu4.inputsexe.isSelected()) {
                    preparedStatement.setString(5, "Féminin");
                }
                preparedStatement.setString(6, etatcivil);
                preparedStatement.setString(7, telephone);
                preparedStatement.setString(8, lien);
                preparedStatement.setString(9, adresse);
                preparedStatement.setString(10, assureur);
                preparedStatement.setString(11, profession);
                preparedStatement.setString(12, taille);
                preparedStatement.setString(13, poids);
                preparedStatement.setString(14, sanguin);
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                     // Créer un dossier médical pour le patient
                    String insertDossierSQL = "INSERT INTO dossier (idpatient, nom_P, prenom_P, infos_medi, prescription, resultats_test, date , matricule_med) VALUES (?,?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement dossierStatement = connection.prepareStatement(insertDossierSQL)) {
                            // Insérer les informations du dossier médical
                            dossierStatement.setInt(1, patientId);
                            dossierStatement.setString(2, nom);
                            dossierStatement.setString(3, prenom);
                            dossierStatement.setString(4, ""); // À remplir avec les informations médicales
                            dossierStatement.setString(5, ""); // À remplir avec la prescription
                            dossierStatement.setString(6, ""); // À remplir avec les résultats des tests
                            dossierStatement.setString(7, formatDate(new Date())); // Date actuelle
                            dossierStatement.setString(8, Authentification.matMed);
                            int dossierInserted = dossierStatement.executeUpdate(); // Ajout du dossier médical
                            if (dossierInserted > 0) {
                                JOptionPane.showMessageDialog(menu4, "Patient ajouté avec succès!");
                                fetchDataToTable();
                                menu1c.fetchDataToTable();
                                menu4.inputdatenais.setDate(null);
                                menu4.inputnompatient.setText("");
                                menu4.inputprenompatient.setText("");
                                menu4.inputlien.setText("");
                                menu4.inputassureur.setText("");
                                menu4.inputadresse.setText("");
                                menu4.inputprofession.setText("");
                                menu4.inputtaille.getText();
                                menu4.inputpoids.setText("");
                                menu4.inputsanguin.setSelectedItem("");
                                menu4.inputetatcivil.setSelectedItem("");
                                menu4.inputtelephone.setText("");
                                
                            } else {
                                JOptionPane.showMessageDialog(menu4, "Erreur lors de la création du dossier médical.");
                            }
                    } catch(SQLException e){
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(menu4, "Erreur lors de l'ajout du dossier médical.");
                    }   
                } else {
                    JOptionPane.showMessageDialog(menu4, "Échec de la création du patient.");
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu4, "Erreur lors de l'ajout du patient.");
        }
    }
}   
    public void handleDeletePatient() {
        int selectedRow = menu4.tablepatient.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu4, "Veuillez sélectionner un patient à supprimer.");
            return;
        }
        Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
            menu4,
            "Voulez-vous vraiment supprimer ce patient ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
         );
        if(response == JOptionPane.YES_OPTION){
        int patientId = (int) menu4.tablepatient.getValueAt(selectedRow, 0);
        try {
            String sql = "DELETE FROM patient_med WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, patientId);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(menu4, "Patient supprimé avec succès!");
                    fetchDataToTable();
                } else {
                    JOptionPane.showMessageDialog(menu4, "Aucun patient trouvé avec cet identifiant.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu4, "Erreur lors de la suppression du patient.");
        }
    }
    }
      public void handleModifyPatient() {
        int selectedRow = menu4.tablepatient.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu4, "Veuillez sélectionner un patient à modifier.");
            return;
        }
        int patientId = (int) menu4.tablepatient.getValueAt(selectedRow, 0);
        try {
            String datenais = formatDate(menu4.inputdatenais.getDate());
            String nom = menu4.inputnompatient.getText();
            String prenom = menu4.inputprenompatient.getText();
            String sexe = menu4.inputsexe.getText();    
            String etatcivil = (String) menu4.inputetatcivil.getSelectedItem();
            String telephone = menu4.inputlien.getText();
            String lien = menu4.inputlien.getText();
            String assureur = menu4.inputassureur.getText();
            String adresse = menu4.inputadresse.getText();
            String profession = menu4.inputprofession.getText();
            String taille = menu4.inputtaille.getText();
            String poids = menu4.inputpoids.getText();
            String sanguin =(String)menu4.inputsanguin.getSelectedItem();
            String sql = "UPDATE patient_med SET dateNais = ?, nom = ?, prenom = ?, sexe = ?, etatcivil = ?, telephone = ? , sanguin =? , poids=?,taille=?"
                    + ",lien=?,assureur=?,profession=?,adresse=? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, datenais);
                preparedStatement.setString(2, nom);
                preparedStatement.setString(3, prenom);
                preparedStatement.setString(4, sexe);
                preparedStatement.setString(5, etatcivil);
                preparedStatement.setString(6, telephone);
                preparedStatement.setString(7, sanguin);
                preparedStatement.setString(8, poids);
                preparedStatement.setString(9, taille);
                preparedStatement.setString(10, lien);
                preparedStatement.setString(11, assureur);
                preparedStatement.setString(12, profession);
                preparedStatement.setString(13, adresse);
                preparedStatement.setInt(14, patientId);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(menu4, "Patient modifié avec succès!");
                    fetchDataToTable();
                } else {
                    JOptionPane.showMessageDialog(menu4, "Aucun patient trouvé avec cet identifiant.");
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu4, "Erreur lors de la modification du patient.");
        }
    }
    public void fetchDataToTable() {
        DefaultTableModel model = (DefaultTableModel) menu4.tablepatient.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM patient_med")) {
            while (resultSet.next()) {
                int patientId = resultSet.getInt("id");
                String datenais = resultSet.getString("dateNais");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String sexe = resultSet.getString("sexe");
                String etatcivil = resultSet.getString("etatcivil");
                String telephone = resultSet.getString("telephone");
                String sanguin = resultSet.getString("sanguin");
                String taille = resultSet.getString("taille");
                String poids = resultSet.getString("poids");
                String lien = resultSet.getString("lien");
                String assureur = resultSet.getString("assureur");
                String profession = resultSet.getString("profession");
                String adresse = resultSet.getString("adresse");
                model.addRow(new Object[]{patientId, datenais, nom, prenom, sexe, etatcivil, telephone,sanguin,taille,poids,lien,
                assureur,profession,adresse});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu4, "Erreur lors de la récupération des données.");
        }
    }
    private String formatDate(java.util.Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    private int genererPatientId() throws SQLException {
    // Générer un ID unique en récupérant le dernier ID utilisé dans la table et en l'incrémentant de 1
    int patientId = 0;
    String sql = "SELECT MAX(id) FROM patient_med";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {
        if (resultSet.next()) {
            patientId = resultSet.getInt(1);
        }
    }
    // Incrémenter l'ID pour obtenir un nouvel ID unique
    return patientId + 1;
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
