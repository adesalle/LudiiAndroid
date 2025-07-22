package playerAndroid.app.display.dialogs.visual_editor.view.panels.header;

import android.graphics.drawable.ColorDrawable;

import androidUtils.awt.Font;
import androidUtils.awt.Graphics;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.JButton;
import androidUtils.swing.JOptionPane;
import androidUtils.swing.border.BorderFactory;
import game.Game;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.view.designPalettes.DesignPalette;

public class PlayButton extends JButton
{
    private static final long serialVersionUID = 2525022058118158573L;
    private boolean compilable = true;

    public PlayButton()
    {
        super("Play");
        setIcon(DesignPalette.COMPILABLE_ICON);
        setFont(new Font("Roboto Bold", Font.PLAIN, 12));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setFocusPainted(false);
        setOpaque(true);
        setBorderPainted(false);
        setBackground(DesignPalette.COMPILABLE_COLOR());
        setForeground(DesignPalette.PLAY_BUTTON_FOREGROUND());

        addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!compilable)
                {
                    Handler.markUncompilable();
                    @SuppressWarnings("unchecked")
                    java.util.List<String> errors = (java.util.List<String>) Handler.lastCompile[1];
                    String errorMessage = "";
                    if (errors.isEmpty())
                    {
                        errorMessage = "Could not create \"game\" ludeme from description.";
                    }
                    else
                    {
                        errorMessage = errors.toString();
                        errorMessage = errorMessage.substring(1, errorMessage.length() - 1);
                    }
                    JOptionPane.showMessageDialog(null, errorMessage, "Couldn't compile", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    Object[] output = Handler.compile();
                    if (output[0] != null)
                    {
                        setCompilable();
                        setToolTipText(null);
                        Handler.play((Game) output[0]);
                    }
                    else
                    {
                        @SuppressWarnings("unchecked")
                        java.util.List<String> errors = (java.util.List<String>) output[1];
                        String errorMessage = "";
                        if (errors.isEmpty())
                        {
                            errorMessage = "Could not create \"game\" ludeme from description.";
                        }
                        else
                        {
                            errorMessage = errors.toString();
                            errorMessage = errorMessage.substring(1, errorMessage.length() - 1);
                        }
                        JOptionPane.showMessageDialog(PlayButton.this, errorMessage, "Couldn't compile", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (compilable)
        {
            if (getBackground() != new ColorDrawable(DesignPalette.COMPILABLE_COLOR().toArgb()))
            {
                setBackground(DesignPalette.COMPILABLE_COLOR());
                setForeground(DesignPalette.PLAY_BUTTON_FOREGROUND());
            }
        }
        else
        {
            if (getBackground() != new ColorDrawable(DesignPalette.NOT_COMPILABLE_COLOR().toArgb()))
            {
                setBackground(DesignPalette.NOT_COMPILABLE_COLOR());
                setForeground(DesignPalette.PLAY_BUTTON_FOREGROUND());
            }
        }
    }

    public void setNotCompilable()
    {
        compilable = false;
        setIcon(DesignPalette.NOT_COMPILABLE_ICON);
        setBackground(DesignPalette.NOT_COMPILABLE_COLOR());
    }

    public void setCompilable()
    {
        compilable = true;
        setIcon(DesignPalette.COMPILABLE_ICON);
        setBackground(DesignPalette.COMPILABLE_COLOR());
    }

    public void updateCompilable(Object[] output)
    {
        if (output[0] != null)
        {
            setCompilable();
            setToolTipText(null);
        }
        else
        {
            @SuppressWarnings("unchecked")
            java.util.List<String> errors = (java.util.List<String>) output[1];
            String errorMessage = "";
            if (errors.isEmpty())
                errorMessage = "Could not create \"game\" ludeme from description.";
            else
            {
                errorMessage = errors.toString();
                errorMessage = errorMessage.substring(1, errorMessage.length() - 1);
            }
            setNotCompilable();
            setToolTipText(errorMessage);
        }
    }
}
