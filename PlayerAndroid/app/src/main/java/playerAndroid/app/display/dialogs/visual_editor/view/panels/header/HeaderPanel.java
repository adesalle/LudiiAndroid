package playerAndroid.app.display.dialogs.visual_editor.view.panels.header;

import android.graphics.drawable.ColorDrawable;

import androidUtils.awt.BorderLayout;
import androidUtils.awt.Dimension;
import androidUtils.awt.Graphics;
import androidUtils.swing.JPanel;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.view.VisualEditorPanel;
import playerAndroid.app.display.dialogs.visual_editor.view.designPalettes.DesignPalette;

public class HeaderPanel extends JPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3999066959490436008L;
	private final EditorPickerPanel editorPickerPanel;
    private final ToolsPanel toolsPanel;

    public HeaderPanel(VisualEditorPanel visualEditorPanel)
    {
        setLayout(new BorderLayout());
        editorPickerPanel = new EditorPickerPanel(visualEditorPanel);
        add(editorPickerPanel, BorderLayout.LINE_START);
        toolsPanel = new ToolsPanel();
        Handler.toolsPanel = toolsPanel;
        add(toolsPanel, BorderLayout.LINE_END);
        setOpaque(true);
        setBackground(DesignPalette.BACKGROUND_HEADER_PANEL());

        int preferredHeight = getPreferredSize().height;
        setPreferredSize(new Dimension(getPreferredSize().width, preferredHeight+20));
    }

    @Override
	public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(getBackground() != new ColorDrawable(DesignPalette.BACKGROUND_HEADER_PANEL().toArgb()))
        {
            setBackground(DesignPalette.BACKGROUND_HEADER_PANEL());
            editorPickerPanel.repaint();
            toolsPanel.repaint();
        }
    }

}
