package Modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Carte {
    private static List<List<Case>> grille = new ArrayList<>();
    private List<Animal> listeAnimaux = new ArrayList<>();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m" ;
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private int nbLigne;
    private int nbColonne;
    private char typeCarte;
    private Personnage personnage;

    private ZoneStrategy strategy;



    public Carte(String fichierCarte) {
        grille = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fichierCarte))){
            String ligneCarte;
            int y = 0;

            this.typeCarte = reader.readLine().charAt(0); // 'F' : Foret, 'J' : Jungle
            this.nbLigne = Integer.parseInt( reader.readLine() );  // 35 dans la carte exemple
            this.nbColonne = Integer.parseInt( reader.readLine() ); // 100 dans la carte exemple

            while ((ligneCarte = reader.readLine()) != null  &&  y < nbLigne ){
                List<Case> ligne = new ArrayList<>();
                for (int x = 0; x < nbColonne; x++){
                    try {
                        char c = ligneCarte.charAt(x);  // Pour en extraire chaque caractère à l'indice x
                        Case caze = null;
                        switch (typeCarte){  // pour l'application des couleurs et tout
                            case 'F':
                                strategy = new ZoneForet();
                                caze= strategy.generer(c,x,y);
                                break;
                            case 'J':
                                strategy = new ZoneJungle();
                                caze = strategy.generer(c,x,y);
                                break;
                            default:
                                break; // plutôt une methode de l'ihm
                        }

                        ligne.add(caze);

                        if ( caze instanceof Animal ){
                            listeAnimaux.add( (Animal) caze);
                        }else if ( caze instanceof Personnage ){
                            personnage = (Personnage) caze;
                        }
                    } catch (Exception e) {
                        // ajouter une case vide à la fin de la ligne s'il n'y a plus de caractère à afficher
                        // mais que nbColonne n'a pas été atteint
                        ligne.add(strategy.generer(' ', x, y));
                    }

                }
                grille.add(ligne); // j'ajoute la ligne à la grille

                y++;  // changer de ligne
            }

            // on ajoute l'inventaire au personnage si la carte en contient un
            if ( ligneCarte != null ){
                for ( int i=0; i<ligneCarte.length(); i++ ){
                    Case caze = strategy.generer(ligneCarte.charAt(i),0, 0);
                    if ( caze instanceof Animal ){
                        (( Animal) caze).setAmi(true);
                    }
                    personnage.addToInventaire( caze );
                }
            }

            majCaseVoisinesAnimaux();
            majCaseVoisinesPersonnage();


        }
        catch (IOException e) {
            System.out.println("Erreur de chargement de carte : " + e.getMessage());
        }

    }

    public void majCaseVoisinesAnimaux(){
        for (Animal animal : listeAnimaux) {
            int animalX = animal.getX();
            int animalY = animal.getY();
            animal.caseVoisines = new ArrayList<>();

            for (int dy = -1; dy <= 1; dy++) {
                List<Case> ligneVoisine = new ArrayList<>();
                for (int dx = -1; dx <= 1; dx++) {
                    int voisinX = animalX + dx;
                    int voisinY = animalY + dy;

                    if (dx == 0 && dy == 0) { // Case de l'animal
                        switch (typeCarte){
                            case 'F':  ligneVoisine.add( strategy.generer('E', dx, dy) ); break;
                            case 'J':  ligneVoisine.add( strategy.generer('S', dx, dy) ); break;
                        }
                    } // on regarde si la case visée se trouve dans la grille
                    else if (voisinX >= 0 && voisinX < this.nbColonne && voisinY >= 0 && voisinY < this.nbLigne) {
                        ligneVoisine.add(grille.get(voisinY).get(voisinX));
                    } else { // la case est hors de la grille
                        ligneVoisine.add(strategy.generer(' ', voisinX, voisinY));
                    }
                }
                animal.caseVoisines.add(ligneVoisine);
            }
        }
    }

    public void majCaseVoisinesPersonnage() {
        personnage.setCaseVoisines( new ArrayList<>() );

        for(int vY = -1; vY <= 1; vY++){
            List<Case> ligneVoisine = new ArrayList<>();
            for(int vX = -1; vX <= 1; vX++){
                int voisinX = personnage.getX() + vX;
                int voisinY = personnage.getY() + vY;

                if (voisinX >= 0 && voisinX < this.nbColonne && voisinY >= 0
                        && voisinY < this.nbLigne) {
                    ligneVoisine.add( grille.get(voisinY).get(voisinX) );
                }else{
                    ligneVoisine.add(strategy.generer(' ', voisinX, voisinY));
                }
            }
            personnage.addLigneToCaseVoisines(ligneVoisine);
        }
    }

    public void majCaseVoisinesPersoEtAnimal(){
        majCaseVoisinesAnimaux();
        majCaseVoisinesPersonnage();
    }

    public Carte modifMap() {
        Random r = new Random();

        // Aplatir la grille en une liste temporaire pour faciliter la permutation
        List<Case> allCases = new ArrayList<>();
        for (List<Case> ligne : grille) {
            allCases.addAll(ligne);
        }

        // Mélanger les éléments
        Collections.shuffle(allCases, r);

        // Répartir les éléments mélangés dans la grille
        int index = 0;
        for (int y = 0; y < nbLigne; y++) {
            for (int x = 0; x < nbColonne; x++) {
                setCaseGrille(x, y, allCases.get(index));
                index++;
            }
        }

        // Mettre à jour les voisins des animaux et personnage
        majCaseVoisinesPersoEtAnimal();
        return this;
    }

    public ZoneStrategy getZoneStrategy() {
        return this.strategy;
    }

    public Case getCaseCible(String direction) {
        int PosX = personnage.getX();
        int PosY = personnage.getY();

        switch (direction) {
            case "z":
                PosY--;
                break;
            case "s":
                PosY++;
                break;
            case "q":
                PosX--;
                break;
            case "d":
                PosX++;
                break;
            case "sq":
                PosX--;
                PosY++;
                break;
            case "zd":
                PosX++;
                PosY--;
                break;
            case "sd":
                PosX++;
                PosY++;
                break;
            case "zq":
                PosY--;
                PosX--;
                break;
            default:
                return null;
        }
        if (PosX < 0 || PosY < 0 ||
                PosX >= nbColonne ||
                PosY >= nbLigne) { //Controle pour la sortie de map
            return null;
        }
        Case caseCible = grille.get(PosY).get(PosX);

        if(caseCible instanceof Vegetation){ //Controle si obstacle présent
            return null;
        }
        if(caseCible instanceof Animal){
            return null;
        }

        return caseCible;
    }

    public Personnage getPersonnage(){
        return this.personnage;
    }
    public int getNbLigne(){
        return this.nbLigne;
    }
    public int getNbColonne(){
        return this.nbColonne;
    }
    public char getTypeCarte(){
        return this.typeCarte;
    }

    // met à jour une case de la grille
    public void setCaseGrille(int posX, int posY, Case newCase){
        newCase.setX(posX);
        newCase.setY(posY);
        grille.get(posY).set(posX, newCase);
    }

    public void deplacerLesAnimaux(){
        for (Animal animal : listeAnimaux){
            animal.deplacement(this);
        }
        majCaseVoisinesPersoEtAnimal();
    }

    public void removeAnimal(Animal animal){
        this.listeAnimaux.remove(animal);
    }
    public void addAnimal(Animal animal){
        this.listeAnimaux.add(animal);
    }

    public static boolean caseValidePourDeplacement(int x, int y){

        try{
            if ( grille.get(y).get(x) instanceof Aliment  ||  grille.get(y).get(x) instanceof Vide ){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List<List<Case>> getGrille() {
        return grille;
    }

    public List<Animal> getListeAnimaux(){
        return this.listeAnimaux;
    }

    public String getCaseCarte(){
        // retourne un String contenant la grille sous forme de lettre des cases
        String carte = "";
        for (List<Case> ligne : grille) {
            for (Case caze : ligne) {
                carte += caze.getLettre();
            }
            carte += "\n";
        }
        return carte;
    }

    public List<String> getListeLigne() {
        // retourne une liste dont les éléments sont les lignes de la carte

        List<String> carte = new ArrayList<>();
        String ligneListe;

        for (List<Case> ligne : grille) {
            ligneListe = "";
            for (Case caze : ligne) {
                ligneListe += caze.toString();
            }
            carte.add( ligneListe );
        }
        return carte;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<Case> ligne : grille) {
            for (Case caze : ligne) {
                sb.append(caze.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

