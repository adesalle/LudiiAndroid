package androidUtils;

import android.content.Context;
import android.util.Log;
import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import playerAndroid.app.StartAndroidApp;

public class ZipManager {
    private static final String TAG = "SvgZipManager";
    private static final String ZIP_NAME_SVG = "img/svg.zip";
    private static final String EXTRACT_DIR_SVG = "svg_cache";

    private static final String ZIP_NAME_LUD = "lud/lud.zip";
    private static final String EXTRACT_DIR_LUD = "lud_cache";

    public static void extractSvgs(String type) {
        Context context = StartAndroidApp.getAppContext();
        File cacheDir;
        if(type.equals(".lud"))cacheDir = new File(context.getFilesDir(), EXTRACT_DIR_LUD);
        else cacheDir = new File(context.getFilesDir(), EXTRACT_DIR_SVG);

        System.out.println(context.getFilesDir());



        cacheDir.mkdirs();
        String name;
        if(type.equals(".lud"))name = ZIP_NAME_LUD;
        else name = ZIP_NAME_SVG;
        try (InputStream is = context.getAssets().open(name);
             ZipInputStream zis = new ZipInputStream(is)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(cacheDir, entry.getName());
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error extracting SVG ZIP", e);
        }
    }

    public static File getFile(String name, String type) {
        Context context = StartAndroidApp.getAppContext();
        if (type.equals(".lud")) return new File(context.getFilesDir(), EXTRACT_DIR_LUD + "/" + name);
        return new File(context.getFilesDir(), EXTRACT_DIR_SVG + "/" + name);
    }

    public static void clearCache(Context context, String type) {
        String extractDir;
        if(type.equals(".lud")) extractDir = EXTRACT_DIR_LUD;
        else extractDir = EXTRACT_DIR_SVG;
        File cacheDir = new File(context.getFilesDir(), extractDir);
        if (cacheDir.exists()) {
            for (File file : Objects.requireNonNull(cacheDir.listFiles())) {
                file.delete();
            }
        }
    }

    public static void clearCache(Context context)
    {
        clearCache(context, ".lud");
        clearCache(context, "svg");
    }

    public static String getExtractDirSVG()
    {
        return EXTRACT_DIR_SVG;
    }

    public static String getExtractDirLud() {
        return EXTRACT_DIR_LUD;
    }
}