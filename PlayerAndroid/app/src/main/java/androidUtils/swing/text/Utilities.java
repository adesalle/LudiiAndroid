package androidUtils.swing.text;

public class Utilities
{
    /**
     * Finds the start of the word at the given position.
     */
    public static int getWordStart(JTextComponent tc, int pos) throws BadLocationException
    {
        String text = String.valueOf(tc.getText());
        if (pos < 0 || pos > text.length())
            throw new BadLocationException("Invalid position", pos);

        // Si on est déjà au début
        if (pos == 0)
            return 0;

        // Trouver le premier caractère non-word avant la position
        int start = pos;
        while (start > 0 && isWordChar(text.charAt(start - 1))) {
            start--;
        }

        return start;
    }

    /**
     * Finds the end of the word at the given position.
     */
    public static int getWordEnd(JTextComponent tc, int pos) throws BadLocationException
    {
        String text = String.valueOf(tc.getText());
        if (pos < 0 || pos > text.length())
            throw new BadLocationException("Invalid position", pos);

        // Si on est déjà à la fin
        if (pos == text.length())
            return pos;

        // Trouver le premier caractère non-word après la position
        int end = pos;
        while (end < text.length() && isWordChar(text.charAt(end))) {
            end++;
        }

        return end;
    }

    /**
     * Finds the start of the row at the given position.
     */
    public static int getRowStart(JTextComponent tc, int pos) throws BadLocationException
    {
        String text = String.valueOf(tc.getText());
        if (pos < 0 || pos > text.length())
            throw new BadLocationException("Invalid position", pos);

        // Trouver le dernier saut de ligne avant la position
        int start = text.lastIndexOf('\n', pos - 1);

        // Si pas trouvé, c'est le début du texte
        if (start == -1)
            return 0;

        return start + 1; // +1 pour passer après le \n
    }

    /**
     * Checks if character is part of a word.
     */
    private static boolean isWordChar(char c)
    {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    /**
     * Gets the text between two positions.
     */
    public static String getText(JTextComponent tc, int start, int length)
            throws BadLocationException
    {
        String text = String.valueOf(tc.getText());
        if (start < 0 || start + length > text.length())
            throw new BadLocationException("Invalid range", start);

        return text.substring(start, start + length);
    }
}