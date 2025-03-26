package Modele;

import java.util.List;
import java.util.Random;

public class Rassasie extends Etat{

    public Rassasie(Animal animal) {
        super(animal);
    }
    public void deplacement(Carte carte) {

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
        }

        if ( animal.decrementerNbTourAvantAffame() <= 0 ){
            animal.setEtat( new Affame(animal) );
        }

        // on met l'animal sur la grille à ses nouvelles coordonnées
        carte.setCaseGrille( animal.getX(), animal.getY(), animal );
    }

}