package Objects;

public class Book extends Item {
    private String author;
    private String ISBN;
    private String Subject;

    public Book(String title, String barcode, String ISBN, String classification, String type, int copies, String author, boolean isAvailable) {
        super(title, barcode, ISBN, classification, author, type, copies, isAvailable);

    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN(){ return this.ISBN;}
    public String getSubject(){ return this.Subject;}


}

