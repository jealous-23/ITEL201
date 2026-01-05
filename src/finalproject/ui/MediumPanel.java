package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import finalproject.gamestate.GameManager;
import finalproject.gamestate.TilePacerBoard;
import finalproject.ui.components.BackgroundPanel;
import finalproject.util.Refreshable;
import finalproject.util.ResourceLoader;

public class MediumPanel extends AbstractTilePacerPanel implements Refreshable {
    private static final long serialVersionUID = 1L;
    private final TilePacerFrame parentFrame;
    private BackgroundPanel mediumPanel;
    private TilePacerBoard gameBoard;
    private JPanel activeItemsPanel;
    
    public MediumPanel(TilePacerFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.parentFrame.currentGame.applyDifficulty(GameManager.Difficulty.MEDIUM);
        parentFrame.currentGame.addEconomyListener(() -> updateActiveItemsDisplay());
        createPanel();
    }
    
    @Override
    protected void createPanel() {
        mediumPanel = new BackgroundPanel(BACKGROUND_ICON);
        mediumPanel.setLayout(new GridBagLayout());
        
        createMediumTiles();
        addMainTitle();
        addGemIcon();
        createExpLabel();
        addCoinIcon();
        createCoinsLabel();   
        createStepsLabel();
        createScoresLabel();
        createActiveItemsDisplay();
        
        ResourceLoader.addBackButtonToPanel(parentFrame, mediumPanel, gameBoard);
        
        this.setLayout(new BorderLayout());
        this.add(mediumPanel, BorderLayout.CENTER);
    }
    
    private void createMediumTiles() { 
        gameBoard = new TilePacerBoard(parentFrame.currentGame);
        GridBagConstraints gbcMedium = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER,
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 50, 50, 50, 50);
        mediumPanel.add(gameBoard, gbcMedium);
    }
    
    @Override
    protected void addMainTitle() {
        JLabel titleLabel = ResourceLoader.createStyledLabel("MEDIUM",
                "Tile Pacer", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
        GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0);
        mediumPanel.add(titleLabel, gbcTitle);
    }
    
    /*private void addGemIcon() {
        try {
            ImageIcon gemImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("/assets/icons/gem.png", 110, 90));
            if (gemImage.getImage() == null) {
                System.err.println("Failed to load gem.png");
                return;  // Or use a default image
            }
            JButton gemFrame = ResourceLoader.createTransparentButton(gemImage);
            
            GridBagConstraints gbcGemIcon = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 130, 100, 60, 150);
            mediumPanel.add(gemFrame, gbcGemIcon);
        } catch (Exception e) {
            System.err.println("Error loading gem icon: " + e.getMessage());
        }
    }*/
    
    private void addGemIcon() {
        ImageIcon gemImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("assets/icons/gem.png", 110, 90));
        JButton gemFrame = ResourceLoader.createTransparentButton(gemImage); 
        
        GridBagConstraints gbcGemIcon = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 130, 100, 60, 150);
        mediumPanel.add(gemFrame, gbcGemIcon);
    }
    
    private void createExpLabel() {
        JLabel expLabel = ResourceLoader.createStyledLabel("" + String.valueOf(
                gameBoard.getExp()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
        gameBoard.setOnExpChange(() -> {
            expLabel.setText("" + gameBoard.getExp());
        });
        GridBagConstraints gbcExp = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHWEST, 
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 230, 152, 60, 170);
        mediumPanel.add(expLabel, gbcExp);
    }      
    
    /*private void addCoinIcon() {
        try {
            ImageIcon coinImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("/assets/icons/coin.png", 120, 100));
            if (coinImage.getImage() == null) {
                System.err.println("Failed to load coin.png");
                return;
            }
            JButton coinFrame = ResourceLoader.createTransparentButton(coinImage);
            
            GridBagConstraints gbcCoinIcon = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHEAST,
                    GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 130, 100, 60, 170);
            mediumPanel.add(coinFrame, gbcCoinIcon);
        } catch (Exception e) {
            System.err.println("Error loading coin icon: " + e.getMessage());
        }
    }*/

    
    private void addCoinIcon() {
        ImageIcon coinImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("assets/icons/coin.png", 120, 100));
        JButton coinFrame = ResourceLoader.createTransparentButton(coinImage); 
        
        GridBagConstraints gbcCoinIcon = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 130, 100, 60, 170);
        mediumPanel.add(coinFrame, gbcCoinIcon);
    }
    
    private void createCoinsLabel() {
        JLabel coinsLabel = ResourceLoader.createStyledLabel(String.valueOf(
                gameBoard.getCoins()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
        gameBoard.setOnCoinChange(() -> {
            coinsLabel.setText("" + gameBoard.getCoins());
        });
        GridBagConstraints gbcCoins = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 230, 100, 60, 210);
        mediumPanel.add(coinsLabel, gbcCoins);
    }
    
    private void createStepsLabel() {
        JLabel stepsLabel = ResourceLoader.createStyledLabel("STEPS: " + String.valueOf(
                gameBoard.getSteps()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
        gameBoard.setOnStepChange(() -> {
            stepsLabel.setText("STEPS: " + gameBoard.getSteps());
        });
        GridBagConstraints gbcSteps = ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTHWEST, 
                GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 20, 300, 60, 0);
        mediumPanel.add(stepsLabel, gbcSteps);   
    }
    
    private void createScoresLabel() {
        JLabel scoresLabel = ResourceLoader.createStyledLabel("SCORE: " + String.valueOf(
                gameBoard.getScores()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER);
        gameBoard.setOnScoreChange(() -> {
            scoresLabel.setText("SCORE: " + gameBoard.getScores());
        });
        GridBagConstraints gbcScoresLabel = ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTH,
                GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 20, 650, 60, 0);
        
        mediumPanel.add(scoresLabel, gbcScoresLabel);
    }
    
    private void createActiveItemsDisplay() {
        activeItemsPanel = new JPanel();
        activeItemsPanel.setLayout(new GridLayout(0, 1));
        activeItemsPanel.setOpaque(false);
        
        updateActiveItemsDisplay();
        
        GridBagConstraints gbcActive = ResourceLoader.createLayoutConstraints(
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 0, 0, 0.0, 1.0, 20, 300, 60, 20);
        mediumPanel.add(activeItemsPanel, gbcActive);
    }
    
    private void updateActiveItemsDisplay() {
        activeItemsPanel.removeAll();
        
        List<String> activeList = getActiveItemsAndArtifacts();
        
        for (String item : activeList) {
            JLabel itemLabel = ResourceLoader.createStyledLabel(
                item, "Orbiton", new Color(0x40E0D0), 30f, SwingConstants.LEFT);
            activeItemsPanel.add(itemLabel);
        }
        
        activeItemsPanel.revalidate();
        activeItemsPanel.repaint();
    }
    
    private List<String> getActiveItemsAndArtifacts() {
        List<String> active = new ArrayList<>();
        GameManager gm = parentFrame.currentGame;
        
        if (gm.isArtifactEquipped("CLEAR_PATH")) active.add("Firewall (Artifact)");
        if (gm.isArtifactEquipped("FULL_DISCLOSURE")) active.add("Satellite (Artifact)");
        if (gm.isArtifactEquipped("DEEP_LEARNING")) active.add("AI Bonus (Artifact)");
        if (gm.isArtifactEquipped("LONG_STRIDE")) active.add("Trojan (Artifact)");
        
        int worms = gm.getInventoryCount("WORMS");
        if (worms > 0) {
            active.add("WORMS: " + worms);
        }
        
        int backdoor = gm.getInventoryCount("BACKDOOR");
        if (backdoor > 0) {
            active.add("BACKDOOR: " + backdoor);
        }
        
        int drone = gm.getInventoryCount("DRONE");
        if (drone > 0) {
            active.add("DRONE: " + drone);
        }
        
        int google = gm.getInventoryCount("GOOGLE");
        if (google > 0) {
            active.add("GOOGLE: " + google);
        }
        
        int reboot = gm.getInventoryCount("REBOOT");
        if (reboot > 0) {
            active.add("REBOOT: " + reboot);
        }
        
        int rngesus = gm.getInventoryCount("RNGESUS");
        if (rngesus > 0) {
            active.add("RNGESUS: " + rngesus);
        }
        
        return active;
    }
    
    @Override
    public void refresh() {
        if (gameBoard != null) {
            gameBoard.resetBoard();
        }
        updateActiveItemsDisplay();
    }
}