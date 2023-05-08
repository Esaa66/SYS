import Objects.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StaffUser extends JFrame {
    private JLabel lblUsername;
    private JTextField txtUsername;
    private JButton btnMyProfile;
    private JButton btnLogOut;
    private JButton btnAddItem;
    private JButton btnEditItem;
    private JButton btnRemoveItem;

    private User user;

    public StaffUser() {
        this.setTitle("Staff User");
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
    // Calls for all the buttons and list that was created in Main
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
        table.getColumnModel().getColumn(0).setPreferredWidth(119);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Create the scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 100, 450, 300);

        // Add the scroll pane to the panel
        panel.add(scrollPane);


        // Creates button for My Profile
        btnMyProfile = new JButton("My Profile");
        btnMyProfile.setBounds(380, 10, 100, 25);
        panel.add(btnMyProfile);

        // Creates button for Log out
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

        // Creates button for Add item
        btnAddItem = new JButton("Add Item");
        btnAddItem.setBounds(180, 400, 85, 20);
        panel.add(btnAddItem);
        btnAddItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //set frame wot enter book details
                JFrame g = new JFrame("Enter item Details");
                //g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // set labels
                JLabel l1,l2,l3,l4,l5;
                l1=new JLabel("Item Barcode");  //lebel 1 for book Barcode
                l1.setBounds(30,15, 100,30);


                l2=new JLabel("Item Name");  //label 2 for name
                l2.setBounds(30,53, 100,30);

                l3=new JLabel("Type");  //label 3 for type
                l3.setBounds(30,90, 100,30);

                l4=new JLabel("Location");  //label 4 for location
                l4.setBounds(30,128, 100,30);

                l5=new JLabel("LibraryID");  //label 5 for ID
                l5.setBounds(30,166, 100,30);

                //set text field for item barcode
                JTextField F_ibar = new JTextField();
                F_ibar.setBounds(110, 15, 200, 30);

                //set text field for name
                JTextField F_name=new JTextField();
                F_name.setBounds(110, 53, 200, 30);

                //set text field for type
                JTextField F_type=new JTextField();
                F_type.setBounds(110, 90, 200, 30);

                //set text field for location
                JTextField F_location=new JTextField();
                F_location.setBounds(110, 128, 200, 30);

                //set text field for ID
                JTextField F_ID=new JTextField();
                F_ID.setBounds(110, 166, 200, 30);


                JButton create_but=new JButton("Submit");//creating instance of JButton to submit details
                create_but.setBounds(185,210,80,25);//x axis, y axis, width, height
                create_but.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e){
                        // assign the book name, genre, price
                        String ibar = F_ibar.getText();
                        String name = F_name.getText();
                        String type = F_type.getText();
                        String location = F_location.getText();
                        String ID = F_ID.getText();
                        int loanable = 1;



                        try {
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123");

                            Statement stmt = conn.createStatement();
                            stmt.executeUpdate("USE LIBRARY");
                            stmt.executeUpdate("INSERT INTO object(Barcode,Title,loanable,type,Location,library_libraryID) VALUES ('"+ibar+"','"+name+"','"+loanable+"','"+type+"','"+location+"','"+ID+"')");
                            JOptionPane.showMessageDialog(null,"Book added!");
                            g.dispose();

                        }

                        catch (SQLException e1) {
                            // TODO Auto-generated catch block
                            JOptionPane.showMessageDialog(null, e1);
                        }

                    }

                });

                g.add(l3);
                g.add(create_but);
                g.add(l1);
                g.add(l2);
                g.add(l4);
                g.add(l5);
                g.add(F_ibar);
                g.add(F_name);
                g.add(F_type);
                g.add(F_location);
                g.add(F_ID);
                g.setSize(350,300);//400 width and 500 height
                g.setLayout(null);//using no layout managers
                g.setVisible(true);//making the frame visible
                g.setLocationRelativeTo(null);

            }
        });


        // Creates button for Edit item
        btnEditItem = new JButton("Edit Item");
        btnEditItem.setBounds(265, 400, 85, 20);
        panel.add(btnEditItem);
        btnEditItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //set frame to edit item details
                JFrame g = new JFrame("Edit Item Details");
                // set labels
                JLabel l1,l2,l3,l4,l5,l6;
                l1=new JLabel("Item Barcode");  //label 1 for item barcode
                l1.setBounds(30,15, 100,30);

                l2=new JLabel("Item Name");  //label 2 for name
                l2.setBounds(30,53, 100,30);

                l3=new JLabel("Loanable");  //label 3 for type
                l3.setBounds(30,90, 100,30);

                l4=new JLabel("Type");  //label 4 for location
                l4.setBounds(30,128, 100,30);

                l5=new JLabel("Location");  //label 5 for library ID
                l5.setBounds(30,166, 100,30);

                l6=new JLabel("LibraryID");  //label 5 for library ID
                l6.setBounds(30,204, 100,30);

                //set text field for item barcode
                JTextField F_ibar = new JTextField();
                F_ibar.setBounds(110, 15, 200, 30);

                //set text field for name
                JTextField F_name=new JTextField();
                F_name.setBounds(110, 53, 200, 30);

                //set text field for loanable
                JTextField F_loanable=new JTextField();
                F_loanable.setBounds(110, 90, 200, 30);

                //set text field for type
                JTextField F_type=new JTextField();
                F_type.setBounds(110, 128, 200, 30);

                //set text field for location
                JTextField F_location=new JTextField();
                F_location.setBounds(110, 166, 200, 30);

                //set text field for ID
                JTextField F_ID=new JTextField();
                F_ID.setBounds(110, 204, 200, 30);


                JButton update_but=new JButton("Update");//creating instance of JButton to update details
                update_but.setBounds(185,245,80,25);//x axis, y axis, width, height
                update_but.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e){

                        // retrieve the item barcode and new details entered by the user
                        String ibar = F_ibar.getText();

                        try {
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123");
                            Statement stmt = conn.createStatement();
                            stmt.executeUpdate("USE LIBRARY");
                            ResultSet rs = stmt.executeQuery("SELECT * FROM object WHERE Barcode = '"+ibar+"'");

                            if (rs.next()) {
                                String name = rs.getString("Title");
                                String loanable = rs.getString("loanable");
                                String type = rs.getString("type");
                                String location = rs.getString("Location");
                                String ID = rs.getString("library_libraryID");

                                // retrieve the new values entered by the user
                                if (!F_name.getText().equals("")) {
                                    name = F_name.getText();
                                }
                                if (!F_loanable.getText().equals("")) {
                                    loanable = F_loanable.getText();
                                }
                                if (!F_type.getText().equals("")) {
                                    type = F_type.getText();
                                }
                                if (!F_location.getText().equals("")) {
                                    location = F_location.getText();
                                }
                                if (!F_ID.getText().equals("")) {
                                    ID = F_ID.getText();
                                }

                                stmt.executeUpdate("UPDATE object SET Title = '"+name+"', loanable = '"+loanable+"', type = '"+type+"', Location = '"+location+"', library_libraryID = '"+ID+"' WHERE Barcode = '"+ibar+"'");
                                JOptionPane.showMessageDialog(null,"Item updated!");
                                g.dispose();
                            } else {
                                JOptionPane.showMessageDialog(null,"Item not found!");
                            }
                        } catch (SQLException e1) {
                            JOptionPane.showMessageDialog(null, e1);
                        }
                    }
                });

                // add components to the frame
                g.add(l3);
                g.add(update_but);
                g.add(l1);
                g.add(l2);
                g.add(l4);
                g.add(l5);
                g.add(l6);
                g.add(F_ibar);
                g.add(F_name);
                g.add(F_loanable);
                g.add(F_type);
                g.add(F_location);
                g.add(F_ID);
                g.setSize(350,330);//400 width and 500 height
                g.setLayout(null);//using no layout managers
                g.setVisible(true);//making the frame visible
                g.setLocationRelativeTo(null);

            }
        });
        // Creates button for Remove Item
        btnRemoveItem = new JButton("Remove Item");
        btnRemoveItem.setBounds(350, 400, 110, 20);
        panel.add(btnRemoveItem);
        btnRemoveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //set frame to delete item
                JFrame g = new JFrame("Delete Item");
                // set labels
                JLabel l1;
                l1 = new JLabel("Item Barcode");  //label 1 for item barcode
                l1.setBounds(30, 15, 100, 30);

                //set text field for item barcode
                JTextField F_ibar = new JTextField();
                F_ibar.setBounds(110, 15, 200, 30);

                JButton delete_but = new JButton("Delete");//creating instance of JButton to delete item
                delete_but.setBounds(185, 60, 80, 25);//x axis, y axis, width, height
                delete_but.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // retrieve the item barcode entered by the user
                        String ibar = F_ibar.getText();

                        try {
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123");

                            Statement stmt = conn.createStatement();
                            stmt.executeUpdate("USE LIBRARY");
                            // delete the record in the database with the specified item barcode
                            stmt.executeUpdate("DELETE FROM object WHERE Barcode = '"+ibar+"'");
                            JOptionPane.showMessageDialog(null, "Item deleted!");
                            g.dispose();

                        } catch (SQLException e1) {
                            JOptionPane.showMessageDialog(null, e1);
                        }
                    }
                });

                g.add(l1);
                g.add(F_ibar);
                g.add(delete_but);
                g.setSize(350, 150);//400 width and 500 height
                g.setLayout(null);//using no layout managers
                g.setVisible(true);//making the frame visible
                g.setLocationRelativeTo(null);
            }
        });
    }

    public static void main(String[] args) {

        new StaffUser().setVisible(true);
    }


}
