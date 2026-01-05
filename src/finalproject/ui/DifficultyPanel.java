package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import finalproject.gamestate.GameManager;
import finalproject.ui.components.BackgroundPanel;
import finalproject.ui.components.CyberDialog;
import finalproject.ui.components.GradientButton;
import finalproject.util.ResourceLoader;
import finalproject.util.audio.SoundManager;

public class DifficultyPanel extends AbstractTilePacerPanel {
	private static final long serialVersionUID = 1L;
	private final TilePacerFrame parentFrame;
	private BackgroundPanel difficultyPanel;
	
	public DifficultyPanel(TilePacerFrame parentFrame) {
		this.parentFrame = parentFrame;
		createPanel();
	}

	@Override
	protected void createPanel() {
		difficultyPanel = new BackgroundPanel(BACKGROUND_ICON);
	    difficultyPanel.setLayout(new GridBagLayout()); 
	    
	    addMainTitle();
	    createEasyButton();
	    createMediumButton();
	    createHardButton();
	    
	    ResourceLoader.addBackButtonToPanel(parentFrame, difficultyPanel);
	    setLayout(new BorderLayout());
        add(difficultyPanel, BorderLayout.CENTER);
	}
	
	@Override
    protected void addMainTitle() {
		JLabel titleLabel = ResourceLoader.createStyledLabel("DIFFICULTY", "Tile Pacer", 
				new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0);
	    difficultyPanel.add(titleLabel, gbcTitle);
	}
	
	private void createEasyButton() {
		GradientButton easyButton = ResourceLoader.createStyledGradientButton("EASY",
				new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), 
	    		true, 100, 85f, 450, 140);	
		easyButton.addActionListener(e -> {
	        SoundManager.play("click");
	        parentFrame.currentGame.applyDifficulty(GameManager.Difficulty.EASY);
	        parentFrame.switchScreen(TilePacerFrame.GAMEPLAY_KEY);
	    });
	    GridBagConstraints gbcEasy = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH,
	    		GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 550, 50);   
	    difficultyPanel.add(easyButton, gbcEasy); 
	}
	
	private void createMediumButton() {
		GradientButton mediumButton = ResourceLoader.createStyledGradientButton("MEDIUM", 
				new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe),
	    		true, 100, 85f, 450, 140);
		if (!parentFrame.currentGame.isDifficultyUnlocked(GameManager.Difficulty.MEDIUM)) {
		    mediumButton.setEnablePressedEffect(false);
		    mediumButton.addActionListener(e -> {
		        CyberDialog.showMessage(
		            parentFrame, String.format("%30s", "You have to reach level 10 first to unlock this difficulty."),
		            "CLOSE", 500, 380);
		    });
		} else {
		    mediumButton.addActionListener(e -> {
		        SoundManager.play("click");
		        parentFrame.currentGame.applyDifficulty(GameManager.Difficulty.MEDIUM);
		        parentFrame.switchScreen(TilePacerFrame.GAMEPLAY_KEY);
		    });
		}    
	    GridBagConstraints gbcMedium = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
	    		GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 100, 50);   
	    difficultyPanel.add(mediumButton, gbcMedium);
	}
	
	private void createHardButton() {
		GradientButton hardButton = ResourceLoader.createStyledGradientButton("HARD",
				new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe),
	    		true, 100, 85f, 450, 140);
		if (!parentFrame.currentGame.isDifficultyUnlocked(GameManager.Difficulty.HARD)) {
			hardButton.setEnablePressedEffect(false);
			hardButton.addActionListener(e -> {
		        CyberDialog.showMessage(
		            parentFrame, String.format("%30s", "You have to reach level 15 first to unlock this difficulty."),
		            "CLOSE", 500, 380);
		    });
		} else {
			hardButton.addActionListener(e -> {
		        SoundManager.play("click");
		        parentFrame.currentGame.applyDifficulty(GameManager.Difficulty.HARD);
		        parentFrame.switchScreen(TilePacerFrame.GAMEPLAY_KEY);
		    });
		}
	    GridBagConstraints gbcHard = ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTH,
	    		GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 100, 50);   
	    difficultyPanel.add(hardButton, gbcHard); 
	}	
}