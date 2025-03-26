package Main;
import Controleur.Controleur;
import Vue.Ihm;

public class Main {
        public static void main(String[] args) {
            //Ihm ihm = new Ihm();   // le main ne doit pas connaître d'objet ihm, mais seulement le controleur
            // String carte = "ChargementCarte/carte.txt";
            Controleur controleur = new Controleur();
            controleur.demarrage();
    }
}