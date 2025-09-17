package playerAndroid.app;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import androidUtils.ZipManager;
import androidUtils.awt.Dialog;
import androidUtils.awt.Graphics;
import androidUtils.awt.MouseInfo;
import androidUtils.awt.SVGGraphics2D;
import androidUtils.swing.JFrame;


import manager.Manager;
import manager.ai.AIDetails;

public class StartAndroidApp extends AppCompatActivity {

    private static StartAndroidApp startAndroidApp;
    private static AndroidApp androidApp;
    private static Context appContext;

    /**
     * App name.
     */
    public static final String AppName = "Ludii Player";

    /**
     * Set me to false if we are making a release jar.
     * NOTE. In reality this is final, but keeping it non final prevents dead-code warnings.
     */
    public static boolean devJar = false;

    /**
     * Main view.
     */
    protected static JFrame frame;

    protected static Graphics graphics;
    protected static SVGGraphics2D svgGraphics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setupInitialVariable();

        androidApp.createAndroidApp();
        frame = AndroidApp.frame();
        frame.setView(StartAndroidApp.startAndroidApp());
        frame.setTitle(AppName);


        frame.setVisible(true);
        MouseInfo.setupTouchTracking(this);

    }

    void showDialog(Dialog dialog) {

        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void setupInitialVariable()
    {
        startAndroidApp = this;
        androidApp = new AndroidApp();
        appContext = this;

        System.out.println("before svg");
        //ZipManager.extractSvgs(".lud");
        ZipManager.extractSvgs(".svg");
        System.out.println("after svg");

    }

    public InputStream getInputStreamFromFilesDir(String filename) throws IOException {
        File file = new File(appContext.getFilesDir(), filename);
        return Files.newInputStream(file.toPath());
    }

    public static String getRelativePath(String absolutePath) {
        String filesDir = StartAndroidApp.getAppContext().getFilesDir().getAbsolutePath();

        if (absolutePath.startsWith(filesDir)) {
            String relative = absolutePath.substring(filesDir.length());
            return relative.startsWith("/") ? relative.substring(1) : relative;
        }
        return absolutePath; // Retourne le chemin original si non trouvé
    }

    public void recordBitmap(Bitmap bitmap)
    {try {
        File file = new File(getExternalFilesDir(null), "debug_bitmap.png");
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();
        Log.d("BitmapCheck", "Sauvegardé à: " + file.getAbsolutePath());
    } catch (IOException e) {
        Log.e("BitmapCheck", "Erreur lors de la sauvegarde", e);
    } }

    public static Context getAppContext() {
        return appContext;
    }

    public static Graphics graphics()
    {
        return graphics;
    }

    public static SVGGraphics2D svgGraphics()
    {
        return svgGraphics;
    }

    public static void setSvgGraphics(SVGGraphics2D svg)
    {
        svgGraphics = svg;
    }


    public static StartAndroidApp startAndroidApp()
    {
        return startAndroidApp;
    }
    public static AndroidApp androidApp()
    {
        return androidApp;
    }
    public AIDetails[] aiSelected()
    {
        return manager().aiSelected();
    }

    public Manager manager()
    {
        return androidApp.manager();
    }


    public JFrame frame() {
        return frame;
    }
}