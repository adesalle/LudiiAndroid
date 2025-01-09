package androidUtils.awt.geom.affineTransform;

import android.graphics.Matrix;

public class ATRotate implements AffineTransformObject{

    Matrix matrice;
    float degree;
    int x;
    int y;

    public ATRotate(Matrix matrice, double rads, int x, int y)
    {
        this.matrice = matrice;
        degree = (float) Math.toDegrees(rads);
        this.x = x;
        this.y = y;
    }

    public ATRotate(Matrix matrice, double rads)
    {
        this.matrice = matrice;
        degree = (float) Math.toDegrees(rads);
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void performPost() {
        matrice.postRotate(degree, x, y);
    }

    @Override
    public void performSet() {
        matrice.setRotate(degree, x, y);
    }
}
