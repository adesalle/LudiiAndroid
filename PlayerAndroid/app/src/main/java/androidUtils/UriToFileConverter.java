package androidUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UriToFileConverter {
    public static String readTextFromUri(Context context, Uri uri){
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public static File convert(Context context, Uri uri) {
        if (uri.getScheme().equals("file")) {
            return new File(uri.getPath());
        }
        return null;
    }

    public static String getFileExtensionFromUri(Context context, Uri uri) {
        // 1. Essayer d'obtenir le nom du fichier d'abord
        String fileName = getFileNameFromUri(context, uri);

        if (fileName != null) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot != -1 && lastDot < fileName.length() - 1) {
                return fileName.substring(lastDot + 1).toLowerCase();
            }
        }

        return null;
    }

    private static String getFileNameFromUri(Context context, Uri uri) {
        String result = null;

        // Si c'est un URI content://
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(
                    uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }

        // Si non ou si la requête a échoué, essayer avec le path
        if (result == null) {
            result = uri.getPath();
            if (result != null) {
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }

        return result;
    }
}