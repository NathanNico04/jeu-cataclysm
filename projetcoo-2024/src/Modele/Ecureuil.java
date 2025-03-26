package Modele;

import java.util.ArrayList;
import java.util.List;

public class Ecureuil extends Animal{

    public Ecureuil(String couleurFond, String couleurPolice, String nom, char lettre, int x, int y, boolean ami, int nbTourAvantAffame, List<List<Case>> caseVoisines) {
        super(couleurFond, couleurPolice, nom, lettre, x, y, ami, nbTourAvantAffame, caseVoisines);
    }

    public List<Integer> chercherAliment() {

        Case gland = null;

        // Chercher un champignon
        for (List<Case> ligneVoisine : this.caseVoisines) {
            for (Case caze : ligneVoisine) {
                if (caze.getLettre() == 'C') {
                    return List.of(caze.getX(), caze.getY());
                }else if (gland == null && caze.getLettre() == 'G') {
                    gland = caze;
                }
            }
        }
        // Chercher un gland
        if (gland != null) {
            return List.of(gland.getX(), gland.getY());
        }

        // ni champignon ni gland n'a été trouvé
        return List.of(-1, -1);

    }
    public void seNourir(){
        this.nbTourAvantAffameActuel = this.nbTourAvantAffameMax;

        //si le personnage est à côté, l'animal devient son ami
        for (List<Case> ligneVoisine : this.caseVoisines) {
            for (Case caze : ligneVoisine) {
                if (caze instanceof Personnage) {
                    ami = true;
                    break;
                }
            }
        }

        setEtat( new Rassasie(this) );
    }
}