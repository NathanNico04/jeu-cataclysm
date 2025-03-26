package Modele;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Affame extends Etat{

    public Affame(Animal animal) {
        super(animal);
    }
    public void deplacement(Carte carte) {
        List<Integer> coordonnees = animal.chercherAliment();

        // un aliment a été trouvé
        if ( coordonnees.get(0) != -1 ) {

            // on met une case vide à l'ancien emplacement de l'animal
            carte.setCaseGrille( animal.getX(), animal.getY(), carte.getZoneStrategy().generer(' ', animal.getX(), animal.getY()));

            animal.setX( coordonnees.get(0) );
            animal.setY( coordonnees.get(1) );
            // on met l'animal sur la grille à ses nouvelles coordonnées
            carte.setCaseGrille( animal.getX(), animal.getY(), animal );
            carte.majCaseVoisinesAnimaux(); // maj des cases voisines pour voir si le personnage se trouve autour de l'aliment ingéré
            animal.seNourir();

        } // déplacement aléatoire
        else{
            Random random = new Random();
            int randomX = (random.nextInt(3) - 1);
            int randomY = (random.nextInt(3) - 1);
            int i=0;

            while ( !Carte.caseValidePourDeplacement(animal.getX() + randomX, animal.getY() + randomY) ){
                randomX = (random.nextInt(3) - 1);
                randomY = (random.nextInt(3) - 1);
                i++;
                if ( i == 3 ) // si au bout de 3 essai on n'a pas trouvé de case disponible, l'animal ne bougera pas
                    break;
            }
            if ( i < 3 ){
                // on met une case vide à l'ancien emplacement de l'animal.
                carte.setCaseGrille( animal.getX(), animal.getY(), carte.getZoneStrategy().generer(' ', animal.getX(), animal.getY()));

                animal.setX( animal.getX() + randomX );
                animal.setY( animal.getY() + randomY );
                // on met l'animal sur la grille à ses nouvelles coordonnées
                carte.setCaseGrille( animal.getX(), animal.getY(), animal );
            }
        }

    }

}