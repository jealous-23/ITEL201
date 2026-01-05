package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import finalproject.gamestate.Achievement;
import finalproject.gamestate.GameManager;
import finalproject.ui.components.BackgroundPanel;
import finalproject.ui.components.GradientButton;
import finalproject.util.Refreshable;
import finalproject.util.ResourceLoader;

public class PlayerRecordScorePanel extends AbstractTilePacerPanel implements Refreshable {
	private static final long serialVersionUID = 1L;
	private final TilePacerFrame parentFrame;
	private final Map<String, JLabel> scoreLabels = new HashMap<>();
	
	private BackgroundPanel playerRecordPanel;
	private JPanel achievementContentPanel;
	private JLabel creditsLabel; 
	
	public PlayerRecordScorePanel(TilePacerFrame parentFrame) {
        this.parentFrame = parentFrame;
        
        parentFrame.currentGame.addEconomyListener(this::refresh);
        parentFrame.currentGame.addDifficultyListener(this::refresh);

        createPanel();
    }
	
	@Override
	protected void createPanel() {
	    playerRecordPanel = new BackgroundPanel(BACKGROUND_ICON);
	    playerRecordPanel.setLayout(new GridBagLayout());

	    addMainTitle();

	    JPanel miniCardPanel = createNavigationTabs();

	    GridBagConstraints gbcMiniPanel = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER,
	    		GridBagConstraints.NONE, 0, 2, 1.0, 1.0, 30, 0, 0, 0);
	    playerRecordPanel.add(miniCardPanel, gbcMiniPanel);

	    ResourceLoader.addBackButtonToPanel(parentFrame, playerRecordPanel);
	    this.setLayout(new BorderLayout());
	    this.add(playerRecordPanel, BorderLayout.CENTER);
	}

	@Override
	protected void addMainTitle() {
	    JLabel titleLabel = ResourceLoader.createStyledLabel("PLAYER RECORDS", 
	    		"Tile Pacer", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	    		GridBagConstraints.NONE, 0, 0, 1.0, 0.2, 20, 0, 60, 0);
	    playerRecordPanel.add(titleLabel, gbcTitle);
	}

	private JPanel createNavigationTabs() {
	    CardLayout cardLayout = new CardLayout();
	    JPanel miniCardPanel = new JPanel(cardLayout);
	    miniCardPanel.setOpaque(false);
	    miniCardPanel.setPreferredSize(new Dimension(900, 450));

	    miniCardPanel.add(scorePanel(), "SCORES");
	    miniCardPanel.add(createAchPanel(), "ACHIEVEMENTS");

	    JPanel scoreHighlight = createHighlightBar(true);
	    JPanel achievementHighlight = createHighlightBar(false);

	    GradientButton scoreButtton = createTabButton("SCORES", 70f, e -> {
	        cardLayout.show(miniCardPanel, "SCORES");
	        scoreHighlight.setVisible(true);
	        achievementHighlight.setVisible(false);
	    });

	    GradientButton achButton = createTabButton("ACHIEVEMENTS", 35f, e -> {
	        cardLayout.show(miniCardPanel, "ACHIEVEMENTS");
	        scoreHighlight.setVisible(false);
	        achievementHighlight.setVisible(true);
	    });

	    addNavContainer(scoreButtton, scoreHighlight, GridBagConstraints.NORTHWEST);
	    addNavContainer(achButton, achievementHighlight, GridBagConstraints.NORTHEAST);

	    return miniCardPanel;
	}

	private GradientButton createTabButton(String text, float fontSize, ActionListener action) {
	    GradientButton button = ResourceLoader.createStyledGradientButton(text, 
	            new Color(0x008B8B), new Color(0x006A80), 
	            new Color(0x008B8B), new Color(0xfefefe), 
	            true, 50, fontSize, 300, 120);
	    button.addActionListener(action);
	    return button;
	}

	private JPanel createHighlightBar(boolean initiallyVisible) {
	    JPanel bar = new JPanel();
	    bar.setBackground(new Color(0x40E0D0));
	    bar.setPreferredSize(new Dimension(300, 5));
	    bar.setVisible(initiallyVisible);
	    return bar;
	}

	private void addNavContainer(JButton btn, JPanel highlight, int anchor) {
	    JPanel container = new JPanel(new BorderLayout());
	    container.setOpaque(false);
	    container.add(btn, BorderLayout.CENTER);
	    container.add(highlight, BorderLayout.SOUTH);
	    
	    GridBagConstraints gbc = ResourceLoader.createLayoutConstraints(anchor,
	    		GridBagConstraints.NONE, 0, 0, 0.0, 0.0, 150, 120, 120, 120);
	    playerRecordPanel.add(container, gbc);
	}

	private JPanel scorePanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(new Color(0, 0, 0, 125));
	   
	    JLabel title = ResourceLoader.createStyledLabel("HIGHEST SCORES", "Tile Pacer", 
	            new Color(0x40E0D0), 25f, SwingConstants.CENTER);
	    panel.add(title, ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	            GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0));
	   
	    addScoreRow(panel, "EASY", GameManager.Difficulty.EASY, new Color(0x4CAF50), new Color(0x2E7D32), 50, 70);
	    addScoreRow(panel, "MEDIUM", GameManager.Difficulty.MEDIUM, new Color(0xFFC107), new Color(0xFF8F00), 190, 220);
	    addScoreRow(panel, "HARD", GameManager.Difficulty.HARD, new Color(0xF44336), new Color(0xD32F2F), 330, 370);

	    return panel;
	}
	
	private void addScoreRow(JPanel panel, String label, GameManager.Difficulty difficulty,
	        Color c1, Color c2, int btnTop, int textTop) {

	    GradientButton btn = ResourceLoader.createStyledGradientButton(label,
	        c1, c2, c1, new Color(0xfefefe), false, 50, 60f, 250, 110);
	    panel.add(btn, ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHWEST,
	        GridBagConstraints.NONE, 0, 0, 0.0, 0.0, btnTop, 10, 10, 20));

	    JLabel lbl = ResourceLoader.createStyledLabel(
	        parentFrame.currentGame.getHighScore(difficulty) + " POINTS",
	        "Tile Pacer", new Color(0xfefefe), 60f, SwingConstants.CENTER
	    );
	    panel.add(lbl, ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHEAST,
	        GridBagConstraints.NONE, 0, 0, 0.0, 0.0, textTop, 10, 10, 30));

	    scoreLabels.put(difficulty.name(), lbl); // ✅ store for refresh
	}

	private JPanel createAchPanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(new Color(0, 0, 0, 125));

	    JLabel title = ResourceLoader.createStyledLabel("ACHIEVEMENTS", "Tile Pacer", 
	            new Color(0x40E0D0), 25f, SwingConstants.CENTER);
	    panel.add(title, ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	            GridBagConstraints.NONE, 0, 0, 1.0, 0.0, 20, 0, 20, 0));

	    List<Achievement> achievements = List.of(
	    		new Achievement("FULL_DISCLOSURE", "Satellite", "Win the game without ever stepping on a \"Normal\" tile."),
	    	    new Achievement("DEEP_LEARNING", "AI", "Answer 3 consecutive Questions correctly without failing."),
	    	    new Achievement("CLEAR_PATH", "Firewall", "Activate the Firewall on a Hard difficulty board with at least 8 obstacles."),
	    	    new Achievement("LONG_STRIDE", "Trojan Horse", "Reach the END tile while still having more than 50% of your starting steps left.")
	    );

	    achievementContentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
	    achievementContentPanel.setOpaque(false);

	    for (Achievement ach : achievements) {
	    	achievementContentPanel.add(createAchievementRow(ach));
	    }

	    JScrollPane scrollPane = new JScrollPane(achievementContentPanel);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

	    panel.add(scrollPane, ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
	            GridBagConstraints.BOTH, 0, 1, 1.0, 1.0, 10, 40, 40, 40));
	    
	    return panel;
	}
	
	private JPanel createAchievementRow(Achievement ach) {
	    JPanel row = new JPanel(new BorderLayout(10, 10));
	  
	    boolean unlocked = parentFrame.currentGame.isAchievementUnlocked(ach.getId());
	    boolean isEquipped = parentFrame.currentGame.isArtifactEquipped(ach.getId());
	    
	    row.setOpaque(true);
	    Color bgColor = unlocked ? (isEquipped ? new Color(0x004D4D) : new Color(0x1A4D4D)) : new Color(40, 40, 40);
	    row.setBackground(bgColor); 
	    row.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); 
	    JPanel textContainer = new JPanel(new GridLayout(2, 1));
	    textContainer.setOpaque(false);

	    JLabel nameLabel = ResourceLoader.createStyledLabel(ach.getTitle(), "Tile Pacer", 
	            unlocked ? new Color(0x40E0D0) : Color.GRAY, 20f, SwingConstants.LEFT);
	  
	    StringBuilder descBuilder = new StringBuilder(ach.getDescription());
	    if (unlocked) {
	        switch (ach.getId()) {
	            case "FULL_DISCLOSURE" -> descBuilder.append(" [Artifact: Satellite Scan]");
	            case "DEEP_LEARNING"   -> descBuilder.append(" [Artifact: AI Hint Bonus]");
	            case "CLEAR_PATH"      -> descBuilder.append(" [Artifact: Firewall Passive]");
	            case "LONG_STRIDE"     -> descBuilder.append(" [Artifact: Trojan Discount]");
	        }
	    }
	    
	    JLabel descLabel = ResourceLoader.createStyledLabel(descBuilder.toString(), "Arial", 
	            unlocked ? Color.WHITE : Color.DARK_GRAY, 14f, SwingConstants.LEFT);

	    textContainer.add(nameLabel);
	    textContainer.add(descLabel);
	    row.add(textContainer, BorderLayout.CENTER);
	   
	    String statusText;
	    Color statusColor;
	    
	    
	    if (!unlocked) {
	        statusText = "🔒 LOCKED";
	        statusColor = Color.GRAY;
	    } else if (isEquipped) {
	        statusText = "● EQUIPPED";
	        statusColor = Color.CYAN;
	    } else {
	        statusText = "✓ UNLOCKED";
	        statusColor = new Color(0x40E0D0);
	    }

	    JLabel statusLabel = ResourceLoader.createStyledLabel(statusText, "Orbiton", 
	            statusColor, 12f, SwingConstants.RIGHT);
	    row.add(statusLabel, BorderLayout.EAST);

	    return row;
	}
	
	@Override
    public void addNotify() {
        super.addNotify();
        System.out.println("Player connected to screen. Syncing data...");
        refresh();
    }
        
	@Override
	public void refresh() {
	    if (parentFrame.currentGame == null) {
	    	return;
	    }
	    
	    for (GameManager.Difficulty diff : GameManager.Difficulty.values()) {
	        JLabel lbl = scoreLabels.get(diff.name());
	        if (lbl != null) {
	            lbl.setText(parentFrame.currentGame.getHighScore(diff) + " POINTS");
	        }
	    }

	   
	    if (creditsLabel != null) {
	        creditsLabel.setText("CREDITS: " + parentFrame.currentGame.getTotalCoins());
	    }
	    
	    if (achievementContentPanel != null) {
	        achievementContentPanel.removeAll(); 
	        
	        List<Achievement> achievements = List.of(
	            new Achievement("FULL_DISCLOSURE", "Satellite", "Win the game without ever stepping on a \"Normal\" tile."),
	            new Achievement("DEEP_LEARNING", "AI", "Answer 5 consecutive Questions correctly without failing."),
	            new Achievement("CLEAR_PATH", "Firewall", "Activate the Firewall on a Hard difficulty board with at least 8 obstacles."),
	            new Achievement("LONG_STRIDE", "Trojan Horse", "Reach the END tile while still having more than 50% of your starting steps left.")
	        );

	        for (Achievement ach : achievements) {
	            achievementContentPanel.add(createAchievementRow(ach));
	        }
	    }

	    this.revalidate();
	    this.repaint();
	}
}