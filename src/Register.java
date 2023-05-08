import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Register extends JFrame {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/library";
    static final String USER = "root";
    static final String PASS = "A1234567e123";

    private JTextField personalNoField;
    private JPasswordField passwordField;

    public Register() {
        // Set up the JFrame
        setTitle("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 130);
        setResizable(false);

        // Set up the container and layout manager
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 2, 3, 3));

        // Add the input fields and labels
        contentPane.add(new JLabel("Personal No:"));
        personalNoField = new JTextField();
        contentPane.add(personalNoField);

        contentPane.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        contentPane.add(passwordField);

        contentPane.add(new JLabel(""));
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        contentPane.add(registerButton);
    }
    // The method to register users
    private void registerUser() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Get the user input from the fields
            String personalNo = personalNoField.getText();
            char[] password = passwordField.getPassword();

            // Generate a user type and debt for the user
            String userType = "1";
            double debt = 0.0;
            String libray_libraryID = "1";

            // Insert the user information into the database using a prepared statement to prevent SQL injection
            String sql = "INSERT INTO reguser (personalNo, password, usertype, debt, library_libraryID) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, personalNo);
            stmt.setString(2, new String(password));
            stmt.setString(3, userType);
            stmt.setDouble(4, debt);
            stmt.setString(5,libray_libraryID);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear the input fields
            personalNoField.setText("");
            passwordField.setText("");
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Register registerForm = new Register();
        registerForm.setVisible(true);
    }
}
