package finalproject.classes;

import javax.swing.SwingUtilities;

import finalproject.gamestate.GameManager;
import finalproject.ui.TilePacerFrame;

public class Main {
	public static void main(String[] args) {
		var record = new GameManager();
		
		/*record.startGame(GameManager.Difficulty.HARD);
		record.incrementScore(3);
		record.saveHighScore();
		
		record.startGame(GameManager.Difficulty.EASY);
		record.incrementScore(3);
		record.saveHighScore();
		
		record.startGame(GameManager.Difficulty.HARD);
		record.incrementScore(4);
		record.saveHighScore();
		
		record.startGame(GameManager.Difficulty.MEDIUM);
		record.incrementScore(100);
		record.saveHighScore();
		
		record.startGame(GameManager.Difficulty.HARD);
		record.incrementScore(104);
		record.saveHighScore();
		
		record.startGame(GameManager.Difficulty.EASY);
		record.incrementScore(6);
		record.saveHighScore();
		
		record.startGame(GameManager.Difficulty.EASY);
		record.incrementScore(129);
		record.saveHighScore();
		
		System.out.println(record.getHighScore(GameManager.Difficulty.HARD));
		
		GameTile tile = new GameTile(GameTile.TileType.NORMAL, "newCrown.png");
		tile.revealTile();*/
		
		SwingUtilities.invokeLater(TilePacerFrame::new);
		
	}
}
