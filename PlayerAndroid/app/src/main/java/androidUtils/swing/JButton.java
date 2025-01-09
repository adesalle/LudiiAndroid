package androidUtils.swing;

import android.annotation.SuppressLint;
import android.view.View;

import androidUtils.swing.action.ActionEvent;
import androidUtils.swing.action.ActionListener;

@SuppressLint("ViewConstructor")
public class JButton extends Component{
    public JButton(String name) {
        super(name);
    }


    public void addActionListener(ActionListener actionListener) {
        setOnClickListener(v -> actionListener.actionPerformed(new ActionEvent()));
    }
}
