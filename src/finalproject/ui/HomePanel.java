package finalproject.ui;

import finalproject.ui.components.BackgroundPanel;
import finalproject.util.ResourceLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class HomePanel extends AbstractTilePacerPanel {
    private static final long serialVersionUID = 1L;
    private BackgroundPanel homePanel;
    
    private final JProgressBar loader;
    private final Runnable onFinish;
    
    private int MAX_PROGRESS;
    
    public HomePanel(Runnable onFinish) {
    	this.onFinish = onFinish;
        setLayout(new BorderLayout());
        
        Random random = new Random();
        this.MAX_PROGRESS = random.nextInt(41) + 60;
        
        loader = createRandomProgressBar();
        createPanel();
    }
    
    @Override
    protected void createPanel() {
    	homePanel = new BackgroundPanel(BACKGROUND_ICON);
        homePanel.setLayout(new GridBagLayout());
        add(homePanel, BorderLayout.CENTER);

        addMainTitle();
        createSubtitleLabel();
               
        GridBagConstraints gbcLoader = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER,
        		GridBagConstraints.NONE, 0, 0, 1.5, 1.0, 200, 0, 60, 0);  
        
        homePanel.add(loader, gbcLoader);
        startLoading();
    }
    
    @Override
    protected void addMainTitle() {
    	JLabel title = ResourceLoader.createStyledLabel("TILE  PACER",
    			"Tile Pacer", new Color(0x40E0D0), 120f, SwingConstants.CENTER);
        GridBagConstraints gbcTitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, 0, 0, 1.5, 1.0, 0, 0, 60, 0);
        homePanel.add(title, gbcTitle);
    }
    
    private void createSubtitleLabel() {
    	JLabel subtitle = ResourceLoader.createStyledLabel("Initializing Systems...", "Orbiton", 
        		new Color(0x40E0D0), 20f, SwingConstants.CENTER);
        GridBagConstraints gbcSubtitle = ResourceLoader.createLayoutConstraints(GridBagConstraints.CENTER,
        		GridBagConstraints.NONE, 0, 0, 1.5, 1.0, 130, 0, 60, 0);  
        homePanel.add(subtitle, gbcSubtitle);
    }
    private JProgressBar createRandomProgressBar() {
    	JProgressBar progressBar = new JProgressBar(0, this.MAX_PROGRESS);
    	
    	progressBar.setPreferredSize(new Dimension(500, 20));
    	progressBar.setMaximumSize(new Dimension(100, 10));
    	progressBar.setMinimumSize(new Dimension(100, 10));
    	progressBar.setBorderPainted(false);
    	progressBar.setStringPainted(true);
    	//progressBar.setIndeterminate(true);
    	progressBar.setForeground(new Color(0, 255, 255));
    	progressBar.setBackground(new Color(0, 40, 40));
    	
    	return progressBar;
    }
    
    private void startLoading() {
     
        int tickMs = 100; 
      
        int maxIncrement = 4; 
        Random random = new Random();

        Timer timer = new Timer(tickMs, null);
        timer.addActionListener(e -> {
         
            int increment = random.nextInt(maxIncrement) + 1; 
            int currentValue = loader.getValue();
            int newValue = currentValue + increment;

           
            if (newValue >= this.MAX_PROGRESS) {
                loader.setValue(this.MAX_PROGRESS);
                timer.stop();
                onFinish.run();
            } else {
                loader.setValue(newValue);
            }
        });
        timer.start();
    }
}