import javax.swing.*;

public class ATMInterface {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Create a sample bank account
            BankAccount account = new BankAccount("1234 5678 9012", "Emily Johnson", 2500.00);

            // Create and show the stylish ATM GUI
            SwingUtilities.invokeLater(() -> {
                StylishATMGUI atmGUI = new StylishATMGUI(account);
                atmGUI.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}