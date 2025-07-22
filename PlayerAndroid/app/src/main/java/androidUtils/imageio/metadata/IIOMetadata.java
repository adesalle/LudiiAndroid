package androidUtils.imageio.metadata;

import org.w3c.dom.Node;

public class IIOMetadata {
    private Node root;
    private final String nativeFormatName;
    private final boolean isReadOnly;

    // Formats de métadonnées courants pour les images
    public static final String STANDARD_METADATA_FORMAT = "javax_imageio_1.0";
    public static final String NATIVE_METADATA_FORMAT = "native_image_metadata";

    public IIOMetadata() {
        this(false, NATIVE_METADATA_FORMAT, null);
    }

    public IIOMetadata(boolean isReadOnly, String nativeFormatName, Node root) {
        this.isReadOnly = isReadOnly;
        this.nativeFormatName = nativeFormatName != null ? nativeFormatName : NATIVE_METADATA_FORMAT;
        this.root = root;
    }

    public IIOMetadata(Node root) {
        this(false, NATIVE_METADATA_FORMAT, root);
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public Node getAsTree(String formatName) {
        if (formatName == null) {
            throw new IllegalArgumentException("formatName cannot be null");
        }

        if (formatName.equals(nativeFormatName) ||
                formatName.equals(STANDARD_METADATA_FORMAT)) {
            return root;
        }
        throw new IllegalArgumentException("Unsupported format: " + formatName);
    }

    public void setFromTree(String formatName, Node root) {
        if (isReadOnly) {
            throw new IllegalStateException("Metadata is read-only");
        }
        if (formatName == null) {
            throw new IllegalArgumentException("formatName cannot be null");
        }
        if (!formatName.equals(nativeFormatName) &&
                !formatName.equals(STANDARD_METADATA_FORMAT)) {
            throw new IllegalArgumentException("Unsupported format: " + formatName);
        }
        this.root = root;
    }

    public void mergeTree(String formatName, Node root) {
        if (isReadOnly) {
            throw new IllegalStateException("Metadata is read-only");
        }
        // Implémentation simplifiée - remplace complètement l'arbre existant
        setFromTree(formatName, root);
    }

    public void reset() {
        if (isReadOnly) {
            throw new IllegalStateException("Metadata is read-only");
        }
        this.root = null;
    }

    public String getNativeMetadataFormatName() {
        return nativeFormatName;
    }

    public String[] getMetadataFormatNames() {
        return new String[] { nativeFormatName, STANDARD_METADATA_FORMAT };
    }

    public String[] getExtraMetadataFormatNames() {
        return new String[0];
    }
}