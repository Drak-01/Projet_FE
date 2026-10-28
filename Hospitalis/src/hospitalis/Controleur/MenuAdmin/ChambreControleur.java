/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuAdmin;

import hospitalis.Interface.componentAD.ChambreTrouve;
import java.sql.Connection;
import hospitalis.Interface.componentAD.Menu1AC;
import hospitalis.Model.Chambre;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class ChambreControleur implements MouseListener, ListSelectionListener{
    //
    private final Menu1AC menu;
    private Connection connection;
    private Chambre chambre;
    private ChambreTrouve resultat;
    private static ChambreControleur instance;
    //
    public ChambreControleur(Connection connection , Menu1AC menu){
        this.connection = connection;
        this.menu = menu;
        this.resultat = ChambreTrouve.getInstance();
        
        this.menu.btajouter.addMouseListener(this);
        this.menu.btsupprimer.addMouseListener(this);
        this.menu.btmodifier.addMouseListener(this);
        this.menu.btcherche.addMouseListener(this);
        
        resultat.ferme.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        resultat.setVisible(false);
                        EffacerChamps();
                    }
                });
        //this.menu.jtables.getSelectionModel().addListSelectionListener(this);
        chargementDeService();
        EffacerChamps();
    }
    //-----------------------------
    public static ChambreControleur getInstance(Connection connection, Menu1AC menu) {
        if (instance == null) {
            synchronized (ChambreControleur.class) {
                if (instance == null) {
                    instance = new ChambreControleur(connection, menu);
                }
            }
        }
        return instance;
    }
    
    //*----------------------------
    
    
    public void afficherMenu() {
       if (menu.isVisible()) {
            menu.setVisible(false);
        } else {
            menu.setVisible(true);
        }
        updateTable();
        EffacerChamps();
    }
    // La methode ajouter chambre
    private void AjouterChambre() {
        try {
            if (menu.type.getText().isEmpty() || menu.numero.getText().isEmpty() || menu.categorie.getText().isEmpty() ) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return; // Sortir de la méthode si les champs sont vides
            }
            chambre = new Chambre(connection);
            chambre.setNumChambre(Integer.parseInt(menu.numero.getText()));
            chambre.setType(menu.type.getText());
            chambre.setCategorie(menu.categorie.getText());
            
            boolean success = chambre.ajouterChambre(connection);
            if (success) {
                JOptionPane.showMessageDialog(null, "Chambre ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                EffacerChamps();
                updateTable();
                // Mise à jour de la table après ajout
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de Chambre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    // La methode Supprimer Chambre
    private void supprimerChambre() {
        if (menu.numero.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Sortir de la méthode si les champs sont vides
        }
        int numeroChambre = Integer.parseInt(menu.numero.getText());

        int response = JOptionPane.showConfirmDialog(null,
            "Êtes-vous sûr de vouloir supprimer la Chambre numéro : " + numeroChambre + " ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            chambre = new Chambre(connection);
            chambre.setNumChambre(numeroChambre);

            boolean success = chambre.supprimerChambre(connection);
           if (success) {
                JOptionPane.showMessageDialog(null, "Chambre supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                EffacerChamps();
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la Chambre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    } 
    
    ///----------------------------------------------------------------------
    private void chargementDeService() {
        List<Chambre> chambreListe = Chambre.getAllChambre(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"  Numéro Chambre", "Type", "Catégorie" , "Disponibilité"}, 
            0
        );
        for(Chambre chambre: chambreListe) {
            model.addRow(new Object[]{
                chambre.getNumChambre(),
                chambre.getType(),
                chambre.getCategorie(),
                chambre.getDisponibilite(),
                //chambre Infos
            });
        }
        menu.jtables.setModel(model);
    }
    ///----------------------------------------------------------------------
    private void EffacerChamps() {
        menu.numero.setText("");
        menu.type.setText("");
        menu.categorie.setText("");
        menu.jtables.clearSelection();
        menu.cherche.setText("");
    }
    private void updateTable(){
        chargementDeService();
    }
    ///----------------------------------------------------------------------
    private void handleModifyChambre() throws ParseException {
        // Vérifier si les champs de saisie ne sont pas vides
        if (menu.numero.getText().isEmpty() || menu.type.getText().isEmpty() || menu.categorie.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Sortir de la méthode si les champs sont vides
        }
        // Créer une nouvelle instance de Chambre avec les informations saisies
        chambre = new Chambre(connection);
        chambre.setNumChambre(Integer.parseInt(menu.numero.getText()));
        chambre.setType(menu.type.getText());
        chambre.setCategorie(menu.categorie.getText());

        try {
            // Récupération des données actuelles du tableau pour comparaison
            int selectedRow = menu.jtables.getSelectedRow();
            if (selectedRow >= 0) {
                int currentNumero = (int) menu.jtables.getValueAt(selectedRow, 0);
                String currentType = (String) menu.jtables.getValueAt(selectedRow, 1);
                String currentCatego = (String) menu.jtables.getValueAt(selectedRow, 2);

                // Comparaison avec les valeurs actuelles et mise à jour si nécessaire
                if (Integer.parseInt(menu.numero.getText()) != currentNumero) {
                    chambre.setNumChambre(Integer.parseInt(menu.numero.getText()));
                }
                if (!menu.type.getText().equals(currentType)) {
                    chambre.setType(menu.type.getText());
                }
                if (!menu.categorie.getText().equals(currentCatego)) {
                    chambre.setCategorie(menu.categorie.getText());
                }
            }

            // Modifier la chambre dans la base de données
            boolean success = chambre.modifierChambre(connection);
            if (success) {
                JOptionPane.showMessageDialog(null, "Chambre modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                updateTable(); // Mise à jour de la table après modification
                EffacerChamps();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification de la Chambre", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification de la Chambre : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    ///----------------------------------------------------------------------
    ///----------------------------------------------------------------------
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == menu.btajouter){
            AjouterChambre();
        }else if (e.getSource() == menu.btsupprimer){
            supprimerChambre();
        }else if (e.getSource() == menu.btmodifier){
            try {
                handleModifyChambre();
            } catch (ParseException ex) {
                Logger.getLogger(ChambreControleur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if (e.getSource() == menu.btcherche){
            chercherChambre();
        }
        
        EffacerChamps();
    }
    
    private void chercherChambre(){
        if (menu.cherche.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Sortir de la méthode si les champs sont vides
        }
        
        List<Chambre> chambreTrouves = Chambre.chercherChambre(connection, menu.cherche.getText());

        if (!chambreTrouves.isEmpty()) {
            // Effacer le modèle actuel de la table de recherche
            DefaultTableModel model = (DefaultTableModel) resultat.jtables.getModel();
            model.setRowCount(0);

            // Remplir le modèle avec les résultats de la recherche
            for (Chambre chambre : chambreTrouves) {
                model.addRow(new Object[]{
                    chambre.getNumChambre(),
                    chambre.getType(),
                    chambre.getCategorie(),
                    chambre.getDisponibilite(),
                    
                });
            }
            EffacerChamps();
            resultat.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Aucune Chambre trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        //EffacerChamps();
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
    }
        
}
    
   
    
    
   