package Modele.fabriqueCarte;

import Controleur.Controleur;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static Modele.Carte.ANSI_RED_BACKGROUND;
import static Modele.Carte.ANSI_RESET;

public class ForetTransformer implements TransformCarte{

    public void Transform(String source, String target) {
        // rien
    }


    public String GrilleGenerator(String type, int nbLigne, int nbColonne) {

        char[][] grille = new char[nbLigne][nbColonne];
        Random rand = new Random();

        // Initialisation de la carte (remplir avec des cases vides)
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                grille[i][j] = ' ';
            }
        }

        int nbCaseTotal = nbLigne * nbColonne;
        int nbGermeA = nbCaseTotal/1166;
        int nbGermeB = nbCaseTotal/437;
        int nbGermeC = nbCaseTotal/1750;
        int r;
        int rd;

        // Les germes
        // A
        for (int a=0; a< nbGermeA; a++) {
            r = rand.nextInt(nbColonne);
            if ( r%4 == 0 ){ // haut
                grille[a][r] = 'a';
            }else if ( r%4 == 1 ){ //droite
                grille[r%25][nbColonne-1] = 'a';
            }else if ( r%4 == 2 ){ //bas
                grille[nbLigne-1][r] = 'a';
            }else{ // gauche
                grille[r%25][0] = 'a';
            }
        }

        // B
        for (int b=0; b< nbGermeB; b++) {
            r = rand.nextInt(nbColonne);
            rd = rand.nextInt(nbLigne);
            while ( checkVoisin(grille, rd, r, nbLigne, nbColonne) != ' '){ // pas de germe autour
                r = rand.nextInt(nbColonne);
                rd = rand.nextInt(nbLigne);
            }

            grille[rd][r] = 'b';
        }

        // C
        for (int c=0; c< nbGermeC; c++) {
            r = rand.nextInt(nbColonne);
            rd = rand.nextInt(nbLigne);
            while ( checkVoisin(grille, rd, r, nbLigne, nbColonne) != ' '){ // pas de germe autour
                r = rand.nextInt(nbColonne);
                rd = rand.nextInt(nbLigne);
            }

            grille[rd][r] = 'c';
        }


        // Ajout des lettres autour des germes
        // b => B soit A,C soit E
        // a => A
        // c => C et G
        int randB = rand.nextInt(2);
        int randC;

        Character voisin;
        for (int i=0; i<nbLigne; i++) {
            for (int j=0; j<nbColonne; j++) {
                voisin = checkVoisin( grille, i, j , nbLigne, nbColonne);
                // la case actuel n'est pas une germe et est à côté d'une germe
                if ( !List.of('a', 'b', 'c').contains( grille[i][j] )  &&  voisin != ' '){
                    if ( voisin.equals('a')) {
                        grille[i][j] = 'A';
                    }else if ( voisin.equals('b')) {
                        if ( randB == 1){
                            randC = rand.nextInt(7);
                            grille[i][j] = (randC < 5) ? 'B' : 'C';
                        }else{
                            grille[i][j] = 'E';
                        }
                        randB = rand.nextInt(2);
                    }else{ // c
                        randC = rand.nextInt(10);
                        grille[i][j] = (randC < 9) ? 'C' : 'G';
                    }

                }
            }
        }

        // on enlève les germes
        for (int i=0; i<nbLigne; i++) {
            for (int j=0; j<nbColonne; j++) {
                if ( List.of('a', 'b', 'c').contains( grille[i][j] ) ){
                    grille[i][j] = ' ';
                }
            }
        }

        // personnage
        r = rand.nextInt(nbColonne);
        rd = rand.nextInt(nbLigne);
        while ( checkVoisin(grille, rd, r, nbLigne, nbColonne) != ' '){ // pas de germe autour
            r = rand.nextInt(nbColonne);
            rd = rand.nextInt(nbLigne);
        }

        grille[rd][r] = '@';

        // créer le fichier
        String dateAdj = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-HH-mm"));
        File fichier = new File("./ChargementCarte/Carte_" + dateAdj + ".txt");
        try (FileWriter writer = new FileWriter(fichier)) {

            fichier.createNewFile();
            String plateau = type + "\n" + nbLigne + "\n" + nbColonne + "\n";
            for (int i = 0; i < nbLigne; i++) {
                for (int j = 0; j < nbColonne; j++) {
                    plateau += grille[i][j];
                }
                plateau += "\n";
            }
            writer.write(  plateau );
        } catch (Exception e) {
            System.out.printf(ANSI_RED_BACKGROUND + "Erreur lors de la création du fichier" + ANSI_RESET);
        }

        return fichier.getName();

    }

    public char checkVoisin(char[][] grille, int i, int j, int nbLigne, int nbColonne) {
        List<int[]> directions = generateRandomDirections(nbLigne, nbColonne);

        for (int[] direction : directions) {
            int newX = i + direction[0];
            int newY = j + direction[1];

            if (newX >= 0 && newX < grille.length && newY >= 0 && newY < grille[0].length) {
                char voisin = grille[newX][newY];
                if (voisin == 'a'){
                    return 'a';
                }else if (voisin == 'b'){
                    return 'b';
                }else if (voisin == 'c'){
                    return 'c';
                }
            }
        }

        return ' ';
    }

    public static List<int[]> generateRandomDirections(int nbLigne, int nbColonne) {
        Random rand = new Random();

        int size = rand.nextInt(((int)(nbLigne*nbColonne)/437)) + ((int)(nbLigne*nbColonne)/94);

        List<int[]> directions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int x = rand.nextInt(((int) nbLigne/5)*2 +1) - ((int) nbLigne/5);
            int y = rand.nextInt(((int) nbColonne/16)*2 +1) - ((int) nbColonne/16);

            if (x != 0 || y != 0) {
                directions.add(new int[]{x, y});
            }
        }

        return directions;
    }
}