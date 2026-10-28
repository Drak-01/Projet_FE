/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuFinance;

import hospitalis.Interface.componentFi.Menu1;
import hospitalis.Interface.componentFi.PrintFacture;
import hospitalis.Model.Facture;
import hospitalis.Model.Patient;
import hospitalis.Model.Traitement;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author pc
 */
public class Menu1Controleur implements MouseListener , ItemListener {
    private Connection connection;
    private final Menu1 menu1;
    private static Menu1Controleur instanceM1;
    private Facture facture;
    private Patient patient;
    
    public Menu1Controleur(Connection connection, Menu1 menu1) {
        this.connection = connection;
        this.menu1 = menu1;
        
        
        menu1.liste_Traitement.setModel(new DefaultListModel<>());
        menu1.listeEffectuer1.setModel(new DefaultListModel<>());

        
        this.menu1.btajout.addMouseListener(this);
        this.menu1.imprimer.addMouseListener(this);
        
        this.menu1.type.addItemListener(this);
        
        this.menu1.listeEffectuer1.addMouseListener(this);
        this.menu1.liste_Traitement.addMouseListener(this);
        this.facture = new Facture(connection);
    }

      //------------------------------------------------------------------------------------------------------------------------------------------
  
    public static Menu1Controleur getInstance(Connection connection,Menu1 menu1){
        if (instanceM1 == null) {
            synchronized (Menu1Controleur.class) {
                if (instanceM1 == null) {
                    instanceM1 = new Menu1Controleur(connection,menu1);
                    //System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM1;
        
    }
    
    
    public void afficherMenu() {
       if (menu1.isVisible()) {
            menu1.setVisible(false);
        } else {
            menu1.setVisible(true);
        }
        
        //menu1.listeEffectuer1
        updateTable();
    }
    //------------------------------------------------------------------------------------------------------------------------------------------

    private void EtablirFacture() {
        try {
            facture = new Facture(connection);

            String type = (String) menu1.type.getSelectedItem();
            //if (!type.equals("Consultation") && !type.equals("Consultation Spécialisé")) {
                chargementDeTraitement(type);
            //}

            DefaultListModel<String> modelEffectuer1 = (DefaultListModel<String>) menu1.listeEffectuer1.getModel();
            double montantTotal = 0;
            List<String> listeTraitement = new ArrayList<>(); // Nouvelle liste pour stocker les détails des traitements

            for (int i = 0; i < modelEffectuer1.size(); i++) {
                String traitementStr = modelEffectuer1.getElementAt(i);
                double prix = Double.parseDouble(traitementStr.substring(traitementStr.lastIndexOf(":") + 1).trim());
                montantTotal += prix;
               listeTraitement.add(modelEffectuer1.getElementAt(i));
            }

            // Vérifier si les champs nécessaires sont vides
            if (menu1.matricule.getText().isEmpty() || menu1.namePatient.getText().isEmpty() || menu1.prenomPatient.getText().isEmpty() || menu1.identifiant.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si les champs sont vides
            }

            String date = formatDate(menu1.naissanceP.getDate());

            patient = new Patient();
            patient.setNom(menu1.namePatient.getText());
            patient.setPrenom(menu1.prenomPatient.getText());
            patient.setCni(menu1.identifiant.getText());
            patient.setMed(menu1.matricule.getText());
            patient.setDateNaissance(date);

            String detailsF = patient.detailsFacturePatient();

            String dateStr = formatDate(new Date());

            // Set the list of treatments in the facture object
            facture.setListeTraitementsEffectues(listeTraitement);

            facture.setMontant(montantTotal);
            facture.setDetails(detailsF);
            facture.setDateFacture(dateStr);
            System.out.println("Date:" + dateStr);
            boolean success = facture.creeFacture();

            if (success) {
                JOptionPane.showMessageDialog(null, "Facture établie avec succès. Montant total: " + montantTotal + " DHS", "Succès", JOptionPane.INFORMATION_MESSAGE);
                EffacerChamps();
                updateTable();
                modelEffectuer1.clear();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'établissement de la facture.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
   //-----------------------------------------
        //------------------------------------------------------------------------------------------------------------------------------------------
     private void EffacerChamps() {
        menu1.identifiant.setText("");
        menu1.namePatient.setText("");
        menu1.naissanceP.setDate(null);
        menu1.prenomPatient.setText("");
        menu1.listeEffectuer1.clearSelection();
        
        //menu1A.buttonGroup1.clearSelection();
        menu1.type.setSelectedIndex(0);
        menu1.matricule.setText("");
    }
    private void updateTable() {
        chargementDeFacture();
        loadTraitementTypes();
    }
        // Exemple de méthode pour obtenir les détails de la facture
   
    //------------------------------------------------------------------------------------------------------------------------------------------
    //FORMAT DE LA DATE
    private String formatDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    private void chargementDeTraitement(String type) {
        List<Traitement> traitementList = facture.getAllTraitement(type);
            DefaultListModel<String> model = new DefaultListModel<>();

        for (Traitement traitement : traitementList) {
            String str = traitement.getNomTraitement() + " Montant : "+ traitement.getPrix() ;
            model.addElement(str);
        }
        menu1.liste_Traitement.setModel(model);
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
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) { // Vérifier si un élément est sélectionné dans le JComboBox
            String selectedType = (String) menu1.type.getSelectedItem();
            //if (!selectedType.equals("Consultation") && !selectedType.equals("Consultation Spécialisé")) {
                chargementDeTraitement(selectedType);
            //}
        }
    }
    /// Affichage après génération
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu1.btajout) {
            EtablirFacture();
        } else if (e.getSource() == menu1.liste_Traitement && e.getClickCount() == 2) {
            
            String selectedTraitementStr = menu1.liste_Traitement.getSelectedValue();
            if (selectedTraitementStr != null) {
                DefaultListModel<String> modelEffectuer1 = getModel(menu1.listeEffectuer1);
                modelEffectuer1.addElement(selectedTraitementStr);
            }
            
        } else if (e.getSource() == menu1.listeEffectuer1 && e.getClickCount() == 2) {
            String selectedTraitementStr = menu1.listeEffectuer1.getSelectedValue();
            if (selectedTraitementStr != null) {
                DefaultListModel<String> modelEffectuer1 = getModel(menu1.listeEffectuer1);
                modelEffectuer1.removeElement(selectedTraitementStr);
            }
        } else if (e.getSource() == menu1.imprimer){
            imprimerFacture();
        }
        
    }

    private DefaultListModel<String> getModel(JList<String> list) {
        ListModel<String> model = list.getModel();
        if (model instanceof DefaultListModel) {
            return (DefaultListModel<String>) model;
        } else {
            throw new IllegalArgumentException("Le modèle de la liste n'est pas un DefaultListModel.");
        }
    }
    
    private void chargementDeFacture() {
        List<Facture> factureList = Facture.getAllFacture(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Numéro", "Détails", "Montant", "Date", "Status"}, 
            0
        );
        for (Facture facture : factureList) {
            model.addRow(new Object[]{
                facture.getIdFacture(),
                facture.getDetails(),
                facture.getMontant(),
                facture.getDateFacture(),
                facture.getStatus(),
            });
        }
        menu1.jTable1.setModel(model);
    }
    //Chercher les type de traitement etablir par l'administrateur
    private void loadTraitementTypes() {
        List<String> traiteTypes = Traitement.getAllTraitementTypes(connection);
        Set<String> uniqueServiceTypes = new HashSet<>(traiteTypes);

        // Efface la JComboBox avant d'ajouter de nouvelles valeurs pour éviter les doublons
        menu1.type.removeAllItems();

        // Ajouter les types de services uniques à la JComboBox
        for (String serviceType : uniqueServiceTypes) {
            menu1.type.addItem(serviceType);
        }
    }
    // Imprimer
    private void imprimerFacture() {
        
        int selectedRow = menu1.jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une facture à imprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) menu1.jTable1.getModel();

        String numFacture = model.getValueAt(selectedRow, 0).toString();
        String detailsP = model.getValueAt(selectedRow, 1).toString(); // Assuming this contains patient details
        String montant = model.getValueAt(selectedRow, 2).toString();
        String date = model.getValueAt(selectedRow, 3).toString();

        // Récupérer la liste des traitements à partir de la base de données
        List<String> traitements = getTraitementsForFacture(numFacture);

        // Concaténer les traitements dans une chaîne distincte
        StringBuilder traitementDetailsBuilder = new StringBuilder();
        for (String traitement : traitements) {
            traitementDetailsBuilder.append(traitement).append("\n");
        }
        String traitementDetails = traitementDetailsBuilder.toString();

        PrintFacture printFacture = new PrintFacture();

        printFacture.numeFacture.setText(numFacture);
        printFacture.details.setText(traitementDetails); // Set patient details here
        printFacture.montant.setText(montant);
        printFacture.date.setText(date);
        printFacture.infosPatient.setText(detailsP); // Set treatment details here

        printFacture.setVisible(true);
        printFrame(printFacture);
    }

    //lISTE DES TRAITEMENTS EFFECTUER
    private List<String> getTraitementsForFacture(String numFacture) {
        return facture.getTraitementsForFacture(numFacture);
    }
    
    private void printFrame(PrintFacture printFacture) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Facture");

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(0.7, 0.7); // Adjust the scale as needed

                printFacture.printAll(g2);
                return Printable.PAGE_EXISTS;
            }
        });

        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
                 printFacture.dispose();
            } catch (PrinterException ex) {
            }
        }
        printFacture.dispose();
    }
}