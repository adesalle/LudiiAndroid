package playerAndroid.app.display.dialogs.visual_editor.view.panels.editor.textEditor;


import androidUtils.awt.BorderLayout;
import androidUtils.awt.event.FocusEvent;
import androidUtils.awt.event.FocusListener;
import androidUtils.swing.JPanel;
import androidUtils.swing.JTextArea;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;

public class TextEditor extends JPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6330942893371205110L;
	JTextArea textArea = new JTextArea("");

        public TextEditor()
        {
            super();
            setLayout(new BorderLayout());
            textArea.setText("");
            textArea.addFocusListener(new FocusListener()
            {
                @Override
                public void focusLost(FocusEvent e)
                {
                    textArea.setEditable(true);
                }

                @Override
                public void focusGained(FocusEvent e)
                {
                    textArea.setEditable(false);
                }
            });
            updateLud();
            add(textArea, BorderLayout.CENTER);
        }

        public void updateLud()
        {
            textArea.setText(Handler.toLud());
        }

}
