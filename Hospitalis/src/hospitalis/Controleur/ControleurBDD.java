/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author badra
 */
public class ControleurBDD {
    private static ControleurBDD instance;
    private Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL charge avec succes.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Construtor
    public ControleurBDD(String url, String username, String passwd) {
        try {
            connection = DriverManager.getConnection(url, username, passwd);
            System.out.println("Connexion a la base de donnes etablie avec succes.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion a la base de donnees : " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Methode
    public Connection getConnection(){
        return connection;   
    }
    
    // Synchronized for got one instance 
    public static synchronized ControleurBDD  getInstance(String url,String user,String pass){
        if( instance == null){
            instance = new ControleurBDD(url, user, pass);
        }
        return instance ;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Fermeture de la connexion & la base de donnees.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion a la base de donnees : " + e.getMessage());
            e.printStackTrace();
        }
    }

}