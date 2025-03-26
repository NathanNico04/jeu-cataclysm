package Modele.fabriqueCarte;

public abstract class CarteFactory {
    protected TransformCarte transformCarte;

    public abstract void CreerCarteTransformee(String source, String destination);

    public abstract String generateCarte(String type, int nbLigne, int nbColonne);
}
