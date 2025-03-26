package Vue;
import Modele.*;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import static Modele.Carte.*;
import Modele.Carte;

public class Menu{
    private Carte carte;
    private Personnage personnage;

    public Menu( Carte carte ){
        this.carte = carte;
        this.personnage = carte.getPersonnage();
    }

    public String affichageTouche(String touche){
        return ANSI_BLACK_BACKGROUND + ANSI_PURPLE + touche + ANSI_RESET;
    }

    public List<Object> affichageAction(String action){
        Scanner sc = new Scanner(System.in);
        List<String> menu = new ArrayList<>();
        String choix;
        boolean bonChoix = false;
        String ligne;
        int nbActionAvantDeplacementAnimaux = 0;

        if (  List.of("z", "q", "s", "d").contains( action ) ){
            Case caseCible = carte.getCaseCible( action );
            if ( caseCible instanceof Vide ){ // gère en même temps caseCible null
                int persoX = personnage.getX();
                int persoY = personnage.getY();
                carte.setCaseGrille( persoX, persoY, carte.getZoneStrategy().generer(' ', persoX,persoY) );
                carte.setCaseGrille( caseCible.getX(), caseCible.getY(), personnage );
                carte.majCaseVoisinesPersoEtAnimal();
                nbActionAvantDeplacementAnimaux--;
            }else{
                System.out.printf(ANSI_RED_BACKGROUND + "Vous ne pouvez pas aller là !" + ANSI_RESET + "\n");
                sc.nextLine();
            }
        }
        else if ( action.equals("f") ){ // seBattre
            if ( !personnage.getAnimauxProches().isEmpty() ){
                List<List<Case>> animauxVoisins = personnage.getAnimauxProches();
                List<Animal> listeAnimaux = new ArrayList<Animal>();

                int animalCpt = 1;

                menu = new ArrayList<>(List.of(
                        " /\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/|\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\",
                        "||     _____                _           _      ||",
                        "||    / ____|              | |         | |     ||",
                        "||   | |     ___  _ __ ___ | |__   __ _| |_    ||",
                        "||   | |    / _ \\| '_ ` _ \\| '_ \\ / _` | __|   ||",
                        "||   | |___| (_) | | | | | | |_) | (_| | |_    ||",
                        "||    \\_____\\___/|_| |_| |_|_.__/ \\__,_|\\__|   ||",
                        "||                                             ||",
                        " \\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\|/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/",
                        ""
                ));



                for (int i=0; i<animauxVoisins.size(); i++) {
                    menu.add(" ".repeat(17) + " + - + - + - +");
                    ligne = " ".repeat(17) + " |";
                    for (int j = 0; j < animauxVoisins.get(i).size(); j++) {

                        Case c = animauxVoisins.get(i).get(j);
                        if ( i == 1 && j == 1 ) { // case du personnage
                            ligne += " " + c;
                        } else if (c instanceof Animal) {
                            ligne += c + "" + animalCpt; // "" pour garder getLettre en char et non en int
                            animalCpt++;
                            listeAnimaux.add( (Animal) c);
                        } else {
                            ligne += "  ";
                        }
                        ligne += " |";
                    }
                    menu.add( ligne );
                }

                menu.add(" ".repeat(17) + " + - + - + - +");

                Ihm.afficherPartie(menu, carte.getListeLigne());

                while ( !bonChoix ){
                    System.out.print("Choississez le nombre d'un Animal à attaquer \nOu annuler avec a\n >> ");
                    choix = sc.nextLine();

                    if ( !choix.equals("a") ){
                        try{
                            personnage.seBattre( listeAnimaux.get( Integer.parseInt(choix)-1) );
                            bonChoix = true;
                            nbActionAvantDeplacementAnimaux --;
                        }catch ( Exception e ){
                            System.out.printf(ANSI_RED_BACKGROUND + "Merci de choisir un nombre compris entre 1 et %d" + ANSI_RESET + "\n", animalCpt-1);
                        }
                    }else{
                        bonChoix = true;
                    }
                }
            }else{
                System.out.printf(ANSI_PURPLE_BACKGROUND + "Vous ne pouvez pas faire cette action !" + ANSI_RESET + "\n");
                sc.nextLine();
            }

        }
        else if ( action.equals("g") ){  // ramasserObjet
            if ( !personnage.getAnimauxAmiEtAlimentVoisins().isEmpty() ){
                List<List<Case>> animauxEtAlimentVoisins = personnage.getAnimauxAmiEtAlimentVoisins();
                List<Case> listeElement = new ArrayList<>();

                int elementCpt = 1;

                menu = new ArrayList<>(List.of(
                        "╔~~~~~{~ ❀ ~}~~~~~{~ ❀ ~}~~~~~{~ ❀ ~}~~~~~{~ ❀ ~}~~~~~╗",
                        "✿    _____                                            ✿",
                        "✿   |  __ \\                                           ✿",
                        "✿   | |__) |__ _ _ __ ___   __ _ ___ ___  ___ _ __    ✿",
                        "✿   |  _  // _` | '_ ` _ \\ / _` / __/ __|/ _ \\ '__|   ✿",
                        "✿   | | \\ \\ (_| | | | | | | (_| \\__ \\__ \\  __/ |      ✿",
                        "✿   |_|  \\_\\__,_|_| |_| |_|\\__,_|___/___/\\___|_|      ✿",
                        "✿                                                     ✿",
                        "╚~~~~~{~ ❀ ~}~~~~~{~ ❀ ~}~~~~~{~ ❀ ~}~~~~~{~ ❀ ~}~~~~~╝",
                        ""
                ));

                for (int i=0; i<animauxEtAlimentVoisins.size(); i++) {
                    menu.add(" ".repeat(20) + " + - + - + - +");
                    ligne = " ".repeat(20) + " |";
                    for (int j = 0; j < animauxEtAlimentVoisins.get(i).size(); j++) {
                        Case c = animauxEtAlimentVoisins.get(i).get(j);
                        if (c instanceof Personnage) {
                            ligne += " " + c;
                        } else if (c instanceof Animal  ||  c instanceof Aliment) {
                            ligne += c + "" + elementCpt; // "" pour garder getLettre en char et non en int
                            elementCpt++;
                            listeElement.add( c);
                        } else {
                            ligne += "  ";
                        }
                        ligne += " |";
                    }
                    menu.add( ligne );
                }

                menu.add(" ".repeat(20) + " + - + - + - +");

                Ihm.afficherPartie(menu, carte.getListeLigne());

                while ( !bonChoix ){
                    System.out.print("Choississez le nombre d'un Animal ou d'un Aliment à récupérer \nOu annuler avec a\n >> ");
                    choix = sc.nextLine();

                    if ( !choix.equals("a") ){
                        try{
                            Case objet = listeElement.get( Integer.parseInt(choix)-1);
                            if ( objet instanceof Animal ){
                                carte.removeAnimal( (Animal) objet);
                            }
                            personnage.ramasserObjet( objet );
                            carte.setCaseGrille( objet.getX(), objet.getY(), carte.getZoneStrategy().generer(' ', objet.getX(), objet.getY()) );
                            carte.majCaseVoisinesPersoEtAnimal();
                            bonChoix = true;
                            nbActionAvantDeplacementAnimaux --;
                        }catch ( Exception e ){
                            System.out.printf(ANSI_RED_BACKGROUND + "Merci de choisir un nombre compris entre 1 et %d" + ANSI_RESET + "\n", elementCpt-1);
                        }
                    }else{
                        bonChoix = true;
                    }
                }
            }else{
                System.out.printf(ANSI_PURPLE_BACKGROUND + "Vous ne pouvez pas faire cette action !" + ANSI_RESET + "\n");
                sc.nextLine();
            }
        }
        else if ( action.equals("o") ){Ihm.afficherPartie( carte.getListeLigne(), List.of(new String(Base64.getDecoder().decode("G1szNm3ioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDio4Dio4DioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIAKG1szM23ioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDiooDio7Tio7/io7/io7/io7/io6bioYDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIAKG1szMm3ioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDio7Dio7/io7/io7/io7/io7/io7/io7/io7/io4bioIDioIDioIDioIDioIDioIDioIDioIDioIDioIAKG1szNG3ioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioKDio6TioKTioIDioIDioKTio6TioITioIDioIDioIDioIDioIDioIDioIDioIDioIDioIDioIAKG1szMW3ioIDioIDioIDioIDioIDioIDioIDioIDioIDio77io7/ioYbioIjioqDio77io7fioYTioIHiorDio7/io7fioIDioIDioIDioIDioIDioIDioIDioIDioIAKG1szNm3ioIDioIDioIDioIDioIDioIDioIDioKDioIbioIjioIvioIDioLbioITioJnioIvioKDioLbioIDioJnioIHioLDioITioIDioIDioIDioIDioIDioIDioIAKG1szMm3ioIDioIDioIDioIDioIDioIDioIDio7Tio7bio7bio7bio7bio7bio7bio7bio7bio7bio7bio7bio7bio7bio7bio6bioIDioIDioIDioIDioIDioIDioIAKG1szM23ioIDioIDioIDioIDioIDioIDioIDioYnioLnio7/ioI/ioInior/iob/ioInioInior/iob/ioInioLnio7/ioI/ioonioIDioIDioIDioIDioIDioIDioIAKG1szNG3ioIDioIDioIDioIDioIDioIDiorDio7/ioIDiob/ioIDioYfioLjioYfiorjioYfiorjioIfiorjioIDior/ioIDio7/ioYbioIDioIDioIDioIDioIDioIAKG1szMm3ioIAg4qCA4qCA4qCA4qCA4qO/4qO/4qOm4qOk4qO84qO/4qOk4qOk4qO+4qO34qOk4qOk4qO/4qOn4qOk4qO04qO/4qO/4qCA4qCA4qCA4qCA4qCA4qCAChtbMzZt4qCA4qCA4qCA4qCA4qCA4qCA4qK74qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qO/4qGf4qCA4qCA4qCA4qCA4qCA4qCAChtbMzFt4qCA4qCA4qCA4qCA4qCA4qCA4qCg4qOk4qOk4qGk4qCA4qGA4qCg4qOk4qOk4qOk4qOk4qCE4qKA4qCA4qKk4qOk4qOk4qCE4qCA4qCA4qCA4qCA4qCA4qCAChtbMzNt4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qC54qCL4qOk4qO+4qO/4qO34qGM4qCb4qCb4qKh4qO+4qO/4qO34qOk4qCZ4qCP4qCA4qCA4qCA4qCA4qCA4qCA4qCAChtbMzRt4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCw4qC24qO24qO24qO24qO24qO24qO24qO24qO24qC24qCG4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCAChtbMzZt4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCI4qCJ4qCJ4qCJ4qCJ4qCB4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA4qCA")).split("\n")) );System.out.print("Oeuf de Pâque :)");choix = sc.nextLine();}

        else if ( action.equals("p") ){ // reposer
            if ( !personnage.getInventaire().isEmpty() ){
                List<Case> inventaire = personnage.getInventaire();

                menu = new ArrayList<>(List.of(
                        "╔═══╦ == ~~*~~ == ~~:~~ == ~~*~~ == ╦═══╗",
                        "║ : ║    _____                      ║ : ║",
                        "║   ║   |  __ \\                     ║   ║",
                        "║ : ║   | |__) |__  ___  ___ _ __   ║ : ║",
                        "║   ║   |  ___/ _ \\/ __|/ _ \\ '__|  ║   ║",
                        "║ : ║   | |  | (_) \\__ \\  __/ |     ║ : ║",
                        "║   ║   |_|   \\___/|___/\\___|_|     ║   ║",
                        "║ : ║                               ║ : ║",
                        "╚═══╩ == ~~*~~ == ~~:~~ == ~~*~~ == ╩═══╝",
                        ""
                ));
                int milieuCadre = 20 - (inventaire.size()*5 +1)/2;

                String ligneDelimitation = " ".repeat( milieuCadre ) + "+";
                ligne = " ".repeat( milieuCadre ) + "|";
                int elementCpt = 1;

                for (int i = 0; i < inventaire.size(); i++) {
                    ligne += " " + inventaire.get(i) + elementCpt + " |";
                    ligneDelimitation += "----";
                    elementCpt++;

                    if ((i + 1) % 6 != 0 && i != inventaire.size() - 1) {
                        ligneDelimitation += "*";
                    }

                    // 4 elements max par ligne
                    if ((i + 1) % 6 == 0 || i == inventaire.size() - 1) {
                        menu.add(ligneDelimitation + "+");
                        menu.add(ligne);
                        menu.add(ligneDelimitation + "+");

                        ligne = " ".repeat( milieuCadre ) + "|";
                        ligneDelimitation = " ".repeat( milieuCadre ) + "+";
                    }
                }

                Ihm.afficherPartie(menu, carte.getListeLigne());

                while ( !bonChoix ){
                    System.out.print("Choississez le nombre d'un Animal ou d'un Aliment à poser \nOu annuler avec a\n >> ");
                    choix = sc.nextLine();

                    if ( !choix.equals("a") ){
                        try{
                            inventaire.get( Integer.parseInt(choix)-1); // déclencher le catch si il y a une erreur
                            // on demande la direction où lancer
                            boolean bonSecondChoix = false;
                            String secondChoix = "";
                            while ( !bonSecondChoix ){
                                System.out.print("Choississez une direction où lancer \nOu annuler avec a\n >> ");
                                secondChoix = sc.nextLine();
                                try{
                                    if (!secondChoix.equals("a") ){
                                        Case caze = carte.getCaseCible( secondChoix );
                                        if ( caze instanceof Vide ){
                                            Case objet = inventaire.get( Integer.parseInt(choix)-1 );
                                            if ( objet instanceof Animal){
                                                carte.addAnimal( (Animal) objet);
                                            }
                                            carte.setCaseGrille( caze.getX(), caze.getY(), objet);
                                            bonSecondChoix = true;
                                            nbActionAvantDeplacementAnimaux --;
                                        }else{
                                            System.out.print(ANSI_RED_BACKGROUND + "Merci de choisir une case Vide" + ANSI_RESET + "\n");
                                        }
                                    }else{
                                        bonSecondChoix = true;
                                    }
                                } catch (Exception e) {
                                    System.out.print(ANSI_RED_BACKGROUND + "Merci de choisir une direction parmis : z q s d zq zd sq sd" + ANSI_RESET + "\n");
                                }
                            }
                            // on enlève l'objet de l'inventaire du personnage
                            personnage.lancer( Integer.parseInt(choix)-1);
                            carte.majCaseVoisinesPersoEtAnimal();

                            bonChoix = true;
                        }catch ( Exception e ){
                            System.out.printf(ANSI_RED_BACKGROUND + "Merci de choisir un nombre compris entre 1 et %d" + ANSI_RESET + "\n", elementCpt-1);
                        }
                    }else{
                        bonChoix = true;
                    }
                }
            }else{
                System.out.printf(ANSI_PURPLE_BACKGROUND + "Vous ne pouvez pas faire cette action !" + ANSI_RESET + "\n");
                sc.nextLine();
            }
        }
        else if ( action.equals("i") ){ // ouvrir l'inventaire
            List<Case> inventaire = personnage.getInventaire();

            menu = new ArrayList<>(List.of(
                    "+ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ +",
                    "║    _____                      _        _              ║",
                    "║   |_   _|                    | |      (_)             ║",
                    "║     | |  _ ____   _____ _ __ | |_ __ _ _ _ __ ___     ║",
                    "║     | | | '_ \\ \\ / / _ \\ '_ \\| __/ _` | | '__/ _ \\    ║",
                    "║    _| |_| | | \\ V /  __/ | | | || (_| | | | |  __/    ║",
                    "║   |_____|_| |_|\\_/ \\___|_| |_|\\__\\__,_|_|_|  \\___|    ║",
                    "║                                                       ║",
                    "+ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ +",
                    ""
            ));

            int milieuCadre = 28 - ( (inventaire.size()>4 ? 4 : inventaire.size() ) *10 +1)/2;
            String ligneDelimitation = " ".repeat(milieuCadre) + "+";
            ligne = " ".repeat(milieuCadre) + "|";

            for (int i = 0; i < inventaire.size(); i++) {
                ligne += " " + inventaire.get(i).toStringNom() + " |";
                ligneDelimitation += "-".repeat(inventaire.get(i).getNom().length()) + "--";

                if ((i + 1) % 4 != 0 && i != inventaire.size() - 1) {
                    ligneDelimitation += "*";
                }

                // 4 elements max par ligne
                if ((i + 1) % 4 == 0 || i == inventaire.size() - 1) {
                    menu.add(ligneDelimitation + "+");
                    menu.add(ligne);
                    menu.add(ligneDelimitation + "+");

                    ligne = " ".repeat(milieuCadre) + "|";
                    ligneDelimitation = " ".repeat(milieuCadre) + "+";
                }
            }

            Ihm.afficherPartie(menu, carte.getListeLigne());

            System.out.print("Fermer l'inventaire en appuyant sur une touche\n >> ");
            sc.nextLine();

        } else if ( action.equals("j") ) { // sauvegarder
            menu = new ArrayList<>(List.of(
                    "╔═══════════════════════════════╤═══════════════════════════════╗",
                    "⚜     _____                                           _         ⚜",
                    "┋    / ____|                                         | |        ┋",
                    "┋   | (___   __ _ _   ___   _____  __ _  __ _ _ __ __| | ___    ┋",
                    "┋    \\___ \\ / _` | | | \\ \\ / / _ \\/ _` |/ _` | '__/ _` |/ _ \\   ┋",
                    "⚜    ____) | (_| | |_| |\\ V /  __/ (_| | (_| | | | (_| |  __/   ⚜",
                    "┋   |_____/ \\__,_|\\__,_| \\_/ \\___|\\__, |\\__,_|_|  \\__,_|\\___|   ┋",
                    "┋                                  __/ |                        ┋",
                    "┋                                 |___/                         ┋",
                    "⚜                                                               ⚜",
                    "╚═══════════════════════════════╧═══════════════════════════════╝",
                    ""
            ));
            File dossier = new File("./ChargementCarte/");
            int cptFichier = 1;
            File[] listeFichiers = new File[0];

            if (dossier.exists() && dossier.isDirectory()) {
                menu.addAll(List.of("        ╔══════════════════════ ⚜ ══════════════════════╗",
                        "        ┋               Cartes disponibles              ┋",
                        "        ╚══════════════════════ ⚜ ══════════════════════╝"));

                listeFichiers = dossier.listFiles((dir, name) -> name.endsWith(".txt"));
                if (listeFichiers != null && listeFichiers.length > 0) {
                    for (File fichier : listeFichiers) {
                        String typeZone = "";
                        try (Scanner scanner = new Scanner(new File("./ChargementCarte/"+fichier.getName() ))) {
                            // on cherche le type de Zone de la carte (premier caractère )
                            typeZone = String.valueOf( scanner.nextLine().charAt(0) );
                            if (scanner.hasNextLine()) {
                                switch (typeZone){
                                    case "F":
                                        typeZone = ANSI_GREEN_BACKGROUND + ANSI_BLACK + "Forêt" + ANSI_RESET + " "; break;
                                    case "J":
                                        typeZone = ANSI_BLACK_BACKGROUND + ANSI_GREEN + "Jungle" + ANSI_RESET; break;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                        }
                        menu.add( " ".repeat(18) + typeZone + " " + fichier.getName() + " " + affichageTouche( String.valueOf( cptFichier) ));
                        cptFichier ++;
                    }
                } else {
                        menu.add("        ┋             " + ANSI_RED_BACKGROUND + "Pas de carte disponible" + ANSI_RESET + "           ┋");
                }

                menu.add("        ╔══════════════════ ⚜ ═════ ⚜ ══════════════════╗");
            }

            menu.addAll(List.of(
                    "        ┋" + " ".repeat(14) + ANSI_BLUE_BACKGROUND + ANSI_BLACK + "Nouvelle Carte" + ANSI_RESET + " " + affichageTouche( String.valueOf( cptFichier) ) + " ".repeat(17) + "┋",
                    "        ╚══════════════════════ ⚜ ══════════════════════╝"));

            Ihm.afficherPartie(menu, carte.getListeLigne());

            while ( !bonChoix ) {
                System.out.print("Choississez le nombre d'un Fichier pour la sauvegarde \nOu annuler avec a\n >> ");
                choix = sc.nextLine();

                File fichier;

                if (!choix.equals("a")) {
                    try {
                        if (choix.equals(String.valueOf(cptFichier))) { // nouvelle carte
                            String dateAdj = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-HH-mm"));
                            fichier = new File("./ChargementCarte/Carte_" + dateAdj + ".txt");
                            fichier.createNewFile();
                        } else {
                            fichier = listeFichiers[Integer.parseInt(choix) - 1];
                        }

                        try (FileWriter writer = new FileWriter(fichier)) {
                            writer.write( carte.getTypeCarte() + "\n" + carte.getNbLigne() + "\n" + carte.getNbColonne() + "\n" + carte.getCaseCarte() +
                                    carte.getPersonnage().getLettreInventaire() );
                            System.out.println( ANSI_YELLOW_BACKGROUND + ANSI_BLACK + "Données enregistrées avec succès dans " + fichier.getName() + ANSI_RESET );

                            System.out.print("Revenir au menu en appuyant sur une touche\n >> ");
                            sc.nextLine();
                        } catch (Exception e) {
                            System.out.printf(ANSI_RED_BACKGROUND + "Erreur lors de la sauvegarde dans le fichier" + ANSI_RESET + "\n");
                        }
                        bonChoix = true;
                    } catch (Exception e) {
                        System.out.printf(ANSI_RED_BACKGROUND + "Merci de choisir un nombre compris entre 1 et %d" + ANSI_RESET + "\n", Integer.valueOf(cptFichier));
                    }
                } else {
                    bonChoix = true;
                }

            }
        }

        else if( !action.equals("") ){
            System.out.printf(ANSI_PURPLE_BACKGROUND + "Cette action n'existe pas !" + ANSI_RESET + "\n");
             sc.nextLine();
        }

        return List.of( nbActionAvantDeplacementAnimaux, affichage());

    }

    public List<String> affichage() {

        List<String> menu = new ArrayList<>(List.of(
                "╔═════✧✦✧═══════════════════✧✦✧═════╗",
                "║ ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ ║",
                "║ ░    __  __                     ░ ║",
                "║ ░   |  \\/  |                    ░ ║",
                "✧ ░   | \\  / | ___ _ __  _   _    ░ ✧",
                "✦ ░   | |\\/| |/ _ \\ '_ \\| | | |   ░ ✦",
                "✧ ░   | |  | |  __/ | | | |_| |   ░ ✧",
                "║ ░   |_|  |_|\\___|_| |_|\\__,_|   ░ ║",
                "║ ░                               ░ ║",
                "║ ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ ║",
                "╚═════✧✦✧═══════════════════✧✦✧═════╝",
                "╔═════✦✧✦═══════════════════✦✧✦═════╗",
                "✧   ⊙  Se déplacer " + affichageTouche("z q s d") + "          ✧",
                "║   ⊙  Ouvrir l'inventaire " + affichageTouche("i") + "        ║"
        ));

        if ( !personnage.getAnimauxProches().isEmpty() ){
            menu.add( "║   ⊙  Se Battre " + affichageTouche("f") + "                  ║");
        }
        if ( !personnage.getAnimauxAmiEtAlimentVoisins().isEmpty() ){
            menu.add( "║   ⊙  Rammasser quelque chose " + affichageTouche("g") + "    ║");
        }
        if ( !personnage.getInventaire().isEmpty() ){
            menu.add( "║   ⊙  Poser quelque chose " + affichageTouche("p") + "        ║");
        }
        menu.add("║   ⊙  Sauvegarder la partie " + affichageTouche("j") + "      ║");
        menu.add( "✧   ⊙  Quitter " + affichageTouche("n") + "                    ✧");
        menu.add("╚═════✦✧✦═══════════════════✦✧✦═════╝");

        return menu;
    }
}