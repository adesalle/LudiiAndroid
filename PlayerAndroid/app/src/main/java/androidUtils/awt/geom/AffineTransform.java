package androidUtils.awt.geom;

import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.geom.affineTransform.ATRotate;
import androidUtils.awt.geom.affineTransform.ATScale;
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

    public AffineTransform(Matrix matrix)
    {
        matrice = matrix;
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
    public void translate(float x, float y)
    {
        ato.add(new ATTranslate(matrice, x, y));
    }

    public void rotate(double rads, int x, int y)
    {
        ato.add(new ATRotate(matrice, rads, x, y));
    }
    public void rotate(double rads)
    {
        ato.add(new ATRotate(matrice, rads));
    }

    public void scale(int x, int y)
    {
        ato.add(new ATScale(matrice, x, y));
    }
    public void scale(double x, double y)
    {
        ato.add(new ATScale(matrice, (float) x, (float)y));
    }

    public void setToIdentity()
    {
        matrice.reset();
        ato.clear();
    }

    public Matrix getMatrix()
    {
        return matrice;
    }

    public List<AffineTransformObject> getAto() {
        return ato;
    }


    public AffineTransform createInverse()
    {
        Matrix inverse = new Matrix();
        boolean success = this.matrice.invert(inverse);

        if (!success) {
            throw new IllegalArgumentException("Matrix cannot be inverted. It may be singular.");
        }
        return new AffineTransform(inverse);
    }
}
