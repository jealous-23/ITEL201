package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import finalproject.ui.components.BackgroundPanel;
import finalproject.ui.components.CyberDialog;
import finalproject.ui.components.GradientButton;
import finalproject.util.Refreshable;
import finalproject.util.ResourceLoader;

public class ProfilePanel extends AbstractTilePacerPanel implements Refreshable {
	private static final long serialVersionUID = 1L;
    private final TilePacerFrame parentFrame;
    private BackgroundPanel profilePanel;
    
    private JPanel itemsContentPanel;
    private JLabel currentLevelLabel;
    private JLabel totalCoinsLabel;
    private JLabel totalExpLabel;
    
    public ProfilePanel(TilePacerFrame parentFrame) {
        this.parentFrame = parentFrame;
        
        this.parentFrame.currentGame.addEconomyListener(this::refresh);
        
        createPanel();
    }
    
    @Override
    protected void createPanel() {
        profilePanel = new BackgroundPanel(BACKGROUND_ICON);
        profilePanel.setLayout(new GridBagLayout());

        addProfileButton();
        addMainTitle();
        addMiniCardPanel();
        addLimitedItemsSection();

        ResourceLoader.addBackButtonToPanel(parentFrame, profilePanel);

        this.setLayout(new BorderLayout());
        this.add(profilePanel, BorderLayout.CENTER);
    }

    private void addProfileButton() {
        ImageIcon profileImage = new ImageIcon(ResourceLoader.createScaledImageFromResource("/assets/icons/newProfile.png", 300, 280));
        JButton profileButton = ResourceLoader.createTransparentButton(profileImage);
        GridBagConstraints gbcProfile = ResourceLoader.createLayoutConstraints(
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                0, 0, 1.0, 1.0,
                150, 190, 310, 100);
        profilePanel.add(profileButton, gbcProfile);
    }
    
    private void addMiniCardPanel() {
        JPanel miniCardPanel = new JPanel(new CardLayout());
        miniCardPanel.setOpaque(true);
        miniCardPanel.setPreferredSize(new Dimension(835, 350));
        miniCardPanel.add(new UserDetailsPanel(), "PROFILE");

        CardLayout miniCardLayout = (CardLayout) miniCardPanel.getLayout();
        miniCardLayout.show(miniCardPanel, "PROFILE");

        GridBagConstraints gbcMiniPanel = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
        		GridBagConstraints.NONE, 0, 0, 0.0, 0.0, 120, 550, 0, 0);
        profilePanel.add(miniCardPanel, gbcMiniPanel);
    }

    @Override
    protected void addMainTitle() {
    	JLabel titleLabel = ResourceLoader.createStyledLabel("PROFILE",
    			"Tile Pacer", new Color(0x40E0D0), 90f, SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	    		GridBagConstraints.NONE, 0, 0, 1.0, 0.2, 20, 0, 60, 0);
	    profilePanel.add(titleLabel, gbcTitle);
    }
    
    private class UserDetailsPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final JTextField nameField;

        public UserDetailsPanel() {
            super(new GridBagLayout());
            setBackground(new Color(0, 0, 0, 125));

            this.nameField = createNameTextField();

            addNameSection();
            addStatsSection();
        }

        private void addNameSection() {
            JLabel nameLabel = ResourceLoader.createStyledLabel("NAME: ", 
            		"DotGothic16", new Color(0x40E0D0), 50f, SwingConstants.LEFT);
            add(nameLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.WEST,
            		GridBagConstraints.NONE, 0, 0, 0.1, 1.0, 10, 100, 0, 10));

            add(nameField, ResourceLoader.createLayoutConstraints(GridBagConstraints.WEST,
            		GridBagConstraints.HORIZONTAL, 1, 0, 0.5, 1.0, 10, 10, 0, 10));

            GradientButton saveButton = ResourceLoader.createStyledGradientButton("SAVE", 
                    new Color(0x008B8B), new Color(0x006A80), 
                    new Color(0x008B8B), new Color(0xfefefe),
                    true, 100, 22f, 100, 50);
            
            saveButton.addActionListener(e -> {
                String newName = nameField.getText();
                parentFrame.currentGame.saveNewName(newName, parentFrame);
            });

            add(saveButton, ResourceLoader.createLayoutConstraints(GridBagConstraints.WEST,
            		GridBagConstraints.NONE, 2, 0, 0.2, 1.0, 10, 10, 0, 20));
        }

        private void addStatsSection() {
            JLabel levelLabel = ResourceLoader.createStyledLabel("LEVEL",
            		"Orbiton", new Color(0x40E0D0), 60f, SwingConstants.LEFT);
            add(levelLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.WEST,
            		GridBagConstraints.NONE, 0, 1, 1.0, 1.0, 0, 10, 70, 0));
            
            currentLevelLabel = ResourceLoader.createStyledLabel("" + parentFrame.currentGame.getCurrentLevel(),
            		"Tile Pacer", new Color(0x40E0D0), 40f, SwingConstants.LEFT);
            add(currentLevelLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTH,
            		GridBagConstraints.NONE, 0, 1, 1.0, 1.0, 120, 10, 70, 110));
            
            JLabel coinsLabel = ResourceLoader.createStyledLabel("COINS",
            		"Orbiton", new Color(0x40E0D0), 60f, SwingConstants.LEFT);
            add(coinsLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER,
            		GridBagConstraints.NONE, 1, 1, 1.0, 1.0, 0, 10, 70, 120));
            
            totalCoinsLabel = ResourceLoader.createStyledLabel("" + parentFrame.currentGame.getTotalCoins(),
            		"Tile Pacer", new Color(0x40E0D0), 40f, SwingConstants.LEFT);
            add(totalCoinsLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
            		GridBagConstraints.NONE, 1, 1, 1.0, 1.0, 120, 10, 70, 110));

            JLabel expLabel = ResourceLoader.createStyledLabel("EXP",
            		"Orbiton", new Color(0x40E0D0), 60f, SwingConstants.LEFT);
            add(expLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.EAST, 
            		GridBagConstraints.NONE, 2, 1, 1.0, 1.0, 0, 10, 70, 70));
            
            totalExpLabel = ResourceLoader.createStyledLabel("" + parentFrame.currentGame.getTotalExp(),
            		"Tile Pacer", new Color(0x40E0D0), 40f, SwingConstants.LEFT);
            add(totalExpLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.EAST, 
            		GridBagConstraints.NONE, 2, 1, 1.0, 1.0, 120, 10, 70, 60));
        }

        private JTextField createNameTextField() {
            JTextField field = new JTextField(parentFrame.currentGame.getName(), 25);
            field.setFont(ResourceLoader.loadCustomFont().deriveFont(37f));
            field.setBackground(new Color(30, 30, 30));
            field.setForeground(Color.WHITE);
            field.setCaretColor(Color.WHITE);
            field.setBorder(BorderFactory.createLineBorder(new Color(0x40E0D0), 2));
            return field;
        }
    }
    
    private void addLimitedItemsSection() {
        JPanel vaultContainer = new JPanel(new GridBagLayout());
        vaultContainer.setOpaque(false);
        vaultContainer.setPreferredSize(new Dimension(1500, 350)); 

        JLabel sectionHeader = ResourceLoader.createStyledLabel("ULTRA-TECH VAULT", 
                "Orbiton", new Color(0xFFD700), 30f, SwingConstants.CENTER);

        vaultContainer.add(sectionHeader, ResourceLoader.createLayoutConstraints(
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 1.0, 0.0, 0, 0, 15, 0));

        itemsContentPanel = new JPanel(new GridLayout(1, 0, 15, 0));
        itemsContentPanel.setOpaque(false);
        
        updateVaultItems();

        JScrollPane scrollPane = new JScrollPane(itemsContentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        vaultContainer.add(scrollPane, ResourceLoader.createLayoutConstraints(
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 1, 1.0, 1.0, 0, 0, 0, 0));

        profilePanel.add(vaultContainer, ResourceLoader.createLayoutConstraints(
                GridBagConstraints.SOUTH, GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 350, 0, 0, 0));
    }

    private JPanel createVaultItem(String name, String id, String iconPath, String desc, boolean unlocked) {
        JPanel itemCard = new JPanel(new GridBagLayout()); 
        itemCard.setPreferredSize(new Dimension(375, 300)); 
  
        boolean isActive = parentFrame.currentGame.isArtifactEquipped(id);

        if (isActive) {
            itemCard.setBackground(new Color(0, 100, 100, 200));
            itemCard.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
        } else {
            itemCard.setBackground(unlocked ? new Color(0, 60, 60, 160) : new Color(20, 20, 20, 220));
            itemCard.setBorder(BorderFactory.createLineBorder(unlocked ? new Color(0xFFD700) : Color.DARK_GRAY, 2));
        }

        ImageIcon icon = new ImageIcon(ResourceLoader.createScaledImageFromResource(iconPath, 120, 120));
        JLabel iconLabel = new JLabel(icon);
        if (!unlocked) iconLabel.setEnabled(false);

        JLabel nameLabel = ResourceLoader.createStyledLabel(name, "Orbiton", 
                unlocked ? Color.WHITE : Color.GRAY, 22f, SwingConstants.CENTER);
    
        if (unlocked && !isActive) {
            GradientButton activateBtn = ResourceLoader.createStyledGradientButton(
                    "500 GEMS TO ACTIVATE", 
                    new Color(0x9400D3), new Color(0x4B0082), 
                    new Color(0xDA70D6), Color.WHITE, 
                    true, 10, 14f, 180, 70);
            
            activateBtn.addActionListener(e -> buyArtifactActivation(id, 500));
            
            itemCard.add(activateBtn, ResourceLoader.createLayoutConstraints(
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 3, 1.0, 0.2, 5, 10, 10, 10));
        }

        String statusText = !unlocked ? "ENCRYPTED" : (isActive ? "● SYSTEM ACTIVE" : "READY");
        JLabel statusLabel = ResourceLoader.createStyledLabel(statusText, "Orbiton", 
                isActive ? Color.CYAN : (unlocked ? new Color(0x40E0D0) : Color.RED), 12f, SwingConstants.CENTER);

        itemCard.add(iconLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0, 1.0, 0.6, 20, 0, 10, 0));
        itemCard.add(nameLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 1, 1.0, 0.2, 0, 10, 5, 10));
        itemCard.add(statusLabel, ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 2, 1.0, 0.2, 0, 10, 20, 10));

        itemCard.setToolTipText("<html><body style='padding:5px;'><b>" + name + "</b><br>" + desc + "</body></html>");

        return itemCard;
    }
    
    private void updateVaultItems() {
        if (itemsContentPanel == null) {
        	return;
        }
        
        itemsContentPanel.removeAll(); 
        
        itemsContentPanel.add(createVaultItem("SATELLITE", "FULL_DISCLOSURE", "/assets/limiteditems/satellite.png",
                "Full Map Vision", parentFrame.currentGame.isAchievementUnlocked("FULL_DISCLOSURE")));

        itemsContentPanel.add(createVaultItem("AI CORE", "DEEP_LEARNING", "/assets/limiteditems/ai.png", 
                "Quiz Assist", parentFrame.currentGame.isAchievementUnlocked("DEEP_LEARNING")));

        itemsContentPanel.add(createVaultItem("FIREWALL", "CLEAR_PATH", "/assets/limiteditems/firewall.png", 
                "Obstacle Clearer", parentFrame.currentGame.isAchievementUnlocked("CLEAR_PATH")));

        itemsContentPanel.add(createVaultItem("TROJAN", "LONG_STRIDE", "/assets/limiteditems/trojan.png", 
                "Shop Discounts", parentFrame.currentGame.isAchievementUnlocked("LONG_STRIDE")));

        itemsContentPanel.revalidate();
        itemsContentPanel.repaint();
    }
    
    private void buyArtifactActivation(String id, int gemCost) {
        if (parentFrame.currentGame.getTotalGems() >= gemCost) { 
            parentFrame.currentGame.spendGems(gemCost);
            parentFrame.currentGame.equipArtifact(id);
            CyberDialog.showMessage(parentFrame, "Artifact System Online: " + id, "OK", 500, 250);
        } else {
            CyberDialog.showMessage(parentFrame, "Insufficient Gems (EXP)!", "ERROR", 500, 250);
        }
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        System.out.println("Profile panel connected to screen. Syncing data...");
        refresh();
    }
    
    @Override
    public void refresh() {
        if (currentLevelLabel != null && parentFrame.currentGame != null) {
            currentLevelLabel.setText("" + parentFrame.currentGame.getCurrentLevel());
        }
        
        if (totalCoinsLabel != null && parentFrame.currentGame != null) {
            totalCoinsLabel.setText("" + parentFrame.currentGame.getTotalCoins());
        }
        
        if (totalExpLabel != null && parentFrame.currentGame != null) {
            totalExpLabel.setText("" + parentFrame.currentGame.getTotalGems()); 
        }

        updateVaultItems();

        this.revalidate();
        this.repaint();
    }
      
}