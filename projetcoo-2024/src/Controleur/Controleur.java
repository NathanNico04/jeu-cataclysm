package Controleur;

import Modele.*;
import Vue.Ihm;
import Vue.Menu;
import Modele.fabriqueCarte.CarteFactory;
import Modele.fabriqueCarte.JungleFactory;
import Modele.fabriqueCarte.ForetFactory;

import java.util.List;
import java.util.Scanner;
import static Modele.Carte.*;


public class Controleur {
    private Carte carte;
    private Personnage personnage;
    private Ihm ihm;
    private CarteFactory factory;

    // Constructeur, charge la carte et initialise la vue
    public Controleur() {
        // this.carte = new Carte(fichierCarte);
        this.ihm = new Ihm(this);
    }

    public String menuAction(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Choississez une action: ");
        String action = sc.nextLine();
        return action;
    }

    public void demarrage(){
        ihm.MenuDemmarrage(carte);
    }

    public void jouer() {
        Menu menu = new Menu( carte );
        menu.affichageAction("");
        Ihm.afficherPartie( menu.affichage(), carte.getListeLigne()); // init
        int nbActionAvantDeplacementAnimaux = 3;
        List<Object> affichageMenu;

        String action;
        boolean continuer = true;

        while (continuer) {
            action = menuAction();
            if ( action.equals("n") ){
                continuer = false;
            }else {
                affichageMenu = menu.affichageAction(action);
                nbActionAvantDeplacementAnimaux += (int) affichageMenu.get(0);
                Ihm.afficherPartie( (List<String>) affichageMenu.get(1), carte.getListeLigne());
                if ( nbActionAvantDeplacementAnimaux <= 0 ){
                    nbActionAvantDeplacementAnimaux = 3;
                    carte.deplacerLesAnimaux();
                    try{
                        System.out.printf(ANSI_GREEN_BACKGROUND + ANSI_BLACK + "Déplacement des animaux" + ANSI_RESET + "\n");
                        Thread.sleep(350);
                        Ihm.afficherPartie( menu.affichage(), carte.getListeLigne());
                    } catch (InterruptedException e) {
                        System.out.println("erreur de sleep");
                    }
                }
            }
        }
        demarrage();
    }


    public void CreerCarte( String typeCarte, int nbLigne, int nbColonne) {  // Pattern fabrique(creation des fichiers) et Strategy pour l'algorithme utilisé
        if (typeCarte.equals("J") || typeCarte.equals("j")) {
            factory = new JungleFactory();
        }else if (typeCarte.equals("F") || typeCarte.equals("f")){
            factory = new ForetFactory();
        }

        // on pourrait envisager un bloc de switch ici pour qu'en fonction du choix
        // on crée des zones. Ici, seul le thème jungle est pris en compte. La methode dans l'ihm renverrai le chiffre
        // approprié et on testera pour créer le fichier avec la bonne factory.

        String NouveauFichier = "./ChargementCarte/" + factory.generateCarte(typeCarte,nbLigne, nbColonne);

        this.carte = new Carte(NouveauFichier); // modifie le fichier d'entrée en thème jungle puis passe la creation de la carte au modele
        ihm.afficherJeu(carte); // faire les modifs et faire un appel au constructeur de Controleur avec le bon chemin de carte ?
    }

    public Carte ModifierCarte() {
        return carte.modifMap();
    }

    public void chargerCarte(String fichierCarte) {
        this.carte = new Carte(fichierCarte);
        ihm.afficherJeu(carte);
    }

}
