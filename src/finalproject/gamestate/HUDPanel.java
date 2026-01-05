package finalproject.gamestate;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final GameManager gameManager;
    private final JLabel scoreLabel;
    private final JLabel stepsLabel;
    private final JLabel nameLabel;

    public HUDPanel(GameManager manager) {
        this.gameManager = manager;
        this.setLayout(new GridLayout(1, 3));
        this.setBackground(new Color(20, 20, 20));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0x40E0D0)));

        nameLabel = createHUDLabel("PLAYER: " + gameManager.getName());
        stepsLabel = createHUDLabel("STEPS: " + gameManager.getCurrentSteps());
        scoreLabel = createHUDLabel("SCORE: " + gameManager.getCurrentScore());

        this.add(nameLabel);
        this.add(stepsLabel);
        this.add(scoreLabel);
    }

    private JLabel createHUDLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(new Color(0x40E0D0));
        label.setFont(new Font("Monospaced", Font.BOLD, 18));
        return label;
    }

    public void updateHUD() {
        nameLabel.setText("PLAYER: " + gameManager.getName());
        stepsLabel.setText("STEPS: " + gameManager.getCurrentSteps());
        scoreLabel.setText("SCORE: " + gameManager.getCurrentScore());
        
        if (gameManager.getCurrentSteps() < 5) {
            stepsLabel.setForeground(Color.RED);
        }
    }
}