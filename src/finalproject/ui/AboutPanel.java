package finalproject.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import finalproject.ui.components.BackgroundPanel;
import finalproject.util.ResourceLoader;

public class AboutPanel extends AbstractTilePacerPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final TilePacerFrame parentFrame;
	private BackgroundPanel aboutPanel;
    
	public AboutPanel(TilePacerFrame parentFrame) {
		this.parentFrame = parentFrame;
		createPanel();
	}
    
    @Override
    protected void createPanel() {
    	this.aboutPanel = new BackgroundPanel(BACKGROUND_ICON);
    	aboutPanel.setLayout(new GridBagLayout());
    	
    	addMainTitle();
        
    	ResourceLoader.addBackButtonToPanel(parentFrame, aboutPanel);
    	this.setLayout(new BorderLayout());
        this.add(aboutPanel, BorderLayout.CENTER);
    	
    }
    
    @Override
    protected void addMainTitle() {
    	JLabel titleLabel = ResourceLoader.createStyledLabel("ABOUT THIS GAME",
    			"Tile Pacer", new Color(0x40E0D0), 90f,
	    		SwingConstants.CENTER);
	    GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.NORTH, 
	    		GridBagConstraints.NONE, 0, 0, 1.0, 1.0, 20, 0, 60, 0);
	    aboutPanel.add(titleLabel, gbcTitle);
    }
}