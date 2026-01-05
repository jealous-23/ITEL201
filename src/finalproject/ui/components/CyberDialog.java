package finalproject.ui.components;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import finalproject.gamestate.GameManager;
import finalproject.gamestate.Question;
import finalproject.util.ResourceLoader;

public class CyberDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    public enum Mode { QUESTION, SUCCESS, FAILURE, INFO }

    private String selectedAnswer = null;
    private static final Color NEON_CYAN  = new Color(0, 255, 255);
    private static final Color NEON_GREEN = new Color(0, 255, 120);
    private static final Color NEON_RED   = new Color(255, 80, 80);
    private static final Color DARK_BG    = new Color(10, 15, 20, 235);

    private float glowPhase = 0f;
    private float opacity = 0f;
    private double scale = 0.8;
    private int scanlinePos = 0;
    private boolean isClosing = false;

    private CyberDialog(Frame parent, String message, String buttonText, String[] options, Mode mode, int width, int height) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setOpacity(0f);

        Color accent = switch (mode) {
            case SUCCESS -> NEON_GREEN;
            case FAILURE -> NEON_RED;
            default -> NEON_CYAN;
        };
        
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Center Scaling
                g2.translate(getWidth()/2.0, getHeight()/2.0);
                g2.scale(scale, scale);
                g2.translate(-getWidth()/2.0, -getHeight()/2.0);

                int arc = 40;
                Shape shape = new RoundRectangle2D.Double(6, 6, getWidth() - 12, getHeight() - 12, arc, arc);

                g2.setColor(DARK_BG);
                g2.fill(shape);

                g2.setClip(shape);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 30));
                g2.fillRect(0, scanlinePos, getWidth(), 2);

                float glow = (float) (2.5 + Math.sin(glowPhase) * 1.5);
                g2.setStroke(new BasicStroke(glow));
                g2.setColor(accent);
                g2.draw(shape);

                g2.dispose();
            }
        };

        mainPanel.setPreferredSize(new Dimension(width, height));     
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
 
        gbc.anchor = GridBagConstraints.NORTH; 
        gbc.insets = new Insets(50, 40, 10, 40);
        
        JTextArea messageArea = new JTextArea(message);
        messageArea.setMargin(new Insets(20, 20, 20, 20));
        messageArea.setWrapStyleWord(true);
        messageArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageArea.setMargin(new Insets(20, 50, 20, 50));
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setOpaque(false);
        messageArea.setForeground(accent);
        messageArea.setFont(new Font("Orbitron", Font.BOLD, 22));
    
        gbc.gridy = 0;
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH; 
        mainPanel.add(messageArea, gbc);
        
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(new Color(0,0,0,0));
        mainPanel.add(scrollPane, gbc);
     
        if (mode == Mode.QUESTION && options != null) {
            JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
            optionsPanel.setOpaque(false);
            Random rand = new Random();
            
            for (int opt = options.length - 1; opt > 0; opt--) {
            	int random =  rand.nextInt(options.length);
            	
            	String temp = options[random];
            	options[random] = options[opt];
            	options[opt] = temp;
            }
            
            for (String opt : options) {
                var optBtn = ResourceLoader.createStyledGradientButton(opt, 
                        new Color(0, 60, 60), new Color(0, 30, 30), accent, 
                        Color.WHITE, true, 50, 40f, 200, 50);
                
                optBtn.setFont(new Font("Monospaced", Font.BOLD, 16));
                optBtn.setPreferredSize(new Dimension(350, 70));
                optBtn.addActionListener(e -> { selectedAnswer = opt; dispose(); });
                optionsPanel.add(optBtn);
            }
            gbc.gridy = 1;
            gbc.weighty = 0.7;
            mainPanel.add(optionsPanel, gbc);
            
            if (buttonText != null) {
                for (Component comp : optionsPanel.getComponents()) {
                    if (comp instanceof javax.swing.JButton btn) {
                        if (btn.getText().equals(buttonText)) {
                            btn.setBackground(new Color(0x34A853)); 
                            btn.setForeground(Color.WHITE);
                            btn.setBorder(javax.swing.BorderFactory.createLineBorder(Color.YELLOW, 4));
                            btn.setText("⭐ " + btn.getText() + " ⭐");                           
                        
                            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 18f));
                        }
                    }
                }
            }
        } else {
            var okBtn = ResourceLoader.createStyledGradientButton(buttonText, 
                    new Color(0, 60, 60), new Color(0, 30, 30), accent, 
                    Color.WHITE, true, 50, 40f, 200, 50);
            okBtn.addActionListener(e -> dispose());
            
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE;
            mainPanel.add(okBtn, gbc);
        }

        add(mainPanel);
        pack();
        setLocationRelativeTo(parent);

        Timer animTimer = new Timer(10, e -> {
            if (!isClosing) {
                if (opacity < 1.0f) { opacity += 0.2f; setOpacity(Math.min(1.0f, opacity)); }
                if (scale < 1.0) scale += 0.05;
            } else {
                opacity -= 0.2f;
                scale -= 0.05;
                if (opacity <= 0) super.dispose();
                else setOpacity(Math.max(0f, opacity));
            }
            glowPhase += 0.3f;
            scanlinePos = (scanlinePos + 8) % Math.max(1, getHeight());
            mainPanel.repaint();
        });
        animTimer.start();
    }

    @Override
    public void dispose() {
        if (!isClosing) {
            isClosing = true;         
        }
    }


    public static String showQuestion(Frame p, String q, String buttonText, String[] opts, int width, int height) {
        CyberDialog d = new CyberDialog(p, q, buttonText, opts, Mode.QUESTION, width, height);
        d.setVisible(true);
        return d.selectedAnswer;
    }
    
    public static String showQuestionWithAIHint(Frame p, String q, String correctAnswer, String[] opts, int width, int height) {
        CyberDialog d = new CyberDialog(p, q, null, opts, Mode.QUESTION, width, height);
    
        List<String> wrongOptions = new ArrayList<>();
        for (String opt : opts) {
            if (!opt.equalsIgnoreCase(correctAnswer)) {
                wrongOptions.add(opt);
            }
        }

        if (!wrongOptions.isEmpty()) {
            String optionToRemove = wrongOptions.get(new Random().nextInt(wrongOptions.size()));
            
         
            disableButtonByText(d, optionToRemove);
        }

        d.setVisible(true);
        return d.selectedAnswer;
    }
    
    public static String showQuestionWithAnswerRevealed(Frame parent, String message, String correctAnswer, String[] options, int width, int height) {
        
    	CyberDialog dialog = new CyberDialog(
                parent,
                message,
                correctAnswer,
                options,
                Mode.QUESTION,
                width,
                height
        );

        highlightCorrectAnswerButton(dialog, correctAnswer);

        dialog.setVisible(true);
        return dialog.selectedAnswer;
    }
    
    public static void showSuccess(Frame p, String m, String buttonText, int width, int height) {
    	new CyberDialog(p, m, buttonText, null, Mode.SUCCESS, width, height).setVisible(true); 
    }
    public static void showFailure(Frame p, String m, String buttonText, int width, int height) {
    	new CyberDialog(p, m, buttonText, null,  Mode.FAILURE, width, height).setVisible(true); 
    }
    public static void showMessage(Frame p, String m, String buttonText, int width, int height) { 
    	new CyberDialog(p, m, buttonText, null, Mode.INFO, width, height).setVisible(true);
    }
    
    public void setDialogSize(int width, int height) {
        Component[] comps = getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel) {
                c.setPreferredSize(new Dimension(width, height));
                break;
            }
        }
        pack();
        setLocationRelativeTo(getOwner());
    }
    
    private static void disableButtonByText(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton btn) {
                if (btn.getText().equals(text)) {
                    btn.setEnabled(false);
                    btn.setText(" [SYSTEM ERROR] ");
                    btn.setBackground(Color.DARK_GRAY);
                }
            } else if (comp instanceof Container) {
                disableButtonByText((Container) comp, text);
            }
        }
    }
    
    private static void highlightCorrectAnswerButton(CyberDialog dialog, String correctAnswer) {
        findAndStyleButton(dialog, correctAnswer);
    }

    private static void findAndStyleButton(Container container, String targetText) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof AbstractButton btn) {
                if (btn.getText().trim().equalsIgnoreCase(targetText.trim())) {
                  
                    btn.setBackground(new Color(0x34A853)); 
                    btn.setForeground(Color.WHITE);
                    btn.setFont(new Font("Orbitron", Font.BOLD, 18));
                    
                    btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.YELLOW, 4),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                  
                    btn.setText("⭐ " + btn.getText() + " ⭐");
                    return; 
                }
            } else if (comp instanceof Container) {
                findAndStyleButton((Container) comp, targetText);
            }
        }
    }
}
