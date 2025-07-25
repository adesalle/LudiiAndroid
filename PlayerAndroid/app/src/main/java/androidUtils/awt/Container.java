package androidUtils.awt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import playerAndroid.app.StartAndroidApp;

public class Container extends Component{
    private final ViewGroup containerView;

    public Container() {
        this(new LinearLayout(StartAndroidApp.getAppContext()));
    }

    public Container(ViewGroup viewGroup) {
        super();
        this.containerView = viewGroup;

    }

    public ViewGroup getView()
    {
        return containerView;
    }


    public Point getLocationOnScreen() {
        int[] location = new int[2];
        containerView.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    public Rectangle getBounds() {
        return new Rectangle(
                0,
                0,
                containerView.getWidth(),
                containerView.getHeight()
        );
    }

    public void add(View comp) {
        containerView.addView(comp);
    }




    public void remove(View comp) {
        containerView.removeView(comp);
    }

    public void removeAll() {
        containerView.removeAllViews();
    }


    public void doLayout() {
        containerView.requestLayout();
    }

    public void validate() {
        containerView.invalidate();
        containerView.requestLayout();
    }

    public void setPreferredSize(Dimension dimension) {

        setSize(dimension);
    }

    private ViewGroup getGroup()
    {
        if (this.getParent() instanceof ViewGroup) {
            return (ViewGroup) this.getParent();

        }
        if(getParent() instanceof Container)
        {

            return  ((Container) getParent()).getView();

        }
        return null;
    }

    public float getParentX() {
        ViewGroup group = getGroup();
        if(group == null) return 0;
        else return group.getX();
    }

    public float getParentY() {
        ViewGroup group = getGroup();
        if(group == null) return 0;
        else return group.getY();
    }

}