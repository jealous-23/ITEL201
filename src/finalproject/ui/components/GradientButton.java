package finalproject.ui.components;

import javax.swing.JButton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class GradientButton extends JButton {

    private static final long serialVersionUID = 1L;
    
    private Color firstColor;
    private Color secondColor;
    private Color borderColor; 
    private int arcSize;
    private boolean enablePressedEffect;
    
    private boolean neonGlow = false;
    private Color neonColor = new Color(0, 255, 255);
    private int glowStrength = 6;
    
    public GradientButton(String text, Color firstColor, Color secondColor, Color borderColor,
    		int arcSize, boolean enablePressedEffect) {
        super(text);
        
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.borderColor = borderColor;
        this.arcSize = arcSize;
        this.enablePressedEffect = enablePressedEffect;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }
    
    public void setGradientColors(Color first, Color second, Color border) {
        this.firstColor = first;
        this.secondColor = second;
        this.borderColor = border;
        repaint();
    }
    
    public void setNeonGlow(boolean enabled) {
        this.neonGlow = enabled;
        repaint();
    }

    public void setNeonColor(Color color) {
        this.neonColor = color;
        repaint();
    }

    public void setGlowStrength(int strength) {
        this.glowStrength = strength;
        repaint();
    }
    
    public void setEnablePressedEffect(boolean enablePressedEffect) {
    	this.enablePressedEffect = enablePressedEffect;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
       
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        
        //RoundRectangle2D buttonShape = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, this.arcSize, this.arcSize);
        RoundRectangle2D buttonShape =
                new RoundRectangle2D.Float(2, 2, w - 4, h - 4, arcSize, arcSize);
        if (neonGlow && isEnabled()) {
            paintNeonGlow(g2, buttonShape);
        }
     
        Color topColor, bottomColor;
        
        if (enablePressedEffect && getModel().isPressed()) {
            topColor = firstColor;
            bottomColor = secondColor;
        } else if (enablePressedEffect && getModel().isRollover()) {
            topColor = secondColor.brighter();
            bottomColor = firstColor.brighter();
        } else {
            topColor = secondColor;
            bottomColor = firstColor;
        }

        GradientPaint gradient = new GradientPaint(
            0, 0, topColor,
            0, h, bottomColor
        );

        g2.setPaint(gradient);
        g2.fill(buttonShape);
        
        g2.setColor(this.borderColor);
        g2.draw(buttonShape);
        
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.draw(buttonShape);
        }

        g2.dispose();

        super.paintComponent(g);
    }
    
    private void paintNeonGlow(Graphics2D g2, Shape shape) {
        Graphics2D glow = (Graphics2D) g2.create();

        for (int i = glowStrength; i > 0; i--) {
            float alpha = (float) i / (glowStrength * 4f);
            glow.setColor(new Color(
                    neonColor.getRed(),
                    neonColor.getGreen(),
                    neonColor.getBlue(),
                    Math.min(255, (int) (alpha * 255))
            ));
            glow.setStroke(new BasicStroke(i));
            glow.draw(shape);
        }

        glow.dispose();
    }
}