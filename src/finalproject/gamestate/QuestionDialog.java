package finalproject.gamestate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuestionDialog extends JDialog {
    private GameManager gameManager;
    private Question question;

    public QuestionDialog(JFrame parent, GameManager manager, Question q) {
        super(parent, "Coding Question!", true);
        this.gameManager = manager;
        this.question = q;

        setLayout(new BorderLayout());
        setSize(500, 300);
        setLocationRelativeTo(parent);

        // Question text
        JLabel questionLabel = new JLabel("<html><h3>" + q.getQuestion() + "</h3></html>", SwingConstants.CENTER);
        add(questionLabel, BorderLayout.NORTH);

        // Options
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        for (String option : q.getOptions()) {
            JButton button = new JButton(option);
            button.addActionListener(new AnswerListener(option));
            optionsPanel.add(button);
        }
        add(optionsPanel, BorderLayout.CENTER);

        // Feedback
        JLabel feedbackLabel = new JLabel("", SwingConstants.CENTER);
        add(feedbackLabel, BorderLayout.SOUTH);
    }

    private class AnswerListener implements ActionListener {
        private String selected;

        public AnswerListener(String sel) { this.selected = sel; }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selected.equals(question.getAnswer())) {
                gameManager.incrementScore(50);
                JOptionPane.showMessageDialog(QuestionDialog.this, "Correct! +50 Score!");
            } else {
                gameManager.decrementSteps(5); // Assuming GameManager has this method
                JOptionPane.showMessageDialog(QuestionDialog.this, "Incorrect! -5 Steps!");
            }
            dispose();
        }
    }
}