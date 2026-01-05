package finalproject.classes;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class FadePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private float alpha = 0f;
	
    public FadePanel() {
        setOpaque(false);
    }

    public void fadeIn(int duration) {
        alpha = 0f;
        int delay = 10;
        Timer timer = new Timer(delay, null);

        timer.addActionListener(new ActionListener() {
            long start = System.currentTimeMillis();

            public void actionPerformed(ActionEvent e) {
                float progress = (float)(System.currentTimeMillis() - start) / duration;
                alpha = Math.min(1f, progress);

                repaint();

                if (alpha >= 1f)
                    timer.stop();
            }
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2);
        g2.dispose();
    }
}
