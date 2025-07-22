package androidUtils.swing.menu;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MenuExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Création du layout principal
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Création de la barre de menus
        JMenuBar menuBar = new JMenuBar(this);

        // Ajout des menus
        JMenu fileMenu = menuBar.addMenu("Fichier");
        JMenu editMenu = menuBar.addMenu("Édition");
        JMenu helpMenu = menuBar.addMenu("Aide");

        // Ajout des items au menu Fichier
        fileMenu.add("Nouveau").setOnMenuItemClickListener(item -> {
            Toast.makeText(this, "Nouveau document", Toast.LENGTH_SHORT).show();
            return true;
        });

        fileMenu.add("Ouvrir").setOnMenuItemClickListener(item -> {
            Toast.makeText(this, "Ouvrir un fichier", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Menu avec sous-menu
        JMenuItem exportItem = (JMenuItem) fileMenu.add("Exporter");
        JSubMenu exportSubMenu = new JSubMenu(this, exportItem);
        exportSubMenu.add("PDF").setOnMenuItemClickListener(item -> {
            Toast.makeText(this, "Export PDF", Toast.LENGTH_SHORT).show();
            return true;
        });
        exportSubMenu.add("PNG").setOnMenuItemClickListener(item -> {
            Toast.makeText(this, "Export PNG", Toast.LENGTH_SHORT).show();
            return true;
        });
        exportItem.setSubMenu(exportSubMenu);

        // Ajout à la vue
        mainLayout.addView(menuBar);

        // Ajout du contenu
        TextView content = new TextView(this);
        content.setText("Contenu principal de l'application");
        mainLayout.addView(content);

        setContentView(mainLayout);
    }
}