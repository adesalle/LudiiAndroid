package androidUtils.swing;


public class ToolTipManager {

    private static ToolTipManager instance;

    private int delay = 3;

    private int reshowDelay = 3;

    private boolean enabled = false;

    private ToolTipManager(){}

    public static ToolTipManager sharedInstance()
    {
        if(instance == null)
        {
            instance = new ToolTipManager();
        }
        return instance;
    }

    public void setReshowDelay(int delay)
    {
        reshowDelay = delay;
    }

    public void setDismissDelay(int delay)
    {
        this.delay = delay;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }


}
