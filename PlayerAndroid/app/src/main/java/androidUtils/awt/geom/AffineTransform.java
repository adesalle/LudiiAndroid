package androidUtils.awt.geom;

import android.graphics.Matrix;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.geom.affineTransform.ATRotate;
import androidUtils.awt.geom.affineTransform.ATScale;
import androidUtils.awt.geom.affineTransform.ATTranslate;
import androidUtils.awt.geom.affineTransform.AffineTransformObject;

public class AffineTransform {
    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_TRANSLATION = 1;
    public static final int TYPE_UNIFORM_SCALE = 2;
    public static final int TYPE_GENERAL_SCALE = 4;
    public static final int TYPE_FLIP = 64;
    public static final int TYPE_QUADRANT_ROTATION = 8;
    public static final int TYPE_GENERAL_ROTATION = 16;
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    public static final int TYPE_MASK_SCALE = TYPE_UNIFORM_SCALE | TYPE_GENERAL_SCALE;
    public static final int TYPE_MASK_ROTATION = TYPE_QUADRANT_ROTATION | TYPE_GENERAL_ROTATION;

    Matrix matrice;
    List<AffineTransformObject> ato;

    public AffineTransform() {
        matrice = new Matrix();
        ato = new ArrayList<>();
    }

    public AffineTransform(Matrix matrix) {
        this.matrice = new Matrix(matrix);
        this.ato = new ArrayList<>();
    }

    public AffineTransform(AffineTransform at) {
        this.matrice = new Matrix(at.matrice);
        this.ato = new ArrayList<>(at.ato);
    }

    // Factory methods
    public static AffineTransform getScaleInstance(double sx, double sy) {
        AffineTransform at = new AffineTransform();
        at.scale(sx, sy);
        return at;
    }

    public static AffineTransform getTranslateInstance(double tx, double ty) {
        AffineTransform at = new AffineTransform();
        at.translate(tx, ty);
        return at;
    }

    public static AffineTransform getRotateInstance(double angle) {
        AffineTransform at = new AffineTransform();
        at.rotate(angle);
        return at;
    }

    public static AffineTransform getRotateInstance(double angle, double x, double y) {
        AffineTransform at = new AffineTransform();
        at.rotate(angle, x, y);
        return at;
    }


    // Transformation methods
    public void translate(int x, int y) {
        ato.add(new ATTranslate(matrice, x, y));
    }

    public void translate(double x, double y) {
        ato.add(new ATTranslate(matrice, (float)x, (float)y));
    }

    public void rotate(double radians) {
        ato.add(new ATRotate(matrice, radians));
    }

    public void rotate(double radians, double x, double y) {
        ato.add(new ATRotate(matrice, radians, (int) x, (int) y));
    }

    public void scale(int x, int y) {
        ato.add(new ATScale(matrice, x, y));
    }

    public void scale(double x, double y) {
        ato.add(new ATScale(matrice, (float)x, (float)y));
    }


    public void concatenate(AffineTransform at) {
        matrice.postConcat(at.matrice);
    }

    public void preConcatenate(AffineTransform at) {
        matrice.preConcat(at.matrice);
    }

    // Query methods
    public double getScaleX() {
        float[] values = new float[9];
        matrice.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    public double getScaleY() {
        float[] values = new float[9];
        matrice.getValues(values);
        return values[Matrix.MSCALE_Y];
    }

    public double getShearX() {
        float[] values = new float[9];
        matrice.getValues(values);
        return values[Matrix.MSKEW_X];
    }

    public double getShearY() {
        float[] values = new float[9];
        matrice.getValues(values);
        return values[Matrix.MSKEW_Y];
    }

    public double getTranslateX() {
        float[] values = new float[9];
        matrice.getValues(values);
        return values[Matrix.MTRANS_X];
    }

    public double getTranslateY() {
        float[] values = new float[9];
        matrice.getValues(values);
        return values[Matrix.MTRANS_Y];
    }

    public boolean isIdentity() {
        return matrice.isIdentity();
    }

    public int getType() {
        float[] values = new float[9];
        matrice.getValues(values);

        int type = TYPE_IDENTITY;
        if (values[Matrix.MSCALE_X] != 1.0 || values[Matrix.MSCALE_Y] != 1.0) {
            type |= TYPE_GENERAL_SCALE;
        }
        if (values[Matrix.MSKEW_X] != 0.0 || values[Matrix.MSKEW_Y] != 0.0) {
            type |= TYPE_GENERAL_TRANSFORM;
        }
        if (values[Matrix.MTRANS_X] != 0.0 || values[Matrix.MTRANS_Y] != 0.0) {
            type |= TYPE_TRANSLATION;
        }
        return type;
    }

    // Transformation application
    public PointF transform(PointF src, PointF dst) {
        if (dst == null) {
            dst = new PointF();
        }
        float[] pts = {src.x, src.y};
        matrice.mapPoints(pts);
        dst.set(pts[0], pts[1]);
        return dst;
    }

    public void transform(float[] srcPts, int srcOff,
                          float[] dstPts, int dstOff,
                          int numPts) {
        System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
        matrice.mapPoints(dstPts, dstOff, dstPts, dstOff, numPts);
    }

    // Other utility methods
    public void setToIdentity() {
        matrice.reset();
        ato.clear();
    }

    public void setToTranslation(double tx, double ty) {
        matrice.reset();
        matrice.postTranslate((float)tx, (float)ty);
    }

    public void setToScale(double sx, double sy) {
        matrice.reset();
        matrice.postScale((float)sx, (float)sy);
    }

    public void setToRotation(double angle) {
        matrice.reset();
        matrice.postRotate((float)Math.toDegrees(angle));
    }

    public void setToRotation(double angle, double x, double y) {
        matrice.reset();
        matrice.postRotate((float)Math.toDegrees(angle), (float)x, (float)y);
    }

    public void setToShear(double shx, double shy) {
        matrice.reset();
        float[] values = {1.0f, (float)shx, 0.0f,
                (float)shy, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f};
        matrice.setValues(values);
    }

    public void setTransform(AffineTransform tx) {
        this.matrice = new Matrix(tx.matrice);
        this.ato = new ArrayList<>(tx.ato);
    }

    public void setTransform(double m00, double m10,
                             double m01, double m11,
                             double m02, double m12) {
        matrice.reset();
        float[] values = {(float)m00, (float)m10, (float)m02,
                (float)m01, (float)m11, (float)m12,
                0.0f, 0.0f, 1.0f};
        matrice.setValues(values);
    }

    public AffineTransform createInverse() {
        Matrix inverse = new Matrix();
        boolean success = this.matrice.invert(inverse);
        if (!success) {
            throw new IllegalArgumentException("Matrix cannot be inverted. It may be singular.");
        }
        return new AffineTransform(inverse);
    }

    public Matrix getMatrix() {
        return new Matrix(matrice);
    }

    public List<AffineTransformObject> getAto() {
        return new ArrayList<>(ato);
    }

    // Clone support
    @Override
    public AffineTransform clone() {
        return new AffineTransform(this);
    }
}