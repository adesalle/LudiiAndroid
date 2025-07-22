package androidUtils.awt.event;

public abstract class ActionListener implements Runnable{
    @Override
    public void run() {
        actionPerformed(new ActionEvent());
    }

    public abstract void actionPerformed(ActionEvent event);
}
