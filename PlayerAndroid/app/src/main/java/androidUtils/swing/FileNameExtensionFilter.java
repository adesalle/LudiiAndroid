package androidUtils.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNameExtensionFilter extends FileFilter {
    private final String description;
    private final List<String> extensions;

    public FileNameExtensionFilter(String description, String... extensions) {
        this.description = description;
        this.extensions = new ArrayList<>();
        for (String ext : extensions) {
            this.extensions.add(ext.toLowerCase());
        }
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            String fileExt = name.substring(dotIndex + 1).toLowerCase();
            return extensions.contains(fileExt);
        }
        return false;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder(description);
        sb.append(" (");
        for (int i = 0; i < extensions.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append("*.").append(extensions.get(i));
        }
        sb.append(")");
        return sb.toString();
    }

    public String[] getExtensions() {
        return extensions.toArray(new String[0]);
    }
}