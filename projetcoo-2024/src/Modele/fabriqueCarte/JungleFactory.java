package Modele.fabriqueCarte;

public class JungleFactory extends CarteFactory {

    @Override
    public void CreerCarteTransformee(String source, String destination) {
        transformCarte = new JungleTransformer();
        transformCarte.Transform(source, destination);
    }

    @Override
    public String generateCarte(String type, int nbLigne, int nbColonne){
        transformCarte = new JungleTransformer();
        return transformCarte.GrilleGenerator(type, nbLigne, nbColonne);
    }
}
