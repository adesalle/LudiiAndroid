package androidUtils.awt;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;

import java.awt.Shape;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.geom.AffineTransform;
import androidUtils.awt.geom.Arc2D;
import androidUtils.awt.geom.Ellipse2D;
import androidUtils.awt.geom.GeneralPath;
import androidUtils.awt.geom.Line2D;
import androidUtils.awt.geom.Rectangle2D;
import androidUtils.awt.image.BufferedImage;

public class SVGGraphics2D extends Graphics2D {

    public SVGGraphics2D(int xSize, int ySize) {
        super();
        btp = Bitmap.createBitmap(xSize, ySize, btp.getConfig());
        canvas = new Canvas(btp);


    }

    private SVGGraphics2D() {
        super();
    }
    public SVGGraphics2D(Bitmap btp)
    {
        super(btp);
    }

    @Override
    public Graphics2D create() {
        SVGGraphics2D g2dnew = new SVGGraphics2D();
        Bitmap btpnew = Bitmap.createBitmap(btp);
        g2dnew.canvas = new Canvas(btpnew);
        g2dnew.btp = btpnew;
        g2dnew.paint = new Paint(paint);
        g2dnew.font = new Font(font);
        if (at != null) g2dnew.at = new AffineTransform(at);
        g2dnew.fontSize = fontSize;
        g2dnew.clip = clip.copy();
        return g2dnew;
    }





    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) return null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e("BitmapConverter", "Encoding error", e);
            return null;
        }
    }


    public static Bitmap base64ToBitmap(String base64) {
        if (base64 == null || base64.isEmpty()) return null;

        try {
            byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } catch (Exception e) {
            Log.e("BitmapConverter", "Decoding error", e);
            return null;
        }
    }


    public AffineTransform getTransform() {
        return at;
    }

    public String getSVGDocument() {
        return SVGGraphics2D.bitmapToBase64(btp);
    }

    public static BufferedImage getBufferedImageFrom64(String string64)
    {
        Bitmap bt = SVGGraphics2D.base64ToBitmap(string64);
        return new BufferedImage(bt);
    }

    public String getSVGElement() {
        return getSVGDocument();
    }
}