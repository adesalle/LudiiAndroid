package androidUtils.swing.action;

public abstract class ActionListener implements Runnable{
    @Override
    public void run() {
        actionPerformed(new ActionEvent());
    }

    public abstract void actionPerformed(ActionEvent event);
}
