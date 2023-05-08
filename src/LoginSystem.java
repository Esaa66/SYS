import Objects.User;
import com.mysql.cj.xdevapi.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginSystem {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton registerButton = new JButton("Registera");
    private JPanel panel = new JPanel(new GridBagLayout());

    // Creates Jframe functions for login
    public LoginSystem() {
        frame = new JFrame("Inloggningssystem");
        frame.setSize(340, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        usernameLabel = new JLabel("Personal No:");
        frame.add(usernameLabel);

        usernameField  = new JTextField(20);
        frame.add(usernameField);
        /*usernameField.setHorizontalAlignment(0.5);*/

        passwordLabel = new JLabel("Password:");
        frame.add(passwordLabel);

        passwordField = new JPasswordField(20);
        frame.add(passwordField);
        /*passwordField.setHorizontalAlignment(0.5);*/

        registerButton = new JButton("Registera");
        frame.add(registerButton);


        loginButton = new JButton("Logga in");
        frame.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();


                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123")) {
                    // Create a prepared statement to retrieve the user with the given username and password
                    try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reguser WHERE personalNo = ? AND password = ?")) {
                        stmt.setString(1, username);
                        stmt.setString(2, new String(password));

                        // Execute the query and retrieve the result set
                        try (ResultSet rs = stmt.executeQuery()) {
                            // If a user with the given username and password is found, log in and show the appropriate form
                            if (rs.next()) {
                                // Get the personal number of the user
                                String personalNo = rs.getString("personalNo");

                                // Check the personal number and open the appropriate form
                                if (personalNo.equals("101010101")) {
                                    StaffUser staffUser = new StaffUser();
                                    staffUser.setVisible(true);
                                } else {
                                    ClientUser userClient = new ClientUser();
                                    userClient.setVisible(true);
                                }

                                // Set the current frame to be invisible
                                frame.setVisible(false);
                            } else {
                                // If no user is found, show an error message
                                JOptionPane.showMessageDialog(frame, "Login failed", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    // If an SQL exception occurs, show an error message with the exception details
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });






        registerButton.addActionListener(new ActionListener()
            {
            public void actionPerformed(ActionEvent e) {


                new Register().setVisible(true); // Main Form to show after the Login Form..
            }
            });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new LoginSystem();
    }
}
