package finalproject.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import finalproject.gamestate.GameManager;
import finalproject.ui.components.BackgroundPanel;
import finalproject.ui.components.CyberDialog;
import finalproject.ui.components.GradientButton;
import finalproject.util.Refreshable;
import finalproject.util.ResourceLoader;
import finalproject.util.audio.SoundManager;

public class MainMenuPanel extends AbstractTilePacerPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final TilePacerFrame parentFrame;
	private BackgroundPanel mainMenuPanel;
			
	public MainMenuPanel(TilePacerFrame parentFrame) {
		this.parentFrame = parentFrame;
		createPanel();
	}
	
	@Override
	protected void createPanel() {
		mainMenuPanel = new BackgroundPanel(BACKGROUND_ICON);
		mainMenuPanel.setLayout(new GridBagLayout());
		
		addMainTitle();
		createPlayButton();
		createSettingsButton();
		createPlayerRecordsButton();
		createPlayerRecordsText();
		createProfileButton();
		createProfileText();
		createShopButton();
		createShopText();
		
		setLayout(new BorderLayout());
        add(mainMenuPanel, BorderLayout.CENTER);		
	}
	
	@Override
    protected void addMainTitle() {
		JLabel titleLabel = ResourceLoader.createStyledLabel("TILE   PACER", 
				"Tile Pacer", new Color(0x40E0D0), 100f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
	    		GridBagConstraints.NONE, 0, 0, 1.5, 1.0, 0, 0, 60, 0);
	    mainMenuPanel.add(titleLabel, gbcTitle);
	}
	
	private void createPlayButton() {
		GradientButton playButton = ResourceLoader.createStyledGradientButton("PLAY", 
				new Color(0x008B8B), new Color(0x006A80), 
				new Color(0x008B8B), new Color(0xfefefe),
				true, 100, 100f, 500, 160);
	    playButton.addActionListener(e -> {
	    	SoundManager.play("click");
	    	parentFrame.switchScreen(TilePacerFrame.DIFFICULTY_KEY);    	
	    });
	    GridBagConstraints gbcPlay = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
	    		GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 345, 50);   
	    mainMenuPanel.add(playButton, gbcPlay);
	}
	
	private void createSettingsButton() {
		GradientButton settingsButton = ResourceLoader.createStyledGradientButton("SETTINGS", 
				new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe),
	    		true, 100, 90f, 500, 160); 
		settingsButton.addActionListener(e -> {
			SoundManager.play("click");
	    	parentFrame.switchScreen(TilePacerFrame.SETTINGS_KEY);  
	    });
		
	    GridBagConstraints gbcSettings = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
	    		GridBagConstraints.NONE, 0, 1, 0.0, 1.0, 170, 50, 50, 50);
	    mainMenuPanel.add(settingsButton, gbcSettings);    
	}
	
	private void createPlayerRecordsButton() {
		ImageIcon crownImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("/assets/icons/newCrown.png", 200, 140));
	    JButton playerRecordsButton = ResourceLoader.createTransparentButton(crownImage);
	    playerRecordsButton.addActionListener(e -> {
	    	parentFrame.switchScreen(TilePacerFrame.RECORDS_ACHIEVEMENTS_KEY);
	    });
	    GridBagConstraints gbcCrown = ResourceLoader.createLayoutConstraints(GridBagConstraints.WEST, 
	    		GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 15, 15, 210, 10);
	    mainMenuPanel.add(playerRecordsButton, gbcCrown);
	}
	
	private void createPlayerRecordsText() {
		JLabel playerRecordsLabel = ResourceLoader.createStyledLabel("Player   Records", "Tile Pacer", 
				new Color(0x40E0D0), 23f, SwingConstants.RIGHT);
	    GridBagConstraints gbcPlayer = ResourceLoader.createLayoutConstraints(GridBagConstraints.WEST, 
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 0, 35, 40, 10);
	    mainMenuPanel.add(playerRecordsLabel, gbcPlayer);
	}
	
	private void createProfileButton() {
		ImageIcon profileImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("/assets/icons/newProfile.png", 150, 120));
	    JButton profileButton = ResourceLoader.createTransparentButton(profileImage); 
	    profileButton.addActionListener(e -> {
	    	parentFrame.switchScreen(TilePacerFrame.PROFILE_KEY);
	    });
	    GridBagConstraints gbcProfile = ResourceLoader.createLayoutConstraints(GridBagConstraints.EAST,
	    		GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 15, 15, 210, 15);
	    mainMenuPanel.add(profileButton, gbcProfile);
	}
	
	private void createProfileText() {
		JLabel profileText = ResourceLoader.createStyledLabel("Profile", "Tile Pacer",
				new Color(0x40E0D0), 23f, SwingConstants.LEFT);
	    GridBagConstraints gbcUser = ResourceLoader.createLayoutConstraints(GridBagConstraints.EAST, 
	    		GridBagConstraints.NONE, 0, 0, 1.5, 1.0, 0, 20, 40, 60);
	    mainMenuPanel.add(profileText, gbcUser);
	}
	
	private void createShopButton() {
		ImageIcon shopImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("/assets/icons/newShop.png", 140, 110));  
	    JButton shopButton = ResourceLoader.createTransparentButton(shopImage);
	    shopButton.addActionListener(e -> {
	    	parentFrame.switchScreen(TilePacerFrame.SHOP_KEY);
	    });
	    GridBagConstraints gbcShop = ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTHEAST, 
	    		GridBagConstraints.NONE, 0, 1, 0.0, 1.0, 50, 15, 90, 20);
	    mainMenuPanel.add(shopButton, gbcShop);
	}
	
	private void createShopText() {
		JLabel shopText = ResourceLoader.createStyledLabel("Shop", "Tile Pacer", 
				new Color(0x40E0D0), 23f, SwingConstants.LEFT);
	    GridBagConstraints gbcBuy = ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTHEAST, 
	    		GridBagConstraints.NONE, 0, 1, 1.0, 1.0, 0, 90, 70, 75);
	    mainMenuPanel.add(shopText, gbcBuy);
	}	
}