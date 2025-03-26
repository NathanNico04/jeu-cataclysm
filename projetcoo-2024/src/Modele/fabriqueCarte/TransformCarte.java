package Modele.fabriqueCarte;

public interface TransformCarte {
    void Transform(String source, String target);

    String GrilleGenerator(String type, int nbLigne, int nbColonne);
}
