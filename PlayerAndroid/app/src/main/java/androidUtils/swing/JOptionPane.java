package androidUtils.swing;

import android.content.Context;
import android.app.AlertDialog;
import android.widget.EditText;

import playerAndroid.app.AndroidApp;


public class JOptionPane {

    public static final Object PLAIN_MESSAGE = 1;

    public static final int YES_NO_CANCEL_OPTION = 0;
    public static final int QUESTION_MESSAGE = 2;

    public static void showMessageDialog(Object nullObject, String message) {

        Context context = AndroidApp.getAppContext();
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showMessageDialog(Object o, String message, String title, Object messageType, Object icon) {
        Context context = AndroidApp.getAppContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    public static int showOptionDialog(String message, String title, String queryType, int optionType, int messageType, Object icon, Object[] options, Integer initialValue)
    {
        Context context = AndroidApp.getAppContext();
        final int[] selectedOption = {initialValue == null ? -1 : initialValue};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message);

        if (options != null) {
            builder.setItems((CharSequence[]) options, (dialog, which) -> selectedOption[0] = which);
        }
        builder.create().show();

        return selectedOption[0];
    }

    public static String showInputDialog(String message) {
        Context context = AndroidApp.getAppContext();

        EditText inputField = new EditText(context);

        final String[] result = new String[1];

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Input")
                .setMessage(message)
                .setView(inputField)
                .setPositiveButton("OK", (dialogInterface, i) -> result[0] = inputField.getText().toString())
                .setNegativeButton("Cancel", (dialogInterface, i) -> result[0] = null)
                .create();

        dialog.show();

        try {
            while (result[0] == null && dialog.isShowing()) {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }
}
