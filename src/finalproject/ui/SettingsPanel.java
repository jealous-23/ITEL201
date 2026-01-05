package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import finalproject.ui.components.BackgroundPanel;
import finalproject.ui.components.CyberDialog;
import finalproject.ui.components.GradientButton;
import finalproject.util.ResourceLoader;
import finalproject.util.audio.SoundManager;

public class SettingsPanel extends AbstractTilePacerPanel {
    
    private static final long serialVersionUID = 1L;
    private final TilePacerFrame parentFrame;
    private BackgroundPanel settingsPanel;
    private JToggleButton soundToggle;

    public SettingsPanel(TilePacerFrame parentFrame) {
        this.parentFrame = parentFrame;
        createPanel();
    }
    
    @Override
    protected void createPanel() {
        settingsPanel = new BackgroundPanel(BACKGROUND_ICON);
        settingsPanel.setLayout(new GridBagLayout());

        addMainTitle();
        createSoundSection();
        createAboutSection();
        createResetSection();
        
        ResourceLoader.addBackButtonToPanel(parentFrame, settingsPanel);
        
        this.setLayout(new BorderLayout());
        this.add(settingsPanel, BorderLayout.CENTER);
    }
    
    @Override
    protected void addMainTitle() {
        JLabel titleLabel = ResourceLoader.createStyledLabel("SETTINGS", 
        		"Tile Pacer", new Color(0x40E0D0), 80f, SwingConstants.CENTER);
        GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0);
        settingsPanel.add(titleLabel, gbcTitle);
    }
    
    private void createSoundSection() {
        addComponent(createHeaderLabel("SYSTEM AUDIO"), 0, 1, 2, 10);

        boolean savedSoundState = parentFrame.currentGame.isSoundEnabled();
        soundToggle = createCyberToggle(savedSoundState ? "SOUND ON" : "SOUND OFF");
        soundToggle.setSelected(!savedSoundState); 
        SoundManager.setMuted(!savedSoundState);

        soundToggle.addActionListener(e -> {
            boolean isOff = soundToggle.isSelected();
         
            soundToggle.setText(isOff ? "SOUND OFF" : "SOUND ON");
            soundToggle.setForeground(isOff ? Color.GRAY : new Color(0x40E0D0));          
            parentFrame.currentGame.setSoundEnabled(!isOff);
            SoundManager.setMuted(isOff);
        });
        
        addComponent(soundToggle, 0, 2, 2, 50);
    }

    private void createAboutSection() {
        GradientButton aboutButton = ResourceLoader.createStyledGradientButton("ABOUT", 
                new Color(0x008B8B), new Color(0x006A80), 
                new Color(0x008B8B), new Color(0xfefefe),
                true, 100, 100f, 500, 160);
        
        aboutButton.addActionListener(e -> {
        	SoundManager.play("click");
        	showAboutDialog();
        });
        addComponent(aboutButton, 0, 3, 2, 20);
    }

    private void createResetSection() {
        GradientButton resetButton = ResourceLoader.createStyledGradientButton("RESET GAME", 
                new Color(0x008B8B), new Color(0x006A80), 
                new Color(0x008B8B), new Color(0xfefefe),
                true, 100, 60f, 500, 160);
        
        resetButton.addActionListener(e -> {
        	SoundManager.play("click");
        	confirmReset();
        });
        addComponent(resetButton, 0, 4, 2, 50);
    }

    private JToggleButton createCyberToggle(String text) {
        JToggleButton toggle = new JToggleButton(text);
        toggle.setFont(ResourceLoader.loadCustomFont());
        toggle.setForeground(new Color(0x40E0D0));
        toggle.setBackground(new Color(0, 0, 0, 150)); 
        toggle.setFocusPainted(false);
        toggle.setBorder(BorderFactory.createLineBorder(new Color(0x40E0D0), 2));
        toggle.setPreferredSize(new Dimension(350, 80));
        toggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return toggle;
    }

    private JLabel createHeaderLabel(String text) {
        return ResourceLoader.createStyledLabel(text, "Orbiton", Color.WHITE, 20f, SwingConstants.CENTER);
    }

    private void addComponent(Component comp, int x, int y, int width, int bottomSpace) {
        GridBagConstraints gbc = ResourceLoader.createLayoutConstraints(
            GridBagConstraints.CENTER, GridBagConstraints.NONE, x, y, 1.0, 0.0, 10, 50, bottomSpace, 50
        );
        gbc.gridwidth = width;
        settingsPanel.add(comp, gbc);
    }

    private void showAboutDialog() {
    	CyberDialog.showMessage(
                parentFrame, 
                "Tile Pacer functions as both a strategic game and an educational tool. "
                + "It strengthens planning, adaptability, \nand logical decision-making by challenging players "
                + "to navigate through varied tiles with limited steps.\nThe project demonstrates how Java"
                + " can deliver interactive learning experiences while providing enjoyable puzzle-based gameplay.",
                "CLOSE", 800, 500);
    }

    private void confirmReset() {
        String answer = CyberDialog.showQuestion(parentFrame, 
            "PERMANENTLY WIPE ALL DATA?", null, new String[]{"YES", "NO"}, 600, 300);
        if ("YES".equalsIgnoreCase(answer)) {
        	System.out.println("Data Purged.");
        	CyberDialog.showMessage(parentFrame, 
                    "The game has been reset.",
                    "OKAY", 500, 250);
        	parentFrame.currentGame.resetGame();
        }
    }
}