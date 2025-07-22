package androidUtils.swing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import java.io.File;
import java.io.FileDescriptor;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import androidUtils.JSONObject;
import androidUtils.awt.Dimension;
import playerAndroid.app.JFrameListener;
import playerAndroid.app.StartAndroidApp;

public class JFileChooser {
    public static final int APPROVE_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    public static final int ERROR_OPTION = -1;
    public static final int FILES_ONLY = 0;
    public static final int DIRECTORIES_ONLY = 1;
    public static final int FILES_AND_DIRECTORIES = 2;

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

    public JFileChooser() {

    }

    public JFileChooser(String initialPath) {
        if (initialPath != null) {
            this.selectedFileUri = Uri.parse(initialPath);
        }
    }

    public int showOpenDialog(JFrame frame) {
        Intent intent;

        if (fileSelectionMode == DIRECTORIES_ONLY) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(fileFilter != null ? getMimeTypeFromFilter() : "*/*");
        }

        intent.putExtra(Intent.EXTRA_TITLE, dialogTitle);
        filePickerLauncher.launch(intent);
        return returnValue;
    }

    public int showSaveDialog(JFrame frame) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(fileFilter != null ? getMimeTypeFromFilter() : "*/*");
        intent.putExtra(Intent.EXTRA_TITLE, dialogTitle);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_SUBJECT, defaultFileName);
        fileSaverLauncher.launch(intent);
        return returnValue;
    }

    private String getMimeTypeFromFilter() {
        if (fileFilter instanceof FileNameExtensionFilter) {
            String[] extensions = ((FileNameExtensionFilter)fileFilter).getExtensions();
            if (extensions.length > 0) {
                // Simplification: on prend juste le premier type MIME correspondant
                return getMimeTypeFromExtension(extensions[0]);
            }
        }
        return "*/*";
    }

    private String getMimeTypeFromExtension(String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "txt":
                return "text/plain";
            case "pdf":
                return "application/pdf";
            default:
                return "*/*";
        }
    }

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
    }

    public void setSelectedFile(File file) {
        if (file != null) {
            this.selectedFileUri = Uri.fromFile(file);
        }
    }

    public File getSelectedFile() {
        if (selectedFileUri == null) {
            return null;
        }

        Context context = StartAndroidApp.getAppContext();
        try (ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(selectedFileUri, "r")) {
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                return new File(fd.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OutputStream getSelectedFileOutputStream() throws FileNotFoundException {
        if (selectedFileUri == null) {
            throw new FileNotFoundException("No file selected");
        }
        return activity.getContentResolver().openOutputStream(selectedFileUri);
    }

    public void setFileFilter(FileFilter filter) {
        this.fileFilter = filter;
        // Note: Android's file picker doesn't support custom file filters
        // This is kept for API compatibility
    }

    public void setSelectedFileUri(Uri uri) {
        this.selectedFileUri = uri;
    }

    public void setPreferredSize(Dimension dimension) {
        // Not applicable on Android
    }

    public void setDefaultFileName(String name) {
        this.defaultFileName = name;
    }

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            activity.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if (uri != null) {
                                selectedFileUri = uri;
                                returnValue = APPROVE_OPTION;
                                if (fileSelectedListener != null) {
                                    fileSelectedListener.onFileSelected(uri);
                                }
                            }
                        } else {
                            returnValue = CANCEL_OPTION;
                        }
                    }
            );

    private final ActivityResultLauncher<Intent> fileSaverLauncher =
            activity.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if (uri != null) {
                                selectedFileUri = uri;
                                returnValue = APPROVE_OPTION;
                                if (saveFileListener != null) {
                                    saveFileListener.onSaveFileSelected(uri);
                                }
                            }
                        } else {
                            returnValue = CANCEL_OPTION;
                        }
                    }
            );

    public Map<String, Action> getActionMap() {
        return actionMap;
    }

    public void putAction(String key, Action action) {
        actionMap.put(key, action);
    }

    public Action getAction(String key) {
        return actionMap.get(key);
    }

    public void setFileSelectionMode(Object directoriesOnly) {
    }

    public void addChoosableFileFilter(FileNameExtensionFilter fileNameExtensionFilter) {

    }

    public interface FileSelectedListener {
        void onFileSelected(Uri fileUri);
    }

    public interface SaveFileListener {
        void onSaveFileSelected(Uri fileUri);
    }

    public void setFileSelectedListener(FileSelectedListener listener) {
        this.fileSelectedListener = listener;
    }

    public void setSaveFileListener(SaveFileListener listener) {
        this.saveFileListener = listener;
    }
}