package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import finalproject.ui.components.BackgroundPanel;
import finalproject.ui.components.CyberDialog;
import finalproject.ui.components.GradientButton;
import finalproject.util.Refreshable;
import finalproject.util.ResourceLoader;

public class ShopPanel extends AbstractTilePacerPanel implements Refreshable {

    private static final long serialVersionUID = 1L;
    private final TilePacerFrame parentFrame;
    private BackgroundPanel shopPanel;
    private JPanel itemContainer;
    private JLabel coinLabel;
    private JPanel inventoryListContainer;
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private static final String SHOP_VIEW = "SHOP_VIEW";
    private static final String ITEMS_BOUGHT_VIEW = "ITEMS_BOUGHT_VIEW";

    public ShopPanel(TilePacerFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.parentFrame.currentGame.addEconomyListener(this::refresh);
        createPanel();
    }

    @Override
    protected void createPanel() {
        shopPanel = new BackgroundPanel(BACKGROUND_ICON);
        shopPanel.setLayout(new GridBagLayout());
        
        addMainTitle();
        addCurrencyDisplay();
        setupMainContentArea();
        addItemsBoughtButton();
        
        ResourceLoader.addBackButtonToPanel(parentFrame, shopPanel);
        setLayout(new BorderLayout());
        add(shopPanel, BorderLayout.CENTER);
    }

    @Override
    protected void addMainTitle() {
        JLabel titleLabel = ResourceLoader.createStyledLabel("SHOP", 
        		"Tile Pacer", new Color(0x40E0D0), 60f, SwingConstants.CENTER);
        GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
        		GridBagConstraints.NONE, 0, 0, 1.0, 0.0, 40, 0, 20, 0);
        shopPanel.add(titleLabel, gbcTitle);
    }

    private void addCurrencyDisplay() {
        JPanel statsBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        statsBar.setOpaque(false);
        coinLabel = ResourceLoader.createStyledLabel("CREDITS: " + parentFrame.currentGame.getTotalCoins(), 
        		"Orbiton", new Color(0xFFD700), 24f, SwingConstants.CENTER);
        statsBar.add(coinLabel);
        GridBagConstraints gbc = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
        		GridBagConstraints.HORIZONTAL, 0, 1, 1.0, 0.0, 10, 0, 20, 0);
        shopPanel.add(statsBar, gbc);
    }

    private void setupMainContentArea() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        JPanel shopItemsView = createShopItemsView();
        cardPanel.add(shopItemsView, SHOP_VIEW);

        JPanel itemsBoughtView = addItemsBoughtPanel();
        cardPanel.add(itemsBoughtView, ITEMS_BOUGHT_VIEW);

        GridBagConstraints gbcCardPanel = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
        		GridBagConstraints.BOTH, 0, 2, 1.0, 1.0, 10, 40, 40, 40);
        shopPanel.add(cardPanel, gbcCardPanel);
    }

    private JPanel createShopItemsView() {
        JPanel shopItemsView = new JPanel(new BorderLayout());
        shopItemsView.setOpaque(false);

        itemContainer = new JPanel(new GridLayout(0, 2, 20, 20)); 
        itemContainer.setOpaque(false);
        
        addShopItem("DRONE", getIconPathForItem("DRONE"), "Reveals a 3x3 area of fog.", 150);
        addShopItem("GOOGLE", getIconPathForItem("GOOGLE"), "Highlights the correct answer once.", 200);
        addShopItem("REBOOT", getIconPathForItem("REBOOT"), "Makes obstacle tiles passable.", 300);
        addShopItem("BACKDOOR", getIconPathForItem("BACKDOOR"), "Undoes a previous movement.", 500);
        addShopItem("WORMS", getIconPathForItem("WORMS"), "Copy current tile effects to another.", 400);
        addShopItem("RNGESUS", getIconPathForItem("RNGESUS"), "Gives 50% success rate on Gamble Tiles.", 250);

        JScrollPane scrollPane = new JScrollPane(itemContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        shopItemsView.add(scrollPane, BorderLayout.CENTER);
        
        return shopItemsView;
    }

    private void addShopItem(String name, String image, String desc, int price) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(0, 40, 40, 180));
        card.setBorder(BorderFactory.createLineBorder(new Color(0x40E0D0), 1));
        
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        
        JLabel nameLabel = ResourceLoader.createStyledLabel(name, "Orbiton",
        		Color.WHITE, 20f, SwingConstants.LEFT);
        JLabel descLabel = ResourceLoader.createStyledLabel("<html>" + desc + "</html>", "Orbiton", Color.GRAY, 15f, SwingConstants.LEFT);
        info.add(nameLabel);
        info.add(descLabel);
        
        GradientButton buyButton = new GradientButton(price + " C", 
        		new Color(0x008B8B), new Color(0x004040), 
        		Color.CYAN, 0, true);
        buyButton.setPreferredSize(new Dimension(100, 40));
        buyButton.addActionListener(e -> processPurchase(name, price));
        
        ImageIcon itemIcon = new ImageIcon(ResourceLoader.createScaledImageFromResource(image, 180, 150));
        JButton itemFrame = ResourceLoader.createTransparentButton(itemIcon);
        itemFrame.setDisabledIcon(itemIcon);
        itemFrame.setOpaque(false);
        itemFrame.setBorderPainted(false);
        itemFrame.setContentAreaFilled(false);
        itemFrame.setFocusPainted(false);
        itemFrame.setEnabled(false);
        card.add(itemFrame, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(buyButton, BorderLayout.EAST);
        itemContainer.add(card);
    }

    private void processPurchase(String item, int price) {
        if (parentFrame.currentGame.getTotalCoins() >= price) {
            parentFrame.currentGame.addRewards(-price, 0);
            parentFrame.currentGame.recordPurchase(item);
            SwingUtilities.invokeLater(() -> {
                refresh();
                CyberDialog.showSuccess(parentFrame, "ACQUIRED: " + item, "CLOSE", 500, 250);
            });
        } else {
            CyberDialog.showFailure(parentFrame, String.format("%28s", "Insufficient Credits"), "CLOSE", 500, 250);
        }
    }

    private void addItemsBoughtButton() {
        ImageIcon itemsBoughtIcon = new ImageIcon(ResourceLoader.createScaledImageFromResource("/assets/icons/items_bought_button.png", 150, 120));
        JButton itemsBoughtButton = ResourceLoader.createTransparentButton(itemsBoughtIcon);

        itemsBoughtButton.addActionListener(e -> {
            updateItemsBoughtDisplay();
            cardLayout.show(cardPanel, ITEMS_BOUGHT_VIEW);
        });

        GridBagConstraints gbcItemsBought = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTHEAST, 
        		GridBagConstraints.NONE,0, 0, 0.0, 0.0, 20, 20, 20, 20);
        shopPanel.add(itemsBoughtButton, gbcItemsBought);
    }

    private JPanel addItemsBoughtPanel() {
        JPanel itemPanel = new JPanel(new GridBagLayout());
        itemPanel.setOpaque(false);

        JLabel title = ResourceLoader.createStyledLabel("ITEMS BOUGHT", 
                "Tile Pacer", new Color(0x40E0D0), 25f, SwingConstants.CENTER);
        itemPanel.add(title, ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH,
                GridBagConstraints.NONE, 0, 0, 1.0, 0.0, 20, 0, 40, 0));
        
        inventoryListContainer = new JPanel(new GridLayout(0, 2, 30, 30)); 
        inventoryListContainer.setOpaque(false);
        inventoryListContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(inventoryListContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        itemPanel.add(scrollPane, ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 0, 1, 1.0, 1.0, 10, 40, 10, 40));

        GradientButton backButton = ResourceLoader.createStyledGradientButton("BACK TO SHOP", 
                new Color(0x008B8B), new Color(0x006A80), 
                new Color(0x008B8B), new Color(0xfefefe),
                true, 15, 15f, 170, 100);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, SHOP_VIEW));
        
        itemPanel.add(backButton, ResourceLoader.createLayoutConstraints(GridBagConstraints.SOUTH,
                GridBagConstraints.NONE, 0, 2, 0.0, 0.0, 20, 0, 20, 0));

        return itemPanel;
    }
    
    private void updateItemsBoughtDisplay() {
        if (inventoryListContainer == null) {
        	return;
        }

        inventoryListContainer.removeAll();
        
        String[] items = {"DRONE", "GOOGLE", "REBOOT", "BACKDOOR", "WORMS", "RNGESUS"};
        
        for (String name : items) {
            int count = parentFrame.currentGame.getInventoryCount(name);
            if (count > 0) {
                inventoryListContainer.add(createInventoryRow(name, getIconPathForItem(name), count));
            }
        }

        inventoryListContainer.revalidate();
        inventoryListContainer.repaint();
    }

    private JPanel createInventoryRow(String name, String image, int qty) {
        int cardWidth = 370;
        int cardHeight = 300;

        JPanel card = new JPanel(new BorderLayout()); 
        card.setBackground(new Color(0, 25, 25, 230));

        Border lineBorder = BorderFactory.createLineBorder(new Color(0x40E0D0), 2);
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        card.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));

        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setMinimumSize(new Dimension(cardWidth, cardHeight));

        JLabel nameLabel = ResourceLoader.createStyledLabel(name,
        		"Orbiton", Color.WHITE, 16f, SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); 
        card.add(nameLabel, BorderLayout.NORTH);
        
        ImageIcon itemIcon = new ImageIcon(ResourceLoader.createScaledImageFromResource(image, 180, 150));
        JButton itemFrame = ResourceLoader.createTransparentButton(itemIcon);
        itemFrame.setDisabledIcon(itemIcon);
        itemFrame.setOpaque(false);
        itemFrame.setBorderPainted(false);
        itemFrame.setContentAreaFilled(false);
        itemFrame.setFocusPainted(false);
        itemFrame.setEnabled(false);
        card.add(itemFrame, BorderLayout.CENTER);

        JPanel qtyBadge = new JPanel();
        qtyBadge.setBackground(new Color(0x40E0D0));
        qtyBadge.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 

        JLabel qtyLabel = ResourceLoader.createStyledLabel("QTY: " + qty, 
        		"Orbiton", Color.BLACK, 14f, SwingConstants.CENTER);
        qtyBadge.add(qtyLabel);
        card.add(qtyBadge, BorderLayout.SOUTH);

        return card;
    }
    
    private String getIconPathForItem(String name) {
        return switch (name.toUpperCase().trim()) {
            case "DRONE" -> "/assets/items/drone.png";
            case "BACKDOOR" -> "/assets/items/backdoor.png";
            case "GOOGLE" -> "/assets/items/google.png";
            case "REBOOT" -> "/assets/items/restart.png";
            case "WORMS" -> "/assets/items/worm_item.png";
            case "RNGESUS" -> "/assets/items/rngesus.png";
            default -> "default_item.png"; 
        };
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        System.out.println("Shop connected to screen. Syncing data...");
        refresh();
    }
    
    @Override
    public void refresh() {
        if (coinLabel != null && parentFrame.currentGame != null) {
            coinLabel.setText("CREDITS: " + parentFrame.currentGame.getTotalCoins());
            updateItemsBoughtDisplay();
        }
        this.revalidate();
        this.repaint();
    }

    /*@Override
    public void refresh() {
        if (coinLabel != null && parentFrame.currentGame != null) {
            coinLabel.setText("CREDITS: " + parentFrame.currentGame.getTotalCoins());
        }
        this.revalidate();
        this.repaint();
    }*/
}