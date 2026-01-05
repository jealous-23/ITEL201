package finalproject.classes;

import java.awt.*;
import javax.swing.*;

import finalproject.gamestate.GameManager;
import finalproject.gamestate.TilePacerBoard;
import finalproject.ui.components.BackgroundPanel;
import finalproject.ui.components.GradientButton;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TilePacer extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String MAIN_MENU_KEY = "MainMenu";
	private static final String DIFFICULTY_KEY = "Difficulty";
	private static final String EASY_KEY = "Easy";
	private static final String GAMEPLAY_KEY = "Gameplay";
	private static final String PROFILE_KEY = "Profile";
	private static final String SHOP_KEY = "Shop";
	private static final String RECORDS_SCORE_KEY = "RecordsScore";
	private static final String RECORDS_ACHIEVEMENTS_KEY = "RecordsAchievements";
	private static final String FONT_PATH = "ARCADECLASSIC.TTF";
	private static final String BACK_BUTTON_PATH = "backButton.png";
	private static final ImageIcon BACKGROUND_ICON = new ImageIcon("background.png");
	
	private GameManager currentGame;
	private final CardLayout cardLayout = new CardLayout();
	private final JPanel mainContentPanel;

	public TilePacer() {
		setTitle("Tile Pacer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		mainContentPanel = new JPanel(cardLayout);
		this.getContentPane().add(mainContentPanel);
		currentGame = new GameManager();
		
		mainContentPanel.add(createMainMenuPanel(), MAIN_MENU_KEY);
		mainContentPanel.add(createDifficultyPanel(), DIFFICULTY_KEY); 
		mainContentPanel.add(createEasyPanel(), EASY_KEY); 
		mainContentPanel.add(createProfilePanel(), PROFILE_KEY);
		/*mainContentPanel.add(createPlaceholderPanel(GAMEPLAY_KEY), GAMEPLAY_KEY);
		mainContentPanel.add(createPlaceholderPanel(PROFILE_KEY), PROFILE_KEY);
		mainContentPanel.add(createPlaceholderPanel(SHOP_KEY), SHOP_KEY);
		mainContentPanel.add(createPlaceholderPanel(RECORDS_SCORE_KEY), RECORDS_SCORE_KEY);*/
		mainContentPanel.add(createPlayerRecordsScorePanel(), RECORDS_ACHIEVEMENTS_KEY);
		
		cardLayout.show(mainContentPanel, MAIN_MENU_KEY);
		setVisible(true);
	}
	
	private JPanel createMainMenuPanel() {
	    BackgroundPanel mainPanel = new BackgroundPanel(BACKGROUND_ICON);
	    mainPanel.setLayout(new GridBagLayout()); 
	    
	    JLabel titleLabel = setToDefaultLabel("TILE   PACER", new Color(0x40E0D0), 100f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = createGbc(GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0, 1.5, 1.0, 0, 0, 60, 0);
	    mainPanel.add(titleLabel, gbcTitle);
	    
	    GradientButton playButton = setToDefaultGradientButton("PLAY", new Color(0x008B8B), new Color(0x006A80), 
	    		new Color(0x008B8B), new Color(0xfefefe), true, 100, 100f, 500, 160);
	    playButton.addActionListener(e -> {
	        switchScreen(DIFFICULTY_KEY);
	    });
	    GridBagConstraints gbcPlay = createGbc(GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 345, 50);   
	    mainPanel.add(playButton, gbcPlay);

	    GradientButton settingsButton = setToDefaultGradientButton("SETTINGS", new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), true, 100, 90f, 500, 160); 
	    GridBagConstraints gbcSettings = createGbc(GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 1, 0.0, 1.0, 170, 50, 50, 50);
	    mainPanel.add(settingsButton, gbcSettings);
	    
	    ImageIcon crownImage = new ImageIcon(setImage("newCrown.png", 200, 140));
	    JButton playerRecordsButton = setButton(crownImage);
	    playerRecordsButton.addActionListener(e -> {
	        switchScreen(RECORDS_ACHIEVEMENTS_KEY);
	    });
	    GridBagConstraints gbcCrown = createGbc(GridBagConstraints.WEST, GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 15, 15, 210, 10);
	    mainPanel.add(playerRecordsButton, gbcCrown);
	    
	    JLabel playerRecordsText = setToDefaultLabel("Player   Records", new Color(0x40E0D0), 23f, SwingConstants.RIGHT);
	    GridBagConstraints gbcPlayer = createGbc(GridBagConstraints.WEST, GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 0, 35, 40, 10);
	    mainPanel.add(playerRecordsText, gbcPlayer);
	    
	    ImageIcon profileImage = new ImageIcon(setImage("newProfile.png", 150, 120));
	    JButton profileButton = setButton(profileImage); 
	    GridBagConstraints gbcProfile = createGbc(GridBagConstraints.EAST, GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 15, 15, 210, 15);
	    mainPanel.add(profileButton, gbcProfile);
	    profileButton.addActionListener(e -> {
	        switchScreen(PROFILE_KEY);
	    });
	    
	    JLabel profileText = setToDefaultLabel("Profile", new Color(0x40E0D0), 23f, SwingConstants.LEFT);
	    GridBagConstraints gbcUser = createGbc(GridBagConstraints.EAST, GridBagConstraints.NONE, 0, 0, 1.5, 1.0, 0, 20, 40, 60);
	    mainPanel.add(profileText, gbcUser);
	    
	    ImageIcon shopImage = new ImageIcon(setImage("newShop.png", 140, 110));  
	    JButton shopButton = setButton(shopImage);
	    GridBagConstraints gbcShop = createGbc(GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 0, 1, 0.0, 1.0, 50, 15, 90, 20);
	    mainPanel.add(shopButton, gbcShop);
	    
	    JLabel shopText = setToDefaultLabel("Shop", new Color(0x40E0D0), 23f, SwingConstants.LEFT);
	    GridBagConstraints gbcBuy = createGbc(GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 0, 1, 1.0, 1.0, 0, 90, 70, 75);
	    mainPanel.add(shopText, gbcBuy);
	    
	    return mainPanel;
	}
	
	private JPanel createDifficultyPanel() {	
	    BackgroundPanel difficultyPanel = new BackgroundPanel(BACKGROUND_ICON);
	    difficultyPanel.setLayout(new GridBagLayout()); 
		
	    JLabel titleLabel = setToDefaultLabel("DIFFICULTY", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = createGbc(GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0);
	    difficultyPanel.add(titleLabel, gbcTitle);
	    
	    GradientButton easyButton = setToDefaultGradientButton("EASY", new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), true, 100, 85f, 450, 140);
	    GridBagConstraints gbcEasy = createGbc(GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 550, 50);   
	    difficultyPanel.add(easyButton, gbcEasy); 
	    easyButton.addActionListener(e -> {
	        switchScreen(EASY_KEY);
	    });
	    
	    GradientButton mediumButton = setToDefaultGradientButton("MEDIUM", new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), true, 100, 85f, 450, 140);
	    GridBagConstraints gbcMedium = createGbc(GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 100, 50);   
	    difficultyPanel.add(mediumButton, gbcMedium); 
	    
	    GradientButton hardButton = setToDefaultGradientButton("HARD", new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), true, 100, 85f, 450, 140);
	    GridBagConstraints gbcHard = createGbc(GridBagConstraints.SOUTH, GridBagConstraints.NONE, 0, 1, 0.0, 0.0, 5, 50, 100, 50);   
	    difficultyPanel.add(hardButton, gbcHard); 
	    
	    addBackButton(difficultyPanel);
	    
		return difficultyPanel;
	}
	
	private JPanel createEasyPanel() {
	    BackgroundPanel easyPanel = new BackgroundPanel(BACKGROUND_ICON);
	    easyPanel.setLayout(new GridBagLayout());
	    
	    TilePacerBoard gameBoard = new TilePacerBoard(this.currentGame);
	    GridBagConstraints gbcEasy = createGbc(GridBagConstraints.CENTER, GridBagConstraints.NONE,
	            0, 0, 1.0, 1.0, 50, 50, 50, 50);
	    easyPanel.add(gameBoard, gbcEasy);
	    
	    currentGame = new GameManager(GameManager.Difficulty.EASY);
	    
	    JLabel titleLabel = setToDefaultLabel("EASY", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = createGbc(GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0);
	    easyPanel.add(titleLabel, gbcTitle);
	    
	    JLabel stepsLabel = setToDefaultLabel("STEPS " + String.valueOf(currentGame.getCurrentSteps()), new Color(0x40E0D0), 50f, SwingConstants.CENTER);
	    GridBagConstraints gbcSteps = createGbc(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 150, 50, 60, 0);
	    easyPanel.add(stepsLabel, gbcSteps);
	    
	    addBackButton(easyPanel);
	    return easyPanel;
	}
	
	private JPanel createPlayerRecordsScorePanel() {
	    BackgroundPanel playerRecordsPanel = new BackgroundPanel(BACKGROUND_ICON);
	    playerRecordsPanel.setLayout(new GridBagLayout());

	    JLabel titleLabel = setToDefaultLabel("PLAYER   RECORDS", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = createGbc(GridBagConstraints.NORTH, GridBagConstraints.NONE,
	            0, 0, 1.0, 0.2, 20, 0, 60, 0);
	    playerRecordsPanel.add(titleLabel, gbcTitle);

	    JPanel miniCardPanel = new JPanel(new CardLayout());
	    miniCardPanel.setOpaque(false);
	    miniCardPanel.setPreferredSize(new Dimension(900, 450));
	    
	    JPanel achievementPanel = new JPanel();
	    achievementPanel.setOpaque(false);

	    miniCardPanel.add(scorePanel(), "SCORES");
	    miniCardPanel.add(achievementPanel, "ACHIEVEMENTS");

	    CardLayout miniCardLayout = (CardLayout) miniCardPanel.getLayout();

	    JPanel scoreHighlight = new JPanel();
	    scoreHighlight.setBackground(new Color(0x40E0D0));
	    scoreHighlight.setPreferredSize(new Dimension(300, 5));
	    scoreHighlight.setVisible(true);

	    JPanel achievementHighlight = new JPanel();
	    achievementHighlight.setBackground(new Color(0x40E0D0));
	    achievementHighlight.setPreferredSize(new Dimension(300, 5));
	    achievementHighlight.setVisible(false);

	    GradientButton scoreButton = setToDefaultGradientButton("SCORES", new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), true, 50, 70f, 300, 120);
	    scoreButton.addActionListener(e -> {
	        miniCardLayout.show(miniCardPanel, "SCORES");
	        scoreHighlight.setVisible(true);
	        achievementHighlight.setVisible(false);
	    });

	    JPanel scoreContainer = new JPanel();
	    scoreContainer.setOpaque(false);
	    scoreContainer.setLayout(new BorderLayout());
	    scoreContainer.add(scoreButton, BorderLayout.CENTER);
	    scoreContainer.add(scoreHighlight, BorderLayout.SOUTH);

	    GridBagConstraints gbcScore = createGbc(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
	    		0, 0, 0.0, 0.0, 150, 120, 120, 120);
	    playerRecordsPanel.add(scoreContainer, gbcScore);
	    
	    GradientButton achievementButton = setToDefaultGradientButton("ACHIEVEMENTS", new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), true, 50, 35f, 300, 120);
	    achievementButton.addActionListener(e -> {
	        miniCardLayout.show(miniCardPanel, "ACHIEVEMENTS");
	        scoreHighlight.setVisible(false);
	        achievementHighlight.setVisible(true);
	    });

	    JPanel achievementContainer = new JPanel();
	    achievementContainer.setOpaque(false);
	    achievementContainer.setLayout(new BorderLayout());
	    achievementContainer.add(achievementButton, BorderLayout.CENTER);
	    achievementContainer.add(achievementHighlight, BorderLayout.SOUTH);

	    GridBagConstraints gbcAchievement = createGbc(GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
	    		0, 0, 0.0, 0.0, 150, 120, 120, 120);
	    playerRecordsPanel.add(achievementContainer, gbcAchievement);
	    
	    GridBagConstraints gbcMiniPanel = createGbc(
	            GridBagConstraints.CENTER, GridBagConstraints.NONE,
	            0, 2, 1.0, 1.0,
	            30, 0, 0, 0
	    );
	    playerRecordsPanel.add(miniCardPanel, gbcMiniPanel);
    
	    GridBagConstraints gbcPanel = createGbc(GridBagConstraints.SOUTH, GridBagConstraints.NONE,
	    		0, 0, 0.0, 0.0, 150, 120, 50, 120);
	    playerRecordsPanel.add(miniCardPanel, gbcPanel);
	    
	    addBackButton(playerRecordsPanel);
	    
	    return playerRecordsPanel;
	}
	
	private JPanel scorePanel() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 0, 0, 125));
		panel.setLayout(new GridBagLayout()); 
		
		JLabel titleLabel = setToDefaultLabel("HIGHEST SCORES", new Color(0x40E0D0), 25f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = createGbc(GridBagConstraints.NORTH, GridBagConstraints.NONE,
	            0, 0, 1.0, 1.0, 20, 0, 60, 0);
	    panel.add(titleLabel, gbcTitle);
	    
		GradientButton easyButton = setToDefaultGradientButton("EASY", new Color(0x4CAF50), new Color(0x2E7D32),
				new Color(0x4CAF50), new Color(0xfefefe), false, 50, 70f, 250, 110);
		GridBagConstraints easyGbc= createGbc(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
	    		0, 0, 0.0, 0.0, 50, 10, 10, 20);
	    panel.add(easyButton, easyGbc);
	    
	    GradientButton mediumButton = setToDefaultGradientButton("MEDIUM", new Color(0xFFC107), new Color(0xFF8F00),
	    		new Color(0xFFC107), new Color(0xfefefe), false, 50, 60f, 250, 110);	
		GridBagConstraints mediumGbc= createGbc(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				0, 0, 0.0, 0.0, 190, 10, 10, 20);
		panel.add(mediumButton, mediumGbc);
		
		GradientButton hardButton = setToDefaultGradientButton("HARD", new Color(0xF44336), new Color(0xD32F2F),
				new Color(0xF44336), new Color(0xfefefe), false, 50, 70f, 250, 110);	
		GridBagConstraints hardGbc= createGbc(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				0, 0, 0.0, 0.0, 330, 10, 10, 20);
		panel.add(hardButton, hardGbc);
			
		JLabel easy = setToDefaultLabel(String.valueOf(currentGame.getHighScore(GameManager.Difficulty.EASY) + "    POINTS"),
				new Color(0xfefefe), 60f, SwingConstants.CENTER);
		GridBagConstraints easyGbcText = createGbc(GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				0, 0, 0.0, 0.0, 70, 10, 10, 30);
		panel.add(easy, easyGbcText);
		
		JLabel medium = setToDefaultLabel(String.valueOf(currentGame.getHighScore(GameManager.Difficulty.MEDIUM) + "    POINTS"),
				new Color(0xfefefe), 60f, SwingConstants.CENTER);
		GridBagConstraints mediumGbcText = createGbc(GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				0, 0, 0.0, 0.0, 220, 10, 10, 30);
		panel.add(medium, mediumGbcText);
		
		JLabel hard = setToDefaultLabel(String.valueOf(currentGame.getHighScore(GameManager.Difficulty.HARD) + "    POINTS"),
				new Color(0xfefefe), 60f, SwingConstants.CENTER);
		GridBagConstraints hardGbcText = createGbc(GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				0, 0, 0.0, 0.0, 370, 10, 10, 30);
		panel.add(hard, hardGbcText);
		
	    return panel;
	}
	
	private JPanel createProfilePanel() {
	    BackgroundPanel profilePanel = new BackgroundPanel(BACKGROUND_ICON);
	    profilePanel.setLayout(new GridBagLayout());
	    
	    ImageIcon profileImage = new ImageIcon(setImage("newProfile.png", 300, 280));
	    JButton profileButton = setButton(profileImage); 
	    GridBagConstraints gbcProfile = createGbc(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 150, 190, 310, 100);
	    profilePanel.add(profileButton, gbcProfile);
	    
	    JLabel titleLabel = setToDefaultLabel("PROFILE", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = createGbc(GridBagConstraints.NORTH, GridBagConstraints.NONE,
	            0, 0, 1.0, 0.2, 20, 0, 60, 0);
	    profilePanel.add(titleLabel, gbcTitle);
	    
	    JPanel miniCardPanel = new JPanel(new CardLayout());
	    miniCardPanel.setOpaque(true);
	    miniCardPanel.setPreferredSize(new Dimension(850, 350));
	    miniCardPanel.add(userDetails(), "PROFILE");
	    
	    CardLayout miniCardLayout = (CardLayout) miniCardPanel.getLayout();
	    
	    miniCardLayout.show(miniCardPanel, "PROFILE");
	    GridBagConstraints gbcMiniPanel = createGbc(
	            GridBagConstraints.NORTH, GridBagConstraints.NONE,
	            0, 0, 0.0, 0.0,
	            120, 550, 0, 0
	    );
	    profilePanel.add(miniCardPanel, gbcMiniPanel);
	    
	    addBackButton(profilePanel);
	    
	    return profilePanel;
	}
	
	private JPanel userDetails() {
	    JPanel panel = new JPanel();
	    panel.setBackground(new Color(0, 0, 0, 125));
	    panel.setLayout(new GridBagLayout()); 

	    // 1. NAME LABEL (Row 0, Col 0)
	    JLabel nameLabel = setToDefaultLabel("NAME", new Color(0x40E0D0), 60f, SwingConstants.LEFT);
	    GridBagConstraints gbcNameLabel = createGbc(GridBagConstraints.WEST, GridBagConstraints.NONE, 
	                                                0, 0, 0.1, 1.0, 10, 30, 0, 10);
	    panel.add(nameLabel, gbcNameLabel);
	    
	    
	    JTextField nameField = new JTextField(currentGame.getName(), 25);
	    nameField.setFont(loadCustomFont().deriveFont(37f));
	    nameField.setBackground(new Color(30, 30, 30));
	    nameField.setForeground(Color.WHITE);
	    nameField.setCaretColor(Color.WHITE);
	    nameField.setBorder(BorderFactory.createLineBorder(new Color(0x40E0D0), 2));
	    GridBagConstraints gbcNameField = createGbc(GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
	                                                1, 0, 0.5, 1.0, 10, 10, 0, 10);
	    
	    panel.add(nameField, gbcNameField);

	    
	    GradientButton saveButton = setToDefaultGradientButton("SAVE", new Color(0x008B8B), new Color(0x006A80),
	    		new Color(0x008B8B), new Color(0xfefefe), true, 100, 22f, 100, 50);
	    GridBagConstraints gbcSave = createGbc(GridBagConstraints.WEST, GridBagConstraints.NONE, 
	                                           2, 0, 0.2, 1.0, 10, 10, 0, 20);
	    panel.add(saveButton, gbcSave);

	    JLabel levelLabel = setToDefaultLabel("LEVEL", new Color(0x40E0D0), 60f, SwingConstants.LEFT);
	    GridBagConstraints gbcLevel = createGbc(GridBagConstraints.WEST, GridBagConstraints.NONE, 
	                                            0, 1, 1.0, 1.0, 0, 10, 70, 10);
	    panel.add(levelLabel, gbcLevel);

	    // 5. COINS (Row 1, Col 1)
	    JLabel coinsLabel = setToDefaultLabel("COINS", new Color(0x40E0D0), 60f, SwingConstants.LEFT);
	    GridBagConstraints gbcCoins = createGbc(GridBagConstraints.CENTER, GridBagConstraints.NONE, 
	                                            1, 1, 1.0, 1.0, 0, 10, 70, 50);
	    panel.add(coinsLabel, gbcCoins);

	    // 6. EXP (Row 1, Col 2)
	    JLabel expLabel = setToDefaultLabel("EXP", new Color(0x40E0D0), 60f, SwingConstants.LEFT);
	    GridBagConstraints gbcExp = createGbc(GridBagConstraints.EAST, GridBagConstraints.NONE, 
	                                          2, 1, 1.0, 1.0, 0, 10, 70, 10);
	    panel.add(expLabel, gbcExp);

	    // Add Action Listener to Save Button
	    saveButton.addActionListener(e -> {
	        String newName = nameField.getText();	        
	        currentGame.saveNewName(newName);
	    });

	    return panel;
	}
	
	/*private JPanel createPlaceholderPanel(String name) {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.add(new JLabel("--- " + name + " Screen Placeholder ---"));
		
		return panel;
	}*/
	
	
	private JLabel setToDefaultLabel(String text, Color color, float textSize, int alignment) {
		JLabel label = new JLabel(text);
	    label.setForeground(color);
	    Font font = loadCustomFont();
	    font = font.deriveFont(font.getStyle() | Font.BOLD, textSize);
	    label.setFont(font);
	    label.setHorizontalAlignment(alignment);
	    
	    GridBagConstraints gbc = createGbc(GridBagConstraints.NORTH, GridBagConstraints.NONE,
	            0, 0, 1.0, 0.2, 20, 0, 60, 0);
	    	
	    return label;
	}
	
	private void addBackButton(JPanel panel) {
		ImageIcon backImage = new ImageIcon(setImage(BACK_BUTTON_PATH, 150, 100));
	    JButton backButton = setButton(backImage);
	    backButton.addActionListener(e -> switchScreen(MAIN_MENU_KEY));

	    GridBagConstraints gbcBack = createGbc(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
	            0, 0, 0.0, 0.0, 20, 20, 20, 20);
	    
	    panel.add(backButton, gbcBack);
	}
	private GradientButton setToDefaultGradientButton(String text, Color firstColor, Color secondColor, Color borderColor, Color textColor,
			boolean enablePressedEffect, int arcSize, float textSize, int width, int height) {
		GradientButton button = new GradientButton(text, firstColor, secondColor, borderColor, arcSize, enablePressedEffect);
	    Font font = loadCustomFont();
	    font = font.deriveFont(font.getStyle() | Font.BOLD, textSize);
	    button.setFont(font);
	    button.setForeground(textColor);
	    button.setPreferredSize(new Dimension(width, height));  
	    
	    return button;
	}
	
	private static GridBagConstraints createGbc(int anchor, int fill, int gridX, int gridY, double weightX, double weightY, 
			int top, int left, int bottom, int right) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = anchor;
	    gbc.fill = fill;
        gbc.gridx = gridX;
	    gbc.gridy = gridY;
	    gbc.weightx = weightX;
	    gbc.weighty = weightY;
	    gbc.insets = new Insets(top, left, bottom, right); 
	    
        return gbc;
    }
	
	private Font loadCustomFont() {
	    try {
	        Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH));
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(customFont);
	        return customFont;
	    } catch (IOException | FontFormatException e) {
	        e.printStackTrace();
	        return new Font("Arial", Font.PLAIN, 12);
	    }
	}
	
	private Image setImage(String path, int width, int height) {
		ImageIcon imageIcon = new ImageIcon(path);
	    Image image = imageIcon.getImage();
	    image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    
	    return image;
	}
	
	private JButton setButton(ImageIcon imageIcon) {
		JButton button = new JButton(imageIcon);
		button.setBorderPainted(false);
	    button.setContentAreaFilled(false);
	    button.setFocusPainted(false);
	    
	    return button;
	}
	
	private void switchScreen(String key) {
		cardLayout.show(mainContentPanel, key);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(TilePacer::new);
		
	}
}