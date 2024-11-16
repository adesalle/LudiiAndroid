package androidUtils.awt.geom;

import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.geom.affineTransform.ATRotate;
import androidUtils.awt.geom.affineTransform.ATTranslate;
import androidUtils.awt.geom.affineTransform.AffineTransformObject;

public class AffineTransform {

    public static AffineTransform getScaleInstance(int x, int y)
    {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.matrice.postScale(x,y);
        return affineTransform;
    }
    public static AffineTransform getTranslateInstance(int x, int y)
    {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.matrice.postTranslate(x,y);
        return affineTransform;
    }

    Matrix matrice;

    List<AffineTransformObject> ato;

    public AffineTransform()
    {
        matrice = new Matrix();
        ato = new ArrayList<>();
    }
    public AffineTransform(AffineTransform at)
    {
        matrice = new Matrix(at.matrice);
        ato = new ArrayList<>(ato);
    }

    public void concatenate(AffineTransform at)
    {

        matrice.postConcat(at.matrice);
    }

    public void translate(int x, int y)
    {
        ato.add(new ATTranslate(matrice, x, y));
    }

    public void rotate(double rads, int x, int y)
    {
        ato.add(new ATRotate(matrice, rads, x, y));
    }

    public Matrix getMatrix()
    {
        return matrice;
    }

    public List<AffineTransformObject> getAto() {
        return ato;
    }
}
