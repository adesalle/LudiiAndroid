package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains data on aliases for games, loaded from Player/res/help/Aliases.txt.
 * This file is not under version control, but can be generated by running
 * Player/player/utils/GenerateAliasesFile.java
 *
 * @author Dennis Soemers
 */
public class AliasesData {

    //-------------------------------------------------------------------------

    /**
     * Path where we expect our data to be generated
     */
    private static final String RESOURCE_PATH = "/help/Aliases.txt";

    /**
     * We'll only load once, and then just cache here
     */
    private static AliasesData data = null;

    //-------------------------------------------------------------------------

    /**
     * Mapping from full game paths to lists of aliases
     */
    private final Map<String, List<String>> gameAliases = new HashMap<String, List<String>>();

    //-------------------------------------------------------------------------

    /**
     * Constructor
     */
    private AliasesData() {
        // Do not instantiate: use static loadData() method instead
    }

    //-------------------------------------------------------------------------

    /**
     * @return Aliases data
     */
    public static AliasesData loadData() {
        if (data == null) {
            data = new AliasesData();

            try (final InputStream resource = AliasesData.class.getResourceAsStream(RESOURCE_PATH)) {
                if (resource != null) {
                    try
                            (
                                    final InputStreamReader isr = new InputStreamReader(resource, "UTF-8");
                                    final BufferedReader rdr = new BufferedReader(isr)
                            ) {
                        String currGame = null;

                        String line;
                        while ((line = rdr.readLine()) != null) {
                            if (line.startsWith("/lud/") && line.endsWith(".lud")) {
                                currGame = line;
                            } else {
                                if (!data.gameAliases.containsKey(currGame))
                                    data.gameAliases.put(currGame, new ArrayList<String>());

                                data.gameAliases.get(currGame).add(line);
                            }
                        }
                    }
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    //-------------------------------------------------------------------------

    /**
     * @param gamePath Full game path (starting with /lud/ and ending with .lud)
     * @return List of aliases for the game (or null if no known aliases)
     */
    public List<String> aliasesForGame(final String gamePath) {
        return gameAliases.get(gamePath);
    }

    //-------------------------------------------------------------------------

    /**
     * @param gameNameInput Full game name.
     * @return List of aliases for the game (or null if no known aliases)
     */
    public List<String> aliasesForGameName(final String gameNameInput) {
        for (final Map.Entry<String, List<String>> entry : gameAliases.entrySet()) {
            final String[] pathSplit = entry.getKey().split("/");
            String gameName = pathSplit[pathSplit.length - 1];
            gameName = gameName.substring(0, gameName.length() - 4);
            if (gameName.toLowerCase().equals(gameNameInput)) {
                return gameAliases.get(entry.getKey());
            }
        }
        return gameAliases.get(gameNameInput);
    }

    //-------------------------------------------------------------------------

}
