package hospitalis.Controleur;

import hospitalis.Controleur.MenuAccueil.HospitalisationControleur;
import hospitalis.Controleur.MenuAccueil.Menu1Controleur;
import hospitalis.Controleur.MenuAccueil.Menu2Controleur;
import hospitalis.Controleur.MenuAccueil.Menu3Controleur;

import hospitalis.Interface.ScreenAccueil;
import hospitalis.Interface.componentAc.Menu1;
import hospitalis.Interface.componentAc.Menu2;
import hospitalis.Interface.componentAc.Menu3;
import hospitalis.Interface.componentAc.Menu4;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;

public class AccueilControleur implements MouseListener {
    private ScreenAccueil screenAccueil;
    private Connection connection;

    private Menu1 menu1;
    private Menu2 menu2;
    private Menu3 menu3;
    private Menu4 menu4;
    
    private Menu1Controleur menu1Controleur;
    private Menu2Controleur menu2Controleur;
    private Menu3Controleur menu3Controleur;
    private HospitalisationControleur menu4Controleur;
    
    public AccueilControleur(Connection connection) {
        this.connection = connection;
        screenAccueil = new ScreenAccueil();
        screenAccueil.menu1.addMouseListener(this);
        screenAccueil.menu2.addMouseListener(this);
        screenAccueil.menu3.addMouseListener(this);
        screenAccueil.menu4.addMouseListener(this);
        //screenAccueil.menu4.addMouseListener(this);
        menu1 = Menu1.getInstance();
        menu2 = Menu2.getInstance();
        menu3 = Menu3.getInstance();
        menu4 = Menu4.getInstance();
      
        menu1Controleur = new Menu1Controleur(connection, menu1);
        menu2Controleur = new Menu2Controleur(connection, menu2);
        menu3Controleur = new Menu3Controleur(connection, menu3);
        menu4Controleur = new HospitalisationControleur(connection , menu4);
        
        mouseClicked(new MouseEvent(screenAccueil.menu1, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false));
        mousePressed(new MouseEvent(screenAccueil.menu1, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false));
    }
    public void afficherScreenAccueil() {
        screenAccueil.setVisible(true);
    }
    private void showMenu1() {
        screenAccueil.getCenterPanel().add(menu1);
        menu1Controleur.afficherMenu();
        //menu1Controleur.fetchDataToTable();
    }
    private void showMenu2() {
        screenAccueil.getCenterPanel().add(menu2);
        menu2Controleur.afficherMenu();
    }
    private void showMenu3() {
        screenAccueil.getCenterPanel().add(menu3);
        menu3Controleur.afficherMenu();
    }
    private void showMenu4() {
        screenAccueil.getCenterPanel().add(menu4);
        menu4Controleur.afficherMenu();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        screenAccueil.getCenterPanel().removeAll();

        if (e.getSource() == screenAccueil.menu1) {
            showMenu1();
        } else if (e.getSource() == screenAccueil.menu2) {
            showMenu2();
        } else if (e.getSource() == screenAccueil.menu3) {
            showMenu3();
        } else if (e.getSource() == screenAccueil.menu4){
            showMenu4();
        }

        screenAccueil.getCenterPanel().revalidate();
        screenAccueil.getCenterPanel().repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
