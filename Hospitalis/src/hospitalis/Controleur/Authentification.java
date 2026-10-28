/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur;

import hospitalis.Interface.ScreenAdmin;
import hospitalis.Interface.ScreenAuthentification;
import hospitalis.Interface.ScreenMedecin;
import hospitalis.Model.Utilisateurs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.JOptionPane;


/**
 *
 * @author badra
 */
public class Authentification implements ActionListener {
    
    //Attributs
    private ScreenAuthentification screenAuthentification;
    private Utilisateurs users;
    protected Connection connection;
    public static String matMed;
    
    //Constructeur
    //Lors de la création de l'instance on obtient la connexion avec la base de données 
    public Authentification (Connection connection){
        this.screenAuthentification = new ScreenAuthentification();
        // Lors de la création de l'instance de l'utilisateur on envoyer la connection existant pour les methodes
        this.users = new Utilisateurs(connection);
        this.connection = connection;
        this.screenAuthentification.btconnecter.addActionListener(this);
    }
    
    //Methode
    public void afficherFenetreAuthentication(){
        screenAuthentification.setVisible(true);
    }
    //Essayer de stock
    //Cette methode nous permet de premet le meilleurs chemin pour chaque interface
    public void redirection() {
    // Obtenez le rôle de l'utilisateur
    String role = users.getRole();
    
    // Effectuez la redirection en fonction du rôle
    // Exemple : new chemin().setVisible(true); et envoyer la connection a la base de données
    switch (role) {
        case "Administrateur":
            // Redirection pour l'administrateur
            AdminControleur adminControleur = new AdminControleur(connection);
            adminControleur.afficherScreenAdmin();
            System.out.println("Admin authentification réussi");
            screenAuthentification.dispose();
            break;
        case "Médecin":
            // Redirection pour le médecin
            matMed = screenAuthentification.jmatricule.getText();
            MedecinControleur medecinControleur = new MedecinControleur(connection);
            medecinControleur.afficherScreenMedecin();
            screenAuthentification.dispose();
            break;
        case "Finance":
            // Redirection pour le service finance
            FinanceControleur finance = new  FinanceControleur(connection);
            finance.afficherScreenFinance();
            screenAuthentification.dispose();            
            break;
        case "Stock":
            // Redirection pour le service stocke
            StockeControleur stockeControleur = new StockeControleur(connection);
            stockeControleur.afficherStockeScreen();
            screenAuthentification.dispose();            
            break;
        case "Accueil":
            // Redirection pour le service accueil
            AccueilControleur ac = new AccueilControleur(connection);
            ac.afficherScreenAccueil();
            screenAuthentification.dispose();           
            break;
        default:
            // Gérer le cas où le rôle n'est pas reconnu
            break;
    }
}
    public void Verification(ActionEvent e){
        //Permet de verifier les informations
        users.setMatricule(screenAuthentification.jmatricule.getText());
        char[] password = screenAuthentification.jpassword.getPassword();//Permet d'obtenir les caractere de la password et le recontruire afin de le stocke dans le users.password
        String passwordString = new String(password);
        users.setPassword(passwordString);
        if(users.seConnecter()){// si la connection est OK
            redirection();
            System.out.println("Les identifiants sont correctent");
        }else{// la connection n'est pas réussi
            JOptionPane.showMessageDialog(null, "Matricule ou mot de passe incorrect!", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            screenAuthentification.resetFields(); // Réinitialise les champs de texte   
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Verification(e);
    }
    
    
      
    
}
