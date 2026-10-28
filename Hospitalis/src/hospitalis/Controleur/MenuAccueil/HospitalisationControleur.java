/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuAccueil;

import hospitalis.Interface.componentAc.Menu4;
import hospitalis.Model.Hospitatlisation;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.List;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author badra
 */
public class HospitalisationControleur implements MouseListener{
    private Connection connection;
    private Menu4 menu;
    private static HospitalisationControleur instanceM1;
    
    public HospitalisationControleur(Connection connection, Menu4 menu) {
        this.connection = connection;
        this.menu = menu;
        
        //this.menu1A.btajouter.addMouseListener(this);
        
    }
    
    //Contrôle la creation des instances lors de l'arriver pour ne pas créer a chaque fois une autre instance ( surcharge).
    public static HospitalisationControleur getInstance(Connection connection, Menu4 menu1A) {
        if (instanceM1 == null) {
            synchronized (HospitalisationControleur.class) {
                if (instanceM1 == null) {
                    instanceM1 = new HospitalisationControleur(connection,menu1A);
                    System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instanceM1;
    }

    
    public void afficherMenu() {
        if (menu.isVisible()) {
            menu.setVisible(false);
        } else {
            menu.setVisible(true);
        }
        chargementDeStocke();
        //EffacerChamps();
    }
    private void chargementDeStocke() {
        List<Hospitatlisation> hospitalList = Hospitatlisation.getAllHospitatlisation(connection);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Numéro", "Numéro Patient", "Numéro Chambre", "Date Admission", "Date Sortie"}, 
            0
        );
        for (Hospitatlisation hospi : hospitalList) {
            model.addRow(new Object[]{
               hospi.getNumHospital(),
               hospi.getIdPatient(),
               hospi.getNumChambre(),
               hospi.getDateAdmission(),
               hospi.getDateSortie(),
            });
        }
        menu.jthospi.setModel(model);
    }
    //
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
