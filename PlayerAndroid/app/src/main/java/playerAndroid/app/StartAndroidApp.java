package playerAndroid.app;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import androidUtils.ZipManager;
import androidUtils.awt.Graphics;
import androidUtils.awt.MouseInfo;
import androidUtils.awt.SVGGraphics2D;
import androidUtils.swing.JFrame;

import manager.Manager;
import manager.ai.AIDetails;
import playerAndroid.app.util.SettingsDesktop;


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
        Rect info = getWindowManager().getCurrentWindowMetrics().getBounds();
        System.out.println("window size " + info.width() + "x" + info.height());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setupInitialVariable();
        androidApp.createAndroidApp();

        frame = AndroidApp.frame();
        frame.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Create container
        FrameLayout container = new FrameLayout(this);
        container.addView(frame);

        setContentView(container);
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = container.getWidth();
                int height = container.getHeight();
                System.out.println("Actual container size: " + width + "x" + height);

                // Now you have the actual dimensions to work with
            }
        });

        frame.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                frame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = frame.getWidth();
                int height = frame.getHeight();
                System.out.println("Actual window size: " + width + "x" + height);
                width = frame.getContentPane().getWidth();
                height = frame.getContentPane().getHeight();
                System.out.println("Actual content size: " + width + "x" + height);

                // Now you have the actual dimensions to work with
            }
        });
        frame.getContentPane().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                frame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = frame.getContentPane().getWidth();
                int height = frame.getContentPane().getHeight();
                System.out.println("Actual content size: " + width + "x" + height);

                // Now you have the actual dimensions to work with
            }
        });
        frame.setVisible(true);
        //MouseInfo.setupTouchTracking(this);


/*        JFrame fram = new JFrame();

        JPanel layout = new JPanel();
        layout.setOrientation(LinearLayout.VERTICAL);


        canvasView.setColor(Color.ORANGE);
        canvasView.drawLine(0, 0, 100, 100);
        layout.addView(canvasView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                800));

        //canvasView.draw(new Line2D.Double(300, 100, 10, 100));
        canvasView.invalidate();


        Button button = new Button(this);
        button.setText("Dessiner une ligne bleue");
        layout.addView(button);


        fram.setContentPane(layout);
        setContentView(fram);*/
    }

    public void setupInitialVariable()
    {
        startAndroidApp = this;
        androidApp = new AndroidApp();
        appContext = this.getBaseContext();

        //ZipManager.extractSvgs(".lud");
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