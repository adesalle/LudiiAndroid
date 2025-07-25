package playerAndroid.app.display.dialogs.util;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.Vector;


import androidUtils.awt.Color;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.ComboBoxModel;
import androidUtils.swing.JCheckBox;
import androidUtils.swing.JComboBox;
import androidUtils.swing.JLabel;
import androidUtils.swing.JList;
import androidUtils.swing.ListCellRenderer;
import playerAndroid.app.StartAndroidApp;

/**
 * Class for creating a JComboBox with check boxes on the options.
 * @author Matthew.Stephenson
 *
 */
@SuppressWarnings({ "rawtypes" })
public class JComboCheckBox extends JComboBox
{
	private static final long serialVersionUID = 1L;

    public JComboCheckBox() {
          init();
       }

       @SuppressWarnings("unchecked")
    public JComboCheckBox(final JCheckBox[] items) {
          super(items);
          initB();
       }

       @SuppressWarnings("unchecked")
    public JComboCheckBox(final Vector items) {
          super(items);
          initB();
       }

       @SuppressWarnings("unchecked")
    public JComboCheckBox(final ComboBoxModel aModel) {
          super(aModel);
          initB();
       }

       @SuppressWarnings("unchecked")
    private void initB() {
          setRenderer(new ComboBoxRenderer());
          addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                itemSelected();
             }
          });
       }
  
	void itemSelected()
	{
      if (getSelectedItem() instanceof JCheckBox) {
         final JCheckBox jcb = (JCheckBox)getSelectedItem();
         jcb.setSelected(!jcb.isSelected());
      }
   }

    class ComboBoxRenderer<E> implements ListCellRenderer<E> {
      private JLabel label;
       
      public ComboBoxRenderer() {
         setOpaque(true);
      }

        @Override
        public View getListCellRendererComponent(Object list,
                                                 E value,
                                                 int index,
                                                 boolean isSelected,
                                                 boolean hasFocus) {
            TextView textView = new TextView(getContextFrom(list));
            if (value instanceof  String)
                textView.setText(((String)value));

            // Customize based on component type
            if (list instanceof JList) {
                // JList specific styling
                textView.setPadding(20, 20, 20, 20);
            } else if (list instanceof JComboBox) {
                // JComboBox specific styling
                textView.setTextSize(18);
            }

            // Selection styling
            if (isSelected) {
                textView.setBackgroundColor(Color.BLUE.toArgb());
                textView.setTextColor(Color.WHITE.toArgb());
            } else {
                textView.setBackgroundColor(Color.TRANSPARENT.toArgb());
                textView.setTextColor(Color.BLACK.toArgb());
            }

            return textView;
        }

        private Context getContextFrom(Object component) {
            if (component instanceof View) {
                return ((View) component).getContext();
            }
            return StartAndroidApp.getAppContext();
        }

    }


}