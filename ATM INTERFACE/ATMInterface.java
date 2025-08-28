import javax.swing.*;

public class ATMInterface {
    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            BankAccount account = new BankAccount("1234 5678 9012", "Prabuddha Gadhe", 2500.00);

            SwingUtilities.invokeLater(() -> {
                StylishATMGUI atmGUI = new StylishATMGUI(account);
                atmGUI.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}