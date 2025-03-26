package Modele;

import java.util.ArrayList;
import java.util.List;

import static Modele.Carte.*;

public abstract class Animal extends Case {

    protected boolean ami;
    protected int nbTourAvantAffameMax;
    protected int nbTourAvantAffameActuel;
    private Etat etat;
    protected List<List<Case>> caseVoisines;

    public Animal(String couleurFond, String couleurPolice, String nom, char lettre, int x, int y, boolean ami, int nbTourAvantAffameMax, List<List<Case>> caseVoisines) {
        super(couleurFond, couleurPolice, nom, lettre, x, y);
        this.ami = ami;
        this.nbTourAvantAffameMax = nbTourAvantAffameMax;
        this.nbTourAvantAffameActuel = nbTourAvantAffameMax;
        this.etat = new Affame(this);
        this.caseVoisines = caseVoisines;

    }

    public void setVoisins(List<List<Case>> caseVoisines){
        this.caseVoisines = caseVoisines;
    }

    public void ajouterVoisin(Case voisin, int x, int y){
        caseVoisines.get(x).set(y, voisin); // ajoute le voisin à la position caseVoisines[x][y]
    }

    public void seNourir(){
        this.nbTourAvantAffameActuel = this.nbTourAvantAffameMax;

        //si le personnage est à côté, l'animal devient son ami
        for (List<Case> ligneVoisine : this.caseVoisines) {
            for (Case caze : ligneVoisine) {
                if (caze.getLettre() == '@') {
                    ami = true;
                }
            }
        }
    }

    public void deplacement(Carte carte){
        // on effectue le déplacement selon l'etat de l'animal
        this.etat.deplacement(carte);
    }

    public void avertir(){
    }

    public boolean getAmi(){
        return ami;
    }

    public abstract List<Integer> chercherAliment();

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public int decrementerNbTourAvantAffame(){
        this.nbTourAvantAffameActuel--;
        return nbTourAvantAffameActuel;
    }

    public void setEtat(Etat etat){
        this.etat = etat;
    }

    public boolean isAmi(){
        return ami;
    }

    public void setAmi(boolean ami){
        this.ami = ami;
    }

    @Override
    public String toString() {
        String colorPolice;
        String colorFond;
        if ( ami ){
            colorPolice = ANSI_BLUE;
        }else{
            colorPolice = this.couleurPolice;
        }
        if ( etat instanceof Affame){
            colorFond = ANSI_RED_BACKGROUND;
        }else{
            colorFond = this.couleurFond;
        }
        return colorFond + colorPolice + this.lettre + ANSI_RESET;
    }
}