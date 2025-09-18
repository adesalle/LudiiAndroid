package playerAndroid.app.display.dialogs;



import android.graphics.Typeface;

import androidUtils.JSONObject;
import androidUtils.awt.BorderLayout;
import androidUtils.awt.Color;
import androidUtils.awt.Dimension;
import androidUtils.awt.EventQueue;
import androidUtils.awt.Font;
import androidUtils.awt.Rectangle;
import androidUtils.awt.event.ActionEvent;
import androidUtils.awt.event.ActionListener;
import androidUtils.swing.JButton;
import androidUtils.swing.JCheckBox;
import androidUtils.swing.JDialog;
import androidUtils.swing.JLabel;
import androidUtils.swing.JPanel;
import other.context.Context;
import androidUtils.swing.border.EmptyBorder;
import app.PlayerApp;
import main.Constants;
import manager.ai.AIUtil;
import playerAndroid.app.AndroidApp;
import playerAndroid.app.display.dialogs.util.DialogUtil;

/**
 * Dialog simplifié pour les préférences des joueurs.
 * Ne conserve que les cases à cocher pour activer l'IA.
 *
 * @author matthew.stephenson
 */
public class SettingsDialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	static SettingsDialog dialog;
	static JCheckBox[] playerAICheckboxes = new JCheckBox[Constants.MAX_PLAYERS + 1];

	//-------------------------------------------------------------------------

	/**
	 * Show the Dialog.
	 */
	public static void createAndShowGUI(final PlayerApp app)
	{
		try
		{
			dialog = new SettingsDialog(app);
			DialogUtil.initialiseSingletonDialog(dialog, "Preferences", new Rectangle(AndroidApp.frame().getX(), AndroidApp.frame().getY(),
					AndroidApp.frame().getWidth() * 0.75f, AndroidApp.frame().getHeight() * .75f));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	//-------------------------------------------------------------------------

	/**
	 * Create the dialog.
	 */
	public SettingsDialog(final PlayerApp app)
	{
		super(null, ModalityType.DOCUMENT_MODAL);
		setTitle("Settings");

		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel playerPanel = new JPanel();
		getContentPane().add(playerPanel, BorderLayout.CENTER);
		playerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		final int numPlayers = app.contextSnapshot().getContext(app).game().players().count();

		// Ajouter les cases à cocher pour chaque joueur
		for (int i = 1; i <= numPlayers; i++)
		{
			addPlayerAICheckbox(app, playerPanel, i);
		}

		playerPanel.setLayout(null);
		playerPanel.setPreferredSize(new Dimension(450, 50 + numPlayers * 40));

		// Calculer la hauteur pour positionner le bouton
		final int buttonY = 50 + numPlayers * 40 + 20;

		final JButton btnApply = new JButton("Apply");
		btnApply.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnApply.setBounds(150, buttonY, 150, 40);
		playerPanel.add(btnApply);

		btnApply.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				applyPlayerAISettings(app);
				dispose();
			}
		});
	}

//-------------------------------------------------------------------------

	/**
	 * Ajoute une case à cocher pour activer/désactiver l'IA pour un joueur
	 */
	static void addPlayerAICheckbox(final PlayerApp app, final JPanel playerJpanel, final int playerId)
	{
		// Label du joueur
		final JLabel playerLabel = new JLabel("Player " + playerId);
		playerLabel.setTextColor(Color.WHITE.toArgb());
		playerLabel.setBounds(20, 20 + playerId * 40, 80, 25);
		playerJpanel.add(playerLabel);

		// Case à cocher pour l'IA
		final JCheckBox aiCheckbox = new JCheckBox("AI Enabled");

		aiCheckbox.setBounds(120, 20 + playerId * 40, 120, 25);

		boolean isAIEnabled = app.manager().aiSelected()[playerId].ai() != null;
		aiCheckbox.setSelected(isAIEnabled);
		playerJpanel.add(aiCheckbox);

		// Sauvegarder la référence
		playerAICheckboxes[playerId] = aiCheckbox;
	}

//-------------------------------------------------------------------------

	/**
	 * Applique les paramètres d'IA sélectionnés - configure Ludii AI si la checkbox est activée
	 */
	public static void applyPlayerAISettings(final PlayerApp app)
	{
		final Context context = app.contextSnapshot().getContext(app);

		for (int i = 1; i <= context.game().players().count(); i++)
		{
			if (playerAICheckboxes[i] != null)
			{
				boolean aiEnabled = playerAICheckboxes[i].isSelected();

				if (aiEnabled)
				{
					// Configurer Ludii AI pour ce joueur
					final JSONObject json = new JSONObject().put("AI",
							new JSONObject()
									.put("algorithm", "Ludii AI")
					);

					AIUtil.updateSelectedAI(app.manager(), json, i, "Ludii AI");

					// Initialiser l'IA si nécessaire
					app.manager().aiSelected()[i].ai().initIfNeeded(context.game(), i);

					// Définir un temps de réflexion par défaut (1 seconde)
					app.manager().aiSelected()[i].setThinkTime(1.0);
				}
				else
				{
					// Désactiver l'IA - configurer comme humain
					final JSONObject json = new JSONObject().put("AI",
							new JSONObject()
									.put("algorithm", "Human")
					);

					AIUtil.updateSelectedAI(app.manager(), json, i, "Human");
				}
			}
		}

		// Sauvegarder la configuration des joueurs IA
		app.manager().settingsNetwork().backupAiPlayers(app.manager());

		// Recréer les panels pour refléter les changements
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				AndroidApp.view().createPanels();
			}
		});
	}
}