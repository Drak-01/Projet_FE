/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuMedecin;

import hospitalis.Controleur.Authentification;
import hospitalis.Interface.componentMe.Menu2;
import hospitalis.Model.Calender;
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
public class Menu2Controleur implements MouseListener {
    private Connection connection;
    private Menu2 menu2;
    private static Menu2Controleur instanceM2;
    private Calender calendrier;
    private String MAT;
    
     public Menu2Controleur(Connection connection , Menu2 menu2){
        this.connection = connection;
        this.menu2 =menu2;
        
        this.menu2.btmodi.addMouseListener(this);
        this.menu2.btajout.addMouseListener(this);
        fetchDataToTable();
    }
     
    public void setMenu(Menu2 menu2) {
        this.menu2 = menu2;
        if (this.menu2 == null) {
            System.out.println("Le menu est null");
        } else {
            System.out.println("Le menu n'est pas null");
            this.menu2.btajout.addMouseListener(this);
           
            this.menu2.btmodi.addMouseListener(this);
        }
    }
    public static Menu2Controleur getInstance(Connection connection,Menu2 menu2){
        if (instanceM2 == null) {
            synchronized (Menu2Controleur.class) {
                if (instanceM2 == null) {
                    instanceM2 = new Menu2Controleur(connection,menu2);
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
    }
    @Override
    public void mouseClicked(MouseEvent e) {
          if (e.getSource() == menu2.btajout) {
            handleAddCalender();
        
        } else if (e.getSource() == menu2.btmodi) {
            handleModifyCalender();
            System.out.println("zz"+MAT);
        }  
    }
    public void handleAddCalender() {
       if (menu2.inputjour.getDate()==null || menu2.inputheure.getText().isEmpty()) {
    JOptionPane.showMessageDialog(menu2, "Veuillez remplir tous les champs", "Champs vides", JOptionPane.ERROR_MESSAGE);
}
         else{
            try{
            String jour = formatDate(menu2.inputjour.getDate()); 
            String heure = menu2.inputheure.getText(); 
            String matricule_med= Authentification.matMed;
            // Insérer la nouvelle chambre dans la base de données
            String sql = "INSERT INTO calendar (jour, heure,matricule_med) VALUES (?, ? ,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, jour);
                preparedStatement.setString(2, heure);            
                preparedStatement.setString(3, matricule_med);            
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(menu2, "Calendrier ajouté avec succès!");
                    fetchDataToTable(); // Mettre à jour le tableau après l'ajout
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(menu2, "Erreur lors de l'ajout du calendrier.");
            }
            } catch(ParseException ex){
                ex.printStackTrace();
            }
             }
    } 
    public void handleModifyCalender() {
        try{
        String jour = formatDate(menu2.inputjour.getDate()); 
        String heure = menu2.inputheure.getText(); 
        int selectedRow = menu2.tablecalender.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu2, "Veuillez sélectionner un calendrier à modifier.");
            return;
        }
            String matricule_med = menu2.tablecalender.getValueAt(selectedRow, 2).toString();      
        // Mettre à jour le calendrier dans la base de données
        String sql = "UPDATE calendar SET jour = ?, heure = ? WHERE matricule_med = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, jour);
            preparedStatement.setString(2, heure);
            preparedStatement.setString(3, matricule_med);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(menu2, "Calendrier modifié avec succès!");
                fetchDataToTable(); // Mettre à jour le tableau après la modification
            } else {
                JOptionPane.showMessageDialog(menu2, "Aucun calendrier trouvé avec ce numéro.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu2, "Erreur lors de la modification du calendrier.");
        }
        } catch(ParseException ex){
            ex.printStackTrace();
        }
    } 
    public void fetchDataToTable() {
        DefaultTableModel model = (DefaultTableModel) menu2.tablecalender.getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM calendar WHERE matricule_med=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, Authentification.matMed);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String jour = resultSet.getString("jour");
                    String heure = resultSet.getString("heure");
                    String matricule_med = resultSet.getString("matricule_med");
                    model.addRow(new Object[]{jour, heure, matricule_med});
                }
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