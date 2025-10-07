package androidUtils.swing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import androidUtils.UriToFileConverter;
import androidUtils.awt.Dimension;
import playerAndroid.app.StartAndroidApp;

public class JFileChooser {
    // Constantes
    public static final int APPROVE_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    public static final int ERROR_OPTION = -1;
    public static final int FILES_ONLY = 0;
    public static final int DIRECTORIES_ONLY = 1;
    public static final int FILES_AND_DIRECTORIES = 2;

    // Variables d'instance
    private final FragmentActivity activity = StartAndroidApp.startAndroidApp();
    private FileSelectedListener fileSelectedListener;
    private SaveFileListener saveFileListener;
    private Uri selectedFileUri;
    private String dialogTitle = "Select File";
    private String defaultFileName = "file.txt";
    private FileFilter fileFilter;
    private int returnValue = CANCEL_OPTION;
    private int fileSelectionMode = FILES_ONLY;
    private Map<String, Action> actionMap = new HashMap<>();
    private JDialog dialog;
    private Dimension preferredSize;

    // Constructeurs
    public JFileChooser() {
        initDialog();
    }

    public JFileChooser(String initialPath) {
        if (initialPath != null) {
            this.selectedFileUri = Uri.parse(initialPath);
        }
        initDialog();
    }

    // Initialisation du JDialog
    private void initDialog() {
        dialog = new JDialog();
        dialog.setTitle(dialogTitle);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        if (preferredSize != null) {
            dialog.setSize(preferredSize);
        }
    }

    // Méthodes principales
    public int showOpenDialog(JFrame frame) {
        Intent intent = createOpenIntent();
        filePickerLauncher.launch(intent);
        return returnValue;
    }

    public int showSaveDialog(JFrame frame) {
        Intent intent = createSaveIntent();
        fileSaverLauncher.launch(intent);
        return returnValue;
    }

    private Intent createOpenIntent() {
        Intent intent;
        if (fileSelectionMode == DIRECTORIES_ONLY) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(getMimeTypeFromFilter());
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        intent.putExtra(Intent.EXTRA_TITLE, dialogTitle);
        return intent;
    }

    private Intent createSaveIntent() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(getMimeTypeFromFilter());
        intent.putExtra(Intent.EXTRA_TITLE, dialogTitle);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_SUBJECT, defaultFileName);
        return intent;
    }

    private void showWaitingDialog() {
        Context context = StartAndroidApp.getAppContext();
        JPanel layout = new JPanel();
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(50, 50, 50, 50);

        TextView textView = new TextView(context);
        textView.setText("Waiting for file selection...");
        textView.setGravity(Gravity.CENTER);

        layout.addView(textView);
        dialog.setContentPane(layout);
        dialog.show();
    }

    // Gestion des résultats
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            activity.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        handlePickerResult(result.getResultCode(), result.getData());
                    }
            );

    private final ActivityResultLauncher<Intent> fileSaverLauncher =
            activity.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        handleSaverResult(result.getResultCode(), result.getData());
                    }
            );

    private void handlePickerResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                selectedFileUri = uri;
                returnValue = APPROVE_OPTION;
                if (fileSelectedListener != null) {
                    fileSelectedListener.onFileSelected(uri);
                }
                return;
            }
        }
        returnValue = CANCEL_OPTION;
    }

    private void handleSaverResult(int resultCode, Intent data) {
        dialog.dismiss();
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                selectedFileUri = uri;
                returnValue = APPROVE_OPTION;
                if (saveFileListener != null) {
                    saveFileListener.onSaveFileSelected(uri);
                }
                return;
            }
        }
        returnValue = CANCEL_OPTION;
    }

    // Gestion des types MIME
    private String getMimeTypeFromFilter() {
        if (fileFilter instanceof FileNameExtensionFilter) {
            String[] extensions = ((FileNameExtensionFilter)fileFilter).getExtensions();
            if (extensions.length > 0) {
                return getMimeTypeFromExtension(extensions[0]);
            }
        }
        return "*/*";
    }

    private String getMimeTypeFromExtension(String extension) {
        switch (extension.toLowerCase()) {
            case "jpg": case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "gif": return "image/gif";
            case "txt": return "text/plain";
            case "pdf": return "application/pdf";
            case "mp3": return "audio/mpeg";
            case "mp4": return "video/mp4";
            default: return "*/*";
        }
    }

    // Getters et Setters
    public void setFileSelectionMode(int mode) {
        if (mode >= FILES_ONLY && mode <= FILES_AND_DIRECTORIES) {
            this.fileSelectionMode = mode;
        } else {
            throw new IllegalArgumentException("Invalid file selection mode");
        }
    }

    public int getFileSelectionMode() {
        return fileSelectionMode;
    }

    public void setDialogTitle(String title) {
        this.dialogTitle = title;
        if (dialog != null) {
            dialog.setTitle(title);
        }
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setSelectedFile(File file) {
        this.selectedFileUri = file != null ? Uri.fromFile(file) : null;
    }

    public File getSelectedFile() {
        if (selectedFileUri == null) return null;
        Context context = StartAndroidApp.getAppContext();
        try {
            return UriToFileConverter.convert(context, selectedFileUri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OutputStream getSelectedFileOutputStream() throws FileNotFoundException {
        if (selectedFileUri == null) {
            throw new FileNotFoundException("No file selected");
        }
        return activity.getContentResolver().openOutputStream(selectedFileUri);
    }

    public void setFileFilter(FileFilter filter) {
        this.fileFilter = filter;
    }

    public FileFilter getFileFilter() {
        return fileFilter;
    }

    public void addChoosableFileFilter(FileFilter filter) {
        // Implémentation simplifiée - pourrait utiliser une liste de filtres
        this.fileFilter = filter;
    }

    public void setSelectedFileUri(Uri uri) {
        this.selectedFileUri = uri;
    }

    public Uri getSelectedFileUri() {
        return selectedFileUri;
    }

    public void setPreferredSize(Dimension dimension) {
        this.preferredSize = dimension;
        if (dialog != null && dimension != null) {
            dialog.setSize(dimension);
        }
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public void setDefaultFileName(String name) {
        this.defaultFileName = name;
    }

    public String getDefaultFileName() {
        return defaultFileName;
    }

    // Gestion des actions
    public Map<String, Action> getActionMap() {
        return actionMap;
    }

    public void putAction(String key, Action action) {
        actionMap.put(key, action);
    }

    public Action getAction(String key) {
        return actionMap.get(key);
    }

    // Listeners
    public void setFileSelectedListener(FileSelectedListener listener) {
        this.fileSelectedListener = listener;
    }

    public void setSaveFileListener(SaveFileListener listener) {
        this.saveFileListener = listener;
    }

    // Interfaces
    public interface FileSelectedListener {
        void onFileSelected(Uri fileUri);
    }

    public interface SaveFileListener {
        void onSaveFileSelected(Uri fileUri);
    }

}