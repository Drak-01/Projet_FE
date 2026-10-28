/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuMedecin;

import hospitalis.Controleur.Authentification;
import java.sql.Connection;
import hospitalis.Interface.componentMe.Menu3;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class Menu3Controleur implements MouseListener {
    private Connection connection;
    private Menu3 menu3;
    private static Menu3Controleur instanceM3;

    public Menu3Controleur(Connection connection, Menu3 menu3) {
        this.connection = connection;
        this.menu3 = menu3;
        fetchDataToTable();
        rdvToTable();
    }
    public void afficherMenu(){
        if (menu3.isVisible()) {
            menu3.setVisible(false);
        } else {
            menu3.setVisible(true);
        }
    }
    public void fetchDataToTable() {
        DefaultTableModel model = (DefaultTableModel) menu3.tablenotif.getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM notification WHERE matricule_med=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, Authentification.matMed);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String mess = resultSet.getString("message");      
                    String date = resultSet.getString("date_envoi");
                    model.addRow(new Object[]{ date, mess});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu3, "Erreur lors de la récupération des données.");
        }
    }
    //pour les rdv
    public void rdvToTable() {
        DefaultTableModel model = (DefaultTableModel) menu3.tablerdv.getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM rendez_vous WHERE matricule_med=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, Authentification.matMed);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String det = resultSet.getString("details_rdv");      
                    String heure = resultSet.getString("heure");      
                    String date = resultSet.getString("date_rdv");
                    model.addRow(new Object[]{  det,date,heure});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu3, "Erreur lors de la récupération des données.");
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
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
