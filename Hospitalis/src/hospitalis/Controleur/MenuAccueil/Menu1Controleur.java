package hospitalis.Controleur.MenuAccueil;

import hospitalis.Interface.componentAc.Menu1;
import hospitalis.Model.RDV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author pc
 */
public class Menu1Controleur implements MouseListener {
    private Connection connection;
    private Menu1 menu1;
    private RDV rdv;
    private static Menu1Controleur instanceM1;
    public String matriculeMed;
    public String heure;
    public String dateRDV;

    private List<String> creneaux = new ArrayList<>();
    private List<java.sql.Date> dates = new ArrayList<>();

    public Menu1Controleur(Connection connection, Menu1 menu1) {
        this.connection = connection;
        this.menu1 = menu1;
        this.menu1.btmodi.addMouseListener(this);
        this.menu1.btajout.addMouseListener(this);
        this.menu1.btsupp.addMouseListener(this);
        this.menu1.combocreneau.addActionListener(e -> mettreAJourDate());
        fetchDataToTable();
        setupListeners(); 
    }
    public static Menu1Controleur getInstance(Connection connection, Menu1 menu1) {
        if (instanceM1 == null) {
            synchronized (Menu1Controleur.class) {
                if (instanceM1 == null) {
                    instanceM1 = new Menu1Controleur(connection, menu1);
                }
            }
        }
        return instanceM1;
    }
    public void setMenu(Menu1 menu1) {
    this.menu1 = menu1;
    if (this.menu1 == null) {
        System.out.println("Le menu est null");
    } else {
        System.out.println("Le menu n'est pas null");
        this.menu1.btajout.addMouseListener(this);
        this.menu1.btsupp.addMouseListener(this);
        this.menu1.btmodi.addMouseListener(this);
    }
    // Si instance de Menu1Controleur est déjà initialisée,
    // mettre à jour menu1 de cette instance
    if (instanceM1 != null) {
        instanceM1.menu1 = menu1;
    }
}
    private void setupListeners() {
        menu1.inputmatrimed.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                afficherCreneau();
            }
            public void removeUpdate(DocumentEvent e) {
                afficherCreneau();
            }
            public void insertUpdate(DocumentEvent e) {
                afficherCreneau();
            }
        });
    } 
    public void afficherCreneau() {
        String val= menu1.inputmatrimed.getText();
        String sql = "SELECT heure, jour FROM calendar WHERE matricule_med=?";
         boolean hasResults = false;
        if(val.length() >=5){
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1,val );
                try (ResultSet rs = stmt.executeQuery()) {
                    // Vider le JComboBox et les listes
                    menu1.combocreneau.removeAllItems();
                    creneaux.clear();
                    dates.clear();
                    // Ajouter une option par défaut
                    menu1.combocreneau.addItem("Choisissez un créneau");
                    // Parcourir les résultats
                    while (rs.next()) {
                        hasResults = true; 
                        String creneau = rs.getString("heure");
                        Date date = rs.getDate("jour");
                        // Ajouter le créneau et la date aux listes
                        creneaux.add(creneau);
                        dates.add(date);
                        // Ajouter le créneau au JComboBox
                        menu1.combocreneau.addItem(creneau);
                    }
                if(hasResults){
                   JOptionPane.showMessageDialog(menu1, "Choisisssez un Creneau", "CHOISIR CRENEAU", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(menu1, "Ce medecin n'apparitent pas à la base", "ERREUR", JOptionPane.ERROR_MESSAGE);

                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu1, "Erreur à l'affichage des créneaux.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
     }
    
    }
// Méthode pour mettre à jour la date dans le JDateChooser lorsqu'un créneau est sélectionné
public void mettreAJourDate() {
    int selectedIndex = menu1.combocreneau.getSelectedIndex();
    if (selectedIndex > 0) { // Ignorer l'option par défaut
        Date selectedDate = dates.get(selectedIndex - 1); // -1 pour compenser l'option par défaut
        menu1.inputdaterdv.setDate(selectedDate);
    } else {
        menu1.inputdaterdv.setDate(null); // Réinitialiser la date si l'option par défaut est sélectionnée
    }
}
    // Méthode pour mettre à jour inputdaterdv lorsque l'utilisateur sélectionne un créneau
    public void onSelectCreneau(String selectedCreneau) {
        // Rechercher l'index du créneau sélectionné dans la liste des créneaux
        int index = creneaux.indexOf(selectedCreneau);
        
        // Vérifier si le créneau sélectionné a été trouvé dans la liste
        if (index != -1) {
            // Récupérer la date associée à l'index correspondant dans la liste des dates
            java.sql.Date selectedDate = dates.get(index);
            // Mettre à jour inputdaterdv avec la date récupérée
            menu1.inputdaterdv.setDate(selectedDate);
            // Supprimer le créneau et la date de la base de données
            String sql = "DELETE FROM calendar WHERE heure = ? AND jour = ? AND matricule_med = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, selectedCreneau);
                stmt.setDate(2, selectedDate);
                stmt.setString(3, menu1.inputmatrimed.getText());
                stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu1, "Erreur lors de la suppression du créneau.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Arrêter l'exécution si une erreur survient
        }
            // Supprimer le créneau et la date des listes
            creneaux.remove(index);
            dates.remove(index);
            // Mettre à jour le JComboBox
            updateCreneauComboBox();
            // Mettre à jour le JComboBox
            updateCreneauComboBox();
        } else {
            JOptionPane.showMessageDialog(menu1, "Aucune date associée à ce créneau.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        }
    private void updateCreneauComboBox() {
    // Vider le JComboBox
    menu1.combocreneau.removeAllItems();
    // Ajouter une option par défaut
    menu1.combocreneau.addItem("Choisissez un créneau");

    // Ajouter les créneaux restants au JComboBox
    for (String creneau : creneaux) {
        menu1.combocreneau.addItem(creneau);
    }
}
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu1.btajout) {
            handleAddRDV();
        } else if (e.getSource() == menu1.btsupp) {
            handleDeleteRDV();
        } else if (e.getSource() == menu1.btmodi) {
            handleModifyRDV();
        }
    }
    private void handleAddRDV() {
        if ( menu1.inputdaterdv.getDate()==null || menu1.inputmatrimed.getText().isEmpty() || menu1.combocreneau.getSelectedItem()==null || menu1.inputdetrdv.getText().isEmpty() || menu1.inputtyperdv.getSelectedItem()==null || menu1.inputstatut.getText().isEmpty()) {
    JOptionPane.showMessageDialog(menu1, "Veuillez remplir tous les champs", "Champs vides", JOptionPane.ERROR_MESSAGE);
}
         else{
            try {
                 dateRDV = formatDate(menu1.inputdaterdv.getDate());
                String matriculeMed = menu1.inputmatrimed.getText();
                 heure = (String)menu1.combocreneau.getSelectedItem();
                String detailsRDV = menu1.inputdetrdv.getText();
                String typeRDV = (String) menu1.inputtyperdv.getSelectedItem();
                String statut = menu1.inputstatut.getText();

                String sql = "INSERT INTO rendez_vous (date_rdv, heure, details_rdv, matricule_med, type_rdv, statut) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, dateRDV);
                    preparedStatement.setString(2, heure);
                    preparedStatement.setString(3, detailsRDV);
                    preparedStatement.setString(4, matriculeMed);
                    preparedStatement.setString(5, typeRDV);
                    preparedStatement.setString(6, statut);

                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(menu1, "Rendez-vous ajouté avec succès!");
                        fetchDataToTable();
                        onSelectCreneau(heure);
                        envoyerNotification(matriculeMed);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(menu1, "Erreur lors de l'ajout du rendez-vous.");
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(menu1, "Erreur de format de date.");
            }
        }
    }
    public void handleModifyRDV() {
        int selectedRow = menu1.getTable().getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = Integer.parseInt(menu1.getTable().getValueAt(selectedRow, 0).toString());
                String dateRDV = formatDate(menu1.inputdaterdv.getDate());
                String matriculeMed = menu1.inputmatrimed.getText();
                String heure = (String)menu1.combocreneau.getSelectedItem();
                String detailsRDV = menu1.inputdetrdv.getText();
                String typeRDV = (String) menu1.inputtyperdv.getSelectedItem();
                String statut = menu1.inputstatut.getText();

                String sql = "UPDATE rendez_vous SET date_rdv = ?, heure = ?, details_rdv = ?, matricule_med = ?, type_rdv = ?, statut = ? WHERE id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, dateRDV);
                    preparedStatement.setString(2, heure);
                    preparedStatement.setString(3, detailsRDV);
                    preparedStatement.setString(4, matriculeMed);
                    preparedStatement.setString(5, typeRDV);
                    preparedStatement.setString(6, statut);
                    preparedStatement.setInt(7, id);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(menu1, "Rendez-vous modifié avec succès!");
                        fetchDataToTable();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(menu1, "Erreur lors de la modification du rendez-vous.");
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(menu1, "Erreur de format de date.");
            }
        } else {
            JOptionPane.showMessageDialog(menu1, "Veuillez sélectionner un rendez-vous à modifier.");
        }
    }
    public void handleDeleteRDV() {
        Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
            menu1,
            "Voulez-vous vraiment supprimer ce rendez_vous ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
         );
        if(response == JOptionPane.YES_OPTION){
        int selectedRow = menu1.getTable().getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(menu1.getTable().getValueAt(selectedRow, 0).toString());
            String sql = "DELETE FROM rendez_vous WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(menu1, "Rendez-vous supprimé avec succès!");
                    fetchDataToTable();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(menu1, "Erreur lors de la suppression du rendez-vous.");
            }
        } else {
            JOptionPane.showMessageDialog(menu1, "Veuillez sélectionner un rendez-vous à supprimer.");
        }
    }
    }
    private String formatDate(java.util.Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public void afficherMenu() {
        if (menu1.isVisible()) {
            menu1.setVisible(false);
        } else {
            menu1.setVisible(true);
        }
    }
    public void fetchDataToTable() {
    final String SQL = "SELECT * FROM rendez_vous";
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(SQL)) {
        DefaultTableModel model = (DefaultTableModel) menu1.getTable().getModel();
        model.setRowCount(0); // Vide le contenu de la table
        while (rs.next()) {
            Object[] rowData = {
                rs.getString("id"),
                rs.getString("date_rdv"),
                rs.getString("heure"),
                rs.getString("details_rdv"),
                rs.getString("matricule_med"),
                rs.getString("type_rdv"),
                rs.getString("statut")
            };
            model.addRow(rowData);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(menu1, "Erreur lors de la récupération des données.");
    }
}
    
    //
     public void handleEnvoiNotification() {
    // Requête SQL pour vérifier si le médecin existe
    String sql = "SELECT COUNT(*) FROM utilisateurs WHERE matricule  = ? AND role='medecin'";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, matriculeMed);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    // Le médecin existe, procéder à l'envoi de la notification
                    envoyerNotification(matriculeMed);
                } else {
                    // Le médecin n'existe pas
                    JOptionPane.showMessageDialog(menu1, "Médecin non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(menu1, "Erreur lors de la vérification du matricule.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
public void envoyerNotification(String matriculeMed) {
    // Obtenir l'heure actuelle
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String heureactuelle = sdf.format(new java.util.Date());
    String message="Vous avez un RDV à "+heure+" le "+dateRDV;
    //String mes=menu1.inputmessage.getText();
    // Message de notification
   // String message = "Notification envoyée au médecin avec le matricule : " + matriculeMed + "\nHeure d'envoi : " + heureactuelle;      
    String ajoutBDD = "INSERT INTO notification (message ,date_envoi ,matricule_med) VALUES (?,?,?)";
    try(PreparedStatement ajout=connection.prepareStatement(ajoutBDD)){
        ajout.setString(1, message);
        ajout.setString(2, heureactuelle);
        ajout.setString(3, matriculeMed);
        ajout.executeUpdate();
        fetchDataToTable();
        JOptionPane.showMessageDialog(menu1, "Notification envoyée au médecin avec le matricule : " + matriculeMed + "\nHeure d'envoi : " + heureactuelle, "Notification", JOptionPane.INFORMATION_MESSAGE);
    } catch(SQLException ex){
        ex.printStackTrace();
        JOptionPane.showMessageDialog(menu1, "Ereur lors de l'envoi de la notification" ,"Erreur",JOptionPane.ERROR_MESSAGE);
    }  
  }
    private void EffacerChamps(){
        
    }
    //
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
