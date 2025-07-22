package androidUtils.awt;

import android.view.View;
import android.view.ViewGroup;

import androidUtils.swing.ViewComponent;


public class BorderLayout implements Layout {
    // Constantes pour les positions
    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String CENTER = "Center";
    public static final String LINE_START = "start";
    public static final Object LINE_END = "end";

    private int hgap;
    private int vgap;
    private ViewGroup target;

    public BorderLayout() {
        this(0, 0);
    }

    public BorderLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
        applyLayout();
    }

    @Override
    public void applyLayout(ViewGroup vg) {
        target= vg;
        applyLayout();
    }


    @Override
    public void addLayoutComponent(View comp, Object constraints) {
        if (constraints instanceof String) {
            String region = (String) constraints;
            // On vérifie que la région est valide
            if (NORTH.equals(region) || SOUTH.equals(region) ||
                    EAST.equals(region) || WEST.equals(region) ||
                    CENTER.equals(region) || LINE_START.equals(region) ||
                    LINE_END.equals(region)) {
                comp.setTag(constraints);
            } else {
                throw new IllegalArgumentException("Invalid region: " + constraints);
            }
        } else if (constraints != null) {
            throw new IllegalArgumentException("Cannot add to layout: constraint must be a string (was " + constraints.getClass() + ")");
        } else {
            // Par défaut, on met au centre
            comp.setTag(CENTER);
        }
    }

    @Override
    public void removeLayoutComponent(View comp) {
        comp.setTag(null);
    }

    private void applyLayout() {

        if (target == null || target.getChildCount() == 0) return;

        int width = target.getWidth();
        int height = target.getHeight();

        if (width == 0 || height == 0) return; // Pas encore mesuré

        // Trouver les vues pour chaque région
        View north = findViewForRegion(NORTH);
        View south = findViewForRegion(SOUTH);
        View west = findViewForRegion(WEST);
        View east = findViewForRegion(EAST);
        View center = findViewForRegion(CENTER);

        // Calcul des dimensions disponibles
        int topHeight = north != null ? north.getMeasuredHeight() : 0;
        int bottomHeight = south != null ? south.getMeasuredHeight() : 0;
        int leftWidth = west != null ? west.getMeasuredWidth() : 0;
        int rightWidth = east != null ? east.getMeasuredWidth() : 0;

        // Positionnement des régions Nord/Sud
        if (north != null) {
            north.layout(
                    target.getPaddingLeft(),
                    target.getPaddingTop(),
                    width - target.getPaddingRight(),
                    target.getPaddingTop() + topHeight
            );
        }

        if (south != null) {
            south.layout(
                    target.getPaddingLeft(),
                    height - target.getPaddingBottom() - bottomHeight,
                    width - target.getPaddingRight(),
                    height - target.getPaddingBottom()
            );
        }

        // Positionnement des régions Ouest/Est
        int middleTop = target.getPaddingTop() + (north != null ? topHeight + vgap : 0);
        int middleBottom = height - target.getPaddingBottom() - (south != null ? bottomHeight + vgap : 0);
        int middleHeight = middleBottom - middleTop;

        if (west != null) {
            west.layout(
                    target.getPaddingLeft(),
                    middleTop,
                    target.getPaddingLeft() + leftWidth,
                    middleBottom
            );
        }

        if (east != null) {
            east.layout(
                    width - target.getPaddingRight() - rightWidth,
                    middleTop,
                    width - target.getPaddingRight(),
                    middleBottom
            );
        }

        // Positionnement de la région centrale
        if (center != null) {
            int centerLeft = target.getPaddingLeft() + (west != null ? leftWidth + hgap : 0);
            int centerRight = width - target.getPaddingRight() - (east != null ? rightWidth + hgap : 0);
            int centerTop = middleTop;
            int centerBottom = middleBottom;

            center.layout(
                    centerLeft,
                    centerTop,
                    centerRight,
                    centerBottom
            );
        }
    }

    private View findViewForRegion(String region) {
        for (int i = 0; i < target.getChildCount(); i++) {
            View child = target.getChildAt(i);
            Object tag = child.getTag();
            if (region.equals(tag)) {
                // Mesurer l'enfant avant le positionnement
                int widthSpec = View.MeasureSpec.makeMeasureSpec(
                        target.getWidth(), View.MeasureSpec.AT_MOST);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(
                        target.getHeight(), View.MeasureSpec.AT_MOST);
                child.measure(widthSpec, heightSpec);
                return child;
            }
        }
        return null;
    }

    // Méthodes utilitaires
    public static void setRegion(View child, String region) {
        child.setTag(region);
    }

    public int getHgap() {
        return hgap;
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
        applyLayout();
    }

    public int getVgap() {
        return vgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
        applyLayout();
    }
}