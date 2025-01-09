package androidUtils.awt.geom.affineTransform;

import android.graphics.Matrix;

public class ATScale implements AffineTransformObject{

    Matrix matrice;
    float x;
    float y;

    public ATScale(Matrix matrice, int x, int y)
    {
        this.matrice = matrice;

        this.x = x;
        this.y = y;
    }

    public ATScale(Matrix matrice, float x, float y)
    {
        this.matrice = matrice;
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void performPost() {
        matrice.postScale(x, y);
    }

    @Override
    public void performSet() {
        matrice.setScale(x, y);
    }
}