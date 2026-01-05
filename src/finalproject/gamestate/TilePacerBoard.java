package finalproject.gamestate;

import javax.imageio.ImageIO;
import javax.swing.*;
import finalproject.util.ResourceLoader;
import finalproject.util.SpriteAnimator;
import finalproject.ui.TilePacerFrame;
import finalproject.ui.components.CyberDialog;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class TilePacerBoard extends JPanel {
    
	private static final long serialVersionUID = 1L;
	private final int GRID_SIZE = 10;
    private GameTile[][] tiles = new GameTile[GRID_SIZE][GRID_SIZE];
    private GameManager gameManager;
    private JFrame parent;
    private TileInteractionManager interactionManager;
    private ItemEffectHandler itemHandler;
    
    private double drawX, drawY;
    private boolean isMoving = false, isWormsTargeting = false;
    private SpriteAnimator animator;
    private Timer moveTimer;
    private BackdoorSnapshot backdoorSnapshot;
    
    private int playerX = 0, playerY = 0, steps, scores, coinsEarned, expEarned, stepsTakenThisRun;
    private GameTile.TileType lastEffectTileType = null;
    private Runnable onStepChange, onScoreChange, onCoinChange, onExpChange;
    private double questionProb, obstacleProb, coinProb, expProb;

    public TilePacerBoard(GameManager gameManager) {
        this.gameManager = gameManager;
        this.interactionManager = new TileInteractionManager();
        this.itemHandler = new ItemEffectHandler(this, gameManager, (JFrame)SwingUtilities.getWindowAncestor(this));
        
        applyDifficulty(this.gameManager.getActiveDifficulty());
        this.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        initializeBoard();
        setupKeyBindings();
        animateAvatar();
        setupMoveTimer();
        updateFog();
    }

    private void setupKeyBindings() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("B"), "useBackdoor");
        this.getActionMap().put("useBackdoor", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { itemHandler.useBackdoor(); }
        });
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "useWorms");
        this.getActionMap().put("useWorms", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                parent = (JFrame) SwingUtilities.getWindowAncestor(TilePacerBoard.this);
                itemHandler.activateWorms(lastEffectTileType); 
            }
        });
    }

    private void applyDifficulty(GameManager.Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> { steps = 40; questionProb = 0.05; obstacleProb = 0.05; coinProb = 0.30; expProb = 0.15; }
            case HARD -> { steps = 20; questionProb = 0.25; obstacleProb = 0.20; coinProb = 0.10; expProb = 0.05; }
            default -> { steps = 30; questionProb = 0.15; obstacleProb = 0.10; coinProb = 0.20; expProb = 0.10; }
        }
        gameManager.resetNormalTileFlag();
    }

    private void initializeBoard() {
        Random rand = new Random();
        this.removeAll();
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                GameTile.TileType type = rollTileType(x, y, rand);
                tiles[y][x] = new GameTile(type, x, y);
                tiles[y][x].addActionListener(new TileClickListener());
                this.add(tiles[y][x]);
            }
        }
        if (gameManager.hasSatelliteEffect()) revealAllTiles();
        if (gameManager.hasFirewallEffect()) applyFirewallEffect();
    }

    private GameTile.TileType rollTileType(int x, int y, Random rand) {
        if (x == 0 && y == 0) return GameTile.TileType.START;
        if (x == GRID_SIZE - 1 && y == GRID_SIZE - 1) return GameTile.TileType.END;
        
        double roll = rand.nextDouble();
        double cumulative = 0;
        if (roll < (cumulative += obstacleProb)) return GameTile.TileType.OBSTACLE;
        if (roll < (cumulative += questionProb)) return GameTile.TileType.QUESTION;
        if (roll < (cumulative += 0.05)) return GameTile.TileType.TELEPORT;
        if (roll < (cumulative += 0.05)) return GameTile.TileType.GAMBLE;
        if (roll < (cumulative += 0.05)) return GameTile.TileType.BONUS_STEP;
        if (roll < (cumulative += coinProb)) return GameTile.TileType.COIN;
        if (roll < (cumulative += expProb)) return GameTile.TileType.GEM;
        return GameTile.TileType.NORMAL;
    }

    private class TileClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GameTile clicked = (GameTile) e.getSource();
            if (isMoving) return;
            parent = (JFrame) SwingUtilities.getWindowAncestor(TilePacerBoard.this);

            if (isWormsTargeting) {
                itemHandler.handleWormClick(clicked, lastEffectTileType, interactionManager);
                return;
            }

            int dx = clicked.x - playerX;
            int dy = clicked.y - playerY;
            if (Math.abs(dx) + Math.abs(dy) == 1 && steps > 0) {
                if (clicked.getTileType() == GameTile.TileType.OBSTACLE) {
                    if (gameManager.getInventoryCount("REBOOT") > 0) {
                        gameManager.useReboot();
                    } else if (clicked.getRevealed()) return;
                }

                if (dx != 0) animator.setFacingLeft(dx < 0);
                if (gameManager.hasBackdoor()) recordBackdoorSnapshot();

                tiles[playerY][playerX].setVisited(true);
                playerX = clicked.x; playerY = clicked.y;
                if (clicked.getTileType() == GameTile.TileType.NORMAL) gameManager.checkNormalTileStep();
                
                steps--;
                isMoving = true;

                if (gameManager.hasTrojanEffect() && ++stepsTakenThisRun % 3 == 0) steps++;
                if (gameManager.isDroneActive()) { applyDroneEffect(); gameManager.useDrone(); }

                moveTimer.start();
                if (onStepChange != null) onStepChange.run();
                scores += 10;
                if (onScoreChange != null) onScoreChange.run();

                if (!clicked.getVisited()) clicked.playSound(clicked.getTileType());
                
                SwingUtilities.invokeLater(() -> {
                    interactionManager.handleSpecialTile(clicked, TilePacerBoard.this, gameManager, parent);
                    checkGameState();
                });

                updateFog();
                repaint();
            }
        }
    }

    public int getPlayerX() { return playerX; }
    public void setPlayerX(int playerX) { this.playerX = playerX; }

    public int getPlayerY() { return playerY; }
    public void setPlayerY(int playerY) { this.playerY = playerY; }

    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }

    public int getScores() { return scores; }
    public void setScores(int scores) { this.scores = scores; }

    public int getCoinsEarned() { return coinsEarned; }
    public void setCoinsEarned(int coinsEarned) { this.coinsEarned = coinsEarned; }

    public int getExpEarned() { return expEarned; }
    public void setExpEarned(int expEarned) { this.expEarned = expEarned; }

    public void addScore(int s) { this.scores += s; }
    public void addCoins(int c) { this.coinsEarned += c; }
    public void addExp(int e) { this.expEarned += e; }
    public void reduceSteps(int s) { this.steps = Math.max(0, this.steps - s); }
    public void multiplyScore(int m) { this.scores *= m; }
    
    public void wipeAllCurrentRunStats() {
        this.scores = 0;
        this.coinsEarned = 0;
        this.expEarned = 0;
    }

    public void executeRandomTeleport() {
        Random rand = new Random();
        int nx, ny;
        do { nx = rand.nextInt(GRID_SIZE); ny = rand.nextInt(GRID_SIZE); } 
        while (tiles[ny][nx].getTileType() != GameTile.TileType.NORMAL);
        playerX = nx; playerY = ny;
        drawX = playerX * (getWidth() / GRID_SIZE);
        drawY = playerY * (getHeight() / GRID_SIZE);
        updateFog(); repaint();
    }

    public void restoreBackdoorSnapshot() {
        tiles[playerY][playerX].setVisited(false);
        tiles[playerY][playerX].setAsPlayerTile(false);
        playerX = backdoorSnapshot.x; playerY = backdoorSnapshot.y;
        steps = backdoorSnapshot.steps; scores = backdoorSnapshot.score;
        coinsEarned = backdoorSnapshot.coins; expEarned = backdoorSnapshot.exp;
        drawX = playerX * ((double)getWidth() / GRID_SIZE);
        drawY = playerY * ((double)getHeight() / GRID_SIZE);
        isMoving = false; moveTimer.stop(); backdoorSnapshot = null;
        updateFog(); repaint(); refreshUI();
    }

    private void recordBackdoorSnapshot() {
        backdoorSnapshot = new BackdoorSnapshot();
        backdoorSnapshot.x = playerX; backdoorSnapshot.y = playerY;
        backdoorSnapshot.steps = steps; backdoorSnapshot.score = scores;
        backdoorSnapshot.coins = coinsEarned; backdoorSnapshot.exp = expEarned;
    }

    private void applyFirewallEffect() {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (tiles[y][x].getTileType() == GameTile.TileType.OBSTACLE) {
                    tiles[y][x].setTileType(GameTile.TileType.NORMAL);
                    tiles[y][x].setRevealed(true);
                }
            }
        }
        repaint();
    }

    private void applyDroneEffect() {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int tx = playerX + dx, ty = playerY + dy;
                if (tx >= 0 && tx < GRID_SIZE && ty >= 0 && ty < GRID_SIZE) tiles[ty][tx].setRevealed(true);
            }
        }
        updateFog();
    }

    private void updateFog() {
        int[][] offsets = {{0,0}, {0,1}, {0,-1}, {1,0}, {-1,0}};
        for (int[] off : offsets) {
            int tx = playerX + off[0], ty = playerY + off[1];
            if (tx >= 0 && tx < GRID_SIZE && ty >= 0 && ty < GRID_SIZE) {
                boolean adj = (Math.abs(tx-playerX) + Math.abs(ty-playerY) <= 1);
                if (adj) { tiles[ty][tx].setRevealed(true); tiles[ty][tx].setReachable(true); }
            }
        }
    }

    public void refreshUI() {
        if (onStepChange != null) onStepChange.run();
        if (onScoreChange != null) onScoreChange.run();
        if (onCoinChange != null) onCoinChange.run();
        if (onExpChange != null) onExpChange.run();
    }

    private void checkGameState() {
        boolean victory = (playerX == GRID_SIZE - 1 && playerY == GRID_SIZE - 1);
        boolean loss = (steps <= 0);
        if (victory || loss) {
            gameManager.consumeArtifact();
            gameManager.recordScore(scores);
            if (victory) handleVictoryAchievements();
            
            String answer = CyberDialog.showQuestion(parent, "Do you want to reset the game?", null, new String[]{"Yes", "No"}, 880, 480);
            if ("Yes".equalsIgnoreCase(answer)) {
                resetBoard();
                ((TilePacerFrame)parent).switchScreen(TilePacerFrame.MAIN_MENU_KEY);
            } else { parent.dispose(); }
        }
    }

    private void handleVictoryAchievements() {
        gameManager.addRewards(200, 500); gameManager.completeLevel();
        if ((double)steps / gameManager.getMaxSteps() > 0.50) gameManager.unlockAchievement("LONG_STRIDE");
        if (gameManager.getActiveDifficulty() == GameManager.Difficulty.HARD) {
            int obs = 0;
            for(GameTile[] row : tiles) for(GameTile t : row) if(t.getTileType() == GameTile.TileType.OBSTACLE) obs++;
            if (obs >= 8) gameManager.unlockAchievement("CLEAR_PATH");
        }
        if (!gameManager.getSteppedOnNormalThisLevel()) gameManager.unlockAchievement("FULL_DISCLOSURE");
    }

    public void resetBoard() {
        if (moveTimer != null) moveTimer.stop();
        isMoving = false; isWormsTargeting = false;
        playerX = 0; playerY = 0; scores = 0; coinsEarned = 0; expEarned = 0;
        drawX = 0; drawY = 0; stepsTakenThisRun = 0;
        applyDifficulty(gameManager.getActiveDifficulty());
        initializeBoard();
        updateFog(); refreshUI();
    }

    private void setupMoveTimer() {
        moveTimer = new Timer(16, e -> {
            double targetX = playerX * (getWidth() / GRID_SIZE);
            double targetY = playerY * (getHeight() / GRID_SIZE);
            if (Math.abs(drawX - targetX) > 4) drawX += (drawX < targetX) ? 4 : -4; else drawX = targetX;
            if (Math.abs(drawY - targetY) > 4) drawY += (drawY < targetY) ? 4 : -4; else drawY = targetY;
            if (drawX == targetX && drawY == targetY) { isMoving = false; moveTimer.stop(); }
            repaint();
        });
    }

    private void animateAvatar() {
        new Thread(() -> {
            try (InputStream in = ResourceLoader.getStream("/assets/sprite/Walking.png")) {
                BufferedImage sheet = ImageIO.read(in);
                SwingUtilities.invokeLater(() -> { animator = new SpriteAnimator(sheet, 14); repaint(); });
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    private void revealAllTiles() { for(GameTile[] r : tiles) for(GameTile t : r) { t.setRevealed(true); t.setReachable(true); } repaint(); }

    public void setLastEffectTileType(GameTile.TileType t) { this.lastEffectTileType = t; }
    public void setWormsTargeting(boolean t) { this.isWormsTargeting = t; }
    public BackdoorSnapshot getBackdoorSnapshot() { return backdoorSnapshot; }

    public void setOnStepChange(Runnable r) { this.onStepChange = r; }
    public void setOnScoreChange(Runnable r) { this.onScoreChange = r; }
    public void setOnCoinChange(Runnable r) { this.onCoinChange = r; }
    public void setOnExpChange(Runnable r) { this.onExpChange = r; }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (animator != null) {
            Image f = isMoving ? animator.getNextFrame() : animator.getStaticFrame();
            g.drawImage(f, (int)drawX, (int)drawY, getWidth()/GRID_SIZE, getHeight()/GRID_SIZE, null);
        }
    }
}