package Modele;

import static Modele.Carte.ANSI_RESET;

public abstract class Case{
    protected String couleurFond;
    protected String couleurPolice;
    protected String nom;
    protected char lettre;
    protected int x;
    protected int y;

    public Case(String couleurFond, String couleurPolice, String nom, char lettre, int x, int y) {
        this.couleurFond = couleurFond;
        this.couleurPolice = couleurPolice;
        this.nom = nom;
        this.lettre = lettre;
        this.x = x;
        this.y = y;
    }

    public void setCouleurFond(String couleurFond) {
        this.couleurFond = couleurFond;
    }

    public void setCouleurPolice(String couleurPolice) {
        this.couleurPolice = couleurPolice;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public String getNom(){
        return nom;
    }

    public char getLettre() {
        return lettre;
    }


    public String toStringNom(){
        return this.couleurFond + this.couleurPolice + this.nom + ANSI_RESET;
    }

    @Override
    public String toString() {
        return this.couleurFond + this.couleurPolice + this.lettre + ANSI_RESET;
    }
}