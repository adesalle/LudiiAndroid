package androidUtils.swing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.view.View;

import androidUtils.awt.Component;
import androidUtils.awt.Graphics;
import androidUtils.awt.Image;
import androidUtils.awt.image.BufferedImage;

import playerAndroid.app.StartAndroidApp;

import java.io.InputStream;

public class ImageIcon implements Icon {
    private Bitmap bitmap;
    private Drawable drawable;
    private int width;
    private int height;
    private String description;

    // Constructeurs
    public ImageIcon() {
    }

    public ImageIcon(String filename) {
        this(StartAndroidApp.getAppContext(), filename);
    }

    public ImageIcon(Context context, String filename) {
        try (InputStream is = context.getAssets().open(filename)) {
            this.bitmap = BitmapFactory.decodeStream(is);
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageIcon(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
        }
    }

    public ImageIcon(Drawable drawable) {
        this.drawable = drawable;
        if (drawable != null) {
            this.width = drawable.getIntrinsicWidth();
            this.height = drawable.getIntrinsicHeight();
        }
    }

    public ImageIcon(Image image) {
        this.bitmap = image.getBitmap();
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();

    }

    // Implémentation des méthodes Icon
    @Override
    public void paintIcon(View c, Graphics g, int x, int y) {
        if (bitmap != null) {
            Canvas canvas = g.getCanvas();
            canvas.drawBitmap(bitmap, x, y, null);
        } else if (drawable != null) {
            drawable.setBounds(x, y, x + width, y + height);
            drawable.draw(g.getCanvas());
        }
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    // Méthodes supplémentaires
    public BufferedImage getImage() {
        return bitmap != null ? new BufferedImage(bitmap) : null;
    }

    public void setImage(BufferedImage image) {
            this.bitmap = image.getBitmap();
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
            this.drawable = null;

    }

    public Drawable getDrawable()
    {
        return new BitmapDrawable(StartAndroidApp.getAppContext().getResources(), bitmap);
    }

}