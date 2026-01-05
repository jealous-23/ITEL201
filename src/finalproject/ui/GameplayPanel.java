package finalproject.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import finalproject.gamestate.GameManager;
import finalproject.gamestate.TilePacerBoard;
import finalproject.ui.components.BackgroundPanel;
import finalproject.util.Refreshable;
import finalproject.util.ResourceLoader;

public class GameplayPanel extends AbstractTilePacerPanel implements Refreshable {
    private static final long serialVersionUID = 1L;

    private final TilePacerFrame parentFrame;
    private BackgroundPanel gameplayPanel;
    private TilePacerBoard gameBoard;

    private JLabel titleLabel;
    private JLabel expLabel;
    private JLabel coinsLabel;
    private JLabel stepsLabel;
    private JLabel scoresLabel;
    private JPanel activeItemsPanel;

    public GameplayPanel(TilePacerFrame parentFrame) {
        this.parentFrame = parentFrame;

        gameBoard = new TilePacerBoard(parentFrame.currentGame);
        parentFrame.currentGame.addEconomyListener(this::updateActiveItemsDisplay);

        createPanel();
    }

    @Override
    protected void createPanel() {
        gameplayPanel = new BackgroundPanel(BACKGROUND_ICON);
        gameplayPanel.setLayout(new GridBagLayout());

        addGameBoard();
        addMainTitle();
        addGemIcon();
        createExpLabel();
        addCoinIcon();
        createCoinsLabel();
        createStepsLabel();
        createScoresLabel();
        createActiveItemsDisplay();

        ResourceLoader.addBackButtonToPanel(parentFrame, gameplayPanel, gameBoard);

        setLayout(new BorderLayout());
        add(gameplayPanel, BorderLayout.CENTER);
    }

    private void addGameBoard() {
        GridBagConstraints gbcBoard = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                0, 0, 1.0, 1.0, 50, 50, 50, 50
        );
        gameplayPanel.add(gameBoard, gbcBoard);
    }

    @Override
    protected void addMainTitle() {
        titleLabel = ResourceLoader.createStyledLabel(
                getDifficultyText(), "Tile Pacer", new Color(0x40E0D0), 90f, SwingConstants.CENTER
        );
        GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.NORTH, GridBagConstraints.NONE,
                0, 0, 1.0, 1.0, 20, 0, 60, 0
        );
        gameplayPanel.add(titleLabel, gbcTitle);
    }

    private void addGemIcon() {
        ImageIcon gemImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("assets/icons/gem.png", 110, 90));
        JButton gemFrame = ResourceLoader.createTransparentButton(gemImage);

        GridBagConstraints gbcGem = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                0, 0, 1.0, 1.0, 130, 100, 60, 150
        );
        gameplayPanel.add(gemFrame, gbcGem);
    }

    private void createExpLabel() {
        expLabel = ResourceLoader.createStyledLabel(
                String.valueOf(gameBoard.getExpEarned()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER
        );
        gameBoard.setOnExpChange(() -> expLabel.setText(String.valueOf(gameBoard.getExpEarned())));

        GridBagConstraints gbcExp = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                0, 0, 1.0, 1.0, 230, 152, 60, 170
        );
        gameplayPanel.add(expLabel, gbcExp);
    }

    private void addCoinIcon() {
        ImageIcon coinImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("assets/icons/coin.png", 120, 100));
        JButton coinFrame = ResourceLoader.createTransparentButton(coinImage);

        GridBagConstraints gbcCoin = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                0, 0, 1.0, 1.0, 130, 100, 60, 170
        );
        gameplayPanel.add(coinFrame, gbcCoin);
    }

    private void createCoinsLabel() {
        coinsLabel = ResourceLoader.createStyledLabel(
                String.valueOf(gameBoard.getCoinsEarned()), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER
        );
        gameBoard.setOnCoinChange(() -> coinsLabel.setText(String.valueOf(gameBoard.getCoinsEarned())));

        GridBagConstraints gbcCoins = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                0, 0, 1.0, 1.0, 230, 100, 60, 210
        );
        gameplayPanel.add(coinsLabel, gbcCoins);
    }

    private void createStepsLabel() {
        stepsLabel = ResourceLoader.createStyledLabel(
                "STEPS: " + gameBoard.getSteps(), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER
        );
        gameBoard.setOnStepChange(() -> stepsLabel.setText("STEPS: " + gameBoard.getSteps()));

        GridBagConstraints gbcSteps = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                0, 0, 0.0, 1.0, 20, 300, 60, 0
        );
        gameplayPanel.add(stepsLabel, gbcSteps);
    }

    private void createScoresLabel() {
        scoresLabel = ResourceLoader.createStyledLabel(
                "SCORE: " + gameBoard.getScores(), "Orbiton", new Color(0x40E0D0), 50f, SwingConstants.CENTER
        );
        gameBoard.setOnScoreChange(() -> scoresLabel.setText("SCORE: " + gameBoard.getScores()));

        GridBagConstraints gbcScores = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                0, 0, 0.0, 1.0, 20, 650, 60, 0
        );
        gameplayPanel.add(scoresLabel, gbcScores);
    }

    private void createActiveItemsDisplay() {
        activeItemsPanel = new JPanel(new GridLayout(0, 1));
        activeItemsPanel.setOpaque(false);
        updateActiveItemsDisplay();

        GridBagConstraints gbcActive = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                0, 0, 0.0, 1.0, 20, 300, 60, 20
        );
        gameplayPanel.add(activeItemsPanel, gbcActive);
    }

    private void updateActiveItemsDisplay() {
        activeItemsPanel.removeAll();
        List<String> active = new ArrayList<>();
        GameManager gm = parentFrame.currentGame;

        if (gm.isArtifactEquipped("CLEAR_PATH")) active.add("Firewall (Artifact)");
        if (gm.isArtifactEquipped("FULL_DISCLOSURE")) active.add("Satellite (Artifact)");
        if (gm.isArtifactEquipped("DEEP_LEARNING")) active.add("AI Bonus (Artifact)");
        if (gm.isArtifactEquipped("LONG_STRIDE")) active.add("Trojan (Artifact)");

        for (String item : new String[]{"WORMS", "BACKDOOR", "DRONE", "GOOGLE", "REBOOT", "RNGESUS"}) {
            int count = gm.getInventoryCount(item);
            if (count > 0) active.add(item + ": " + count);
        }

        for (String item : active) {
            JLabel label = ResourceLoader.createStyledLabel(item, "Orbiton", new Color(0x40E0D0), 30f, SwingConstants.LEFT);
            activeItemsPanel.add(label);
        }

        activeItemsPanel.revalidate();
        activeItemsPanel.repaint();
    }

    private String getDifficultyText() {
        GameManager.Difficulty diff = parentFrame.currentGame.getActiveDifficulty();
        return switch (diff) {
            case EASY -> "EASY";
            case MEDIUM -> "MEDIUM";
            case HARD -> "HARD";
            default -> "TILE PACER";
        };
    }

    @Override
    public void refresh() {
        if (gameBoard != null) {
            gameBoard.resetBoard();
        }

        titleLabel.setText(getDifficultyText());
        updateActiveItemsDisplay();
        coinsLabel.setText(String.valueOf(gameBoard.getCoinsEarned()));
        stepsLabel.setText("STEPS: " + gameBoard.getSteps());
        scoresLabel.setText("SCORE: " + gameBoard.getScores());
        expLabel.setText(String.valueOf(gameBoard.getExpEarned()));
    }
}
