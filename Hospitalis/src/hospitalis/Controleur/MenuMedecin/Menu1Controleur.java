/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuMedecin;

import hospitalis.Interface.componentMe.Menu1;
import hospitalis.Model.Dossiers;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
public class Menu1Controleur implements MouseListener {
    private ResultR result;
    private DossierImpr pd;
    private Connection connection;
    private Menu1 menu1;
    private Menu4Controleur menu4;
    
    private static Menu1Controleur instanceM1;
    private Dossiers dossier;
    private int patientId;
    private String nomP;
    private String prenomP;
    private String idD , idP;
    private String nomp;
    private String prenomp;
    private String infos;
    private String presc;
    private String resul;
    private String date , dateNais;
    private String matmed;
    private String nomprenom;
    public  String chercher;
    
    public Menu1Controleur(Connection connection , Menu1 menu1){
        this.connection=connection;
        this.menu1 = menu1;
        //this.menu1.btajout.addMouseListener(this);
        this.menu1.btmodi.addMouseListener(this);
        this.menu1.btimpri.addMouseListener(this);
        this.menu1.btcherche.addMouseListener(this);
        fetchDataToTable();
    }
    public static Menu1Controleur getInstance(Connection connection,Menu1 menu1){
        if (instanceM1 == null) {
            synchronized (Menu1Controleur.class) {
                if (instanceM1 == null) {
                    instanceM1 = new Menu1Controleur(connection,menu1);               
                }
            }
        }
        return instanceM1;     
    }
    public void setMenu(Menu1 menu1) {
        this.menu1 = menu1;
        if (this.menu1 == null) {
            // test
            //System.out.println("Le menu est null");
        } else {
            //System.out.println("Le menu n'est pas null");
        }
    }
    public void afficherMenu() {
        if (menu1.isVisible()) {
            menu1.setVisible(false);
        } else {
            menu1.setVisible(true);  
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
         if (e.getSource() == menu1.btimpri) {
            handleImprimerDossier();
        } else if (e.getSource() == menu1.btmodi) {
            handleModifyDossier();
        }
        else if (e.getSource()==menu1.btcherche){
            handleChercherDossier();
        }
    } 
    
    public void handleModifyDossier() {
        int selectedRow = menu1.tabledossier.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu1, "Veuillez sélectionner un dossier à modifier.");
            return;
        }
        String id =  (String)menu1.tabledossier.getValueAt(selectedRow, 0);
        try {
            System.out.println(id);
            String infos_medi = menu1.inputinfosmed.getText();
            String prescription = menu1.inputpresc.getText();
            String resultats_test = menu1.inputresultattest.getText();   
            String sql = "UPDATE dossier SET infos_medi = ?, prescription = ?, resultats_test = ?  WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, infos_medi);
                preparedStatement.setString(2, prescription);
                preparedStatement.setString(3, resultats_test);
                preparedStatement.setString(4, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(menu1, "Dossier modifié avec succès!");
                    fetchDataToTable();
                } else {
                    JOptionPane.showMessageDialog(menu1, "Aucun dossier trouvé avec cet identifiant.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu1, "Erreur lors de la modification du dossier."+ex);
        }
    }
    //
   public void handleChercherDossier() {
    String chercher = menu1.inputchercher.getText().trim();
    boolean trouve = false;
    ResultR result = new ResultR();
    result.getInstance();
    DefaultTableModel resultModel = result.getTableModel();
    resultModel.setRowCount(0); // Clear previous search results

    for (int i = 0; i < menu1.tabledossier.getRowCount(); i++) {
        for (int j = 0; j < menu1.tabledossier.getColumnCount(); j++) {
            String cellValue = menu1.tabledossier.getValueAt(i, j).toString().trim();
            if (cellValue.equalsIgnoreCase(chercher)) {
                Object[] rowData = new Object[menu1.tabledossier.getColumnCount()];
                for (int k = 0; k < rowData.length; k++) {
                    rowData[k] = menu1.tabledossier.getValueAt(i, k);
                }
                result.afficherResultDossier(rowData); // Add row to result table
                trouve = true;
            }
        }
    }
    if (!trouve) {
        // Afficher un message si aucun patient n'est trouvé
        JOptionPane.showMessageDialog(menu1, "Aucun dossier trouvé avec cette indication.");
    }
}
   //
   public void handleImprimerDossier(){
        int selectedRow = menu1.tabledossier.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(menu1, "Veuillez sélectionner un dossier à imprimer.");
            return;
        }
        else {
            
            pd = new DossierImpr();
            populateFields(selectedRow);
            pd.setVisible(true);
             // Imprimer le dossier
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName("Imprimer Dossier Médical");

        printerJob.setPrintable(pd.createPrintable(pd));


        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                pd.setVisible(false);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(menu1, "Erreur lors de l'impression du dossier : " + ex.getMessage());
            }
        }
        }
        
    }
   //
    public void fetchDataToTable() {
        DefaultTableModel model = (DefaultTableModel) menu1.tabledossier.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM dossier")) {
            while (resultSet.next()) {
                idD = resultSet.getString("id");
                idP = resultSet.getString("idpatient");
                nomp = resultSet.getString("nom_P");
                prenomp = resultSet.getString("prenom_P");
                infos = resultSet.getString("infos_medi");
                presc = resultSet.getString("prescription");
                resul = resultSet.getString("resultats_test");
                date = resultSet.getString("date");
                matmed = resultSet.getString("matricule_med");
                model.addRow(new Object[]{idD,idP,nomp,prenomp, infos,presc, resul, date,matmed});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu1, "Erreur lors de la récupération des données.");
        }
    }
    public void populateFields(int Slt) {
         String sql = "SELECT nom , prenom FROM utilisateurs WHERE matricule=?";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matmed);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String nomd = resultSet.getString("nom");      
                    String prenomd = resultSet.getString("prenom");   
                    nomprenom=nomd+" "+prenomd;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu1, "Erreur lors de la récupération des données.");
        }
         String sql2 = "SELECT dateNais FROM patient_med WHERE id=?";
      try (PreparedStatement statement = connection.prepareStatement(sql2)) {
            statement.setString(1, idP);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                     dateNais = resultSet.getString("dateNais");         
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu1, "Erreur lors de la récupération des données.");
        }
        pd.inputdossierid.setText(String.valueOf(menu1.tabledossier.getValueAt(Slt, 0)));
        pd.inputnomp.setText(String.valueOf(menu1.tabledossier.getValueAt(Slt, 2)));
        pd.inputprenomp.setText(String.valueOf(menu1.tabledossier.getValueAt(Slt, 3)));
        pd.inputnaisp.setText(dateNais);
        pd.inputinfos.setText(String.valueOf(menu1.tabledossier.getValueAt(Slt, 4)));
        pd.inputmatri.setText(String.valueOf(menu1.tabledossier.getValueAt(Slt, 8)));
        pd.inputnpMed.setText(nomprenom);
        pd.inputpresc.setText(String.valueOf(menu1.tabledossier.getValueAt(Slt, 5)));
        pd.inputresultat.setText(String.valueOf(menu1.tabledossier.getValueAt(Slt, 6)));
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
