/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospitalis.Controleur.MenuAdmin;

import hospitalis.Model.Utilisateurs;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author badra
 */
public class UtilisateursTable extends AbstractTableModel{
    
    private final String[] columnNames = {"Matricule", "Nom", "Prénom", "Date de Naissance", "Téléphone", "Email", "Spécialité", "Rôle", "Sexe"};
    private List<Utilisateurs> utilisateursList;
    
    public UtilisateursTable(List<Utilisateurs> utilisateursList) {
        this.utilisateursList = utilisateursList;
    }
    
    @Override
    public int getRowCount() {
        return utilisateursList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Utilisateurs user = utilisateursList.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getMatricule();
            case 1: return user.getNom();
            case 2: return user.getPrenom();
            case 3: return user.getDateNaissance();//Les mots de pase ne sont pas afficher
            case 4: return user.getPassword();
            case 5: return user.getTelephone();
            case 6: return user.getEmail();
            case 7: return user.getSpecialite();
            case 8: return user.getRole();
            case 9: return user.getSexe();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
