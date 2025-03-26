package Vue;

import Controleur.Controleur;
import Modele.Carte;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import static Modele.Carte.*;
import static Modele.Carte.ANSI_RESET;

public class Ihm {

    private Controleur controleur;

    public Ihm(Controleur controleur) {
        this.controleur = controleur;
    }

    public static void afficherPartie(List<String> listeMenu, List<String> listeCarte) {

        String jeu = "";
        int quart = listeCarte.size()/4;

        for (int i=0; i<listeCarte.size(); i++){

            // on affiche le menu à mi-hauteur de la carte
            if ( i >= quart   &&   i < quart + listeMenu.size() ){
                jeu += listeCarte.get(i)  +  " ".repeat(15)  +  listeMenu.get( i-quart )  + "\n";
            }else{
                jeu += listeCarte.get(i) + "\n";
            }
        }

        System.out.println(jeu);
    }

    public void afficherJeu(Carte carte) {
        System.out.println(carte);
    }

    public String affichageTouche(String touche){
        return ANSI_BLACK_BACKGROUND + ANSI_PURPLE + touche + ANSI_RESET;
    }


    public void MenuDemmarrage(Carte carte) {
        Scanner sc = new Scanner(System.in);
        String n;
        boolean ok = false;
        String menu =
                "\n\n\n\n     ╔═══════════════════════════╤══════════════════════════╗\n" +
                "     ┋     _____      _             _                       ┋\n" +
                "     ┋    / ____|    | |           | |                      ┋\n" +
                "     ┋   | |     __ _| |_ __ _  ___| |_   _ ___ _ __ ___    ┋\n" +
                "     ┋   | |    / _` | __/ _` |/ __| | | | / __| '_ ` _ \\   ┋\n" +
                "     ┋   | |___| (_| | || (_| | (__| | |_| \\__ \\ | | | | |  ┋\n" +
                "     ┋    \\_____\\__,_|\\__\\__,_|\\___|_|\\__, |___/_| |_| |_|  ┋\n" +
                "     ┋                                 __/ |                ┋\n" +
                "     ┋                                |___/                 ┋\n" +
                "     ┋                                                      ┋\n" +
                "     ╚═══════════════════════════╧══════════════════════════╝\n" +
                "                ╔═══════════════ ✧ ═══════════════╗\n" +
                "                ✦     ⊙  Créer une carte " + affichageTouche("c") + "        ✦\n" +
                "                ✦     ⊙  Charger une carte " + affichageTouche("l") + "      ✦\n" +
                "                ✦     ⊙  Quitter " + affichageTouche("q") + "                ✦\n" +
                "                ╚═══════════════ ✧ ═══════════════╝\n";
        while (!ok) {

            System.out.println(menu);
            n = sc.nextLine();
            if (n.equals("c")){ // creer une carte

                String typeCarte;
                String nbLigne;
                String nbColonne;
                boolean bonchoix = false;

                while (!bonchoix) {
                    System.out.print("\n\n\n\n         ╦ == ~~*~~ == ~~:~~ == ~~*~~ == ╦\n" +
                            "         ║    _____                      ║\n" +
                            "         ║   / ____|                     ║\n" +
                            "         :  | |     _ __ ___  ___ _ __   :\n" +
                            "         ║  | |    | '__/ _ \\/ _ \\ '__|  ║\n" +
                            "         ║  | |____| | |  __/  __/ |     ║\n" +
                            "         :   \\_____|_|  \\___|\\___|_|     :\n" +
                            "         ║                               ║\n" +
                            "         ║                               ║\n" +
                            "         ╩ == ~~*~~ == ~~:~~ == ~~*~~ == ╩\n\n");

                    System.out.print("        - Vous pouvez annuler à nimporte quel moment, en appuyant sur a\n");
                    System.out.print("         > Type de Carte: ");
                    typeCarte = sc.nextLine();
                    if ( List.of("F", "J", "f", "j").contains(typeCarte) ){
                        System.out.print("         > Nb de Ligne: ");
                        nbLigne = sc.nextLine();
                        if ( nbLigne.equals("a") ){
                            bonchoix = true;
                        }else {
                            try{
                                if ( Integer.parseInt(nbLigne) > 0 ){
                                    System.out.print("         > Nb de Colonne: ");
                                    nbColonne = sc.nextLine();
                                    if ( nbColonne.equals("a") ){
                                        bonchoix = true;
                                    }else {
                                        try{
                                            if ( Integer.parseInt(nbColonne) > 0 ) {
                                                controleur.CreerCarte(typeCarte.toUpperCase(), Integer.parseInt(nbLigne), Integer.parseInt(nbColonne));
                                                bonchoix = true;
                                                ok = true;
                                                ChoixModificationCarte();
                                            }else {
                                            System.out.println(ANSI_RED_BACKGROUND + "Choississez un nombre de colonne plus grand que 0" + ANSI_RESET);
                                        }
                                        } catch (Exception e) {
                                            System.out.println(ANSI_RED_BACKGROUND + "Choississez un nombre svp col" + ANSI_RESET);
                                        }
                                    }
                                }else {
                                    System.out.println(ANSI_RED_BACKGROUND + "Choississez un nombre de ligne plus grand que 0" + ANSI_RESET);
                                }
                            } catch (Exception e) {
                                System.out.println(ANSI_RED_BACKGROUND + "Choississez un nombre svp lig" + ANSI_RESET);
                            }
                        }

                    }else if ( typeCarte.equals("a") ){
                        bonchoix = true;
                    }else{
                        System.out.println(ANSI_RED_BACKGROUND + "Choississez un type de Carte parmis J j et F f" + ANSI_RESET);
                    }
                }
            }
            else if (n.equals("l")){ // charger une carte
                System.out.println(
                        "\n\n\n\n         + ══ ══ ══ ══ ══ ══ ══ = ══ ══ ══ ══ ══ ══ ══ +\n" +
                        "         ║     _____ _                                 ║\n" +
                        "         ║    / ____| |                                ║\n" +
                        "         ║   | |    | |__   __ _ _ __ __ _  ___ _ __   ║\n" +
                        "         ║   | |    | '_ \\ / _` | '__/ _` |/ _ \\ '__|  ║\n" +
                        "         ║   | |____| | | | (_| | | | (_| |  __/ |     ║\n" +
                        "         ║    \\_____|_| |_|\\__,_|_|  \\__, |\\___|_|     ║\n" +
                        "         ║                            __/ |            ║\n" +
                        "         ║                           |___/             ║\n" +
                        "         ║                                             ║\n" +
                        "         + ══ ══ ══ ══ ══ ══ ══ = ══ ══ ══ ══ ══ ══ ══ +\n"
                );
                File dossier = new File("./ChargementCarte/");
                File[] listeFichiers = dossier.listFiles((dir, name) -> name.endsWith(".txt"));
                int cptFichier = 1;
                if (listeFichiers != null && listeFichiers.length > 0) {
                    System.out.print("        ╔══════════════════════ ✧ ══════════════════════╗\n" +
                            "        ┋               Cartes disponibles              ┋\n" +
                            "        ╚══════════════════════ ✧ ══════════════════════╝\n");
                    for ( File fichier : listeFichiers ){
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


                        System.out.println( " ".repeat(15) + typeZone + " " + fichier.getName() + " " + affichageTouche( String.valueOf(cptFichier) ));
                        cptFichier ++;
                    }
                    System.out.print("        ╚══════════════════════ ✧ ══════════════════════╝\n");
                }
                Scanner Stringsc = new Scanner(System.in);
                String choix;
                boolean bonChoix = false;
                while (!bonChoix) {
                    System.out.println("Choississez un fichier à charger \n a pour Annuler");
                    choix = Stringsc.nextLine();
                    if ( !choix.equals("a")){

                            File fichier = listeFichiers[ Integer.parseInt(choix)-1 ];
                            controleur.chargerCarte( "./ChargementCarte/" + fichier.getName() );
                            bonChoix = true;
                            ok=true;
                            ChoixModificationCarte();

                    }else{
                        bonChoix = true;
                    }
                }
            }
            else if ( n.equals("q")){
                ok=true;
            }
            else
                System.out.printf(ANSI_PURPLE_BACKGROUND + "Cette action n'existe pas !" + ANSI_RESET + "\n");
        }

    }

    public void ChoixModificationCarte() {

        Scanner sc = new Scanner(System.in);
        String n;
        boolean poursuivre = false;
        String menu = "         ╔~~~~~~~~~~{~ ❀ ~}~~~~~~~~{~ ❀ ~}~~~~~~~~~~╗\n" +
                "         ✿    __  __           _ _  __ _            ✿\n" +
                "         ✿   |  \\/  |         | (_)/ _(_)           ✿\n" +
                "         ✿   | \\  / | ___   __| |_| |_ _  ___ _ __  ✿\n" +
                "         ✿   | |\\/| |/ _ \\ / _` | |  _| |/ _ \\ '__| ✿\n" +
                "         ✿   | |  | | (_) | (_| | | | | |  __/ |    ✿\n" +
                "         ✿   |_|  |_|\\___/ \\__,_|_|_| |_|\\___|_|    ✿\n" +
                "         ✿                                          ✿\n" +
                "         ╚~~~~~~~~~~{~ ❀ ~}~~~~~~~~{~ ❀ ~}~~~~~~~~~~╝\n" +
                "               ╔ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ╗\n" +
                "               ❀     ⊙  Modifier la carte " + affichageTouche("m") + "    ❀\n" +
                "               ✿     ⊙  Poursuivre " + affichageTouche("p") + "           ✿\n" +
                "               ❀     ⊙  Annuler " + affichageTouche("a") + "              ❀\n" +
                "               ╚ ══ ══ ══ ══ ══ ══ ══ ══ ══ ══ ╝\n\n";
        while (!poursuivre) { // tant qu'il veut modifier la carte
            System.out.print(menu);

            n = sc.nextLine();

            if (n.equals("m")) {
                Carte c = controleur.ModifierCarte(); // en fonction des elements de la carte, les permute
                afficherJeu( c );
            }
            else if (n.equals("p")) {
                poursuivre = true;
                controleur.jouer();
            }
            else if (n.equals("a")) {
                poursuivre = true;
                controleur.demarrage();
            }
            else
                System.out.printf(ANSI_PURPLE_BACKGROUND + "Veuillez choisir une option correcte !" + ANSI_RESET + "\n");
        }

    }
}

