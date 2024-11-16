package androidUtils.awt.geom.affineTransform;

import android.graphics.Matrix;

import androidUtils.awt.geom.AffineTransform;

public class ATConcatenate implements AffineTransformObject{

    Matrix matrice;
    AffineTransform at;

    public ATConcatenate(Matrix matrice, AffineTransform at)
    {
        this.matrice = matrice;
        this.at = at;
    }
    @Override
    public void performPost() {
        matrice.postConcat(at.getMatrix());
    }

    @Override
    public void performSet() {
        matrice.preConcat(at.getMatrix());
    }
}
