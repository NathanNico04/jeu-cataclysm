package Modele;

import java.util.ArrayList;
import java.util.List;

public class ZoneForet implements ZoneStrategy
{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m" ;
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";



    // Transforme une carte ( .txt ) en matrice d'objet 'Case'
    // Peut-être découper en plusieurs fonctions
    @Override
    public Case generer(char c, int x, int y) {
        String nom;
        Case caze;
        switch (c){
            case '@' :
                nom = "Personnage";
                caze = new Personnage(ANSI_WHITE_BACKGROUND,ANSI_PURPLE,nom,c,x,y);
                break;
            case 'A' :
                nom = "Arbre";
                caze = new Vegetation(ANSI_BLACK_BACKGROUND,ANSI_GREEN,nom,c,x,y);
                break;
            case 'B' :
                nom = "Buisson";
                caze = new Vegetation(ANSI_BLACK_BACKGROUND,ANSI_GREEN,nom,c,x,y);
                break;
            case 'G' :
                nom = "Gland";
                caze = new Aliment("\u001B[48;5;94m",ANSI_YELLOW,nom,c,x,y); //"\u001B[48;5;94m" : marron
                break;
            case 'C' :
                nom = "Champignon";
                caze = new Aliment(ANSI_WHITE_BACKGROUND,"\u001B[38;5;94m",nom,c,x,y); // "\u001B[38;5;94m" couleur police marron
                break;
            case 'E' :
                nom = "Ecureuil";
                caze = new Ecureuil(ANSI_YELLOW_BACKGROUND,ANSI_BLACK,nom,c,x,y,false,5, new ArrayList<>());
                break;
            default:  // caractère vide
                nom = "Zone vide";
                caze = new Vide(ANSI_GREEN_BACKGROUND,ANSI_GREEN,nom,c,x,y);
                break;

        }
        return caze;
    }

}