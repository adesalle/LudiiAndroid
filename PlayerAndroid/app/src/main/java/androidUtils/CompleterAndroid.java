package androidUtils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import completer.Completer;
import completer.Completion;
import main.grammar.Report;
import parser.Expander;
import playerAndroid.app.StartAndroidApp;

public class CompleterAndroid extends Completer {

    public static List<Completion> completeExhaustive(final String raw, final int maxCompletions, final Report report) {
        System.out.println("Completer.complete(): Completing at most " + maxCompletions + " descriptions...");

        final List<Completion> completions = new ArrayList<Completion>();

        // Create list of alternative Descriptions, as each will need to be expanded
        final Map<String, String> ludMap = getAllLudContents();
        final Map<String, String> defMap = getAllDefContents();

        final List<Completion> queue = new ArrayList<Completion>();
        queue.add(new Completion(new String(raw)));

        while (!queue.isEmpty()) {
            final Completion comp = queue.remove(0);
            if (!needsCompleting(comp.raw())) {
                // Completed!
                completions.add(comp);

                if (completions.size() >= maxCompletions)
                    return completions;

                continue;
            }

            // Complete the next completion clause
            nextCompletionExhaustive(comp, queue, ludMap, defMap, report);
        }

        return completions;
    }
    //-------------------------------------------------------------------------

    /**
     * Creates list of completions irrespective of previous completions.
     *
     * @param raw            Partial raw game description.
     * @param maxCompletions Maximum number of completions to make (default is 1, e.g. for Travis tests).
     * @param report         Report log for warnings and errors.
     * @return List of completed (raw) game descriptions ready for expansion and parsing.
     */
    public static List<Completion> completeSampled(final String raw, final int maxCompletions, final Report report) {
//		System.out.println("\nCompleter.complete(): Completing at most " + maxCompletions + " descriptions...");

        final List<Completion> completions = new ArrayList<Completion>();

        // Create list of alternative Descriptions, as each will need to be expanded
        final Map<String, String> ludMap = getAllLudContents();
        final Map<String, String> defMap = getAllDefContents();
        System.out.println("sampled " + ludMap.size());

        for (int n = 0; n < maxCompletions; n++) {
            Completion comp = new Completion(new String(raw));
            while (needsCompleting(comp.raw()))
                comp = nextCompletionSampled(comp, ludMap, defMap, report);
            completions.add(comp);
        }

//		System.out.println("\nList of completions:");
//		for (final Completion comp : completions)
//			System.out.println(comp.raw());

        return completions;
    }

    /**
     * @return Names and contents of all files within the lud/board path.
     */
    public static Map<String, String> getAllLudContents() {
        return getAllDirectoryContents("lud/board/");
    }

    /**
     * @return Names and contents of all files within the define path.
     */
    public static Map<String, String> getAllDefContents() {
        return getAllDirectoryContents("def/");
    }

    /**
     * @return Names and contents of all files within the specific directory path.
     */

    public static Map<String, String> getAllDirectoryContents(String dirInAssets) {
        Map<String, String> fileContents = new HashMap<>();
        AssetManager assetManager = StartAndroidApp.startAndroidApp().getAssets();

        try {
            // Lister les fichiers dans le dossier assets
            String[] files = assetManager.list(dirInAssets); // Ex: "mon_dossier"

            if (files != null) {
                for (String filename : files) {
                    // Lire le contenu de chaque fichier
                    try (InputStream is = assetManager.open(dirInAssets + "/" + filename);
                         BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        // Nettoyer le contenu si nécessaire
                        String content = Expander.cleanUp(sb.toString(), null);
                        fileContents.put(filename, content);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContents;
    }
    public static void saveCompletion(
            final String path,
            final String name,
            final Completion completion
    ) throws IOException {
        Context context = StartAndroidApp.getAppContext();
        // Déterminer le chemin de base
        File baseDir;
        if (path != null) {
            baseDir = new File(path);
        } else {
            // Chemin par défaut dans le stockage interne de l'application
            baseDir = new File(context.getFilesDir(), "Common/res/out/recons/");
        }

        // Créer le répertoire s'il n'existe pas
        if (!baseDir.exists()) {
            if (!baseDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + baseDir.getAbsolutePath());
            }
        }

        // Créer le fichier de sortie
        File outFile = new File(baseDir, name + ".lud");

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
            writer.write(completion.raw());
        }
    }
}
