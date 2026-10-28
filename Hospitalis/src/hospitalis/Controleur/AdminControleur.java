package hospitalis.Controleur;

import hospitalis.Controleur.MenuAdmin.ChambreControleur;
import hospitalis.Controleur.MenuAdmin.Menu1AControleur;
import hospitalis.Controleur.MenuAdmin.Menu1ASControleur;
import hospitalis.Controleur.MenuAdmin.Menu1ATControleur;
import hospitalis.Interface.ScreenAdmin;
import hospitalis.Interface.componentAD.Menu1A;
import hospitalis.Interface.componentAD.Menu1AC;
import hospitalis.Interface.componentAD.Menu1AS;
import hospitalis.Interface.componentAD.Menu1AT;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;

public class AdminControleur implements MouseListener {
    // Attributs
    private final ScreenAdmin screenAdmin;
    private final Connection connection;
    
    
    private  Menu1A menu1A;    
    private  Menu1AS menu1AS;
    private  Menu1AT menu1AT;
    private  Menu1AC menu1AC;
    
    //Permet de stocke lors des switchs vers les differents panel
    private final Menu1AControleur menu1AControleur;
    private final Menu1ASControleur menu1ASControleur;
    private final Menu1ATControleur menu1ATControleur;
    private final ChambreControleur chambreControleur ;
    

    // Constructeur
    public AdminControleur(Connection connection) {
        this.connection = connection;
        screenAdmin = new ScreenAdmin();
        
        this.screenAdmin.menu1.addMouseListener(this);
        this.screenAdmin.menu2.addMouseListener(this);
        this.screenAdmin.menu3.addMouseListener(this);
        this.screenAdmin.menu4.addMouseListener(this);
        
        
        this.menu1A = Menu1A.getInstance();
        this.menu1AS = Menu1AS.getInstance();
        this.menu1AT = Menu1AT.getInstance();
        this.menu1AC = Menu1AC.getInstance();
        
        this.menu1AControleur = Menu1AControleur.getInstance(connection, menu1A);
        this.menu1ASControleur = Menu1ASControleur.getInstance(connection, menu1AS);
        this.menu1ATControleur = Menu1ATControleur.getInstance(connection, menu1AT);
        this.chambreControleur = ChambreControleur.getInstance(connection, menu1AC);
        mouseClicked(new MouseEvent(screenAdmin.menu1, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false));

    }

    // Méthode
    public void afficherScreenAdmin() {
        screenAdmin.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        screenAdmin.getCenterPanel().removeAll();

        if (e.getSource() == screenAdmin.menu1) {
            showMenu1A();
        } else if (e.getSource() == screenAdmin.menu2) {
            showMenu1AS();
        } else if (e.getSource() == screenAdmin.menu3) {
            showMenu1AT();
        } else if(e.getSource() == screenAdmin.menu4){
            showMenu1AC();
        }

        screenAdmin.getCenterPanel().revalidate();
        screenAdmin.getCenterPanel().repaint();
    }
    //**************
    private void showMenu1A() {
        //System.out.println("Appel a getInstance");
        //Menu1A menu1A = Menu1A.getInstance(); // Using the singleton instance
        screenAdmin.getCenterPanel().add(menu1A);
        menu1AControleur.afficherMenu();
    }

    private void showMenu1AS() {
        System.out.println("Appel a getInstance menu1AS");
        //Menu1A menu1A = Menu1A.getInstance(); // Using the singleton instance
        screenAdmin.getCenterPanel().add(menu1AS);
        menu1ASControleur.afficherMenu();
    }

    private void showMenu1AT() {
        // System.out.println("Appel a getInstance menu1AT");
        //Menu1A menu1A = Menu1A.getInstance(); // Using the singleton instance
        screenAdmin.getCenterPanel().add(menu1AT);
        menu1ATControleur.afficherMenu();
    }
    private void showMenu1AC() {
       // System.out.println("Appel a getInstance");
        //Menu1A menu1A = Menu1A.getInstance(); // Using the singleton instance
        screenAdmin.getCenterPanel().add(menu1AC);
        menu1AControleur.afficherMenu();
    }
    //**************

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
