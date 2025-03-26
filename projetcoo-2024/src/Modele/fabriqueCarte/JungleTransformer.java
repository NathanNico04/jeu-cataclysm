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

public class JungleTransformer implements TransformCarte{
    @Override
    /*public void Transform(String source, String target){
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {

            String ligne;
            int compte = 0;

            while ((ligne = reader.readLine()) != null){
                if (compte == 0) { // Modifier la première ligne
                    writer.write('J'); // Nouveau type pour la Jungle
                }
                else if (compte == 1) {
                    writer.write(toString().valueOf(35));
                }
                else if (compte == 2) {
                    writer.write(100);

                }else{
                    String nouvelleLigne = ligne.replace('A', 'K').replace('G', 'B').replace('E','S').replace('B','R');
                    writer.write(nouvelleLigne);
                }
                writer.newLine();
                compte++;
            }


        }
        catch(IOException e){
            throw new RuntimeException(e);
            //ihm.ErreurModificationCarte(e);

        }
    }*/


    public void Transform(String source, String target) {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {

            String ligne;
            int compte = 0;

            while ((ligne = reader.readLine()) != null) {
                if (compte == 0) { // Modifier la première ligne (type de carte)
                    writer.write('J'); // Nouveau type pour la Jungle
                } else if (compte == 1) { // Modifier le nombre de lignes
                    writer.write(String.valueOf(35)); // Exemple : 35 lignes
                } else if (compte == 2) { // Modifier le nombre de colonnes
                    writer.write(String.valueOf(100)); // Exemple : 100 colonnes
                } else { // Modifier les autres lignes (contenu de la carte)
                    String nouvelleLigne = ligne.replace('A', 'K')
                            .replace('G', 'B')
                            .replace('E', 'S')
                            .replace('B', 'R');
                    writer.write(nouvelleLigne);
                }
                writer.newLine(); // Ajouter une nouvelle ligne
                compte++;
            }


        } catch (IOException e) {
            System.err.println("Erreur lors de la transformation de la carte : " + e.getMessage());
            // ih.ErreurModificationCarte(e); // Utiliser l'IHM pour afficher une erreur si besoin
        }
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
        int nbGermeA = 3;
        int nbGermeB = 8;
        int nbGermeC = 2;
        int r;
        int rd;

        // Les germes
        // A
        for (int a=0; a< nbGermeA; a++) {
            r = rand.nextInt(nbColonne - 10 - 10) + 10;
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
            r = rand.nextInt(nbColonne - 10 - 10) + 10;
            rd = rand.nextInt(nbLigne - 5 - 5) + 5;
            while ( checkVoisin(grille, rd, r) != ' '){ // pas de germe autour
                r = rand.nextInt(nbColonne - 10 - 10) + 10;
                rd = rand.nextInt(nbLigne - 5 - 5) + 5;
            }

            grille[rd][r] = 'b';
        }

        // C
        for (int c=0; c< nbGermeC; c++) {
            r = rand.nextInt(nbColonne - 20 - 20) + 20;
            rd = rand.nextInt(nbLigne - 10 - 10) + 10;
            while ( checkVoisin(grille, rd, r) != ' '){ // pas de germe autour
                r = rand.nextInt(nbColonne - 20 - 20) + 20;
                rd = rand.nextInt(nbLigne - 10 - 10) + 10;
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
                voisin = checkVoisin( grille, i, j );
                // la case actuel n'est pas une germe et est à côté d'une germe
                if ( !List.of('a', 'b', 'c').contains( grille[i][j] )  &&  voisin != ' '){
                    if ( voisin.equals('a')) {
                        grille[i][j] = 'K';
                    }else if ( voisin.equals('b')) {
                        if ( randB == 1){
                            randC = rand.nextInt(7);
                            grille[i][j] = (randC < 5) ? 'R' : 'C';
                        }else{
                            grille[i][j] = 'S';
                        }
                        randB = rand.nextInt(2);
                    }else{ // c
                        randC = rand.nextInt(10);
                        grille[i][j] = (randC < 9) ? 'C' : 'B';
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
        while ( checkVoisin(grille, rd, r) != ' '){ // pas de germe autour
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

    public char checkVoisin(char[][] grille, int i, int j) {
        List<int[]> directions = generateRandomDirections();

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

    public static List<int[]> generateRandomDirections() {
        Random rand = new Random();

        int size = rand.nextInt(8) + 30;

        List<int[]> directions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int x = rand.nextInt(13) - 6;
            int y = rand.nextInt(13) - 6;

            if (x != 0 || y != 0) {
                directions.add(new int[]{x, y});
            }
        }

        return directions;
    }
}