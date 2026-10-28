package hospitalis.Controleur.MenuAccueil;

import hospitalis.Controleur.MenuMedecin.Menu1Controleur;
import hospitalis.Interface.componentAc.Menu2;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Menu2Controleur implements MouseListener {
    private final Connection connection;
    private final Menu2 menu2;
    public static Menu1Controleur menu1controleur;
  
    public Menu2Controleur(Connection connection, Menu2 menu2) {
        this.connection = connection;
        this.menu2 = menu2;
        this.menu2.btajout.addMouseListener(this);
        this.menu2.btsupp.addMouseListener(this);
        this.menu2.btmodi.addMouseListener(this);
        fetchDataToTable();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu2.btajout) {
            handleAddPatient();
        } else if (e.getSource() == menu2.btsupp) {
            handleDeletePatient();
        } else if (e.getSource() == menu2.btmodi) {
            handleModifyPatient();
        }
    }

    public void handleAddPatient() {
        
         if (menu2.inputdatenais.getDate()==null || menu2.inputnompatient.getText().isEmpty() || menu2.inputetatcivil.getSelectedItem()==null  
                 || menu2.inputlien.getText().isEmpty()|| menu2.inputadresse.getText().isEmpty()||menu2.inputassureur.getText().isEmpty()
                 || menu2.inputtelephone.getText().isEmpty() || menu2.inputpoids.getText().isEmpty() || menu2.inputprofession.getText().isEmpty()
                 || menu2.inputsanguin.getSelectedItem()==null || menu2.inputtaille.getText().isEmpty()) {
                 JOptionPane.showMessageDialog(menu2, "Veuillez remplir tous les champs", "Champs vides", JOptionPane.ERROR_MESSAGE);
            }
         else{
            try {               
                String datenais = formatDate(menu2.inputdatenais.getDate());
                String nom = menu2.inputnompatient.getText();
                String prenom = menu2.inputprenompatient.getText();
                String lien = menu2.inputlien.getText();
                String assureur = menu2.inputassureur.getText();
                String adresse = menu2.inputadresse.getText();
                String profession = menu2.inputprofession.getText();
                String taille = menu2.inputtaille.getText();
                String poids = menu2.inputpoids.getText();
                String sanguin =(String)menu2.inputsanguin.getSelectedItem();
                String etatcivil = (String) menu2.inputetatcivil.getSelectedItem();
                String telephone = menu2.inputtelephone.getText();
                //
                String sql = "INSERT INTO patient (id,dateNais, nom, prenom, sexe, etatcivil, telephone , lien , adresse,assureur,profession,taille,"
                        + "poids,sanguin) VALUES (?,?, ?, ?, ?, ?, ? ,? ,? , ?, ?, ?, ?, ?)";
              
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    //
                    int patientId = genererPatientId();
                    //
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setString(2, datenais);
                    preparedStatement.setString(3, nom);
                    preparedStatement.setString(4, prenom);
                    if(menu2.inputsexeH.isSelected()){
                     preparedStatement.setString(5, "Masculin");
                    }
                    else if(menu2.inputsexe.isSelected()){
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
                        JOptionPane.showMessageDialog(menu2, "Patient ajouté avec succès!");
                        fetchDataToTable();
                        //menu1controleur.handleAddDossier(patientId);      
                    }
                }
            } catch (SQLException | ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(menu2, "Erreur lors de l'ajout du patient.");
            }
        }
    }
    public void handleDeletePatient() {
        int selectedRow = menu2.tablepatient.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu2, "Veuillez sélectionner un patient à supprimer.");
            return;
        }
         Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
            menu2,
            "Voulez-vous vraiment supprimer ce patient ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
         );
        if(response == JOptionPane.YES_OPTION){
        int patientId = (int) menu2.tablepatient.getValueAt(selectedRow, 0);
        try {
            String sql = "DELETE FROM patient WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, patientId);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(menu2, "Patient supprimé avec succès!");
                    fetchDataToTable();
                } else {
                    JOptionPane.showMessageDialog(menu2, "Aucun patient trouvé avec cet identifiant.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu2, "Erreur lors de la suppression du patient.");
        }
    }
    }
    public void handleModifyPatient() {
        int selectedRow = menu2.tablepatient.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu2, "Veuillez sélectionner un patient à modifier.");
            return;
        }
        int patientId = (int) menu2.tablepatient.getValueAt(selectedRow, 0);
        try {
            String datenais = formatDate(menu2.inputdatenais.getDate());
            String nom = menu2.inputnompatient.getText();
            String prenom = menu2.inputprenompatient.getText();
            String sexe = menu2.inputsexe.getText();    
            String etatcivil = (String) menu2.inputetatcivil.getSelectedItem();
            String telephone = menu2.inputlien.getText();
            String lien = menu2.inputlien.getText();
            String assureur = menu2.inputassureur.getText();
            String adresse = menu2.inputadresse.getText();
            String profession = menu2.inputprofession.getText();
            String taille = menu2.inputtaille.getText();
            String poids = menu2.inputpoids.getText();
            String sanguin =(String)menu2.inputsanguin.getSelectedItem();
            String sql = "UPDATE patient SET dateNais = ?, nom = ?, prenom = ?, sexe = ?, etatcivil = ?, telephone = ? , sanguin =? , poids=?,taille=?"
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
                    JOptionPane.showMessageDialog(menu2, "Patient modifié avec succès!");
                    fetchDataToTable();
                } else {
                    JOptionPane.showMessageDialog(menu2, "Aucun patient trouvé avec cet identifiant.");
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu2, "Erreur lors de la modification du patient.");
        }
    }
    public void fetchDataToTable() {
        DefaultTableModel model = (DefaultTableModel) menu2.tablepatient.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM patient")) {
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
            JOptionPane.showMessageDialog(menu2, "Erreur lors de la récupération des données.");
        }
    }
    private String formatDate(java.util.Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public void afficherMenu() {
        if (menu2.isVisible()) {
            menu2.setVisible(false);
        } else {
            menu2.setVisible(true);
        }
    }
     private int genererPatientId() throws SQLException {
    // Générer un ID unique en récupérant le dernier ID utilisé dans la table et en l'incrémentant de 1
    int patientId = 0;
    String sql = "SELECT MAX(id) FROM patient";
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
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
