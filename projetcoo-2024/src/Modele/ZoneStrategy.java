package Modele;

import java.util.List;

public interface ZoneStrategy
{

    // Transforme une carte ( .txt ) en matrice d'objet 'Case'
    // Peut-être découpé en plusieurs fonctions
    Case  generer(char c, int x, int y);
}