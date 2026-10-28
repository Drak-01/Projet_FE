/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur;

import hospitalis.Controleur.MenuStocke.ControleurMenu1S;
import hospitalis.Controleur.MenuStocke.ControleurMenu1S2;
import hospitalis.Controleur.MenuStocke.ControleurMenu1S3;
import hospitalis.Controleur.MenuStocke.ControleurMenu1S4;
import hospitalis.Interface.ScreenStock;
import hospitalis.Interface.componentSt.Menu1S;
import hospitalis.Interface.componentSt.Menu1S2;
import hospitalis.Interface.componentSt.Menu1S3;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;


/**
 *
 * @author badra
 */
public final class StockeControleur implements MouseListener {
    //Attributsp
    private Connection connection;
    private final  ScreenStock screenStock;
    
    
    private final Menu1S menu1S;    
    private final Menu1S2 menu2S;
    private final Menu1S3 menu3S;
    //private final Menu1S4 menu4S;
    
        //Permet de stocke lors des switchs vers les differents panel
    private final ControleurMenu1S menu1SControleur;
    private final ControleurMenu1S2 menu1S2Controleur;
    private final ControleurMenu1S3 menu1S3Controleur;
    //private final ControleurMenu1S4 menu1S4Controleur;

    
    //Constructeurs
    public StockeControleur(Connection connection){
        this.connection = connection;
        screenStock = new ScreenStock();
        
        this.screenStock.menu1.addMouseListener(this);
        this.screenStock.menu2.addMouseListener(this);
        this.screenStock.menu3.addMouseListener(this);
        //this.screenStock.menu4.addMouseListener(this);
        
        this.menu1S = Menu1S.getInstance();
        this.menu2S = Menu1S2.getInstance();
        this.menu3S = Menu1S3.getInstance();
        //this.menu4S = Menu1S4.getInstance();
        
        this.menu1SControleur = ControleurMenu1S.getInstance(connection, menu1S);
        this.menu1S2Controleur = ControleurMenu1S2.getInstance(connection, menu2S);
        this.menu1S3Controleur = ControleurMenu1S3.getInstance(connection, menu3S);
        //this.menu1S4Controleur = ControleurMenu1S4.getInstance(connection, menu4S);
        mouseClicked(new MouseEvent(screenStock.menu1, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false));

       //afficherStocke();
    }

    public void afficherStockeScreen() {
        screenStock.setVisible(true);
        //showMenu1S(); // Affiche le Menu1S par défaut
    }
    //Methode
    /*
    public void afficherStocke(){
        screenStock.setVisible(true);
    }
*/
    @Override
    public void mouseClicked(MouseEvent e) {
        screenStock.getCenterPanel().removeAll();

        if (e.getSource() == screenStock.menu1) {
            showMenu1S();
        } else if (e.getSource() == screenStock.menu2) {
            showMenu1S2();
        } else if (e.getSource() == screenStock.menu3) {
            showMenu1S3();
        }/*
        else if (e.getSource() == screenStock.menu4){
            showMenu1S4();
        }
        */

        screenStock.getCenterPanel().revalidate();
        screenStock.getCenterPanel().repaint();
    }

    private void showMenu1S() {
        System.out.println("Appel à getInstance menu1S");
        screenStock.getCenterPanel().add(menu1S);
        menu1SControleur.afficherMenu();
    }

    private void showMenu1S2() {
        System.out.println("Appel a getInstance menu1S2");
        //Menu1A menu1A = Menu1A.getInstance(); // Using the singleton instance
        screenStock.getCenterPanel().add(menu2S);
        menu1S2Controleur.afficherMenu();
    }

    private void showMenu1S3() {
         System.out.println("Appel a getInstance menu1s3");
        //Menu1A menu1A = Menu1A.getInstance(); // Using the singleton instance
        screenStock.getCenterPanel().add(menu3S);
        menu1S3Controleur.afficherMenu();
    }
    /*
    private void showMenu1S4() {
         System.out.println("Appel a getInstance menu1s4");
        //Menu1A menu1A = Menu1A.getInstance(); // Using the singleton instance
        screenStock.getCenterPanel().add(menu4S);
        menu1S4Controleur.afficherMenu();
    }*/
    //---------------------------------------------------------------------------------------------
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
