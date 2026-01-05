package finalproject.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import finalproject.gamestate.TilePacerBoard;
import finalproject.ui.TilePacerFrame;
import finalproject.ui.components.GradientButton;

public class ResourceLoader {
	private static final String BACK_BUTTON_PATH = "/assets/icons/backButton.png";
	private static final String FONT_PATH = "/assets/font/ARCADECLASSIC.TTF";
	private static final String MAIN_MENU_KEY = "MainMenu";
	
	private ResourceLoader() {}
	
	public static InputStream getStream(String path) {
		path = path.startsWith("/") ? path : "/" + path;
		InputStream output = ResourceLoader.class.getResourceAsStream(path);
		
		return output;
	}
	
	public static ImageIcon getImageIconFromResource(String path) {
		path = path.startsWith("/") ? path : "/" + path;
		
		try (InputStream icon = ResourceLoader.class.getResourceAsStream(path)) {
			if (icon == null) {
				return null;
			}
			
			BufferedImage image = ImageIO.read(icon);
			
			if (image == null) {
                System.err.println("Could not decode image from resource: " + path);
                return null;
           }
			
			return new ImageIcon(image);
		} catch (IOException e) {
			System.err.println("The file can't be found or read.");
			return null;
		}
	}
	
	public static Image createScaledImageFromResource(String path, int width, int height) {
        path = path.startsWith("/") ? path : "/" + path;

        try (InputStream iconStream = ResourceLoader.class.getResourceAsStream(path)) {
            if (iconStream == null) {
                System.err.println("Resource not found: " + path);
                return null;
            }

            BufferedImage originalImage = ImageIO.read(iconStream);
            if (originalImage == null) {
                System.err.println("Could not decode image from resource: " + path);
                return null;
            }

            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return scaledImage;

        } catch (IOException e) {
            System.err.println("An error occurred while reading the image resource: " + path);
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image format or data for resource: " + path);
            e.printStackTrace();
            return null;
        }
    }
	
	public static JLabel createStyledLabel(String text, String fontName, Color color, float textSize, int alignment) {
		JLabel label = new JLabel(text);
	    label.setForeground(color);
	    
	    Font font;
	    if ("Tile Pacer".equals(fontName)) {
	    	font = loadCustomFont();
	    	font = font.deriveFont(font.getStyle() | Font.BOLD, textSize);
	    } else {
	    	font = new Font(fontName, Font.BOLD, (int) textSize);
	    }
	    	    
	    label.setFont(font);
	    label.setHorizontalAlignment(alignment);
	    
	    return label;
	}
	
	public static void addBackButtonToPanel(TilePacerFrame tilePacer, JPanel panel, TilePacerBoard currentBoard) {
		ImageIcon backImage = new ImageIcon(createScaledImageFromResource(BACK_BUTTON_PATH, 150, 100));
	    JButton backButton = createTransparentButton(backImage);
	    
	    backButton.addActionListener(e -> {   
	    	currentBoard.resetBoard();
	    	tilePacer.switchScreen(MAIN_MENU_KEY);
	    });

	    GridBagConstraints gbcBack = createLayoutConstraints(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
	            0, 0, 0.0, 0.0, 20, 20, 20, 20);
	    panel.add(backButton, gbcBack);
	}
	
	public static void addBackButtonToPanel(TilePacerFrame tilePacer, JPanel panel) {
		ImageIcon backImage = new ImageIcon(createScaledImageFromResource(BACK_BUTTON_PATH, 150, 100));
	    JButton backButton = createTransparentButton(backImage);

	    backButton.addActionListener(e -> {
	    	tilePacer.switchScreen(MAIN_MENU_KEY);
	    });
	    
	    GridBagConstraints gbcBack = createLayoutConstraints(GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
	            0, 0, 0.0, 0.0, 20, 20, 20, 20);
	    panel.add(backButton, gbcBack);
	}
	
	public static GradientButton createStyledGradientButton(String text, Color firstColor, Color secondColor, Color borderColor, Color textColor,
			boolean enablePressedEffect, int arcSize, float textSize, int width, int height) {
		GradientButton button = new GradientButton(text, firstColor, secondColor, borderColor, arcSize, enablePressedEffect);
	    Font font = loadCustomFont();
	    font = font.deriveFont(font.getStyle() | Font.BOLD, textSize);
	    button.setFont(font);
	    button.setForeground(textColor);
	    button.setPreferredSize(new Dimension(width, height));  
	    
	    return button;
	}
	
	public static GridBagConstraints createLayoutConstraints(int anchor, int fill, int gridX, int gridY, double weightX, double weightY, 
			int top, int left, int bottom, int right) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = anchor;
	    gbc.fill = fill;
        gbc.gridx = gridX;
	    gbc.gridy = gridY;
	    gbc.weightx = weightX;
	    gbc.weighty = weightY;
	    gbc.insets = new Insets(top, left, bottom, right); 
	    
        return gbc;
    }
	
	public static Font loadCustomFont() {
	    try (InputStream fontStream = ResourceLoader.class.getResourceAsStream(FONT_PATH)){
	        Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(customFont);
	        return customFont;
	    } catch (IOException | FontFormatException e) {
	        e.printStackTrace();
	        return new Font("Arial", Font.PLAIN, 12);
	    }
	}
	
	public static Image reateScaledImage(String path, int width, int height) {
		ImageIcon imageIcon = new ImageIcon(path);
	    Image image = imageIcon.getImage();
	    image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    
	    return image;
	}
	
	public static JButton createTransparentButton(ImageIcon imageIcon) {
		JButton button = new JButton(imageIcon);
		button.setBorderPainted(false);
	    button.setContentAreaFilled(false);
	    button.setFocusPainted(false);
	    
	    return button;
	}
}
