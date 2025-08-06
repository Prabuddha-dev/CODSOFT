import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GradeCalculator extends JFrame {
    private List<String> subjectNames = new ArrayList<>();
    private List<Integer> marksList = new ArrayList<>();
    
    private JTextField subjectNameField;
    private JTextField marksField;
    private JTextArea resultArea;
    
    private Color PRIMARY_COLOR = new Color(70, 130, 180);
    private Color SECONDARY_COLOR = new Color(255, 215, 0);
    private Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private Color ACCENT_COLOR = new Color(220, 20, 60);
    
    public GradeCalculator() {
        setTitle("Student Grade Calculator");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        setupUI();
        setLocationRelativeTo(null);
    }
    
    private void setupUI() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("STUDENT GRADE CALCULATOR");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel subjectLabel = createLabel("Subject Name:");
        subjectNameField = createTextField();
        JLabel marksLabel = createLabel("Marks (0-100):");
        marksField = createTextField();
        
        inputPanel.add(subjectLabel);
        inputPanel.add(subjectNameField);
        inputPanel.add(marksLabel);
        inputPanel.add(marksField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton addButton = createButton("Add Subject", new Color(200, 230, 200));
        JButton calculateButton = createButton("Calculate", new Color(200, 200, 230));
        JButton resetButton = createButton("Reset", new Color(230, 200, 200));
        
        buttonPanel.add(addButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(resetButton);
        
        resultArea = new JTextArea(15, 30);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        
        addButton.addActionListener(e -> addSubject());
        calculateButton.addActionListener(e -> calculateResults());
        resetButton.addActionListener(e -> resetCalculator());
        
        subjectNameField.addActionListener(e -> marksField.requestFocus());
        marksField.addActionListener(e -> addSubject());
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(PRIMARY_COLOR);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
    
    private void addSubject() {
        String subjectName = subjectNameField.getText().trim();
        String marksText = marksField.getText().trim();
        
        if (subjectName.isEmpty()) {
            showError("Please enter subject name");
            return;
        }
        
        try {
            int marks = Integer.parseInt(marksText);
            if (marks < 0 || marks > 100) {
                showError("Marks must be between 0 and 100");
                return;
            }
            
            subjectNames.add(subjectName);
            marksList.add(marks);
            resultArea.append(String.format("%-25s: %3d\n", subjectName, marks));
            
            subjectNameField.setText("");
            marksField.setText("");
            subjectNameField.requestFocus();
        } catch (NumberFormatException ex) {
            showError("Please enter valid marks");
        }
    }
    
    private void calculateResults() {
        if (marksList.isEmpty()) {
            showError("No subjects added");
            return;
        }
        
        int total = marksList.stream().mapToInt(Integer::intValue).sum();
        double average = (double) total / marksList.size();
        String grade = calculateGrade(average);
        
        resultArea.append("\n══════════════════════════\n");
        resultArea.append(String.format("%-25s: %3d\n", "Total Marks", total));
        resultArea.append(String.format("%-25s: %6.2f%%\n", "Average Percentage", average));
        resultArea.append(String.format("%-25s: %3s\n", "Grade", grade));
    }
    
    private String calculateGrade(double percentage) {
        if (percentage >= 90) return "A+ ★";
        if (percentage >= 80) return "A ✦";
        if (percentage >= 70) return "B ●";
        if (percentage >= 60) return "C ○";
        if (percentage >= 50) return "D △";
        return "F ✗";
    }
    
    private void resetCalculator() {
        subjectNames.clear();
        marksList.clear();
        subjectNameField.setText("");
        marksField.setText("");
        resultArea.setText("");
        subjectNameField.requestFocus();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.WARNING_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new GradeCalculator().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}