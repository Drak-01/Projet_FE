package hospitalis.Controleur;

import hospitalis.Controleur.MenuMedecin.Menu1Controleur;
import hospitalis.Controleur.MenuMedecin.Menu2Controleur;
import hospitalis.Controleur.MenuMedecin.Menu3Controleur;
import hospitalis.Controleur.MenuMedecin.Menu4Controleur;
import hospitalis.Controleur.MenuMedecin.Menu5Controleur;
import hospitalis.Controleur.MenuMedecin.Menu6Controleur;
import hospitalis.Interface.ScreenMedecin;
import hospitalis.Interface.componentMe.Menu1;
import hospitalis.Interface.componentMe.Menu2;
import hospitalis.Interface.componentMe.Menu3;
import hospitalis.Interface.componentMe.Menu4;
import hospitalis.Interface.componentMe.Menu5;
import hospitalis.Interface.componentMe.Menu6;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;

public class MedecinControleur implements MouseListener {
    
    private ScreenMedecin screenMedecin;
    private Connection connection;

    private Menu1 menu1;
    private Menu2 menu2;
    private Menu3 menu3;
    private Menu4 menu4;
    private Menu5 menu5;
    private Menu6 menu6;

    private Menu1Controleur menu1Controleur;
    private Menu2Controleur menu2Controleur;
    private Menu3Controleur menu3Controleur;
    private Menu4Controleur menu4Controleur;
    private Menu5Controleur menu5Controleur;
    private Menu6Controleur menu6Controleur;
    
    public MedecinControleur(Connection connection) {
        this.connection = connection;
        screenMedecin = new ScreenMedecin();
       
        this.screenMedecin.menu1.addMouseListener(this);
        this.screenMedecin.menu2.addMouseListener(this);
        this.screenMedecin.menu3.addMouseListener(this);
        this.screenMedecin.menu4.addMouseListener(this);
        this.screenMedecin.menu5.addMouseListener(this);
        this.screenMedecin.menu6.addMouseListener(this);
        
        menu1 = Menu1.getInstance();
        menu2 = Menu2.getInstance();
        menu3 = Menu3.getInstance();
        menu4 = Menu4.getInstance();
        menu5 = Menu5.getInstance();
        menu6 = Menu6.getInstance();

        menu1Controleur = new Menu1Controleur(connection, menu1);
        menu2Controleur = new Menu2Controleur(connection, menu2);
        menu3Controleur = new Menu3Controleur(connection, menu3);
        menu4Controleur = new Menu4Controleur(connection, menu4,menu1Controleur);
        menu5Controleur = new Menu5Controleur(connection, menu5);
        menu6Controleur = new Menu6Controleur(connection, menu6);
        
        // Simuler un événement de clic sur le menu 1
        mouseClicked(new MouseEvent(screenMedecin.menu1, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false));
    }

    public void afficherScreenMedecin() {
        screenMedecin.setVisible(true);
    }

    private void showMenu1() {
        screenMedecin.getCenterPanel().add(menu1);
        menu1Controleur.afficherMenu();
    }

    private void showMenu2() {
        screenMedecin.getCenterPanel().add(menu2);
        menu2Controleur.afficherMenu();
    }

    private void showMenu3() {
        screenMedecin.getCenterPanel().add(menu3);
        menu3Controleur.afficherMenu();
    }

    private void showMenu4() {
        screenMedecin.getCenterPanel().add(menu4);
        menu4Controleur.afficherMenu();
    }

    private void showMenu5() {
        screenMedecin.getCenterPanel().add(menu5);
        menu5Controleur.afficherMenu();
    }

    private void showMenu6() {
        screenMedecin.getCenterPanel().add(menu6);
        menu6Controleur.afficherMenu();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        screenMedecin.getCenterPanel().removeAll();

        if (e.getSource() == screenMedecin.menu1) {
            showMenu1();
        } else if (e.getSource() == screenMedecin.menu2) {
            showMenu2();
        } else if (e.getSource() == screenMedecin.menu3) {
            showMenu3();
        } else if (e.getSource() == screenMedecin.menu4) {
            showMenu4();                 
        } else if (e.getSource() == screenMedecin.menu5) {
            showMenu5();
        } else if (e.getSource() == screenMedecin.menu6) {
            showMenu6();
        }

        screenMedecin.getCenterPanel().revalidate();
        screenMedecin.getCenterPanel().repaint();
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
