package androidUtils.swing.text;

/**
 * Represents a position within a document model.
 * Based on Swing's Position interface but adapted for Android.
 */
public interface Position {

    /**
     * Gets the current offset within the document.
     * @return the offset >= 0
     */
    int getOffset();

    /**
     * Bias enumeration for indicating a position bias.
     */
    enum Bias {
        /**
         * Indicates a bias toward the previous character.
         */
        Backward,

        /**
         * Indicates a bias toward the next character.
         */
        Forward
    }

    /**
     * A simple implementation of the Position interface.
     */
    class DefaultPosition implements MutablePosition {
        private int offset;

        public DefaultPosition(int offset) {
            if (offset < 0) {
                throw new IllegalArgumentException("Offset must be >= 0");
            }
            this.offset = offset;
        }

        @Override
        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        @Override
        public String toString() {
            return "Position[" + offset + "]";
        }
    }

    /**
     * A mutable implementation of Position that tracks changes in the document.
     */
    interface MutablePosition extends Position {
        /**
         * Updates the offset of this position.
         * @param offset the new offset >= 0
         */
        void setOffset(int offset);
    }

    /**
     * Factory method to create a new Position instance.
     * @param offset the offset for the position
     * @return a new Position instance
     */
    static Position create(int offset) {
        return new DefaultPosition(offset);
    }

    /**
     * Factory method to create a new mutable Position instance.
     * @param offset the initial offset
     * @return a new MutablePosition instance
     */
    static MutablePosition createMutable(int offset) {
        return new DefaultPosition(offset);
    }
}