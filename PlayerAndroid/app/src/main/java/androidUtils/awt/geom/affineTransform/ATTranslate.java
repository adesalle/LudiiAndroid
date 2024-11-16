package androidUtils.awt.geom.affineTransform;

import android.graphics.Matrix;

public class ATTranslate  implements AffineTransformObject{

    Matrix matrice;
    int x;
    int y;

    public ATTranslate(Matrix matrice, int x, int y)
    {
        this.matrice = matrice;
        this.x = x;
        this.y = y;
    }
    @Override
    public void performPost() {
        matrice.postTranslate(x, y);
    }

    @Override
    public void performSet() {
        matrice.setTranslate(x, y);
    }
}
