import Objects.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class LibrarySystem {
    private List<Item> items;
    private List<User> users;
    private List<Loan> loans;

    public LibrarySystem() {
        this.items = new ArrayList<>();
        this.users = new ArrayList<>();
        this.loans = new ArrayList<>();
    }
    // Retrieves all variables from the Loan object
    public ArrayList<Loan> getLoans() {
        ArrayList<Loan> loans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan instanceof Loan) {
                loans.add((Loan) loan);
            }
        }
        return loans;
    }
    // Retrieves all variables from the Item object
    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        for (Item item : items) {
                items.add(item);
            }

        return items;
    }

    // Retrieves the number of loans
    public int getBorrowedCount(Connection conn, int borrowerId) {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT COUNT(*) as borrowed_count FROM loans WHERE borrower_id=" + borrowerId;
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt("borrowed_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // Retrieves an item by the id
    public Item getById(Connection conn, int id) throws SQLException {
        Item item = null;
        String query = "SELECT * FROM objects WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String type = rs.getString("type");
            String title = rs.getString("title");
            int copies = rs.getInt("copies");
            item = new Item(id, type, title, copies) {
            };
        }
        return item;
    }
    // Retrieves a specified number of days for the calender
    public Date addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }


    // Chooses the specific date form
    public String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    // Search function to find items
    public static ArrayList<Item> searchItem(Connection conn, String barcode, String title, String loanable, String type, String location, String library_libraryID) {
        ArrayList<Item> results = new ArrayList<Item>();
        try {
            String query = "SELECT * FROM object WHERE barcode LIKE ? AND  title LIKE ? AND loanable LIKE ? AND type LIKE ? AND location LIKE ? AND library_libraryID LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + barcode + "%");
            pstmt.setString(2, "%" + title + "%");
            pstmt.setString(3, "%" + loanable + "%");
            pstmt.setString(4, "%" + type + "%");
            pstmt.setString(5, "%" + location + "%");
            pstmt.setString(6, "%" + library_libraryID + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("barcode");
                String objTitle = rs.getString("title");
                String objAuthor = rs.getString("type");
                String objIsbn = rs.getString("loanable");
                String objSubject = rs.getString("location");
                String objType = rs.getString("library_libraryID");
                /*int objCopies = rs.getInt("copies");*/
                Item obj = new Item("","", "", "","","",0,true) {
                };
                results.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }



    // Gets a list of objects that are currently overdue for the specified borrower
    public static ArrayList<Item> getOverdueObjects(Connection conn, int borrowerId) {
        ArrayList<Item> results = new ArrayList<Item>();
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT objects.*, loans.due_date FROM objects, loans WHERE objects.id=loans.object_id AND loans.borrower_id=" + borrowerId + " AND loans.returned_date IS NULL AND loans.due_date < CURDATE()";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                String subject = rs.getString("subject");
                String type = rs.getString("type");
                int copies = rs.getInt("copies");
                Date dueDate = rs.getDate("due_date");
                Item item = new Item("","", "", "", "", "", 0, true) {
                };
                results.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    // Generates a receipt for a loan
    public String generateReceipt(Loan loan) {
        String receipt = "Loan ID: " + loan.getId() + "\n";
        receipt += "Borrower: " + loan.getPatron().getName() + "\n";
        receipt += "Item: " + loan.getItem().getTitle() + "\n";
        receipt += "Loan Date: " + loan.getLoanDate().toString() + "\n";
        receipt += "Due Date: " + loan.getDueDate().toString() + "\n";
        return receipt;
    }
    // Sends a reminder email during loans
    public void sendReminderEmails() {
        for (Loan loan : this.loans) {
            if (loan.isOverdue()) {
                String recipient = loan.getPatron().getEmail();
                String subject = "Reminder: Overdue Item";
                String message = "Dear " + loan.getPatron().getName() + ",\n\n";
                message += "This is a reminder that the item '" + loan.getItem().getTitle() + "' you borrowed from our library is overdue.\n";
                message += "The due date was on " + loan.getDueDate().toString() + ".\n";
                message += "Please return the item as soon as possible.\n\n";
                message += "Thank you,\n";
                message += "Library Staff";

                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("ahmadissa66666@gmail.com", "1234567e..");
                    }
                });

                try {
                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.setFrom(new InternetAddress("library@yourlibrary.com"));
                    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                    mimeMessage.setSubject(subject);
                    mimeMessage.setText(message);
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
