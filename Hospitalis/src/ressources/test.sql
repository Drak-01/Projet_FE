-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : jeu. 20 juin 2024 à 13:56
-- Version du serveur : 8.2.0
-- Version de PHP : 8.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `test`
--

-- --------------------------------------------------------

--
-- Structure de la table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
CREATE TABLE IF NOT EXISTS `calendar` (
  `jour` date NOT NULL,
  `heure` time NOT NULL,
  `matricule_med` int NOT NULL,
  PRIMARY KEY (`jour`,`heure`,`matricule_med`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `chambre`
--

DROP TABLE IF EXISTS `chambre`;
CREATE TABLE IF NOT EXISTS `chambre` (
  `num_chambre` int NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `disponibilite` varchar(20) DEFAULT NULL,
  `categorie` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`num_chambre`)
) ;

--
-- Déchargement des données de la table `chambre`
--

INSERT INTO `chambre` (`num_chambre`, `type`, `disponibilite`, `categorie`) VALUES
(12, 'Double', 'Disponible', 'Hospitalisation'),
(145, 'Simple', 'Disponible', 'Hospitalisation');

-- --------------------------------------------------------

--
-- Structure de la table `consultation`
--

DROP TABLE IF EXISTS `consultation`;
CREATE TABLE IF NOT EXISTS `consultation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `nom_prenom` varchar(100) NOT NULL,
  `heure` varchar(50) NOT NULL,
  `dure` varchar(50) NOT NULL,
  `contenu` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `dossier`
--

DROP TABLE IF EXISTS `dossier`;
CREATE TABLE IF NOT EXISTS `dossier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idpatient` int NOT NULL,
  `nom_P` varchar(100) NOT NULL,
  `prenom_P` varchar(100) NOT NULL,
  `infos_medi` varchar(50) DEFAULT NULL,
  `prescription` varchar(50) DEFAULT NULL,
  `resultats_test` varchar(50) DEFAULT NULL,
  `date` date NOT NULL,
  `matricule_med` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `facture`
--

DROP TABLE IF EXISTS `facture`;
CREATE TABLE IF NOT EXISTS `facture` (
  `num_facture` varchar(50) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `montant` decimal(10,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `listeTraitement` text,
  `status` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`num_facture`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `facture`
--

INSERT INTO `facture` (`num_facture`, `details`, `montant`, `date`, `listeTraitement`, `status`) VALUES
('FACT240528160857', 'Nom: Younoussa\nPrénom: ABDOUL WAHAB\nDate de Naissance: 2002-11-11\nCNI: U001136K\n\nNom: BADRA\n', 10200.00, '2024-05-28', 'Appendicectomie Montant : 10000.0\nRadiographie Thoracique Montant : 200.0', 'Attent'),
('FACT240528152357', 'Nom: ABDOUL\nPrénom: WAHAB\nDate de Naissance: 2024-05-01\nCNI: U001136K\n\nNom: STOPEUR\n', 95.00, '2024-05-28', 'Consultation en gastro-entérologie Montant : 95.0', 'payé'),
('FACT240528152608', 'Nom: SOUNA\nPrénom: ABDOUL\nDate de Naissance: 2024-05-22\nCNI: 111\n\nNom: BOUBA\n', 275.00, '2024-05-28', 'Consultation en gastro-entérologie Montant : 95.0\nConsultation en cardiologie Montant : 90.0\nConsultation en urologie Montant : 90.0', 'payé'),
('FACT240528152754', 'Nom: ABS\nPrénom: KALI\nDate de Naissance: 2024-05-02\nCNI: U7856\n\nNom: KJ\n', 275.00, '2024-05-28', 'Consultation en gastro-entérologie Montant : 95.0\nConsultation en cardiologie Montant : 90.0\nConsultation en urologie Montant : 90.0', 'payé'),
('FACT240528153520', 'Nom: AL\nPrénom: s\nDate de Naissance: 2024-05-08\nCNI: ud\n\nNom: edd\n', 75.00, '2024-05-28', 'Consultation en dermatologie Montant : 75.0', 'payé'),
('FACT240528155000', 'Nom: BADRA\nPrénom: AZERTY\nDate de Naissance: 2024-05-01\nCNI: U001141D\n\nNom: ALIOU\n', 325.00, '2024-05-28', 'Consultation en dermatologie Montant : 75.0\nConsultation en orthopédie Montant : 85.0\nConsultation en neurologie Montant : 80.0\nConsultation en endocrinologie Montant : 85.0', 'payé'),
('FACT240528155811', 'Nom: Keita\nPrénom: Badra\nDate de Naissance: 2024-05-01\nCNI: 1455i\n\nNom: lkdq\n', 270.00, '2024-05-28', 'Consultation en endocrinologie Montant : 85.0\nConsultation en gastro-entérologie Montant : 95.0\nConsultation en urologie Montant : 90.0', 'Attent'),
('FACT240528160103', 'Nom: Younoussa\nPrénom: Abdoul Wahab\nDate de Naissance: 2002-11-11\nCNI: U001136K\n\nNom: Badra Aliou\n', 860.00, '2024-05-28', 'Consultation en urologie Montant : 90.0\nConsultation en cardiologie Montant : 90.0\nConsultation en soins primaires Montant : 50.0\nConsultation en gynécologie Montant : 70.0\nConsultation en médecine interne Montant : 65.0\nConsultation médicale générale Montant : 50.0\nConsultation en gastro-entérologie Montant : 95.0\nConsultation en endocrinologie Montant : 85.0\nConsultation en endocrinologie Montant : 85.0\nConsultation en cardiologie Montant : 90.0\nConsultation en cardiologie Montant : 90.0', 'Attent');

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `facturespayees`
-- (Voir ci-dessous la vue réelle)
--
DROP VIEW IF EXISTS `facturespayees`;
CREATE TABLE IF NOT EXISTS `facturespayees` (
`num_facture` varchar(50)
,`details` varchar(255)
,`montant` decimal(10,2)
,`date` date
,`status` varchar(15)
);

-- --------------------------------------------------------

--
-- Structure de la table `hospitalisation`
--

DROP TABLE IF EXISTS `hospitalisation`;
CREATE TABLE IF NOT EXISTS `hospitalisation` (
  `id_hospitalisation` int NOT NULL AUTO_INCREMENT,
  `id_patient` int DEFAULT NULL,
  `num_chambre` int DEFAULT NULL,
  `date_admission` date DEFAULT NULL,
  `date_sortie` date DEFAULT NULL,
  PRIMARY KEY (`id_hospitalisation`),
  KEY `id_patient` (`id_patient`),
  KEY `num_chambre` (`num_chambre`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `hospitalisation`
--

INSERT INTO `hospitalisation` (`id_hospitalisation`, `id_patient`, `num_chambre`, `date_admission`, `date_sortie`) VALUES
(3, 1, 12, '2024-05-30', '2024-05-30'),
(2, 1, 12, '2024-05-30', '2024-05-30'),
(5, 1, 12, '2024-05-30', '2024-05-30'),
(6, 2, 12, '2024-05-30', '2024-05-30');

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification` (
  `message` text NOT NULL,
  `date_envoi` date NOT NULL,
  `matricule_med` int NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `patient`
--

DROP TABLE IF EXISTS `patient`;
CREATE TABLE IF NOT EXISTS `patient` (
  `id` int NOT NULL,
  `dateNais` date NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `sexe` varchar(10) NOT NULL,
  `etatcivil` varchar(20) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `sanguin` varchar(10) NOT NULL,
  `taille` varchar(50) DEFAULT NULL,
  `poids` varchar(50) DEFAULT NULL,
  `lien` varchar(100) DEFAULT NULL,
  `assureur` varchar(100) DEFAULT NULL,
  `profession` varchar(100) DEFAULT NULL,
  `adresse` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `patient`
--

INSERT INTO `patient` (`id`, `dateNais`, `nom`, `prenom`, `sexe`, `etatcivil`, `telephone`, `sanguin`, `taille`, `poids`, `lien`, `assureur`, `profession`, `adresse`) VALUES
(1, '2024-05-03', 'Keita', 'ALi', 'Masculin', 'Celibataire', '0614163443', 'A+', '1.70', '70', 'oui', '010', 'etudiant', 'errachidia'),
(2, '2003-07-11', 'Coulibaly', 'Lassine', 'Masculin', 'Celibataire', '0758921235', 'B+', '1.70', '69', 'Moussa', '487', 'Etudiant', 'Errachidia');

-- --------------------------------------------------------

--
-- Structure de la table `patient_med`
--

DROP TABLE IF EXISTS `patient_med`;
CREATE TABLE IF NOT EXISTS `patient_med` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dateNais` date NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `sexe` varchar(10) NOT NULL,
  `etatcivil` varchar(20) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `sanguin` varchar(10) NOT NULL,
  `taille` varchar(50) NOT NULL,
  `poids` varchar(50) NOT NULL,
  `lien` varchar(100) NOT NULL,
  `assureur` varchar(100) NOT NULL,
  `profession` varchar(100) NOT NULL,
  `adresse` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `rendez_vous`
--

DROP TABLE IF EXISTS `rendez_vous`;
CREATE TABLE IF NOT EXISTS `rendez_vous` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_rdv` date NOT NULL,
  `heure` varchar(50) NOT NULL,
  `details_rdv` text,
  `matricule_med` varchar(50) NOT NULL,
  `type_rdv` varchar(50) NOT NULL,
  `statut` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE IF NOT EXISTS `service` (
  `code_service` int NOT NULL AUTO_INCREMENT,
  `type_service` varchar(50) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`code_service`)
) ENGINE=MyISAM AUTO_INCREMENT=476 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `service`
--

INSERT INTO `service` (`code_service`, `type_service`, `description`) VALUES
(123, 'D\'Urgance', 'Le service des urgences est dédié à la prise en charge rapide et efficace des patients présentant des problèmes de santé aigus, potentiellement graves ou menaçant la vie. Ce service est opérationnel 24 heures sur 24, 7 jours sur 7, et est équipé pour répondre à une large gamme de situations médicales urgentes.'),
(124, 'Administratifs', 'pour le accueil et autre '),
(125, 'Traumatologie', 'Le service de traumatologie est une branche spécialisée de la médecine qui s\'occupe du diagnostic, du traitement et de la réhabilitation des patients ayant subi des blessures physiques, souvent causées par des accidents ou des impacts violents. Ce service traite une variété de blessures, allant des fractures osseuses aux traumatismes crâniens, en passant par les lésions des tissus mous et les blessures articulaires.,'),
(12, 'Cardiologie', 'Le service de cardiologie est spécialisé dans le diagnostic et le traitement des maladies du cœur et des vaisseaux sanguins. Il traite des conditions telles que les crises cardiaques, l\'insuffisance cardiaque, les arythmies et l\'hypertension. Les cardiologues effectuent des tests diagnostiques tels que les ECG, les échocardiogrammes et les tests de stress cardiaque.'),
(17, 'Neurologie', 'Le service de neurologie traite les troubles du système nerveux, y compris le cerveau, la moelle épinière et les nerfs périphériques. Les neurologues diagnostiquent et traitent des conditions telles que les accidents vasculaires cérébraux, l\'épilepsie, la sclérose en plaques, la maladie de Parkinson et les migraines.'),
(475, 'Pédiatrie', ' Le service de pédiatrie est dédié à la santé et au bien-être des nourrissons, des enfants et des adolescents. Les pédiatres fournissent des soins préventifs, diagnostiquent et traitent les maladies infantiles, et gèrent les troubles chroniques chez les enfants.'),
(148, 'Gynécologie et Obstétrique', 'Ce service couvre la santé reproductive des femmes, y compris la grossesse, l\'accouchement et les troubles gynécologiques. Les obstétriciens-gynécologues (OB-GYN) prennent en charge les grossesses normales et à risque, effectuent des accouchements et traitent des conditions telles que les fibromes utérins, les cancers gynécologiques et les troubles menstruels.'),
(178, 'Radiologie', 'Le service de radiologie utilise des techniques d\'imagerie médicale pour diagnostiquer et traiter des maladies. Les radiologues interprètent les images obtenues par radiographie, tomodensitométrie (CT), résonance magnétique (IRM), échographie et médecine nucléaire pour aider à diagnostiquer diverses conditions médicales.');

-- --------------------------------------------------------

--
-- Structure de la table `stock`
--

DROP TABLE IF EXISTS `stock`;
CREATE TABLE IF NOT EXISTS `stock` (
  `id_article` int NOT NULL AUTO_INCREMENT,
  `article` varchar(50) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `quantite` mediumtext,
  `date_entre` date DEFAULT NULL,
  PRIMARY KEY (`id_article`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `stock`
--

INSERT INTO `stock` (`id_article`, `article`, `type`, `quantite`, `date_entre`) VALUES
(1, 'A1', 'Médicament', '25', '2024-05-16'),
(2, 'AD4', 'Médicament', '2', '2024-05-16'),
(3, 'A7', 'Matriel', '10', '2024-05-16'),
(4, 'A', 'Médicament', '230', '2024-05-31'),
(5, 'A7', 'Matriel', '14', '2024-05-24'),
(6, 'Q4', 'Médicament', '145', '2024-05-08');

-- --------------------------------------------------------

--
-- Structure de la table `stock_livre`
--

DROP TABLE IF EXISTS `stock_livre`;
CREATE TABLE IF NOT EXISTS `stock_livre` (
  `code_service` int DEFAULT NULL,
  `id_article` int DEFAULT NULL,
  `quantite` mediumtext,
  `date` date DEFAULT NULL,
  KEY `code_service` (`code_service`),
  KEY `id_article` (`id_article`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `stock_livre`
--

INSERT INTO `stock_livre` (`code_service`, `id_article`, `quantite`, `date`) VALUES
(123, 3, '14', '2024-05-23'),
(124, 3, '200', '2024-05-14'),
(124, 3, '30', '2024-05-31'),
(123, 4, '24', '2024-05-06'),
(125, 2, '23', '2024-05-28');

-- --------------------------------------------------------

--
-- Structure de la table `traitement`
--

DROP TABLE IF EXISTS `traitement`;
CREATE TABLE IF NOT EXISTS `traitement` (
  `codeTraitement` int NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `description` text,
  `prix` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`codeTraitement`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `traitement`
--

INSERT INTO `traitement` (`codeTraitement`, `type`, `nom`, `description`, `prix`) VALUES
(1, 'Consultation générale', 'Consultation médicale générale', 'Examen médical de routine pour évaluer la santé globale du patient', 50.00),
(2, 'Consultation spécialisée', 'Consultation en neurologie', 'Examen médical avec un spécialiste des troubles neurologiques', 80.00),
(3, 'Consultation générale', 'Consultation pédiatrique', 'Examen médical pour les enfants', 60.00),
(4, 'Consultation spécialisée', 'Consultation en cardiologie', 'Examen médical avec un spécialiste des problèmes cardiaques', 90.00),
(5, 'Consultation générale', 'Consultation en gynécologie', 'Examen médical pour les problèmes de santé des femmes', 70.00),
(6, 'Consultation spécialisée', 'Consultation en dermatologie', 'Examen médical avec un spécialiste des problèmes de peau', 75.00),
(7, 'Consultation générale', 'Consultation en médecine interne', 'Examen médical pour les problèmes de santé internes', 65.00),
(8, 'Consultation spécialisée', 'Consultation en orthopédie', 'Examen médical avec un spécialiste des problèmes musculo-squelettiques', 85.00),
(9, 'Consultation générale', 'Consultation en médecine familiale', 'Examen médical pour les membres de la famille', 55.00),
(10, 'Consultation spécialisée', 'Consultation en ophtalmologie', 'Examen médical avec un spécialiste des problèmes de vision', 80.00),
(11, 'Consultation générale', 'Consultation en médecine sportive', 'Examen médical pour les athlètes et les amateurs de sport', 75.00),
(12, 'Consultation spécialisée', 'Consultation en gastro-entérologie', 'Examen médical avec un spécialiste des troubles gastro-intestinaux', 95.00),
(13, 'Consultation générale', 'Consultation en santé mentale', 'Examen médical pour les problèmes de santé mentale', 60.00),
(14, 'Consultation spécialisée', 'Consultation en endocrinologie', 'Examen médical avec un spécialiste des troubles hormonaux', 85.00),
(15, 'Consultation générale', 'Consultation en soins primaires', 'Examen médical de routine pour les soins de santé de base', 50.00),
(16, 'Consultation spécialisée', 'Consultation en urologie', 'Examen médical avec un spécialiste des troubles urinaires et reproductifs', 90.00),
(17, 'Consultation', 'Consultation Générale', 'Consultation médicale générale', 40.00),
(18, 'Consultation Spécialisée', 'Consultation Cardiologique', 'Consultation avec un cardiologue', 500.00),
(19, 'Radiologie', 'Radiographie Thoracique', 'Imagerie radiographique de la région thoracique', 200.00),
(20, 'Chirurgie', 'Appendicectomie', 'Chirurgie de retrait de l\'appendice', 10000.00),
(21, 'Médecine Interne', 'Consultation Diabétologie', 'Consultation pour gestion du diabète', 450.00),
(22, 'Laboratoire', 'Analyse de Sang', 'Tests sanguins complets', 150.00),
(23, 'Physiothérapie', 'Séance de Kinésithérapie', 'Traitement par kinésithérapie', 250.00);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateurs`
--

DROP TABLE IF EXISTS `utilisateurs`;
CREATE TABLE IF NOT EXISTS `utilisateurs` (
  `Matricule` varchar(50) NOT NULL,
  `Nom` varchar(100) DEFAULT NULL,
  `Prenom` varchar(100) DEFAULT NULL,
  `DateNaissance` date DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Telephone` varchar(15) DEFAULT NULL,
  `Specialite` varchar(100) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Role` varchar(50) DEFAULT NULL,
  `Sexe` enum('Homme','Femme') DEFAULT NULL,
  PRIMARY KEY (`Matricule`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `utilisateurs`
--

INSERT INTO `utilisateurs` (`Matricule`, `Nom`, `Prenom`, `DateNaissance`, `Password`, `Telephone`, `Specialite`, `Email`, `Role`, `Sexe`) VALUES
('12345', 'Keita', 'Badra', '2002-12-09', 'h5ZzYeARgPCmzGKzhOzRMbVt9E+3By4wKEqlHaS8prA=', '614163443', 'Chirugie', 'keitamister@gmail.com', 'Administrateur', 'Homme'),
('12369', 'sy', 'Kali', '2000-06-01', 'LQpscbUyUINxytxF/HI374o9SI+1Akp1Fap+oZO81aI=', '678895124', 'Docteur', 'sy@gmail.com', 'Administrateur', 'Homme'),
('56884', 'Bah', 'Demba', '2003-06-05', 'nGHAoWOgnk98+BOmNWv0neKNabPz5p64ghnRzb2l+vk=', '689456235', 'Traumatologue', 'bad@gmail.com', 'Finance', 'Homme'),
('75788', 'Sissoko', 'Oumou', '2000-06-09', 'Pa1XZUuPKLwGW5JdlKsoE2+io2k1Nl5zoStWr10IwcQ=', '785124896', 'Docteur', 'sissoko@gmail.com', 'Accueil', 'Femme'),
('12452', 'S', 'Moussa', '2002-06-05', 'khyv3cx0ZkBsbXKNByCoARWHYDfHn3L+iNeLADRH3a8=', '784595269', 'Medecin', 'moussa@gmail.com', 'Médecin', 'Homme'),
('234', 'Souna', 'Abdoul', '2002-09-11', 'rc6OUaFcUIX4gGROdNRc/1NK7N7m0TgPdSfg3ndtdKI=', '678521124', 'responsable stockage', 'souna@gmail.com', 'Stock', 'Homme');

-- --------------------------------------------------------

--
-- Structure de la vue `facturespayees`
--
DROP TABLE IF EXISTS `facturespayees`;

DROP VIEW IF EXISTS `facturespayees`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `facturespayees`  AS SELECT `facture`.`num_facture` AS `num_facture`, `facture`.`details` AS `details`, `facture`.`montant` AS `montant`, `facture`.`date` AS `date`, `facture`.`status` AS `status` FROM `facture` WHERE (`facture`.`status` = 'payé') ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
