package Modele;

import java.util.List;

public class Singe extends Animal{
    private int nbNourrituresProchesConsecutives = 0;
    private boolean personnageEstProche;

    public Singe(String couleurFond, String couleurPolice, String nom, char lettre, int x, int y, boolean ami, int nbTourAvantAffameMax, List<List<Case>> caseVoisines) {
        super(couleurFond, couleurPolice, nom, lettre, x, y, ami, nbTourAvantAffameMax, caseVoisines);
    }

    @Override
    public void seNourir() {
        this.nbTourAvantAffameActuel = this.nbTourAvantAffameMax;

        personnageEstProche = false;
        for (List<Case> ligneVoisine : this.caseVoisines) {
            for (Case caze : ligneVoisine) {
                if (caze instanceof Personnage) {
                    personnageEstProche = true;
                    break;
                }
            }
        }

        if (personnageEstProche) {
            nbNourrituresProchesConsecutives++;
            if (nbNourrituresProchesConsecutives >= 2) {
                ami = true; // Devient ami après deux nourritures consécutives
            }
        } else {
            nbNourrituresProchesConsecutives = 0; // Réinitialisation si le personnage n'est pas proche
        }

        setEtat( new Rassasie(this));
    }

    @Override
    public List<Integer> chercherAliment() {
        Case banane= null;

        // Chercher un champignon
        for (List<Case> ligneVoisine : this.caseVoisines) {
            for (Case caze : ligneVoisine) {
                if (caze.getLettre() == 'C') { // si champignon trouvé
                    return List.of(caze.getX(), caze.getY());
                }else if (banane == null && caze.getLettre() == 'B') {
                    banane = caze;
                }
            }
        }
        // Banane trouvée !
        if (banane != null) {
            return List.of(banane.getX(), banane.getY());
        }

        // ni champignon ni banane n'a été trouvé
        return List.of(-1, -1);
    }
}
