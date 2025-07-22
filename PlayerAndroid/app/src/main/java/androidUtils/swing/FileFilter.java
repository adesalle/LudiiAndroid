package androidUtils.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Imitation de javax.swing.filechooser.FileFilter pour Android
 */
public abstract class FileFilter {
    /**
     * Teste si un fichier doit être affiché dans le sélecteur
     */
    public abstract boolean accept(File file);

    /**
     * Description du filtre à afficher à l'utilisateur
     */
    public abstract String getDescription();
}