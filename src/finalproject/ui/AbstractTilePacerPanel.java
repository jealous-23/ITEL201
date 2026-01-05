package finalproject.ui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import finalproject.util.ResourceLoader;

public abstract class AbstractTilePacerPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected static final ImageIcon BACKGROUND_ICON = ResourceLoader.getImageIconFromResource("/assets/icons/background.png");
	
	protected abstract void createPanel();
	protected abstract void addMainTitle();
}
