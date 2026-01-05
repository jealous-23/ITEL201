package finalproject.ui;

import finalproject.gamestate.GameManager;
import finalproject.util.Refreshable;
import finalproject.util.audio.SoundManager;

import java.awt.CardLayout;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TilePacerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final JPanel mainContentPanel;
    private final CardLayout cardLayout = new CardLayout();
    protected GameManager currentGame;  
    private Map<String, JPanel> allPanels;
 
    private SettingsPanel settingsPanel;
    private PlayerRecordScorePanel playerRecordScorePanel;
    private ProfilePanel profilePanel;
    private ShopPanel shopPanel;
    private DifficultyPanel difficultyPanel;
    private GameplayPanel gameplayPanel;
   
    protected static final String HOME_KEY = "Home";
    public static final String MAIN_MENU_KEY = "MainMenu";
    protected static final String DIFFICULTY_KEY = "Difficulty";
    protected static final String EASY_KEY = "Easy";
    protected static final String MEDIUM_KEY = "Medium";
    protected static final String HARD_KEY = "Hard";
    protected static final String SETTINGS_KEY = "Settings";
    protected static final String ABOUT_KEY = "About";
    protected static final String GAMEPLAY_KEY = "Gameplay";
	protected static final String PROFILE_KEY = "Profile";
	protected static final String SHOP_KEY = "Shop";
	protected static final String RECORDS_SCORE_KEY = "RecordsScore";
	protected static final String RECORDS_ACHIEVEMENTS_KEY = "RecordsAchievements";
	
    public TilePacerFrame() {
    	setTitle("Tile Pacer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		mainContentPanel = new JPanel(cardLayout);
		this.getContentPane().add(mainContentPanel);
		currentGame = new GameManager();
		SoundManager.initialize();
	
		settingsPanel = new SettingsPanel(this);
		playerRecordScorePanel = new PlayerRecordScorePanel(this);
		profilePanel = new ProfilePanel(this);
		shopPanel = new ShopPanel(this);
		difficultyPanel = new DifficultyPanel(this);
		gameplayPanel = new GameplayPanel(this);
	    
		allPanels = new HashMap<>();

		allPanels.put(SETTINGS_KEY, settingsPanel);
		allPanels.put(RECORDS_ACHIEVEMENTS_KEY, playerRecordScorePanel);
		allPanels.put(PROFILE_KEY, profilePanel);
		allPanels.put(SHOP_KEY, shopPanel);
		allPanels.put(DIFFICULTY_KEY, difficultyPanel);
		allPanels.put(GAMEPLAY_KEY, gameplayPanel);

		registerScreens();
        cardLayout.show(mainContentPanel, HOME_KEY);
        setVisible(true);
    }

    private void registerScreens() {	
    	mainContentPanel.add(new HomePanel(() -> {
    		switchScreen(MAIN_MENU_KEY); 
    	}), HOME_KEY);
    	mainContentPanel.add(new MainMenuPanel(this), MAIN_MENU_KEY); 
    	mainContentPanel.add(settingsPanel, SETTINGS_KEY);
    	mainContentPanel.add(playerRecordScorePanel, RECORDS_ACHIEVEMENTS_KEY); 
    	mainContentPanel.add(profilePanel, PROFILE_KEY); 
    	mainContentPanel.add(shopPanel, SHOP_KEY);
    	mainContentPanel.add(new AboutPanel(this), ABOUT_KEY); 
    	mainContentPanel.add(difficultyPanel, DIFFICULTY_KEY);
    	mainContentPanel.add(gameplayPanel, GAMEPLAY_KEY);
    }

    public void switchScreen(String key) {  
    	JPanel panel = allPanels.get(key);
    	
    	if (panel == gameplayPanel) {
            gameplayPanel.refresh();
        }

    	
    	if (panel instanceof Refreshable) {
    		((Refreshable) panel).refresh();
    		System.out.println("It has been refreshed.");
    	}
    	
        cardLayout.show(mainContentPanel, key);   
    }
}