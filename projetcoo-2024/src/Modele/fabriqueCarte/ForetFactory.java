package Modele.fabriqueCarte;

public class ForetFactory extends CarteFactory {

    @Override
    public void CreerCarteTransformee(String source, String destination) {
        // rien
    }

    @Override
    public String generateCarte(String type, int nbLigne, int nbColonne){
        transformCarte = new ForetTransformer();
        return transformCarte.GrilleGenerator(type, nbLigne, nbColonne);
    }
}
