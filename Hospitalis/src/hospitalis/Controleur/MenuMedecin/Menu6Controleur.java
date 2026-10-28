/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuMedecin;

import hospitalis.Interface.componentMe.Menu6;
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
/**
 *
 * @author pc
 */
public class Menu6Controleur implements MouseListener {
    private Connection connection;
    private static Menu6Controleur instanceM6;
    private Menu6 menu6;
    // ajout consultation private Consultation consultation;
    
    public Menu6Controleur(Connection connection , Menu6 menu6){
        this.connection = connection ;
        this.menu6 = menu6;
        this.menu6.btajout.addMouseListener(this);
        this.menu6.btsupp.addMouseListener(this);
        fetchDataToTable();
    }
    public static Menu6Controleur getInstance(Connection connection,Menu6 menu6){
        if (instanceM6 == null) {
            synchronized (Menu1Controleur.class) {
                if (instanceM6 == null) {
                    instanceM6 = new Menu6Controleur(connection,menu6);
                    //System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM6;
        
    }
    public void afficherMenu(){
        if (menu6.isVisible()) {
            menu6.setVisible(false);
        } else {
            menu6.setVisible(true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
         if (e.getSource() == menu6.btajout) {
            handleAddConsultation();
        } else if (e.getSource() == menu6.btsupp) {
            handleDeleteConsultation();       
            }
    }
    public void handleAddConsultation() {
        if ( menu6.inputheure.getText().isEmpty()||menu6.inputnomP.getText().isEmpty() ||menu6.inputprenomP.getText().isEmpty() || menu6.inputdate.getDate()==null || menu6.inputduree.getText().isEmpty() || menu6.inputcontenu.getText().isEmpty() ) {
    JOptionPane.showMessageDialog(menu6, "Veuillez remplir tous les champs", "Champs vides", JOptionPane.ERROR_MESSAGE);
}
         else{
            // Récupérer les données de la nouvelle consultation depuis l'interface utilisateur
            try{
            String heure = menu6.inputheure.getText();  
            String date = formatDate(menu6.inputdate.getDate()); 
            String duree = menu6.inputduree.getText(); 
            String contenu = menu6.inputcontenu.getText();
            String nomP = menu6.inputnomP.getText();
            String prenomP = menu6.inputprenomP.getText();
            String np=nomP+" "+prenomP;
            // Insérer la nouvelle consultation dans la base de données
            String sql = "INSERT INTO consultation (date,nom_prenom ,heure,duree,contenu) VALUES (?,?, ?, ?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, date);
                preparedStatement.setString(2, np);
                preparedStatement.setString(3, heure);
                preparedStatement.setString(4, duree);
                preparedStatement.setString(5, contenu);
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(menu6, "Consultation ajoutée avec succès!");
                    fetchDataToTable(); // Mettre à jour le tableau après l'ajout
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(menu6, "Erreur lors de l'ajout de la consultation.");
            }
            } catch(ParseException e){
                e.printStackTrace();
            }
        }
    }
    public void handleDeleteConsultation() {
        int selectedRow = menu6.tableconsultation.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu6, "Veuillez sélectionner une consultation à supprimer.");
            return;
        }
        Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
            menu6,
            "Voulez-vous vraiment supprimer cette consultation ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );
        if (response == JOptionPane.YES_OPTION) {
        int consultationId = (int) menu6.tableconsultation.getValueAt(selectedRow, 0);
       
        int patientId = (int) menu6.tableconsultation.getValueAt(selectedRow, 0);
        try {
            String sql = "DELETE FROM consultation WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, patientId);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(menu6, "Consultation supprimée avec succès!");
                    fetchDataToTable();
                } else {
                    JOptionPane.showMessageDialog(menu6, "Aucune consultation trouvée avec cet identifiant.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu6, "Erreur lors de la suppression de la consultation.");
        }
     }
    }
    private String formatDate(java.util.Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public void fetchDataToTable() {
        DefaultTableModel model = (DefaultTableModel) menu6.tableconsultation.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM consultation")) {
            while (resultSet.next()) {
                int idconsultation = resultSet.getInt("id");
                String date = resultSet.getString("date");
                String np = resultSet.getString("nom_prenom");
                String heure = resultSet.getString("heure");
                String duree = resultSet.getString("duree");
                String contenu = resultSet.getString("contenu");
                model.addRow(new Object[]{idconsultation, date ,np, heure, duree, contenu});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu6, "Erreur lors de la récupération des données.");
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
