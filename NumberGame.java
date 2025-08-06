import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")
public class NumberGame extends JFrame {
    private int randomNumber;
    private int attemptsLeft;
    private int score;
    private int round;
    private int difficulty;
    private final int MAX_ROUNDS = 3;
    
    private JLabel titleLabel;
    private JLabel promptLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JLabel roundLabel;
    private JLabel difficultyLabel;
    private JTextField guessField;
    private JButton submitButton;
    private JButton newRoundButton;
    private JButton newGameButton;
    private JButton quitButton;
    private JComboBox<String> difficultyCombo;
    
    private final Color[] THEME_COLORS = {
        new Color(255, 105, 97),
        new Color(119, 221, 119),
        new Color(97, 175, 255)
    };
    private final Color BACKGROUND_COLOR = new Color(253, 245, 230);

    public NumberGame() {
        setTitle("Number Guessing Game");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        
        score = 0;
        round = 1;
        difficulty = 2;
        
        createComponents();
        startNewRound();
        setLocationRelativeTo(null);
    }
    
    private void createComponents() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(THEME_COLORS[1]);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titleLabel = new JLabel("NUMBER GUESSING GAME");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(BACKGROUND_COLOR);
        gamePanel.setLayout(new GridLayout(7, 1, 10, 15));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        difficultyPanel.setBackground(BACKGROUND_COLOR);
        difficultyLabel = new JLabel("Difficulty:", SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 16));
        difficultyPanel.add(difficultyLabel);
        
        String[] difficulties = {"Easy (15 attempts)", "Medium (10 attempts)", "Hard (5 attempts)"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedIndex(1);
        difficultyCombo.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        difficultyCombo.setBackground(Color.WHITE);
        difficultyCombo.addActionListener(e -> {
            difficulty = difficultyCombo.getSelectedIndex() + 1;
            startNewRound();
        });
        difficultyPanel.add(difficultyCombo);
        gamePanel.add(difficultyPanel);
        
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        infoPanel.setBackground(BACKGROUND_COLOR);
        
        roundLabel = new JLabel("Round: " + round + "/" + MAX_ROUNDS, SwingConstants.CENTER);
        roundLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 16));
        roundLabel.setForeground(THEME_COLORS[0]);
        
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 16));
        scoreLabel.setForeground(THEME_COLORS[2]);
        
        attemptsLabel = new JLabel("Attempts: " + getMaxAttempts(), SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 16));
        attemptsLabel.setForeground(THEME_COLORS[1]);
        
        infoPanel.add(roundLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(attemptsLabel);
        gamePanel.add(infoPanel);
        
        promptLabel = new JLabel("Enter your guess (1-100):", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        gamePanel.add(promptLabel);
        
        guessField = new JTextField();
        guessField.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(THEME_COLORS[2], 3),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        gamePanel.add(guessField);
        
        submitButton = new JButton("SUBMIT GUESS");
        submitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        submitButton.setBackground(THEME_COLORS[1]);
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                submitButton.setBackground(THEME_COLORS[1].darker());
            }
            public void mouseExited(MouseEvent e) {
                submitButton.setBackground(THEME_COLORS[1]);
            }
        });
        gamePanel.add(submitButton);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        newRoundButton = new JButton("NEXT ROUND");
        newRoundButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        newRoundButton.setBackground(THEME_COLORS[2]);
        newRoundButton.setForeground(Color.BLACK);
        newRoundButton.setFocusPainted(false);
        newRoundButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        newRoundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newRoundButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                newRoundButton.setBackground(THEME_COLORS[2].darker());
            }
            public void mouseExited(MouseEvent e) {
                newRoundButton.setBackground(THEME_COLORS[2]);
            }
        });
        newRoundButton.setVisible(false);
        
        newGameButton = new JButton("NEW GAME");
        newGameButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        newGameButton.setBackground(THEME_COLORS[0]);
        newGameButton.setForeground(Color.BLACK);
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newGameButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                newGameButton.setBackground(THEME_COLORS[0].darker());
            }
            public void mouseExited(MouseEvent e) {
                newGameButton.setBackground(THEME_COLORS[0]);
            }
        });
        newGameButton.setVisible(false);
        
        quitButton = new JButton("QUIT");
        quitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        quitButton.setBackground(new Color(220, 20, 60));
        quitButton.setForeground(Color.BLACK);
        quitButton.setFocusPainted(false);
        quitButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                quitButton.setBackground(new Color(200, 0, 40));
            }
            public void mouseExited(MouseEvent e) {
                quitButton.setBackground(new Color(220, 20, 60));
            }
        });
        
        buttonPanel.add(newRoundButton);
        buttonPanel.add(newGameButton);
        buttonPanel.add(quitButton);
        gamePanel.add(buttonPanel);
        
        add(gamePanel, BorderLayout.CENTER);
        
        submitButton.addActionListener(e -> checkGuess());
        newRoundButton.addActionListener(e -> nextRound());
        newGameButton.addActionListener(e -> resetGame());
        quitButton.addActionListener(e -> System.exit(0));
        
        guessField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkGuess();
                }
            }
        });
    }
    
    private int getMaxAttempts() {
        switch(difficulty) {
            case 1: return 15;
            case 2: return 10;
            case 3: return 5;
            default: return 10;
        }
    }
    
    private void startNewRound() {
        Random random = new Random();
        randomNumber = random.nextInt(100) + 1;
        attemptsLeft = getMaxAttempts();
        updateUI();
        guessField.setText("");
        guessField.setEnabled(true);
        submitButton.setEnabled(true);
        newRoundButton.setVisible(false);
        newGameButton.setVisible(false);
        guessField.requestFocus();
    }
    
    private void checkGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            
            if (guess < 1 || guess > 100) {
                showMessage("Please enter a number between 1 and 100.", "Invalid Input", THEME_COLORS[0]);
                return;
            }
            
            attemptsLeft--;
            
            if (guess == randomNumber) {
                int pointsEarned = attemptsLeft + 1;
                score += pointsEarned;
                showMessage(String.format(
                    "<html><center><font size='5' color='#006400'>🎉 CORRECT! 🎉</font><br><br>" +
                    "You guessed the number in %d attempts.<br>" +
                    "<font size='4' color='#006400'>+%d points!</font></center></html>", 
                    (getMaxAttempts() - attemptsLeft), pointsEarned
                ), "Congratulations!", THEME_COLORS[1]);
                
                guessField.setEnabled(false);
                submitButton.setEnabled(false);
                
                if (round < MAX_ROUNDS) {
                    newRoundButton.setVisible(true);
                } else {
                    showMessage(String.format(
                        "<html><center><font size='5'>GAME COMPLETE!</font><br><br>" +
                        "Your final score is: <font size='5' color='#FF4500'>%d</font></center></html>", 
                        score
                    ), "Game Over", THEME_COLORS[2]);
                    newGameButton.setVisible(true);
                }
            } else if (guess < randomNumber) {
                showMessage("Too low! Try a higher number.", "Hint", THEME_COLORS[2]);
            } else {
                showMessage("Too high! Try a lower number.", "Hint", THEME_COLORS[0]);
            }
            
            if (attemptsLeft <= 0 && guess != randomNumber) {
                showMessage(String.format(
                    "<html><center><font color='#8B0000'>Out of attempts!</font><br>" +
                    "The number was <font size='5'>%d</font></center></html>", 
                    randomNumber
                ), "Game Over", THEME_COLORS[0]);
                
                guessField.setEnabled(false);
                submitButton.setEnabled(false);
                
                if (round < MAX_ROUNDS) {
                    newRoundButton.setVisible(true);
                } else {
                    showMessage(String.format(
                        "<html><center><font size='5'>GAME COMPLETE!</font><br><br>" +
                        "Your final score is: <font size='5' color='#FF4500'>%d</font></center></html>", 
                        score
                    ), "Game Over", THEME_COLORS[2]);
                    newGameButton.setVisible(true);
                }
            }
            
            updateUI();
        } catch (NumberFormatException ex) {
            showMessage("Please enter a valid number.", "Invalid Input", THEME_COLORS[0]);
        }
    }
    
    private void showMessage(String message, String title, Color bgColor) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        JOptionPane pane = new JOptionPane(label, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(this, title);
        dialog.getContentPane().setBackground(bgColor);
        dialog.setVisible(true);
    }
    
    private void nextRound() {
        round++;
        startNewRound();
    }
    
    private void resetGame() {
        score = 0;
        round = 1;
        startNewRound();
    }
    
    private void updateUI() {
        roundLabel.setText("Round: " + round + "/" + MAX_ROUNDS);
        scoreLabel.setText("Score: " + score);
        attemptsLabel.setText("Attempts: " + attemptsLeft);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            NumberGame game = new NumberGame();
            game.setVisible(true);
        });
    }
}