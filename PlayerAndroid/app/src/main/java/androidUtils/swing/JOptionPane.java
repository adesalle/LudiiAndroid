package androidUtils.swing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import playerAndroid.app.JFrameListener;
import playerAndroid.app.StartAndroidApp;

public class JOptionPane {
    // Message type constants
    public static final int PLAIN_MESSAGE = -1;
    public static final int ERROR_MESSAGE = 0;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    public static final int QUESTION_MESSAGE = 3;

    // Option constants
    public static final int DEFAULT_OPTION = -1;
    public static final int YES_NO_OPTION = 0;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final int OK_CANCEL_OPTION = 2;

    // Result constants
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int OK_OPTION = 0;
    public static final int CLOSED_OPTION = -1;

    private Integer value;
    private JDialog dialog;

    public JOptionPane(Object contentPane, int messageType, int optionType, Object message, Object title, Object icon) {
        // Constructor implementation if needed
    }

    public static void showMessageDialog(Object parentComponent, String message) {
        showMessageDialog(parentComponent, message, "Message", PLAIN_MESSAGE);
    }

    public static void showMessageDialog(Object parentComponent, String message,
                                         String title, int messageType) {
        Context context = StartAndroidApp.getAppContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null);

        setMessageTypeIcon(builder, messageType);
        builder.show();
    }
    public static void showMessageDialog(JFrame frame, String message, String title, int messageType, ImageIcon icon) {
        Context context = StartAndroidApp.getAppContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(icon.getDrawable());

        setMessageTypeIcon(builder, messageType);
        builder.show();
    }

    public static int showConfirmDialog(Object parentComponent, Object message,String title, int option) {
        return showConfirmDialog(parentComponent, message, title,
                option, QUESTION_MESSAGE, null);
    }


    public static int showConfirmDialog(Object parentComponent, String message,
                                        String title, int optionType,
                                        int messageType, Object icon) {
        Context context = StartAndroidApp.getAppContext();
        final int[] result = {CLOSED_OPTION};
        final Object lock = new Object();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message.toString());

        setMessageTypeIcon(builder, messageType);

        switch (optionType) {
            case YES_NO_OPTION:
                builder.setPositiveButton("Yes", (d, w) -> {
                            result[0] = YES_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNegativeButton("No", (d, w) -> {
                            result[0] = NO_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        });
                break;
            case YES_NO_CANCEL_OPTION:
                builder.setPositiveButton("Yes", (d, w) -> {
                            result[0] = YES_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNegativeButton("No", (d, w) -> {
                            result[0] = NO_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNeutralButton("Cancel", (d, w) -> {
                            result[0] = CANCEL_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        });
                break;
            case OK_CANCEL_OPTION:
                builder.setPositiveButton("OK", (d, w) -> {
                            result[0] = OK_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNegativeButton("Cancel", (d, w) -> {
                            result[0] = CANCEL_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        });
                break;
            default:
                builder.setPositiveButton("OK", (d, w) -> {
                    result[0] = OK_OPTION;
                    synchronized (lock) { lock.notifyAll(); }
                });
        }

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(d -> {
            synchronized (lock) { lock.notifyAll(); }
        });

        // Pour les dialogues modaux
        if (parentComponent instanceof JFrame) {
            dialog.setOwnerActivity(StartAndroidApp.startAndroidApp());
        }

        dialog.show();

        // Bloque le thread courant jusqu'à ce que l'utilisateur fasse un choix
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return CLOSED_OPTION;
            }
        }

        return result[0];
    }


    public static int showConfirmDialog(Object parentComponent, Object message,
                                        String title, int optionType,
                                        int messageType, Object icon) {
        Context context = StartAndroidApp.getAppContext();
        final int[] result = {CLOSED_OPTION};
        final Object lock = new Object();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message.toString());

        setMessageTypeIcon(builder, messageType);

        switch (optionType) {
            case YES_NO_OPTION:
                builder.setPositiveButton("Yes", (d, w) -> {
                            result[0] = YES_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNegativeButton("No", (d, w) -> {
                            result[0] = NO_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        });
                break;
            case YES_NO_CANCEL_OPTION:
                builder.setPositiveButton("Yes", (d, w) -> {
                            result[0] = YES_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNegativeButton("No", (d, w) -> {
                            result[0] = NO_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNeutralButton("Cancel", (d, w) -> {
                            result[0] = CANCEL_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        });
                break;
            case OK_CANCEL_OPTION:
                builder.setPositiveButton("OK", (d, w) -> {
                            result[0] = OK_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        })
                        .setNegativeButton("Cancel", (d, w) -> {
                            result[0] = CANCEL_OPTION;
                            synchronized (lock) { lock.notifyAll(); }
                        });
                break;
            default:
                builder.setPositiveButton("OK", (d, w) -> {
                    result[0] = OK_OPTION;
                    synchronized (lock) { lock.notifyAll(); }
                });
        }

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(d -> {
            synchronized (lock) { lock.notifyAll(); }
        });

        dialog.show();

        // Blocking wait to simulate Swing's modal behavior
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return CLOSED_OPTION;
            }
        }

        return result[0];
    }

    public static String showInputDialog(String message) {
        return showInputDialog(null, message);
    }

    public static String showInputDialog(Object parentComponent, String message) {
        Context context = StartAndroidApp.getAppContext();
        final EditText input = new EditText(context);
        final String[] result = {null};
        final Object lock = new Object();

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Input")
                .setMessage(message)
                .setView(input)
                .setPositiveButton("OK", (d, w) -> {
                    result[0] = input.getText().toString();
                    synchronized (lock) { lock.notifyAll(); }
                })
                .setNegativeButton("Cancel", (d, w) -> {
                    synchronized (lock) { lock.notifyAll(); }
                })
                .create();

        dialog.setOnDismissListener(d -> {
            synchronized (lock) { lock.notifyAll(); }
        });

        dialog.show();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return result[0];
    }

    private static void setMessageTypeIcon(AlertDialog.Builder builder, int messageType) {
        // You should replace these with your actual icon resources
        switch (messageType) {
            case ERROR_MESSAGE:
                // builder.setIcon(android.R.drawable.ic_dialog_alert);
                break;
            case INFORMATION_MESSAGE:
                // builder.setIcon(android.R.drawable.ic_dialog_info);
                break;
            case WARNING_MESSAGE:
                // builder.setIcon(android.R.drawable.ic_dialog_warning);
                break;
            case QUESTION_MESSAGE:
                // builder.setIcon(android.R.drawable.ic_dialog_question);
                break;
        }
    }

    public static int showOptionDialog(Object parentComponent, Object message,
                                       String title, int optionType, int messageType,
                                       Object icon, Object[] options, Object initialValue) {
        Context context = StartAndroidApp.getAppContext();
        final int[] selectedOption = {initialValue instanceof Integer ? (Integer) initialValue : -1};
        final Object lock = new Object();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message.toString());

        if (options != null && options.length > 0) {
            CharSequence[] items = new CharSequence[options.length];
            for (int i = 0; i < options.length; i++) {
                items[i] = options[i].toString();
            }

            builder.setSingleChoiceItems(items, selectedOption[0], (dialog, which) -> {
                selectedOption[0] = which;
            });

            builder.setPositiveButton("OK", (d, w) -> {
                synchronized (lock) { lock.notifyAll(); }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(d -> {
            synchronized (lock) { lock.notifyAll(); }
        });

        dialog.show();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return CLOSED_OPTION;
            }
        }

        return selectedOption[0];
    }



    public JDialog createDialog(String title) {
        Context context = StartAndroidApp.getAppContext();
        this.dialog = new JDialog();
        dialog.setTitle(title);
        return this.dialog;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}