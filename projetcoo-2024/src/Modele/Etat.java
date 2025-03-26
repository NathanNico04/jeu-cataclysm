package Modele;

import java.util.List;

public abstract class Etat {
    protected Animal animal;

    public Etat(Animal animal) {
        this.animal = animal;
    }
    public abstract void deplacement(Carte carte);
}