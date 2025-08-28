import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

public class StylishATMGUI extends JFrame {
    private BankAccount bankAccount;
    private JTextPane displayPane;
    private JTextField amountField;
    private JButton checkBalanceBtn, depositBtn, withdrawBtn, exitBtn, confirmBtn, cancelBtn;
    private JPanel mainPanel, buttonPanel, inputPanel, headerPanel;
    private DecimalFormat currencyFormat;
    private Timer animationTimer;
    private int animationCounter = 0;

    private enum TransactionType {
        NONE, DEPOSIT, WITHDRAW
    }

    private TransactionType currentTransaction;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(231, 76, 60);
    private final Color DARK_BG = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color CARD_COLOR = new Color(255, 255, 255);

    public StylishATMGUI(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.currencyFormat = new DecimalFormat("$#,##0.00");
        this.currentTransaction = TransactionType.NONE;

        initializeGUI();
        startWelcomeAnimation();
    }

    private void initializeGUI() {
        setTitle("💳 Modern ATM Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, DARK_BG,
                        getWidth(), getHeight(), new Color(52, 73, 94));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        createHeaderPanel();

        displayPane = new JTextPane();
        displayPane.setEditable(false);
        displayPane.setContentType("text/html");
        displayPane.setBackground(new Color(255, 255, 255, 200));
        displayPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255, 100), 2, true),
                new EmptyBorder(15, 15, 15, 15)));
        displayPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        createInputPanel();

        createButtonPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        add(mainPanel);

        setupActionListeners();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                getComponentAt(e.getPoint()).setLocation(e.getPoint());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - getWidth() / 2, e.getYOnScreen() - getHeight() / 2);
            }
        });
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("🏦 MODERN ATM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(closeBtn, BorderLayout.EAST);
    }

    private void createInputPanel() {
        inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setOpaque(false);

        JLabel amountLabel = new JLabel("💵 Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountLabel.setForeground(Color.WHITE);

        amountField = new JTextField(12);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amountField.setEnabled(false);
        amountField.setBorder(new RoundBorder(15, SECONDARY_COLOR));
        amountField.setHorizontalAlignment(JTextField.CENTER);

        confirmBtn = createModernButton("✅ Confirm", ACCENT_COLOR, 12);
        confirmBtn.setEnabled(false);

        cancelBtn = createModernButton("❌ Cancel", WARNING_COLOR, 12);
        cancelBtn.setEnabled(false);

        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(confirmBtn);
        inputPanel.add(cancelBtn);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 10, 0, 0));

        checkBalanceBtn = createModernButton("💰 Check Balance", PRIMARY_COLOR, 14);
        depositBtn = createModernButton("📥 Deposit", ACCENT_COLOR, 14);
        withdrawBtn = createModernButton("📤 Withdraw", new Color(255, 165, 0), 14);
        exitBtn = createModernButton("🚪 Exit", WARNING_COLOR, 14);

        buttonPanel.add(checkBalanceBtn);
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(exitBtn);
    }

    private JButton createModernButton(String text, Color color, int fontSize) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 5, 12, 5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void setupActionListeners() {
        checkBalanceBtn.addActionListener(e -> checkBalance());

        depositBtn.addActionListener(e -> {
            currentTransaction = TransactionType.DEPOSIT;
            amountField.setEnabled(true);
            confirmBtn.setEnabled(true);
            cancelBtn.setEnabled(true);
            showMessage("Please enter deposit amount and click Confirm.", "info");
            amountField.requestFocus();
        });

        withdrawBtn.addActionListener(e -> {
            currentTransaction = TransactionType.WITHDRAW;
            amountField.setEnabled(true);
            confirmBtn.setEnabled(true);
            cancelBtn.setEnabled(true);
            showMessage("Please enter withdrawal amount and click Confirm.", "info");
            amountField.requestFocus();
        });

        exitBtn.addActionListener(e -> exitATM());

        confirmBtn.addActionListener(e -> processTransaction());

        cancelBtn.addActionListener(e -> resetTransaction());

        amountField.addActionListener(e -> processTransaction());
    }

    private void checkBalance() {
        String htmlMessage = "<html><div style='text-align: center; color: #2c3e50;'>" +
                "<h2 style='color: #2980b9;'>💰 Account Balance</h2>" +
                "<p><b>Holder:</b> " + bankAccount.getAccountHolderName() + "</p>" +
                "<p><b>Account:</b> " + bankAccount.getAccountNumber() + "</p>" +
                "<p style='font-size: 18px; color: #27ae60;'><b>Balance:</b> " +
                currencyFormat.format(bankAccount.getBalance()) + "</p>" +
                "</div></html>";

        displayPane.setText(htmlMessage);
        playSuccessAnimation();
    }

    private void processTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText());

            if (amount <= 0) {
                showMessage("❌ Amount must be greater than zero!", "error");
                return;
            }

            boolean success = false;
            String message = "";

            switch (currentTransaction) {
                case DEPOSIT:
                    success = bankAccount.deposit(amount);
                    if (success) {
                        message = "✅ Successfully deposited " + currencyFormat.format(amount) +
                                "<br>💰 New Balance: " + currencyFormat.format(bankAccount.getBalance());
                        playSuccessAnimation();
                    } else {
                        message = "❌ Deposit failed!";
                    }
                    break;

                case WITHDRAW:
                    if (!bankAccount.hasSufficientBalance(amount)) {
                        message = "❌ Insufficient balance!<br>💵 Current: " +
                                currencyFormat.format(bankAccount.getBalance());
                    } else {
                        success = bankAccount.withdraw(amount);
                        if (success) {
                            message = "✅ Successfully withdrew " + currencyFormat.format(amount) +
                                    "<br>💰 New Balance: " + currencyFormat.format(bankAccount.getBalance());
                            playSuccessAnimation();
                        } else {
                            message = "❌ Withdrawal failed!";
                        }
                    }
                    break;

                default:
                    message = "⚠️ No transaction selected!";
            }

            showMessage(message, success ? "success" : "error");
            resetTransaction();

        } catch (NumberFormatException ex) {
            showMessage("❌ Please enter a valid number!", "error");
            amountField.requestFocus();
        }
    }

    private void resetTransaction() {
        currentTransaction = TransactionType.NONE;
        amountField.setText("");
        amountField.setEnabled(false);
        confirmBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
    }

    private void exitATM() {
        String htmlMessage = "<html><div style='text-align: center; color: #2c3e50;'>" +
                "<h2 style='color: #e74c3c;'>👋 Thank You!</h2>" +
                "<p>Thank you for using our ATM!</p>" +
                "<p>Have a nice day! 🌟</p>" +
                "<p style='font-size: 16px; color: #27ae60;'><b>Final Balance:</b> " +
                currencyFormat.format(bankAccount.getBalance()) + "</p>" +
                "</div></html>";

        displayPane.setText(htmlMessage);

        checkBalanceBtn.setEnabled(false);
        depositBtn.setEnabled(false);
        withdrawBtn.setEnabled(false);
        confirmBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        Timer timer = new Timer(3000, e -> System.exit(0));
        timer.setRepeats(false);
        timer.start();
    }

    private void showMessage(String message, String type) {
        String color = type.equals("error") ? "#e74c3c" : type.equals("success") ? "#27ae60" : "#3498db";

        String htmlMessage = "<html><div style='text-align: center; color: " + color + ";'>" +
                message.replace("\n", "<br>") +
                "</div></html>";
        displayPane.setText(htmlMessage);
    }

    private void startWelcomeAnimation() {
        String[] welcomeMessages = {
                "🚀 Starting ATM System...",
                "🔒 Loading Security Protocols...",
                "💳 Initializing Banking Services...",
                "✅ System Ready!"
        };

        animationTimer = new Timer(800, e -> {
            if (animationCounter < welcomeMessages.length) {
                showMessage(welcomeMessages[animationCounter], "info");
                animationCounter++;
            } else {
                animationTimer.stop();
                showWelcomeMessage();
            }
        });
        animationTimer.start();
    }

    private void showWelcomeMessage() {
        String htmlMessage = "<html><div style='text-align: center; color: #2c3e50;'>" +
                "<h1 style='color: #2980b9;'>🏦 WELCOME</h1>" +
                "<p><b>Account:</b> " + bankAccount.getAccountHolderName() + "</p>" +
                "<p><b>Number:</b> " + bankAccount.getAccountNumber() + "</p>" +
                "<div style='background: #ecf0f1; padding: 15px; border-radius: 10px; margin: 10px;'>" +
                "<p style='color: #27ae60; font-weight: bold;'>Please select an option:</p>" +
                "<p>💰 Check Balance</p>" +
                "<p>📥 Deposit Money</p>" +
                "<p>📤 Withdraw Cash</p>" +
                "<p>🚪 Exit ATM</p>" +
                "</div>" +
                "</div></html>";

        displayPane.setText(htmlMessage);
    }

    private void playSuccessAnimation() {
        Timer successTimer = new Timer(100, e -> {
            buttonPanel.setBackground(animationCounter % 2 == 0 ? ACCENT_COLOR : new Color(236, 240, 241));
            animationCounter++;
            if (animationCounter > 6) {
                ((Timer) e.getSource()).stop();
                buttonPanel.setBackground(new Color(236, 240, 241));
            }
        });
        successTimer.start();
    }

    class RoundBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = this.radius + 1;
            insets.top = insets.bottom = this.radius + 1;
            return insets;
        }
    }
}