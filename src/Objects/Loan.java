package Objects;

import java.time.LocalDate;

public class Loan {
    private String loanID;
    private Item item;
    private User user;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private boolean isOverdue;

    public Loan(String loanID, Item item, User user, LocalDate loanDate, LocalDate dueDate) {
        this.loanID = loanID;
        this.item = item;
        this.user = user;
        this.loanDate = LocalDate.of(2023,02,10);
        this.dueDate = loanDate.plusDays(30);
        this.isOverdue = true;
    }

    public Loan(Item item, User user) {

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getId() {
        return loanID;
    }

    public Item getItem() {
        return item;
    }

    public User getPatron() {
        return user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isOverdue() {
        LocalDate today = LocalDate.now();
        return today.isAfter(dueDate);
    }
}
