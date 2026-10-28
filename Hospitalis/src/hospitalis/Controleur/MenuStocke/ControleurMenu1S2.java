/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuStocke;

import hospitalis.Controleur.ImprimerDriver;
import hospitalis.Interface.componentSt.Menu1S2;
import hospitalis.Model.Stock;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.List;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author badra
 */
public class ControleurMenu1S2 implements MouseListener {
    //
    private Connection connection;
    private final Menu1S2 menu;
    private static ControleurMenu1S2 instance;
 
    //
    public ControleurMenu1S2(Connection connection, Menu1S2 menu) {
        this.connection = connection;
        this.menu = menu;
        chargementNiveau();
        System.out.println("Appe creation menuStocke1 ");  
        menu.Imprimer.addMouseListener(this);
        // Ajouter un écouteur pour détecter quand le panneau devient visible
        this.menu.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                chargementNiveau();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {}

            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }

       
    //
    //Contrôle la creation des instances lors de l'arriver pour ne pas créer a chaque fois une autre instance ( surcharge).
    public static ControleurMenu1S2 getInstance(Connection connection, Menu1S2 menu) {
        if (instance == null) {
            synchronized (ControleurMenu1S2.class) {
                if (instance == null) {
                    instance = new ControleurMenu1S2(connection,menu);
                    System.out.println("Appel a l'instance de comtr men");
                }
            }
        }
        return instance;
    }
    ///
        //**************************************************************************

        public void afficherMenu() {
            chargementNiveau();
            menu.setVisible(!menu.isVisible());
        }
        private void chargementNiveau() {
            List<Stock> stockList = Stock.getNiveau(connection);
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Article", "Type", "total_quantite", "last_date_entre"}, 
                0
            );
            for (Stock stock : stockList) {
                model.addRow(new Object[]{
                    stock.getNom(),
                    stock.getType(),
                    stock.getQuantite(),
                    stock.getDate(),
                });
            }
            menu.jtniveau.setModel(model);
        }
    //**************************************************************************
        
     private void imprimerTable(){
         String titre = "Niveau Des Stockes";
        ImprimerDriver.imprimerJtable(menu.jtniveau, titre);
     }   
    @Override
    public void mouseClicked(MouseEvent e) { 
        if(e.getSource() == menu.Imprimer){
            imprimerTable();
        }
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
