package androidUtils.awt;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;

public class AlphaComposite {

    public static Mode SRC_OVER = PorterDuff.Mode.SRC_OVER;
    public static Mode DST_OVER = PorterDuff.Mode.DST_OVER;

    private PorterDuffXfermode composite;
    private float alpha;

    private AlphaComposite(Mode rules)
    {
        alpha = -1;
        composite = new PorterDuffXfermode(rules);
    }

    private AlphaComposite(Mode rules, float alpha)
    {
        this.alpha = alpha;
        composite = new PorterDuffXfermode(rules);
    }
    public static AlphaComposite getInstance(Mode rules)
    {
        return new AlphaComposite(rules);

    }

    public static AlphaComposite getInstance(Mode rules, float alpha)
    {
        return new AlphaComposite(rules, alpha);
    }

    public PorterDuffXfermode getComposite()
    {
        return composite;
    }

    public float getAlpha() {
        return alpha;
    }
}
