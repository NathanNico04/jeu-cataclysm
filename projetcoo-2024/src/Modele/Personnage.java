package Modele;

import java.util.ArrayList;
import java.util.List;

import static Modele.Carte.ANSI_GREEN;
import static Modele.Carte.ANSI_GREEN_BACKGROUND;

public class Personnage extends Case{

    private List<Case> inventaire;

    private List<List<Case>> caseVoisines = new ArrayList<>();


    public Personnage(String couleurFond, String couleurPolice, String nom, char lettre, int x, int y) {
        super(couleurFond, couleurPolice, nom, lettre, x, y);
        this.inventaire = new ArrayList<>();
    }


    public List<List<Case>> getAnimauxProches(){
        boolean animalTrouve = false; // pour éviter d'avoir une liste de case Vide
        List<List<Case>> animauxProches = new ArrayList<>();
        for(List<Case> ligne : this.caseVoisines){
            List<Case> ligneProche = new ArrayList<>();
            for ( Case voisin : ligne){
                if(voisin instanceof Animal){
                    ligneProche.add((Animal)voisin);
                    animalTrouve = true;
                }else{
                    ligneProche.add( new Vide(ANSI_GREEN_BACKGROUND, ANSI_GREEN, "Zone vide", ' ', 0,0));
                }
            }
            animauxProches.add(ligneProche);
        }
        if ( !animalTrouve){
            return new ArrayList<>();
        }
        return animauxProches;
    }

    public List<List<Case>> getAnimauxAmiEtAlimentVoisins(){
        boolean trouve = false; // pour éviter d'avoir une liste de case Vide
        List<List<Case>> animauxEtAlimentProches = new ArrayList<>();
        for(List<Case> ligne : this.caseVoisines){
            List<Case> ligneProche = new ArrayList<>();
            for ( Case voisin : ligne){
                if(voisin instanceof Animal  && ((Animal) voisin).getAmi() || voisin instanceof Aliment){
                    ligneProche.add(voisin);
                    trouve = true;
                }else{
                    ligneProche.add( new Vide(ANSI_GREEN_BACKGROUND, ANSI_GREEN, "Zone vide", ' ', 0,0));
                }
            }
            animauxEtAlimentProches.add(ligneProche);
        }
        if ( !trouve){
            return new ArrayList<>();
        }
        return animauxEtAlimentProches;
    }



    public void seBattre(Animal animal){
        animal.setAmi(false);
    }

    public void ramasserObjet(Case objet){
        inventaire.add( objet );
    }

    public void lancer(int objetIndex){
        inventaire.remove(objetIndex);

    }

    public List<Case> getInventaire() {
        return inventaire;
    }
    public void setInventaire(List<Case> inventaire) {
        this.inventaire = inventaire;
    }
    public String getLettreInventaire(){
        // retourne un String contenant les lettres des cases de l'inventaire
        String inventaireLettres = "";
        for (Case caze : inventaire) {
            inventaireLettres += caze.getLettre();
        }
        return inventaireLettres;
    }

    public void addToInventaire(Case caze){
        inventaire.add(caze);
    }

    public void setCaseVoisines(List<List<Case>> caseVoisines) {
        this.caseVoisines = caseVoisines;
    }
    public void addLigneToCaseVoisines(List<Case> ligneVoisine){
        this.caseVoisines.add(ligneVoisine);
    }



}