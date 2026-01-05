package finalproject.gamestate;

import finalproject.ui.components.CyberDialog;
import javax.swing.JFrame;
import java.awt.Cursor;

public class ItemEffectHandler {
    private final TilePacerBoard board;
    private final GameManager gameManager;
    private final JFrame parent;

    public ItemEffectHandler(TilePacerBoard board, GameManager gameManager, JFrame parent) {
        this.board = board;
        this.gameManager = gameManager;
        this.parent = parent;
    }

   
    public void useBackdoor() {
        if (board.getBackdoorSnapshot() == null) {
            CyberDialog.showMessage(parent, "No BACKDOOR available.", "OKAY", 500, 250);
            return;
        }
        
        if (!gameManager.hasBackdoor()) {
            CyberDialog.showMessage(parent, "No BACKDOOR available.", "OKAY", 500, 250);
            return;
        }

        System.out.println("DEBUG: Backdoor item consumed. Restoring state...");
        board.restoreBackdoorSnapshot();
        gameManager.consumeItem("BACKDOOR");
    }

   
    public void activateWorms(GameTile.TileType lastEffect) {
        if (!gameManager.hasWorms()) {
            CyberDialog.showMessage(parent, "No WORMS available.", "OKAY", 500, 250);
            return;
        }

        if (lastEffect == null || lastEffect == GameTile.TileType.NORMAL) {
            CyberDialog.showMessage(
                parent,
                "No recent tile effect to replicate.\nStep on a special tile first!",
                "OKAY",
                550,
                280
            );
            return;  
        }

        board.setWormsTargeting(true);
        gameManager.useWorms(); 
        
        board.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        CyberDialog.showMessage(
            parent,
            "🐛 WORMS TARGETING MODE\nClick a revealed normal tile to infect it with: " + lastEffect,
            "OKAY",
            550,
            280
        );
    }

    public void handleWormClick(GameTile clicked, GameTile.TileType lastEffect, TileInteractionManager interaction) {
        if (clicked.getTileType() == GameTile.TileType.NORMAL && clicked.getRevealed()) {
            clicked.setTileType(lastEffect);
            clicked.setReachable(true);

            board.setWormsTargeting(false);
            board.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            CyberDialog.showMessage(
                parent,
                "🐛 WORMS INFECTED THE TILE\nEffect replicated: " + lastEffect,
                "OKAY",
                550,
                280
            );

            interaction.handleSpecialTile(clicked, board, gameManager, parent);
            
            clicked.setRevealed(true);
            board.repaint();
            
        } else {
            CyberDialog.showMessage(
                parent,
                "Invalid target! Select a revealed normal tile.",
                "OKAY",
                500,
                250
            );
        }
    }
}