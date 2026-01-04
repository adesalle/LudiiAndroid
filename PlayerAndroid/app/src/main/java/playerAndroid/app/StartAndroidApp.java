package playerAndroid.app;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import androidUtils.ZipManager;
import androidUtils.awt.MouseInfo;
import androidUtils.swing.JFrame;
import manager.Manager;
import manager.ai.AIDetails;

public class StartAndroidApp extends AppCompatActivity {

    private static StartAndroidApp startAndroidApp;
    private static AndroidApp androidApp;
    private static Context appContext;
    public static String AppName = "Ludii";
    protected static JFrame frame;

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

    public void setupInitialVariable()
    {
        startAndroidApp = this;
        androidApp = new AndroidApp();
        appContext = this;
        ZipManager.extractSvgs(".svg");
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

    public static Context getAppContext() {
        return appContext;
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