package androidUtils.swing;


// Basic, have to be modifie
public class JTable {

    private Object[][] data;
    private String[] columnNames;

    public JTable(Object[][] data, String[] columnNamesArray)
    {
        this.data = data;
        this.columnNames = columnNamesArray;
    }
}
