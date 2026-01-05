package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import finalproject.gamestate.GameManager;
import finalproject.gamestate.TilePacerBoard;
import finalproject.ui.components.BackgroundPanel;
import finalproject.util.Refreshable;
import finalproject.util.ResourceLoader;

public class HardPanel extends AbstractTilePacerPanel implements Refreshable {
	private static final long serialVersionUID = 1L;
    private final TilePacerFrame parentFrame;
    private BackgroundPanel hardPanel;
    private TilePacerBoard gameBoard;
    
    
    public HardPanel(TilePacerFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.parentFrame.currentGame.applyDifficulty(GameManager.Difficulty.HARD);
  
        createPanel();
    }
    
    @Override
    protected void createPanel() {
    	hardPanel = new BackgroundPanel(BACKGROUND_ICON);
    	hardPanel.setLayout(new GridBagLayout());
	    
	    createEasyTiles();
	    addMainTitle();
	    addGemIcon();
	    createExpLabel();
	    addCoinIcon();
	    createCoinsLabel();   
	    createStepsLabel();
	    createScoresLabel();
	    
	    ResourceLoader.addBackButtonToPanel(parentFrame, hardPanel, gameBoard);
		
	    this.setLayout(new BorderLayout());
        this.add(hardPanel, BorderLayout.CENTER);
    }
    
    private void createEasyTiles() {   
    	gameBoard = new TilePacerBoard(parentFrame.currentGame);
	    System.out.println(parentFrame.currentGame.getActiveDifficulty());
	    GridBagConstraints gbcEasy = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER,
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 50, 50, 50, 50);
	    hardPanel.add(gameBoard, gbcEasy);
    }
    
    @Override
    protected void addMainTitle() {
    	JLabel titleLabel = ResourceLoader.createStyledLabel("HARD",
    			"Tile Pacer", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0);
	    hardPanel.add(titleLabel, gbcTitle);
    }
    
    private void addGemIcon() {
    	ImageIcon gemImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("assets/icons/gem.png", 110, 90));
	    JButton gemFrame = ResourceLoader.createTransparentButton(gemImage); 
	    
	    GridBagConstraints gbcGemIcon = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHWEST,
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 130, 100, 60, 150);
	    hardPanel.add(gemFrame, gbcGemIcon);
    }
    
    private void createExpLabel() {
    	JLabel expLabel = ResourceLoader.createStyledLabel("" + String.valueOf(
    			gameBoard.getExp()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
	    gameBoard.setOnExpChange(() -> {
	    	expLabel.setText("" + gameBoard.getExp());
	    });
        GridBagConstraints gbcExp = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHWEST, 
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 230, 152, 60, 170);
        hardPanel.add(expLabel, gbcExp);
    }      
    
    private void addCoinIcon() {
    	ImageIcon coinImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("assets/icons/coin.png", 120, 100));
	    JButton coinFrame = ResourceLoader.createTransparentButton(coinImage); 
	    
	    GridBagConstraints gbcCoinIcon = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHEAST,
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 130, 100, 60, 170);
	    hardPanel.add(coinFrame, gbcCoinIcon);
    }
    
    private void createCoinsLabel() {
    	JLabel coinsLabel = ResourceLoader.createStyledLabel(String.valueOf(
    			gameBoard.getCoins()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
	    gameBoard.setOnCoinChange(() -> {
	    	coinsLabel.setText("" + gameBoard.getCoins());
	    });
        GridBagConstraints gbcCoins = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 230, 100, 60, 210);
        hardPanel.add(coinsLabel, gbcCoins);
    }
    
    private void createStepsLabel() {
    	JLabel stepsLabel = ResourceLoader.createStyledLabel("STEPS: " + String.valueOf(
    			gameBoard.getSteps()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
	    gameBoard.setOnStepChange(() -> {
	    	stepsLabel.setText("STEPS: " + gameBoard.getSteps());
	    });
	    GridBagConstraints gbcSteps = ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTHWEST, 
                GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 20, 300, 60, 0);
	    hardPanel.add(stepsLabel, gbcSteps);  	
    }
    
    private void createScoresLabel() {
    	JLabel scoresLabel = ResourceLoader.createStyledLabel("SCORE: " + String.valueOf(
    			gameBoard.getScores()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
	    gameBoard.setOnScoreChange(() -> {
	    	scoresLabel.setText("SCORE: " + gameBoard.getScores());
	    });
	    GridBagConstraints gbcScoresLabel = ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTH,
	    		GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 20, 650, 60, 0);
	    
	    hardPanel.add(scoresLabel, gbcScoresLabel);
    }
    
    @Override
    public void refresh() {
        if (gameBoard != null) {
        	gameBoard.resetBoard(); 
        }
    }
}