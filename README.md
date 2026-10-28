# Hospitalis — Logiciel de gestion hospitalière

Application de bureau en **Java Swing** et **MySQL** pour gérer les opérations internes d'un hôpital : accueil des patients, rendez-vous, dossiers médicaux, hospitalisations, facturation et stocks.

> Projet académique de fin d'études, réalisé en équipe. Prototype pédagogique, **non destiné à un usage en production**.

## Contexte et objectifs

Le volume croissant des données patients, des dossiers médicaux et des rendez-vous rend la gestion manuelle difficile et augmente les risques pour la confidentialité. Hospitalis vise à :

- automatiser les processus administratifs et opérationnels de l'hôpital ;
- centraliser les dossiers patients et la planification ;
- protéger les informations médicales par une authentification et un accès selon le rôle ;
- suivre les stocks de médicaments et de matériel.

Le cahier des charges et le dossier de conception UML se trouvent dans `Rapport/` et `Conception/`.

## Fonctionnalités

Après authentification par **matricule** et **mot de passe**, chaque utilisateur est redirigé vers l'espace de son rôle.

| Rôle | Fonctionnalités |
|---|---|
| **Accueil** | Gestion des patients, rendez-vous, chambres, hospitalisations (admission et sortie) |
| **Médecin** | Ajout de patients, consultations, dossiers médicaux (consultation, mise à jour, impression), calendrier, notifications et rendez-vous, chambres |
| **Finance** | Facturation à partir des traitements, paiement, suivi et impression des factures |
| **Stock** | Gestion des articles, livraisons aux services, suivi des niveaux de stock |
| **Administrateur** | Gestion des comptes utilisateurs, services, traitements et chambres |

## Technologies

- **Java 21**, compilé avec `--enable-preview`
- **Java Swing**, interfaces créées avec le GUI Builder de NetBeans
- **JDBC** avec **MySQL Connector/J 8.4.0**
- **MySQL 8**
- **JCalendar 1.4** (sélecteurs de date) et **AbsoluteLayout** (bibliothèque NetBeans)
- **Apache Ant** via un projet **NetBeans**

## Architecture

Le code suit une organisation **MVC** :

- `Model/` : entités métier et accès aux données (Patient, RDV, Dossiers, Facture, Stock, Chambre, Utilisateurs…)
- `Interface/` : fenêtres principales (`Screen*`) et panneaux par rôle (`component*`)
- `Controleur/` : liaison entre les vues et les modèles, organisée par rôle (`MenuAccueil`, `MenuMedecin`, `MenuFinance`, `MenuStocke`, `MenuAdmin`)

Le suivi du stock utilise le patron **Observer** (`StockObserable`).

## Arborescence

```
Projet_FE/
├── Conception/              # Diagrammes UML et modèle de données
├── Rapport/                 # Cahier des charges, cas d'utilisation, rapport de conception
└── Hospitalis/              # Projet NetBeans
    ├── build.xml
    ├── nbproject/
    └── src/
        ├── hospitalis/
        │   ├── Hospitalis.java      # Point d'entrée
        │   ├── Model/
        │   ├── Interface/
        │   └── Controleur/
        └── ressources/
            ├── Image/ et icon/      # Ressources graphiques
            └── test.sql             # Script de création de la base
```

## Prérequis

- JDK 21
- MySQL 8.x (ou équivalent compatible)
- Apache NetBeans (version 21 utilisée pendant le développement)
- Fichiers JAR :
  - `mysql-connector-j-8.4.0.jar`
  - `jcalendar-1.4.jar`
  - AbsoluteLayout (fourni avec NetBeans)

## Installation et configuration

### 1. Base de données *(à vérifier)*

Le script ne crée pas la base elle-même. Il faut donc la créer avant d'importer le script :

```bash
mysql -u <utilisateur> -p -e "CREATE DATABASE test CHARACTER SET utf8mb4;"
mysql -u <utilisateur> -p test < Hospitalis/src/ressources/test.sql
```

> Le code utilise certains noms de tables avec une majuscule (`Chambre`, `Stock`, `Facture`…). Sous Linux, réglez `lower_case_table_names=1` dans MySQL, ou utilisez Windows, pour éviter des erreurs « table inexistante ».

### 2. Connexion à la base

Les paramètres de connexion sont pour l'instant définis directement dans `Hospitalis/src/hospitalis/Hospitalis.java` :

```java
String url = "jdbc:mysql://localhost:3306/test";
String username = "<utilisateur>";
String password = "<mot_de_passe>";
```

Adaptez-les à votre environnement. Ne versionnez jamais de vrais identifiants.

### 3. Bibliothèques

Les chemins des JAR dans `nbproject/project.properties` pointent vers l'ordinateur d'origine. Dans NetBeans, ouvrez **Project Properties → Libraries** et indiquez l'emplacement local de Connector/J et de JCalendar.

## Utilisation *(à vérifier)*

1. Ouvrez le dossier `Hospitalis/` dans NetBeans.
2. Lancez **Clean and Build**, puis **Run**. La classe principale est `hospitalis.Hospitalis`.
3. Connectez-vous avec un matricule et un mot de passe existants dans la table `utilisateurs`.

Pour lancer le JAR produit dans `dist/` :

```bash
java --enable-preview -jar Hospitalis/dist/Hospitalis.jar
```

> Les mots de passe sont stockés hachés. Les identifiants de démonstration ne sont pas fournis dans ce dépôt.

## Tests et validation

Aucun test automatisé n'est fourni pour l'instant. Le projet a été validé manuellement pendant le développement.

## Sécurité et limites connues

Mesures en place :
- mots de passe hachés avec **PBKDF2-HMAC-SHA256** (65 536 itérations, sel aléatoire de 16 octets) ;
- requêtes SQL paramétrées (`PreparedStatement`) ;
- interface filtrée selon le rôle de l'utilisateur.

Limites :
- paramètres de connexion à la base écrits directement dans le code ;
- droits contrôlés uniquement par l'interface affichée, sans vérification côté logique métier ;
- tables MyISAM, donc sans clés étrangères ni transactions ;
- certaines fonctions sont incomplètes, notamment l'archivage des factures payées et l'ajout de traitements ;
- aucune journalisation des accès aux données médicales.

## Pistes d'amélioration

- Externaliser la configuration (fichier `.properties` ou variables d'environnement)
- Migrer vers Maven ou Gradle pour gérer les dépendances
- Passer à InnoDB avec des clés étrangères
- Ajouter des tests unitaires (JUnit) et d'intégration
- Contrôler les autorisations côté logique métier et journaliser les accès
- Fournir un jeu de données de démonstration fictif

## Auteurs

Projet de fin d'études réalisé en équipe *(noms des membres et établissement à compléter)*.

## Licence

À définir.
