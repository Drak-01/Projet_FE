/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hospitalis;

import hospitalis.Controleur.Authentification;
import hospitalis.Controleur.ControleurBDD;
import java.sql.Connection;


/**
 *
 * @author badra
 */
public class Hospitalis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Vérification de la connection a la Base de données
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "";        
        //Ceci permet d'établir la connection  à la base de donnée
        ControleurBDD BDD = new ControleurBDD(url,username,password);
        // Ici on obtient la connexion existant a la base de données
        Connection connection = BDD.getConnection();
        //La connection est base a la base de données pour les vérification 
        Authentification authentifier = new Authentification(connection);
        authentifier.afficherFenetreAuthentication();
        
    
    }
    
}
