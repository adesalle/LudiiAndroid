package androidUtils.swing;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import androidUtils.awt.Color;
import androidUtils.awt.Component;
import androidUtils.awt.Font;
import androidUtils.swing.event.ChangeEvent;
import androidUtils.swing.event.ChangeListener;
import playerAndroid.app.StartAndroidApp;

public class JTabbedPane extends LinearLayout {
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    public static final int WRAP_TAB_LAYOUT = 0;
    public static final int SCROLL_TAB_LAYOUT = 1;

    private final TabHeaderLayout tabHeader;
    private final ViewPager viewPager;
    private final List<Tab> tabs = new ArrayList<>();
    private int tabPlacement = TOP;
    private int tabLayoutPolicy = WRAP_TAB_LAYOUT;
    private Color background = new Color(240, 240, 240);
    private Color foreground = Color.BLACK;
    private Color selectedColor = new Color(200, 200, 255);
    private final List<ChangeListener> changeListeners = new ArrayList<>();

    private Font tabFont = new Font(Typeface.DEFAULT, 14);
    private JPopupMenu componentPopupMenu;


    public JTabbedPane() {
        this(TOP, WRAP_TAB_LAYOUT);
    }

    public JTabbedPane(int tabPlacement) {
        this(tabPlacement, WRAP_TAB_LAYOUT);
    }

    public JTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(StartAndroidApp.getAppContext());
        this.tabPlacement = tabPlacement;
        this.tabLayoutPolicy = tabLayoutPolicy;

        setOrientation(tabPlacement == TOP || tabPlacement == BOTTOM ?
                VERTICAL : HORIZONTAL);

        // Configuration du header des tabs
        tabHeader = new TabHeaderLayout(getContext());
        tabHeader.setBackground(new ColorDrawable(background.toArgb()));

        // Configuration du ViewPager
        viewPager = new ViewPager(getContext());
        viewPager.setAdapter(new TabPagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                fireStateChanged();
                updateTabSelection();
            }
        });

        // Ajout des vues selon le placement
        if (tabPlacement == TOP || tabPlacement == LEFT) {
            addView(tabHeader);
            addView(viewPager);
        } else {
            addView(viewPager);
            addView(tabHeader);
        }

        setWeightDistribution();
    }

    private void setWeightDistribution() {
        LayoutParams headerParams = (LayoutParams) tabHeader.getLayoutParams();
        LayoutParams pagerParams = (LayoutParams) viewPager.getLayoutParams();

        if (tabPlacement == TOP || tabPlacement == BOTTOM) {
            headerParams.width = LayoutParams.MATCH_PARENT;
            headerParams.height = LayoutParams.WRAP_CONTENT;
            pagerParams.width = LayoutParams.MATCH_PARENT;
            pagerParams.height = 0;
            pagerParams.weight = 1;
        } else {
            headerParams.width = LayoutParams.WRAP_CONTENT;
            headerParams.height = LayoutParams.MATCH_PARENT;
            pagerParams.width = 0;
            pagerParams.height = LayoutParams.MATCH_PARENT;
            pagerParams.weight = 1;
        }
    }

    public void addTab(String title, View component) {
        addTab(title, null, component, null);
    }

    public void addTab(String title, Drawable icon, View component) {
        addTab(title, icon, component, null);
    }

    public void addTab(String title, Drawable icon, View component, String tooltip) {
        Tab tab = new Tab(title, icon, component, tooltip);
        tabs.add(tab);

        // Ajout du header de tab
        tabHeader.addTab(tab);

        // Mise à jour de l'adapter
        ((TabPagerAdapter) viewPager.getAdapter()).notifyDataSetChanged();

        if (tabs.size() == 1) {
            setSelectedIndex(0);
        }
    }

    public void removeTabAt(int index) {
        if (index >= 0 && index < tabs.size()) {
            tabs.remove(index);
            tabHeader.removeTabAt(index);
            ((TabPagerAdapter) viewPager.getAdapter()).notifyDataSetChanged();

            if (index == getSelectedIndex() && !tabs.isEmpty()) {
                setSelectedIndex(Math.max(0, index - 1));
            }
        }
    }

    public void setSelectedIndex(int index) {
        if (index >= 0 && index < tabs.size()) {
            viewPager.setCurrentItem(index);
        }
    }

    public int getSelectedIndex() {
        return viewPager.getCurrentItem();
    }

    public View getSelectedComponent() {
        int index = getSelectedIndex();
        return index >= 0 ? tabs.get(index).component : null;
    }

    public int getTabCount() {
        return tabs.size();
    }

    public String getTitleAt(int index) {
        return tabs.get(index).title;
    }

    public void setTitleAt(int index, String title) {
        if (index >= 0 && index < tabs.size()) {
            tabs.get(index).title = title;
            tabHeader.updateTabTitle(index, title);
        }
    }

    public void setTabPlacement(int placement) {
        if (placement != tabPlacement) {
            this.tabPlacement = placement;
            removeAllViews();

            if (tabPlacement == TOP || tabPlacement == LEFT) {
                addView(tabHeader);
                addView(viewPager);
            } else {
                addView(viewPager);
                addView(tabHeader);
            }

            setWeightDistribution();
            requestLayout();
        }
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    private void fireStateChanged() {
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(e);
        }
    }

    private void updateTabSelection() {
        tabHeader.updateSelection(getSelectedIndex());
    }

    public int indexOfTab(String title) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).title.equals(title)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfComponent(Component component) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).component == component) {
                return i;
            }
        }
        return -1;
    }

    public void setComponentPopupMenu(JPopupMenu popupMenu) {
        this.componentPopupMenu = popupMenu;
        if (popupMenu != null) {
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        componentPopupMenu.show(v, 0, 0);
                        return true;
                    }
                    return false;
                }
            });
        } else {
            setOnTouchListener(null);
        }
    }

    public void setTabFont(Font font) {
        this.tabFont = font;
        for (Tab tab : tabs) {
            if (tab.tabView instanceof ViewGroup) {
                updateTabFont(tab.tabView, font);
            }
        }
    }

    private void updateTabFont(View tabView, Font font) {
        if (tabView instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) tabView;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTypeface(font.getFont(), font.getStyle());
                    ((TextView) child).setTextSize(font.getSize());
                }
            }
        }
    }


    public void setBackgroundAt(int index, Color color) {
        if (index >= 0 && index < tabs.size() && tabs.get(index).tabView != null) {
            tabs.get(index).tabView.setBackgroundColor(color.toArgb());
        }
    }

    public void setForegroundAt(int index, Color color) {
        if (index >= 0 && index < tabs.size() && tabs.get(index).tabView != null) {
            if (tabs.get(index).tabView instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) tabs.get(index).tabView;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child instanceof TextView) {
                        ((TextView) child).setTextColor(color.toArgb());
                    }
                }
            }
        }
    }

    public void setSelectedComponent(ViewGroup scrollPane) {
    }

    public void remove(JScrollPane scrollPane) {

    }

    public void setVisible(boolean visible) {
        setVisibility(visible ? VISIBLE: INVISIBLE);
    }

    protected void repaint() {
        invalidate();
    }


    private class Tab {
        String title;
        Drawable icon;
        View component;
        String tooltip;
        View tabView;

        Tab(String title, Drawable icon, View component, String tooltip) {
            this.title = title;
            this.icon = icon;
            this.component = component;
            this.tooltip = tooltip;
        }
    }

    private class TabHeaderLayout extends HorizontalScrollView {
        private final LinearLayout tabsContainer;
        private int selectedPosition = -1;

        public TabHeaderLayout(Context context) {
            super(context);
            setHorizontalScrollBarEnabled(false);

            tabsContainer = new LinearLayout(context);
            tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
            tabsContainer.setGravity(Gravity.CENTER_VERTICAL);
            addView(tabsContainer);
        }

        public void addTab(Tab tab) {
            // Création de la vue du tab
            LinearLayout tabView = new LinearLayout(getContext());
            tabView.setOrientation(LinearLayout.HORIZONTAL);
            tabView.setGravity(Gravity.CENTER);
            tabView.setPadding(20, 10, 20, 10);

            // Configuration de la hauteur


            // Icone
            if (tab.icon != null) {
                AppCompatImageView iconView = new AppCompatImageView(getContext());
                iconView.setImageDrawable(tab.icon);
                iconView.setPadding(0, 0, 10, 0);
                tabView.addView(iconView);
            }

            // Texte
            TextView textView = new TextView(getContext());
            textView.setText(tab.title);
            textView.setTextColor(foreground.toArgb());
            textView.setTypeface(tabFont.getFont(), tabFont.getStyle());
            textView.setTextSize(tabFont.getSize());
            tabView.addView(textView);

            // Gestion du clic
            final int position = tabs.size() - 1;
            tabView.setOnClickListener(v -> setSelectedIndex(position));

            tab.tabView = tabView;
            tabsContainer.addView(tabView);
        }


        public void removeTabAt(int index) {
            if (index >= 0 && index < tabsContainer.getChildCount()) {
                tabsContainer.removeViewAt(index);
            }
        }

        public void updateTabTitle(int index, String title) {
            if (index >= 0 && index < tabsContainer.getChildCount()) {
                View tabView = tabsContainer.getChildAt(index);
                if (tabView instanceof ViewGroup) {
                    for (int i = 0; i < ((ViewGroup) tabView).getChildCount(); i++) {
                        View child = ((ViewGroup) tabView).getChildAt(i);
                        if (child instanceof TextView) {
                            ((TextView) child).setText(title);
                            break;
                        }
                    }
                }
            }
        }

        public void updateSelection(int position) {
            if (selectedPosition >= 0 && selectedPosition < tabsContainer.getChildCount()) {
                tabsContainer.getChildAt(selectedPosition)
                        .setBackgroundColor(Color.TRANSPARENT.toArgb());
            }

            selectedPosition = position;

            if (selectedPosition >= 0 && selectedPosition < tabsContainer.getChildCount()) {
                tabsContainer.getChildAt(selectedPosition)
                        .setBackgroundColor(selectedColor.toArgb());

                // Scroll pour rendre visible le tab sélectionné
                scrollToTab(position);
            }
        }

        private void scrollToTab(int position) {
            if (position >= 0 && position < tabsContainer.getChildCount()) {
                View tab = tabsContainer.getChildAt(position);
                int scrollX = tab.getLeft() - (getWidth() - tab.getWidth()) / 2;
                smoothScrollTo(scrollX, 0);
            }
        }
    }

    private class TabPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((Tab) object).component;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = tabs.get(position).component;
            container.addView(view);
            return tabs.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((Tab) object).component);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).title;
        }
    }
    public void setBackgroundColor(Color color) {
        this.background = color;
        tabHeader.setBackground(new ColorDrawable(color.toArgb()));
    }
    

    public void setForegroundColor(Color color) {
        this.foreground = color;
        for (Tab tab : tabs) {
            if (tab.tabView instanceof ViewGroup) {
                updateTabForeground(tab.tabView, color);
            }
        }
    }

    private void updateTabForeground(View tabView, Color color) {
        if (tabView instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) tabView;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(color.toArgb());
                }
            }
        }
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
        updateTabSelection();
    }
}