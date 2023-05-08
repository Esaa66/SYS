import Objects.Loan;
import Objects.User;
import Objects.Item;
import com.mysql.cj.util.StringUtils;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class ClientUser extends JFrame {

    private JLabel lblUsername;
    private JTextField txtUsername;
    private JButton btnMyProfile;
    private JButton btnLogOut;
    private JButton btnTidKvitton;
    private JButton btnLåna;

    private User user;

    private Connection conn;
    private static int nextLoanNumber = 1;
    private String loanId;

    private List<Item> items;
    private List<User> users;
    private List<Loan> loans;

    // Define the maximum number of objects a borrower can have on loan at one time
    private static final int MAX_LOANS_STUDENT = 3;
    private static final int MAX_LOANS_FACULTY = 6;
    private static final int MAX_LOANS_OTHER = 2;

    // Define the loan periods for different types of objects
    private static final int LOAN_PERIOD_TEXTBOOK = 14;
    private static final int LOAN_PERIOD_OTHER_BOOK = 30;
    private static final int LOAN_PERIOD_JOURNAL = 0;
    private static final int LOAN_PERIOD_REFERENCE = 0;
    private static final int LOAN_PERIOD_DVD = 7;
    private int loanCounter = 0;



    // This code generates a unique loan id
    public class LoanIdGenerator {
        private static final String PREFIX = "LNLIB";
        private static final AtomicInteger counter = new AtomicInteger(1);

        public static String getNextLoanId() {
            int number = counter.getAndIncrement();
            String paddedNumber = String.format("%04d", number);
            return PREFIX + paddedNumber;
        }
    }






    // Checks the database for already existing loan id
    public String getLoanId() {
        String loanId = LoanIdGenerator.getNextLoanId();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123");
            Statement stmt = conn.createStatement();
            String query = "SELECT loanID FROM loan ORDER BY loanID DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String lastLoanId = rs.getString("loanID");
                if (lastLoanId.length() < 8) {
                    lastLoanId = String.format("%1$-8s", lastLoanId).replace(' ', '0');
                } else {
                    loanId = "LNLIB00" + lastLoanId.substring(lastLoanId.length() - 2);
                }
                int lastLoanNumber = Integer.parseInt(lastLoanId.substring(5));
                loanCounter = lastLoanNumber;
            }

            // Generate new loan ID if the current one already exists
            while (true) {
                loanCounter++;
                loanId = LoanIdGenerator.getNextLoanId();
                String checkQuery = "SELECT loanID FROM loan WHERE loanID = '" + loanId + "'";
                ResultSet checkRs = stmt.executeQuery(checkQuery);
                if (!checkRs.next()) {
                    break;
                }
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loanId;
    }



    public ClientUser() {
        this.setTitle("Client User");
        this.conn = conn;
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panel = new JPanel();
        this.add(panel);
        this.initComponents(panel);
    }



    LibrarySystem librarySystem = new LibrarySystem();
    // set the date and time to start sending reminders
    Date date = new Date();
    java.util.Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            librarySystem.sendReminderEmails();
            timer.scheduleAtFixedRate(task, date, 24 * 60 * 60 * 1000);// send reminders every 24 hours
        }
    };

    public void setClientUser(User user) {
        this.user = user;
        // Set the user information to the appropriate components in the form
        JLabel usernameLabel = (JLabel) this.getContentPane().getComponent(1);
        usernameLabel.setText(user.getId());
        JLabel nameLabel = (JLabel) this.getContentPane().getComponent(3);
        nameLabel.setText(user.getName());
        JLabel emailLabel = (JLabel) this.getContentPane().getComponent(5);
        emailLabel.setText(user.getEmail());
        JButton staffOptionsButton = (JButton) this.getContentPane().getComponent(7);
        // If the user is a staff member, show the staff options
        if (user instanceof User) {
            // Enable the staff options button
            staffOptionsButton.setEnabled(true);
        } else {
            // Disable the staff options button
            staffOptionsButton.setEnabled(false);
        }
    }


    // Builds up Jframe buttons for varies functions
    private void initComponents(JPanel panel) {
        panel.setLayout(null);


        JPanel panel12 = new JPanel();
        JButton magazinesButton = Main.addMagazinesButton(panel);
        panel.add(magazinesButton);

        JPanel panel13 = new JPanel();
        JButton filmsButton = Main.addFilmsButton(panel);
        panel.add(filmsButton);

        JPanel panel14 = new JPanel();
        JButton booksButton = Main.addBooksButton(panel);
        panel.add(booksButton);

        JPanel panel15 = new JPanel();
        JButton searchButton = Main.addSearchButton(panel);
        panel.add(searchButton);


        DefaultTableModel tableModel = Main.createTableModel();
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(121);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Create the scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 100, 450, 300);

        // Add the scroll pane to the panel
        panel.add(scrollPane);


        btnMyProfile = new JButton("My Profile");
        btnMyProfile.setBounds(380, 10, 100, 25);
        panel.add(btnMyProfile);
        btnMyProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the client data from the database using their unique identifier
                    String clientId = "";
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123");
                    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reguser WHERE personalNO = ?");
                    pstmt.setString(1, clientId);
                    ResultSet rs = pstmt.executeQuery();

                    // Create a new panel to display the client data
                    JPanel profilePanel = new JPanel();
                    profilePanel.setLayout(new GridLayout(2, 2));
                    profilePanel.add(new JLabel("User Type:"));
                    profilePanel.add(new JLabel(rs.getString("usertype")));
                    profilePanel.add(new JLabel("Library ID"));
                    profilePanel.add(new JLabel(rs.getString("library_libraryID")));

                    // Display the panel in a new dialog
                    JOptionPane.showMessageDialog(null, profilePanel, "My Profile", JOptionPane.PLAIN_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to retrieve client data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnLogOut = new JButton("Log Out");
        btnLogOut.setBounds(380, 50, 100, 25);
        panel.add(btnLogOut);
        btnLogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Confirm Log Out", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    // code to close current class
                    dispose();

                }
                Main main = new Main();
                main.setVisible(true);
            }
        });

        btnLåna = new JButton("Låna");
        btnLåna.setBounds(370, 400, 90, 20);
        panel.add(btnLåna);

        btnLåna.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e){

                //enter details
                JFrame g = new JFrame("Enter Details");
                //g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //create labels
                JLabel l1,l2,l3,l4;
                l1=new JLabel("Item ID(IID)");  // Label 1 for Book ID
                l1.setBounds(30,15, 100,30);


                l2=new JLabel("User ID(UID)");  //Label 2 for user ID
                l2.setBounds(30,53, 100,30);

                JTextField F_bid = new JTextField();
                F_bid.setBounds(110, 15, 200, 30);


                JTextField F_uid=new JTextField();
                F_uid.setBounds(110, 53, 200, 30);


                JButton create_but=new JButton("Submit");//creating instance of JButton
                create_but.setBounds(130,100,80,25);//x axis, y axis, width, height
                create_but.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e){

                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_MONTH, 30);


                        String uid = F_uid.getText();
                        String bid = F_bid.getText();
                        /*String period = F_period.getText();*/
                        SimpleDateFormat period = new SimpleDateFormat("yyyy-MM-dd");
                        String period1 = period.format(calendar.getTime());
                        SimpleDateFormat issued_date = new SimpleDateFormat("yyyy-MM-dd");
                        String loanId = getLoanId();



                        String username = "";
                        String password = "";
                        int id = 0;

                        try {
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123");

                            Statement stmt = conn.createStatement();
                            String query = "SELECT * FROM reguser WHERE personalNo='" + username + "' AND password='" + password + "'";
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                int borrowerId = rs.getInt("id");
                                String borrowerType = rs.getString("type");
                                int maxLoans = 0;
                                switch (borrowerType) {
                                    case "Student":
                                        maxLoans = MAX_LOANS_STUDENT;
                                        break;
                                    case "Faculty":
                                        maxLoans = MAX_LOANS_FACULTY;
                                        break;
                                    default:
                                        maxLoans = MAX_LOANS_OTHER;
                                        break;
                                }
                                int currentLoans = librarySystem.getBorrowedCount(conn, borrowerId);
                                if (currentLoans < maxLoans) {
                                    Date dueDate = new Date();
                                    Item item = librarySystem.getById(conn, id);
                                    switch (item.getType()) {
                                        case "Textbook":
                                            dueDate = librarySystem.addDays(LOAN_PERIOD_TEXTBOOK);
                                            break;
                                        case "Other Book":
                                            dueDate = librarySystem.addDays(LOAN_PERIOD_OTHER_BOOK);
                                            break;
                                        case "Journal":
                                            dueDate = librarySystem.addDays(LOAN_PERIOD_JOURNAL);
                                            break;
                                        case "Reference":
                                            JOptionPane.showMessageDialog(null, "Reference materials cannot be borrowed");
                                            return;
                                        case "DVD":
                                            dueDate = librarySystem.addDays(LOAN_PERIOD_DVD);
                                            break;
                                        default:
                                            JOptionPane.showMessageDialog(null, "Invalid object type");
                                            return;
                                    }
                                    int copies = item.getCopies();
                                    if (copies > 0) {
                                        String insertQuery = "INSERT INTO loans (object_id, borrower_id, due_date) VALUES (" + id + ", " + borrowerId + ", '" + librarySystem.formatDate(dueDate) + "')";
                                        stmt.executeUpdate(insertQuery);
                                        String updateQuery = "UPDATE objects SET copies=" + (copies - 1) + " WHERE id=" + id;
                                        stmt.executeUpdate(updateQuery);
                                        JOptionPane.showMessageDialog(null, "Object borrowed successfully");
                                    } else {
                                        JOptionPane.showMessageDialog(null, "No copies available to borrow");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Maximum loan limit reached");
                                }
                            } /*else {
                                JOptionPane.showMessageDialog(null, "Invalid username or password");
                            }
*/


                            stmt.executeUpdate("USE LIBRARY");
                            stmt.executeUpdate("INSERT INTO loan(loanID, regUser_personalNo, object_Barcode, loanDATE, ReturnDate) VALUES ('"+loanId+"', '"+uid+"', '"+bid+"', '"+issued_date.format(new Date())+"', '"+period1+"')");

                            JOptionPane.showMessageDialog(null,"Book Issued!");
                            g.dispose();

                        }

                        catch (SQLException e1) {
                            // TODO Auto-generated catch block
                            JOptionPane.showMessageDialog(null, e1);
                        }

                    }

                });



                g.add(create_but);
                g.add(l1);
                g.add(l2);
                g.add(F_uid);
                g.add(F_bid);
                g.setSize(350,180);//400 width and 500 height
                g.setLayout(null);//using no layout managers
                g.setVisible(true);//making the frame visible
                g.setLocationRelativeTo(null);


            }


            /*public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Do you want a receipt for your loan?", "Receipt", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    // Code to generate receipt for loan
                    // ...

                    // Print the receipt

                    JOptionPane.showMessageDialog(new JFrame(), "Ta ditt kvitto", "Kvitto", JOptionPane.INFORMATION_MESSAGE);


                }
            }*/
        });


        // Creates Jframe button for "Lånehistorik"
        btnTidKvitton = new JButton("Lånehistorik");
        btnTidKvitton.setBounds(240, 400, 130, 20);
        panel.add(btnTidKvitton);

        btnTidKvitton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the list of loans
                LibrarySystem librarySystem = new LibrarySystem();
                ArrayList<Loan> loans = librarySystem.getLoans();

                // Create a list model and add the loans to it
                DefaultListModel<String> loanListModel = new DefaultListModel<String>();
                for (Loan loan : loans) {
                    String loanInfo = loan.getId() + " | " + loan.getItem() + " | " + loan.getUser() + " | " + loan.getLoanDate()
                            + " | " + loan.getDueDate();
                    loanListModel.addElement(loanInfo);
                }

                // Create a JList and add the list model to it
                JList<String> loanList = new JList<String>(loanListModel);

                // Show the JList in a new window
                JFrame loanFrame = new JFrame("Lånehistorik");
                loanFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                loanFrame.setSize(500, 500);
                loanFrame.add(new JScrollPane(loanList));
                loanFrame.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        new ClientUser().setVisible(true);
    }
}

