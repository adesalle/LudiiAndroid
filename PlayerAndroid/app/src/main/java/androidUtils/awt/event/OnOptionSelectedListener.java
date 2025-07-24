package androidUtils.awt.event;

public interface OnOptionSelectedListener {

    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;

    void onOptionSelected(int selectedOption);
}