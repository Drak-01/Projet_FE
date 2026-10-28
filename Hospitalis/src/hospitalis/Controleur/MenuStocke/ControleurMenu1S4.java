/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuStocke;

import hospitalis.Interface.componentSt.Menu1S4;
import java.sql.Connection;

/**
 *
 * @author badra
 */
public class ControleurMenu1S4 {
 
    //
    private Connection connection;
    private Menu1S4 menu;
    private static ControleurMenu1S4 instance;
    //
    public ControleurMenu1S4(Connection connection, Menu1S4 menu) {
        this.connection = connection;
        this.menu = menu;
        System.out.println("Appe creation menuStocke1 ");
        /*
        this.menu.btajouter.addMouseListener(this);
        this.menu1A.btsupprimer.addMouseListener(this);
        this.menu1A.btmodifier.addMouseListener(this);
        this.menu1A.jtables.getSelectionModel().addListSelectionListener(this);
        chargementDeUsers();
        */
    }
    //
    //Contrôle la creation des instances lors de l'arriver pour ne pas créer a chaque fois une autre instance ( surcharge).
    public static ControleurMenu1S4 getInstance(Connection connection, Menu1S4 menu) {
        if (instance == null) {
            synchronized (ControleurMenu1S4.class) {
                if (instance == null) {
                    instance = new ControleurMenu1S4(connection,menu);
                    System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instance;
    }
    ///
    public void afficherMenu() {
        if (menu.isVisible()) {
            menu.setVisible(false);
        } else {
            menu.setVisible(true);
        }
    }
    //**************************************************************************
    
}
