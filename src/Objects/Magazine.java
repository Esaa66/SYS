package Objects;

import java.util.Date;

public class Magazine extends Item{

    private String ISSN;
    private Date date;

    public Magazine(String title, String barcode, String ISBN, String classification, String type, int copies, String director, boolean isAvailable, Date date) {
        super(title, barcode, ISBN, classification, director, type, copies, isAvailable);

    }



    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

