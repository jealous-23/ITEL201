package finalproject.classes;

import javax.swing.SwingUtilities;

import finalproject.gamestate.GameManager;
import finalproject.ui.TilePacerFrame;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(TilePacerFrame::new);
	}
}

