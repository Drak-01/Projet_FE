package hospitalis.Controleur.MenuMedecin;

import hospitalis.Interface.componentMe.Menu5;
import hospitalis.Model.Chambre;
import hospitalis.Model.Patient;
import hospitalis.Model.Hospitatlisation;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class Menu5Controleur implements MouseListener {
    private Connection connection;
    private Menu5 menu3;
    private Chambre chambre;
    private static Menu5Controleur instanceM3;
    
    public Menu5Controleur(Connection connection, Menu5 menu3) {
        this.connection = connection;
        this.menu3 = menu3;
        //System.out.println("Appel création menuCont1");
        this.menu3.btajout.addMouseListener(this);
        //this.menu3.btsupp.addMouseListener(this);
        this.menu3.btmodi.addMouseListener(this);
        //fetchDataToTable();
    }

    public static Menu5Controleur getInstance(Connection connection, Menu5 menu3) {
        if (instanceM3 == null) {
            synchronized (Menu5Controleur.class) {
                if (instanceM3 == null) {
                    instanceM3 = new Menu5Controleur(connection,menu3);
                    System.out.println("Appel à l'instance du contrôleur de menu");
                }
            }
        }
        return instanceM3;
    }

    public void setMenu(Menu5 menu3) {
        this.menu3 = menu3;
        if (this.menu3 == null) {
            System.out.println("Le menu est null");
        } else {
            System.out.println("Le menu n'est pas null");
            
        }
    }
    
    public void afficherMenu() {
        if (menu3.isVisible()) {
            menu3.setVisible(false);
        } else {
            menu3.setVisible(true);
        }
        update();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
   
        if (e.getSource() == menu3.btajout) {
            try {
                handleAffecterPatient();
            } catch (ParseException ex) {
                Logger.getLogger(Menu5Controleur.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == menu3.btmodi) {
            handleModifyChambre();
        }
    }

    public void handleAffecterPatient() throws ParseException {
        if (menu3.nom.getText().isEmpty() || menu3.prenom.getText().isEmpty() || menu3.Naissance.getDate() == null) {
            JOptionPane.showMessageDialog(menu3, "Veuillez remplir tous les champs", "Champs vides", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
            Patient patient = new Patient(connection);
            
            String nom = menu3.nom.getText();
            String prenom = menu3.prenom.getText();
            
            String dateNaissance = formatDate(menu3.Naissance.getDate());// Exception
            //String infosPatient = null;
            
            boolean success = patient.verificationPatient(nom ,prenom , dateNaissance);
            System.out.println("Verification "+ patient.getId_patient());
            if(success){ 
                System.out.println("a++++");
                chambre  = new Chambre(connection);
                String categori = ((String) menu3.categorie.getSelectedItem());
                String type = ((String) menu3.type.getSelectedItem());
                int numChambre = Integer.parseInt(menu3.numeroChambre.getText());
                
                boolean verifieChambre = chambre.isDisponibleTexte(type, categori);
                if(!verifieChambre){
                    verifieChambre = chambre.isDisponible(numChambre);
                }
                if(verifieChambre){
                    // Si la chambre est disponible mettre a jour la disponibilité de la chambre
                    boolean su = chambre.updateDisponibilite(numChambre,"Occupée");
                    
                    //Effectuer l'hospitalisation
                    String dateAdmission = formatDate(new Date());
                    int id_patient = patient.getId_patient();
                    
                    boolean operationEffectue = Hospitatlisation.insertHospi(connection, id_patient, numChambre, dateAdmission);
                    if(operationEffectue){
                        fetchDataToTable();
                        JOptionPane.showMessageDialog(menu3, "Patient Affecté à la chambre numéro :" + numChambre+" avec Succès");                 
                    }else{
                        JOptionPane.showMessageDialog(menu3, "Erreur Affectation", "Succès", JOptionPane.ERROR_MESSAGE);
                    }
                       
                }else {
                    JOptionPane.showMessageDialog(menu3, "Chambre Occupé", "occupé", JOptionPane.ERROR_MESSAGE);
                }                          
                
            }else{
                JOptionPane.showMessageDialog(menu3, "Veuillez Enregistre le Patient", "Enregistrement", JOptionPane.ERROR_MESSAGE);
            }         
        }
        EffacerChamp();
    }
    
    private String formatDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    public void handleModifyChambre() {
        // Récupérer les nouvelles valeurs de disponibilité depuis l'interface utilisateur
        String nouvelleDisponibilite = (String) menu3.status.getSelectedItem();

        // Récupérer le numéro de la chambre sélectionnée ou saisie
    
        String numeroChambre;
        numeroChambre = menu3.numeroChambre.getText();


        if (numeroChambre == null || numeroChambre.isEmpty()) {
            JOptionPane.showMessageDialog(menu3, "Veuillez  entrer le numéro de la chambre.");
            return;
        }

        chambre = new Chambre(connection);

        if (nouvelleDisponibilite.equals("Disponible")) {
            try {
                String dateSortie = formatDate(new Date());
                System.out.println("dare"+dateSortie);
                boolean success = Hospitatlisation.hospitUpdate(connection, Integer.parseInt(numeroChambre), dateSortie);

                if (!success) {
                    JOptionPane.showMessageDialog(menu3, "Erreur lors de la mise à jour de la date de sortie.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (ParseException ex) {
                Logger.getLogger(Menu5Controleur.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(menu3, "Erreur lors de la conversion de la date.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Mettre à jour la disponibilité de la chambre
        boolean updateSuccess = chambre.updateDisponibilite(Integer.parseInt(numeroChambre), nouvelleDisponibilite);

        if (updateSuccess) {
            fetchDataToTable();
            JOptionPane.showMessageDialog(menu3, "Statut de la chambre changé avec succès.");
        } else {
            JOptionPane.showMessageDialog(menu3, "Erreur lors de la mise à jour de la disponibilité de la chambre.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        menu3.numeroChambre.setText("");
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) { }
    
    /**************************************************************************/
    public void fetchDataToTable() {
        DefaultTableModel model = (DefaultTableModel) menu3.tablechambre.getModel();
        model.setRowCount(0); // Effacer le tableau avant de le remplir à nouveau
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chambre");
            while (resultSet.next()) {
             
                int numero = Integer.parseInt(resultSet.getString("num_chambre"));
                String disponibilite = resultSet.getString("disponibilite");
                String type = resultSet.getString("type");
                String categorie = resultSet.getString("categorie");
                
                model.addRow(new Object[]{numero, type,  categorie ,disponibilite});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu3, "Erreur lors de la récupération des données.");
        }
    }
    ///------------------------
    //Chargement de la categorie et le type de chambre
    private void loadChambreTypes() {
        List<String> chambreTypes = Chambre.getAllTypesChambre(connection);
        Set<String> uniquechambreTypes = new HashSet<>(chambreTypes);

        // Efface la JComboBox avant d'ajouter de nouvelles valeurs pour éviter les doublons
        menu3.type.removeAllItems();

        // Ajouter les types de services uniques à la JComboBox
        for (String serviceType : uniquechambreTypes) {
            menu3.type.addItem(serviceType);
        }
    }
    
    private void loadChambreCategorie() {
        List<String> chambreCategorie = Chambre.getAllCategorieChambre(connection);
        Set<String> uniquechambreCategorie = new HashSet<>(chambreCategorie);

        // Efface la JComboBox avant d'ajouter de nouvelles valeurs pour éviter les doublons
        menu3.categorie.removeAllItems();

        // Ajouter les types de services uniques à la JComboBox
        for (String serviceType : uniquechambreCategorie) {
            menu3.categorie.addItem(serviceType);
        }
    }
    
    private void update(){
        loadChambreCategorie();
        loadChambreTypes();
        fetchDataToTable();
    }
    private void EffacerChamp(){
        menu3.nom.setText("");
        menu3.prenom.setText("");
        menu3.numeroChambre.setText("");
        menu3.Naissance.setDate(null);
        menu3.numeroChambre.setText("");
    }
    
}
