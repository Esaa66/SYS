package Objects;

public class Film extends Item {
    private String director;
    private int releaseYear;
    private String genre;
    private String ageRestriction;
    private String actor;
    private String country;

    public Film(String title, String barcode, String ISBN, String classification, String director, String type, int copies, boolean isAvailable, int releaseYear) {
        super(title, barcode, ISBN, classification, director, type, copies, isAvailable);
        this.director = director;
        this.releaseYear = releaseYear;
    }


    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;

    }

    public String getGenre() {
        return genre;
    }

    public void setGener(String gener) {
        this.genre = gener;
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(String ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
