package finalproject.ui.components;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

public class BackgroundPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Image backgroundImage;
    private float offsetX = 0;
    private float offsetY = 0;
    private float phase = 0f;

    private final int hexSize = 120;

    public BackgroundPanel(ImageIcon bgIcon) {
        this.backgroundImage = bgIcon.getImage();
        setLayout(new GridBagLayout());
        setOpaque(false);

        Timer animationTimer = new Timer(45, e -> {
        	phase += 0.05f;
            offsetX = (float) Math.sin(phase) * 4;
            offsetY = (float) Math.cos(phase) * 2;

            repaint();

            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        int overscan = 10;
       
        g2.drawImage(
        	    backgroundImage,
        	    (int) -offsetX - overscan / 2,
        	    (int) -offsetY - overscan / 2,
        	    w + overscan,
        	    h + overscan,
        	    this
        );
           
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.45f));
        g2.setColor(new Color(5, 15, 20));
        g2.fillRect(0, 0, w, h);

        
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.05f));
        g2.setColor(new Color(0, 255, 255));
        g2.fillRect(0, 0, w, h);

        
        drawHexGrid(g2, w, h);

        g2.dispose();
    }

    private void drawHexGrid(Graphics2D g2, int w, int h) {
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.12f));
        g2.setColor(new Color(0, 255, 255));
        g2.setStroke(new BasicStroke(1f));

        for (int y = -hexSize; y < h + hexSize; y += hexSize) {
            for (int x = -hexSize; x < w + hexSize; x += hexSize) {
                drawHex(g2,
                        x + (y / hexSize % 2 == 0 ? hexSize / 2 : 0)
                                - (int) offsetX,
                        y - (int) offsetY,
                        hexSize / 2);
            }
        }
    }

    private void drawHex(Graphics2D g2, int x, int y, int r) {
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            hex.addPoint(
                    (int) (x + r * Math.cos(i * Math.PI / 3)),
                    (int) (y + r * Math.sin(i * Math.PI / 3))
            );
        }
        g2.draw(hex);
    }
}
