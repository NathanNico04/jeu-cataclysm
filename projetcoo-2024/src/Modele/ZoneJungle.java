package Modele;

import java.util.ArrayList;
import java.util.List;

public class ZoneJungle implements ZoneStrategy
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



    public Case generer(char c, int x, int y) {
        String nom;
        Case caze;
        switch (c) {
            case '@':
                nom = "Personnage";
                caze = new Personnage("\u001B[48;5;28m", "\u001B[38;5;226m", nom, c, x, y); // Vert jungle + Jaune vif
                break;
            case 'K':
                nom = "Cocotier";
                caze = new Vegetation(ANSI_BLACK_BACKGROUND,ANSI_GREEN, nom, c, x, y); // Vert foncé + Vert tropical
                break;
            case 'R':
                nom = "Petit rocher";
                caze = new Vegetation(ANSI_BLACK_BACKGROUND,ANSI_GREEN, nom, c, x, y); // Marron clair (sol) + Marron texte
                break;
            case 'B':
                nom = "Banane";
                caze = new Aliment("\u001B[48;5;226m", "\u001B[38;5;22m", nom, c, x, y); // Jaune vif (banane) + Vert foncé
                break;
            case 'C':
                nom = "Champignon";
                caze = new Aliment("\u001B[48;5;94m", "\u001B[38;5;31m", nom, c, x, y); // Marron clair (sol) + Bleu clair
                break;
            case 'S':
                nom = "Singe";
                caze = new Singe("\u001B[48;5;28m", "\u001B[38;5;214m", nom, c, x, y, false, 5, new ArrayList<>()); // Vert jungle + Orange
                break;
            default:  // caractère vide
                nom = "Zone vide";
                caze = new Vide("\u001B[48;5;22m", "\u001B[38;5;22m", nom, c, x, y); // Vert foncé (vide)
                break;
        }
        return caze;
    }















}