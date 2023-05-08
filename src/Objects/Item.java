package Objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Item {
    private String id;
    private String title;
    private String barcode;
    private String ISBN;
    private String classification;
    private String type;
    private int copies;
    private boolean isAvailable;
    private String conn;


    public Item(String id, String title, String barcode, String ISBN, String classification, String type, int copies, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.barcode = barcode;
        this.ISBN = ISBN;
        this.classification = classification;
        this.type = type;
        this.copies = copies;
        this.isAvailable = isAvailable;
    }

    public Item(int id, String type, String title, int copies) {
    }

    public Item getById(Connection conn, int id) throws SQLException {
        Item obj = null;
        String query = "SELECT * FROM objects WHERE barcode = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String type = rs.getString("type");
            String title = rs.getString("title");
            int copies = rs.getInt("copies");
            obj = new Item(id, type, title, copies) {
            };
        }
        return obj;
    }






    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getISBN() {
        return this.ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getClassification() {
        return this.classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
