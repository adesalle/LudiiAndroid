package androidUtils.swing;

import androidUtils.awt.BorderLayout;
import androidUtils.awt.Rectangle;
import androidUtils.awt.event.WindowAdapter;
import androidUtils.awt.event.WindowEvent;
import playerAndroid.app.AndroidApp;
import playerAndroid.app.JFrameListener;
import playerAndroid.app.StartAndroidApp;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JOptionPane extends ConstraintLayout {
    // Constantes pour les types de message
    public static final int PLAIN_MESSAGE = -1;
    public static final int ERROR_MESSAGE = 0;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    public static final int QUESTION_MESSAGE = 3;

    // Constantes pour les types d'options
    public static final int DEFAULT_OPTION = -1;
    public static final int YES_NO_OPTION = 0;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final int OK_CANCEL_OPTION = 2;

    // Constantes pour les valeurs de retour
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int OK_OPTION = 0;
    public static final int CLOSED_OPTION = -1;


    // Propriétés
    private Object message;
    private int messageType = INFORMATION_MESSAGE;
    private int optionType = DEFAULT_OPTION;
    private Object[] options;
    private Object initialValue;
    private Object value;
    private Icon icon;
    private Object[] selectionValues;
    private Object inputValue;
    private Object initialSelectionValue;
    private boolean wantsInput = false;



    // Constructeurs
    public JOptionPane() {
        this("Message");
    }

    public JOptionPane(Object message) {
        this(message, PLAIN_MESSAGE);
    }

    public JOptionPane(Object message, int messageType) {
        this(message, messageType, DEFAULT_OPTION);
    }

    public JOptionPane(Object message, int messageType, int optionType) {
        this(message, messageType, optionType, null);
    }

    public JOptionPane(Object message, int messageType, int optionType, Icon icon) {
        this(message, messageType, optionType, icon, null);
    }

    public JOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options) {
        this(message, messageType, optionType, icon, options, null);
    }

    public JOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        super(StartAndroidApp.getAppContext());

        this.message = message;
        this.messageType = messageType;
        this.optionType = optionType;
        this.icon = icon;
        this.options = options;
        this.initialValue = initialValue;
        this.value = UNINITIALIZED_VALUE;
    }

    // Méthodes statiques pour afficher des dialogues
    public static void showMessageDialog(View parentComponent, Object message){
        showMessageDialog(parentComponent, message, "Message", INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(View parentComponent, Object message, String title, int messageType){
        showMessageDialog(parentComponent, message, title, messageType, null);
    }

    public static void showMessageDialog(View parentComponent, Object message, String title, int messageType, Icon icon){
        JOptionPane pane = new JOptionPane(message, messageType, DEFAULT_OPTION, icon);
        pane.createDialog(parentComponent, title).show();
    }

    public static int showConfirmDialog(View parentComponent, Object message){
        return showConfirmDialog(parentComponent, message, "Select an Option", YES_NO_CANCEL_OPTION);
    }

    public static int showConfirmDialog(View parentComponent, Object message, String title, int optionType){
        return showConfirmDialog(parentComponent, message, title, optionType, QUESTION_MESSAGE);
    }

    public static int showConfirmDialog(View parentComponent, Object message, String title, int optionType, int messageType){
        return showConfirmDialog(parentComponent, message, title, optionType, messageType, null);
    }

    public static int showConfirmDialog(View parentComponent, Object message, String title, int optionType, int messageType, Icon icon){
        JOptionPane pane = new JOptionPane(message, messageType, optionType, icon);
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.show();
        Object value = pane.getValue();

        if (value == null) {
            return CLOSED_OPTION;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return CLOSED_OPTION;
    }

    public static String showInputDialog(View parentComponent, Object message) {
        return showInputDialog(parentComponent, message, "Input", QUESTION_MESSAGE);
    }

    public static String showInputDialog(Object message) {
        return showInputDialog(null, message, "Input", QUESTION_MESSAGE);
    }


    public static String showInputDialog(View parentComponent, Object message, String title, int messageType) {
        JOptionPane pane = new JOptionPane(message, messageType, OK_CANCEL_OPTION);
        pane.setWantsInput(true);
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.show();
        Object value = pane.getInputValue();
        return (value != null) ? value.toString() : null;
    }

    public static Object showInputDialog(View frame, String aiClasses, String s, int questionMessage, ImageIcon icon, String[] choices, String choice) {
        JOptionPane pane = new JOptionPane(aiClasses, questionMessage, OK_CANCEL_OPTION, icon, choices, choice);
        pane.setWantsInput(true);
        JDialog dialog = pane.createDialog(frame, s);
        dialog.setVisible(true);

        Object value = pane.getInputValue();
        if (value == JOptionPane.UNINITIALIZED_VALUE) {
            return null;
        }
        return value;
    }

    public static int showOptionDialog(View parentComponent,
                                       Object message, String title, int optionType, int messageType,
                                       Icon icon, Object[] options, Object initialValue) {

        // Création du JOptionPane avec les paramètres fournis
        JOptionPane pane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);

        // Création de la boîte de dialogue
        JDialog dialog = pane.createDialog(parentComponent, title);

        // Affichage de la boîte de dialogue
        dialog.show();

        // Récupération de la valeur sélectionnée
        Object selectedValue = pane.getValue();

        // Traitement de la valeur de retour
        if (selectedValue == null) {
            return CLOSED_OPTION;
        }

        // Si pas d'options personnalisées, retourne directement la valeur
        if (options == null) {
            if (selectedValue instanceof Integer) {
                return (Integer) selectedValue;
            }
            return CLOSED_OPTION;
        }

        // Recherche l'index de l'option sélectionnée
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selectedValue)) {
                return i;
            }
        }

        return CLOSED_OPTION;
    }



    // Méthodes d'instance
    public JDialog createDialog(View parentComponent, String title){
        JDialog dialog = new JDialog((ViewGroup) parentComponent);


        dialog.setTitle(title);
        
        setupButtons(dialog);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initDialog(dialog, -1, parentComponent);
        return dialog;
    }
    public JDialog createDialog(String title){
        JDialog dialog = new JDialog();


        dialog.setTitle(title);

        setupButtons(dialog);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initDialog(dialog, -1, null);
        return dialog;
    }


    private void initDialog(final JDialog dialog, int style, View parentComponent) {
        // Android n'a pas de concept de ComponentOrientation comme Swing
        // On utilise simplement la disposition par défaut

        // Configuration du contenu

        //setLayout(new BorderLayout());
        Rectangle bounds = new Rectangle(AndroidApp.frame().getX(), AndroidApp.frame().getY(),
                AndroidApp.frame().getWidth() * 0.75f, AndroidApp.frame().getHeight() * .75f);
        dialog.setLocation(bounds.getLocation());
        if (bounds.width > 0 && bounds.height > 0)
        {
            dialog.setSize(bounds.width, bounds.height);
        }


        View messageView = createMessageView();


        JScrollPane scrollView = new JScrollPane(messageView);

        dialog.getContentPane().addView(scrollView);
        dialog.setResizable(false);

        //dialog.pack();

        if (parentComponent != null) {
            dialog.setLocationRelativeTo(parentComponent);
        }

        // Gestion des événements
        final PropertyChangeListener listener = event -> {
            if (dialog.isShowing() && event.getSource() == this &&
                    event.getPropertyName().equals("VALUE_PROPERTY") &&
                    event.getNewValue() != null &&
                    event.getNewValue() != UNINITIALIZED_VALUE) {
                dialog.dismiss();
            }
        };

        WindowAdapter adapter = new WindowAdapter() {
            private boolean gotFocus = false;

            @Override
            public void windowClosing(WindowEvent we) {
                setValue(null);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                removePropertyChangeListener(listener);
                removeAllViews();
            }
            
        };

        dialog.addWindowListener(adapter);
        dialog.addWindowFocusListener(adapter);

        // Pas d'équivalent exact pour ComponentListener sur Android
        // On utilise un callback de visibilité
        dialog.setOnShowListener(d -> {
            setValue(UNINITIALIZED_VALUE);
        });

        addPropertyChangeListener(listener);
    }

    private void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    private void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    // Méthode utilitaire pour configurer les boutons
    private void setupButtons(JDialog dialog, JPanel contentPane) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        buttonPanel.setOrientation(LinearLayout.HORIZONTAL);

        Object[] buttons = getButtonsToShow();
        System.out.println("button to show " + Arrays.toString(buttons));
        for (int i = 0; i < buttons.length; i++) {
            Button button = new Button(StartAndroidApp.getAppContext());
            button.setText(buttons[i].toString());
            final int returnValue = getReturnValueForButton(i);

            button.setOnClickListener(v -> {
                setValue(returnValue);
                dialog.dismiss();
            });

            buttonPanel.add(button);
        }

        contentPane.add(buttonPanel);
    }
    // Crée la vue du message selon son type
    private View createMessageView() {
        if (message == null) {
            return new View(getContext()); // Vue vide
        }

        // Si le message est déjà une View
        if (message instanceof View) {
            return (View) message;
        }

        // Si le message est un tableau d'objets
        if (message instanceof Object[]) {
            return createMessageViewFromArray(getContext(), (Object[]) message);
        }

        // Par défaut, traitement comme texte
        TextView textView = new TextView(getContext());
        textView.setText(message.toString());
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 0, 0, 16);
        return textView;
    }

    // Crée une vue à partir d'un tableau de messages
    private View createMessageViewFromArray(Context context, Object[] messages) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (Object item : messages) {
            if (item instanceof View) {
                layout.addView((View) item);
            } else {
                TextView textView = new TextView(context);
                textView.setText(item.toString());
                textView.setGravity(Gravity.CENTER);
                layout.addView(textView);
            }
        }

        return layout;
    }
    private void setupButtons(JDialog dialog) {
        LinearLayout buttonPanel = new LinearLayout(getContext());
        buttonPanel.setOrientation(LinearLayout.HORIZONTAL);

        Object[] buttons = getButtonsToShow();
        for (int i = 0; i < buttons.length; i++) {
            Button button = new Button(getContext());
            button.setText(buttons[i].toString());
            final int returnValue = getReturnValueForButton(i);

            button.setOnClickListener(v -> {
                setValue(returnValue);
                dialog.dismiss();
            });

            buttonPanel.addView(button);
        }

        dialog.getContentView().add(buttonPanel);
    }

    private Object[] getButtonsToShow() {
        if (options != null) {
            return options;
        }

        switch (optionType) {
            case YES_NO_OPTION:
                return new Object[]{"Yes", "No"};
            case YES_NO_CANCEL_OPTION:
                return new Object[]{"Yes", "No", "Cancel"};
            case OK_CANCEL_OPTION:
                return new Object[]{"OK", "Cancel"};
            default:
                return new Object[]{"OK"};
        }
    }

    private int getReturnValueForButton(int index) {
        if (options != null) {
            return index;
        }

        switch (optionType) {
            case YES_NO_OPTION:
            case YES_NO_CANCEL_OPTION:
                return (index == 0) ? YES_OPTION : (index == 1) ? NO_OPTION : CANCEL_OPTION;
            case OK_CANCEL_OPTION:
                return (index == 0) ? OK_OPTION : CANCEL_OPTION;
            default:
                return OK_OPTION;
        }
    }

    // Getters et Setters
    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public Object[] getOptions() {
        return options;
    }

    public void setOptions(Object[] options) {
        this.options = options;
    }

    public Object getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Object initialValue) {
        this.initialValue = initialValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Object[] getSelectionValues() {
        return selectionValues;
    }

    public void setSelectionValues(Object[] selectionValues) {
        this.selectionValues = selectionValues;
    }

    public Object getInputValue() {
        return inputValue;
    }

    public void setInputValue(Object inputValue) {
        this.inputValue = inputValue;
    }

    public Object getInitialSelectionValue() {
        return initialSelectionValue;
    }

    public void setInitialSelectionValue(Object initialSelectionValue) {
        this.initialSelectionValue = initialSelectionValue;
    }

    public boolean getWantsInput() {
        return wantsInput;
    }

    public void setWantsInput(boolean wantsInput) {
        this.wantsInput = wantsInput;
    }




    // Valeur spéciale pour indiquer qu'aucune sélection n'a été faite
    public static final Object UNINITIALIZED_VALUE = "uninitializedValue";

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}