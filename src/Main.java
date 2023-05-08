import Objects.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class Main {

    private static JScrollPane scrollPane;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Library System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    public void setVisible(boolean visible) {

        // code to set visibility
    }


    LibrarySystem librarySystem = new LibrarySystem();
    // set the date and time to start sending reminders
    Date date = new Date();
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            librarySystem.sendReminderEmails();
            timer.scheduleAtFixedRate(task, date, 24 * 60 * 60 * 1000);// send reminders every 24 hours
        }
    };


    // Creates Jframe buttons and functions for magazines
    public static JButton addMagazinesButton(JPanel panel) {
        JButton magazinesButton = new JButton("Magazines");
        magazinesButton.setBounds(160, 0, 93, 20);
        panel.add(magazinesButton);

        magazinesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Connect to the database
                Connection conn = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "A1234567e123");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Retrieve the list of magazines from the database
                ArrayList<Magazine> magazines = new ArrayList<Magazine>();
                try {
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM journals";
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        Magazine magazine = new Magazine(rs.getString("object_Barcode"), rs.getString("ISSN"),
                                rs.getString("Date"),"","",1,"",true, new Date());
                        magazines.add(magazine);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Define the column names and data for the JTable
                String[] columnNames = {"Barcode", "ISSN", "Title", "Date"};
                Object[][] data = new Object[magazines.size()][4];
                for (int i = 0; i < magazines.size(); i++) {
                    Magazine magazine = magazines.get(i);
                    data[i][3] = magazine.getBarcode();
                    data[i][2] = magazine.getISSN();
                    data[i][1] = magazine.getTitle();
                    data[i][0] = magazine.getDate();
                }

                // Create a JTable and add it to a JScrollPane
                JTable magazineTable = new JTable(data, columnNames);
                magazineTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                magazineTable.getColumnModel().getColumn(0).setPreferredWidth(100);
                magazineTable.getColumnModel().getColumn(1).setPreferredWidth(100);
                magazineTable.getColumnModel().getColumn(2).setPreferredWidth(200);
                magazineTable.getColumnModel().getColumn(3).setPreferredWidth(100);
                JScrollPane scrollPane = new JScrollPane(magazineTable);

                // Show the JTable in a new window
                JFrame magazineFrame = new JFrame("List of Magazines");
                magazineFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                magazineFrame.setSize(500, 500);
                magazineFrame.add(scrollPane);
                magazineFrame.setVisible(true);

                // Close the database connection
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return magazinesButton;
    }
    // Creates Jframe buttons and functions for books
    public static JButton addBooksButton(JPanel panel) {
        JButton booksButton = new JButton("Books");
        booksButton.setBounds(0, 0, 80, 20);
        panel.add(booksButton);

        booksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Connect to the database
                Connection conn = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "A1234567e123");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Retrieve the list of books from the database
                ArrayList<Book> books = new ArrayList<Book>();
                try {
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM books";
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        Book book = new Book(rs.getString("object_Barcode"), rs.getString("Author"),
                                rs.getString("ISBN"), rs.getString("Subject"),"",0,"", true);
                        books.add(book);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Define the column names and data for the JTable
                String[] columnNames = {"Barcode", "Author", "ISBN", "Subject"};
                Object[][] data = new Object[books.size()][4];
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    data[i][3] = book.getBarcode();
                    data[i][2] = book.getAuthor();
                    data[i][1] = book.getISBN();
                    data[i][0] = book.getSubject();
                }

                // Create a JTable and add it to a JScrollPane
                JTable bookTable = new JTable(data, columnNames);
                bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                bookTable.getColumnModel().getColumn(0).setPreferredWidth(100);
                bookTable.getColumnModel().getColumn(1).setPreferredWidth(100);
                bookTable.getColumnModel().getColumn(2).setPreferredWidth(200);
                bookTable.getColumnModel().getColumn(3).setPreferredWidth(100);
                JScrollPane scrollPane = new JScrollPane(bookTable);

                // Show the JTable in a new window
                JFrame bookFrame = new JFrame("List of Books");
                bookFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                bookFrame.setSize(500, 500);
                bookFrame.add(scrollPane);
                bookFrame.setVisible(true);

                // Close the database connection
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return booksButton;
    }

    // Creates Jframe buttons and functions for films
    public static JButton addFilmsButton(JPanel panel) {
        JButton filmsButton = new JButton("Films");
        filmsButton.setBounds(80, 0, 80, 20);
        panel.add(filmsButton);

        filmsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Connect to the database
                Connection conn = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "A1234567e123");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // Retrieve the list of films from the database
                ArrayList<Film> films = new ArrayList<Film>();
                try {
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM movies";
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        Film film = new Film(rs.getString("object_Barcode"), rs.getString("Director"),
                                rs.getString("Genre"), rs.getString("AgeRestriction"),
                                rs.getString("Actor"), rs.getString("Country"),0,true,0);
                        films.add(film);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Create a DefaultTableModel and add the data
                DefaultTableModel filmTableModel = new DefaultTableModel(new Object[][]{}, new String[] {"Barcode", "Director", "Genre", "AgeRestriction", "Actor", "Country"});
                for (int i = 0; i < films.size(); i++) {
                    Film film = films.get(i);
                    filmTableModel.addRow(new Object[] { film.getBarcode(), film.getDirector(), film.getGenre(), film.getAgeRestriction(), film.getActor(), film.getCountry() });
                }

                // Create a JTable using the DefaultTableModel
                JTable filmTable = new JTable(filmTableModel);
                filmTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                filmTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                filmTable.getColumnModel().getColumn(1).setPreferredWidth(50);
                filmTable.getColumnModel().getColumn(2).setPreferredWidth(50);
                filmTable.getColumnModel().getColumn(3).setPreferredWidth(50);
                filmTable.getColumnModel().getColumn(4).setPreferredWidth(50);
                filmTable.getColumnModel().getColumn(5).setPreferredWidth(50);

                JScrollPane scrollPane = new JScrollPane(filmTable);

                // Set the horizontal alignment of each column
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                for (int i = 0; i < filmTable.getColumnCount(); i++) {
                    TableColumn column = filmTable.getColumnModel().getColumn(i);
                    column.setCellRenderer(centerRenderer);
                    column.setPreferredWidth(100);
                }

                // Show the JTable in a new window
                JFrame filmFrame = new JFrame("List of Films");
                filmFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                filmFrame.setSize(600, 500);
                filmFrame.add(scrollPane);
                filmFrame.setVisible(true);


                // Close the database connection
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return filmsButton;
    }

    // Creates Jframe buttons and functions for search
    private static JPanel panel = new JPanel();
    private static JTable table = new JTable();
    public static JButton addSearchButton(JPanel panel) {
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(10, 50, 80, 25);
        panel.add(searchLabel);

        JTextField searchField = new JTextField(20);
        searchField.setBounds(70, 50, 165, 25);
        panel.add(searchField);

        JButton searchButton = new JButton("Sök");
        searchButton.setBounds(240, 50, 60, 25);
        panel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "A1234567e123")) {
                    ArrayList<Item> results = LibrarySystem.searchItem(conn, searchTerm, "", "", "","","");
                    DefaultTableModel searchTableModel = new DefaultTableModel();
                    searchTableModel.addColumn("Barcode");
                    searchTableModel.addColumn("Title");
                    searchTableModel.addColumn("Loanable");
                    searchTableModel.addColumn("Type");
                    for (Item result : results) {
                        searchTableModel.addRow(new Object[] { result.getBarcode(), result.getTitle(), result.isAvailable(), result.getType() });
                    }
                    table.setModel(searchTableModel);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to connect to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        return searchButton;
    }

    // Creates Jframe buttons and functions for login
    public static JButton addLoginButton(JPanel panel) {
        JButton loginButton = new JButton("Logga in");
        loginButton.setBounds(380, 50, 100, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new LoginSystem(); // Main Form to show after the Login Form..
            }
        });

        return loginButton;
    }
    // Creates list of items
    public static DefaultTableModel createTableModel() {

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Barcode");
        tableModel.addColumn("Title");
        tableModel.addColumn("Loanable");
        tableModel.addColumn("Type");


        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 100, 450, 300);
        panel.add(scrollPane);

        try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library",
                "root", "A1234567e123");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT barcode, title, loanable, type FROM object");

        while (resultSet.next()) {
            String barcode = resultSet.getString("barcode");
            String title = resultSet.getString("title");
            String loanable = resultSet.getString("loanable");
            String type = resultSet.getString("type");
            tableModel.addRow(new Object[] { barcode, title, loanable, type });
        }

        } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Failed to connect to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        return tableModel;
    }


    // Calls for all the previous added buttons and list
    public static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton booksButton = addBooksButton(panel);
        JButton filmsButton = addFilmsButton(panel);
        JButton magazinesButton = addMagazinesButton(panel);
        JButton searchButton = addSearchButton(panel);
        JButton loginButton = addLoginButton(panel);

        DefaultTableModel tableModel = createTableModel();
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Create the scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 100, 450, 300);

        // Add the scroll pane to the panel
        panel.add(scrollPane);


        }



}