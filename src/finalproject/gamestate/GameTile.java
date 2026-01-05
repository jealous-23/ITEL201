package finalproject.gamestate;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import finalproject.ui.components.GradientButton;
import finalproject.util.SpriteAnimator;
import finalproject.util.audio.SoundManager;


public class GameTile extends GradientButton {
    private static final long serialVersionUID = 1L;
    
    public enum TileType {
        NORMAL, QUESTION, OBSTACLE, TELEPORT, GAMBLE, BONUS_STEP, START, END, COIN, GEM
    }
    
    private SpriteAnimator obstacleAnimator;
    private Timer animationTimer;
    private static final Color VISITED_COLOR = Color.BLACK;
    
    private boolean isPlayerHere;
    protected TileType tileType;
    private final int row, col;
    protected final int x, y;
    private boolean isHidden = false;
    private boolean isVisited = false;
    private boolean isWormsTargeting = false; 
    
    private boolean isRevealed = false;
   
    private static final Color PLAYER_TILE_COLOR = new Color(0, 255, 255, 60);
    
    public GameTile(TileType type, int x, int y) {
    	super("", new Color(0x008B8B), new Color(0x006A80), 
	    		new Color(0x008B8B), 0, false);
    	this.tileType = type;
    	
    	this.row = x;
        this.col = y;
    
        this.x = x;
        this.y = y;
       
        this.setPreferredSize(new Dimension(80, 80));
        this.setMinimumSize(new Dimension(65, 65));
        this.setContentAreaFilled(false);
        this.setBorderPainted(true);
        this.setEnabled(false);
        this.setFocusPainted(false);
        
        if (tileType != TileType.NORMAL || 
        	tileType != TileType.START ||
        	tileType != TileType.END) {
        	setupAnimation();
        }
    }
    
    public void setupAnimation() {
        final String path;
        final int size;
        
        switch (tileType) {
            case QUESTION -> {
            	path = "/assets/sprite/question_tile.png"; 
            	size = 9; 
            }
            case OBSTACLE -> {
            	path = "/assets/sprite/obstacle_tile.png";
            	size = 12; 
            }
            case TELEPORT -> {
            	path = "/assets/sprite/teleport.png";
            	size = 5; 
            }
            case GAMBLE -> {
            	path = "/assets/sprite/gamble_tile.png"; 
            	size = 8; 
            }
            case BONUS_STEP -> { 
            	path = "/assets/sprite/bonus_tile.png";
            	size = 8; 
            }
            case START -> { 
            	path = "/assets/sprite/start_tile.png";
            	size = 8; 
            }
            case END -> {
            	path = "/assets/sprite/end_tile.png";
            	size = 8;
            }
            case COIN -> {
            	path = "/assets/sprite/coin_tile.png";
            	size = 5;
            }
            case GEM -> {
            	path = "/assets/sprite/gem_tile.png";
            	size = 6;
            }
            default -> {
            	return; 
            }
        }

        Runnable imageLoaderTask = new Runnable() {
            @Override
            public void run() {
            	InputStream icon = this.getClass().getResourceAsStream(path);
                BufferedImage sheet = null;
                try {
                    sheet = ImageIO.read(icon);
                } catch (Exception e) {
                    System.err.println("Load failed!");
                    return;
                }

                final BufferedImage finalSheet = sheet;
            
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        obstacleAnimator = new SpriteAnimator(finalSheet, size);
                        if (animationTimer != null) {
                        	animationTimer.start();
                            repaint();
                        }
                    }
                });
            }
        };
        new Thread(imageLoaderTask).start();
    }
    
    

    public void playSound(GameTile.TileType assignedType) {
    	switch (assignedType) {
    	    case TELEPORT -> SoundManager.play("teleport");
    	    case BONUS_STEP -> SoundManager.play("bonus");
    	    case GAMBLE -> SoundManager.play("gamble");
    	    case QUESTION -> SoundManager.play("question");
    	    default -> SoundManager.play("click");
    	}
    }
    
    public void stopSound(GameTile.TileType assignedType) {
    	switch (assignedType) {
    	    case TELEPORT -> SoundManager.stop("teleport");
    	    case BONUS_STEP -> SoundManager.stop("bonus");
    	    case GAMBLE -> SoundManager.stop("gamble");
    	    case QUESTION -> SoundManager.play("question");
    	    default -> SoundManager.stop("click");
    	}
    }
    
    public void setRevealed(boolean isRevealed) {
    	this.isRevealed = isRevealed;
    }

    public void setVisited(boolean visited) {       
        if (tileType == TileType.OBSTACLE) {
        	return;
        }
        
        this.isVisited = visited;
        if (visited) {
            setNeonGlow(true);
            setNeonColor(new Color(0, 180, 180));
            setGlowStrength(4);
      
            stopSound(tileType);
        }
        
        if (visited && animationTimer != null) {
            animationTimer.stop(); 
        }        
    }
    
    public void setReachable(boolean reachable) {
        if (isHidden) {
            setEnabled(false);
            setNeonGlow(false);
            return;
        }

        setEnabled(reachable);

        if (reachable) {
        	
            setNeonGlow(true);
            setNeonColor(new Color(0, 255, 255));
            setGlowStrength(8);
            setEnablePressedEffect(true);
            setEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setNeonGlow(false);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    public void setTileType(TileType newType) {
    	
    	if (this.tileType != newType && animationTimer != null) {
            animationTimer.stop();
            obstacleAnimator = null; 
        }
    	
    	this.tileType = newType;
        
        if (newType != TileType.NORMAL && newType != TileType.START && newType != TileType.END) {
            setupAnimation();
        }

        if (newType == TileType.NORMAL) {
            this.setRevealed(true);
        }
        repaint();
    }
  
    public void setAsPlayerTile(boolean isPlayerHere) {
    	
        if (isPlayerHere) {
        	this.isPlayerHere = isPlayerHere;
        	this.setVisited(isPlayerHere);
            this.setBackground(PLAYER_TILE_COLOR);
            this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        } else {
            this.setBackground(new Color(0, 139, 139, 100)); 
            this.setBorder(BorderFactory.createLineBorder(new Color(0x00FFFF), 2));
        }
        this.repaint();
    }
    
    public void setWormsTargeting(boolean isWormsTargeting) {
    	this.isWormsTargeting = isWormsTargeting;
    }
    
    public void reveal() {
        this.isHidden = false;
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.repaint();
    }
    
    public TileType getTileType() { return this.tileType; }
    public boolean getVisited() { return isVisited; }
    public boolean getRevealed() { return this.isRevealed; }
    public int getRow() { return row; }
    public int getCol() { return col; }   
    
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	if (isRevealed && !isVisited && obstacleAnimator != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            BufferedImage frame = obstacleAnimator.getNextFrame();
         
            g2.drawImage(frame, 0, 0, getWidth(), getHeight(), null);
            
            g2.dispose();
            this.setGradientColors(Color.BLACK, Color.BLACK, Color.BLACK);
            
        }
    	
        if (isVisited || isPlayerHere) {
            this.setGradientColors(VISITED_COLOR, VISITED_COLOR.darker(), VISITED_COLOR);
        }
               
    }
}