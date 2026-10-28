import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    // Champs de texte pour le nom d'utilisateur et le mot de passe
    private JTextField usernameField;
    private JPasswordField passwordField;

    // Informations de connexion à la base de données MySQL
    private static final String URL ="jdbc:mysql://localhost:3306/mabase";
    private static final String UTILISATEUR_DB = "root";
    private static final String MOT_DE_PASSE_DB = "";

    public LoginFrame() {
        super("Login");

        // Créer les composants de l'interface utilisateur
        usernameField = new JTextField(50);
        passwordField = new JPasswordField(50);
        JButton loginButton = new JButton("Login");
        JButton clearButton = new JButton("Clear");

        // Ajouter des écouteurs d'événements pour les boutons
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (verifierIdentifiants(username, password)) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Authentification réussie pour l'utilisateur " + username);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Échec de l'authentification. Veuillez vérifier vos identifiants.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        // Créer le panneau et ajouter les composants
        JPanel panel = new JPanel(new GridLayout(30, 20));
        panel.add(new JLabel("Nom d'utilisateur:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(clearButton);
        //JLabel backgroundLabel = new JLabel(new ImageIcon("hms.jpg")); 
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        // Ajouter le panneau au cadre
        add(panel);

        // Configurer le cadre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login Java");
        panel.setSize(150, 180);
        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre
        setVisible(true);

       
    }

    // Méthode pour vérifier les identifiants de connexion en interrogeant la base de données MySQL
    private boolean verifierIdentifiants(String username, String password) {
        try (Connection connexion = DriverManager.getConnection(URL, UTILISATEUR_DB, MOT_DE_PASSE_DB)) {
            // Préparer la requête SQL pour récupérer l'utilisateur avec le nom d'utilisateur et le mot de passe spécifiés
            String sql = "SELECT * FROM user WHERE nom = ? AND pass = ?";
            try (PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Vérifier si l'utilisateur existe et si les identifiants sont valides
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(LoginFrame.this, "Erreur de connexion à la base de données. Veuillez vérifier les paramètres de connexion.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        // Les identifiants sont invalides ou une erreur s'est produite lors de la connexion à la base de données
        return false;
    }

    public static void main(String[] args) {
        // Créer et afficher le cadre de connexion
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame();
            }
        });
    }
}
