import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Statement;


public class Loan {
    private int loanID;
    private String bookTitle;
    private int memberID;
    private static String memberName;

    private Date dueDate;
    private int BookID;



    static ArrayList<Loan> loanList = new ArrayList<>();

    // Constructor
    public Loan(int loanID, int bookID, String bookTitle, int memberID, String memberName) {
        this.loanID = loanID;
        this.BookID = bookID;
        this.bookTitle = bookTitle;
        this.memberID = memberID;
        this.memberName = memberName;
        this.dueDate = calculateDueDate();
    }

    public static void viewAllLoans() {
        int loanCount = Loan.loanList.size();
        System.out.println();
        if (loanCount == 0) {
            System.out.println("Error: There are no active loans!");
            System.out.println();
        } else {

            System.out.println();
            System.out.println("Loans: ");
            for (Loan loan : loanList) {
                System.out.println(loan);
            }
            System.out.println();
        }
    }

    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        Loan.memberName = memberName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getBookID() {
        return BookID;
    }

    public void setBookID(int bookID) {
        BookID = bookID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("LoanID: %d, BookID: %d, Book Title: %s, MemberID: %d, Member: %s, Due Date: %s",
                getLoanID(), getBookID(), getBookTitle(), getMemberID(), getMemberName(), sdf.format(getDueDate()));
    }

    private Date calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        return calendar.getTime();
    }

    public static int generateLoanID() {
        Random loanID = new Random();
        int newloanID;

        while (true) {
            newloanID = loanID.nextInt(101);
            boolean exists = false;

            for (Loan loan : Loan.loanList) {
                if (loan.getLoanID() == newloanID) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                break;
            }
        }
        return newloanID;// Generates a random number between 0 and 100
    }

    public static Book getBookById(int bookID) throws Exception {
        String query = "SELECT * FROM Book WHERE bookID = " + bookID;
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return new Book(
                        rs.getInt("bookID"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("yearPublished"),
                        rs.getBoolean("onLoan"),
                        rs.getString("onLoanTo")
                );
            }
        }
        return null; // Return null if the book is not found
    }

    public static Book selectedBook(Scanner scanner) throws Exception {

        System.out.println();
        System.out.println("Please select a book by typing in the BookID:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Book selectedBook = getBookById(choice);
        return selectedBook;

    }

    public static Member selectMember(Scanner scanner){
        System.out.println();
        System.out.println("Please select a member by typing in the memberID:");


        int choiceTwo = scanner.nextInt();
        String memName = null;
        scanner.nextLine();

        Member selectedMember = null;
        for (Member member : Member.memberList) {
            if (member.getMembershipID() == choiceTwo) {
                selectedMember = member;
                memName = selectedMember.getName();
                break;
            }
        }


        return selectedMember;
    }

    public static List<Book> getBooksNotOnLoan() throws Exception {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Book WHERE onLoan = false";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("bookID"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("yearPublished"),
                        rs.getBoolean("onLoan"),
                        rs.getString("onLoanTo")
                );
                books.add(book);
            }
        }
        return books;
    }

    public static void createLoan() throws Exception {
        Scanner scanner = new Scanner(System.in);

        int bookCount = DatabaseConnection.getCount("book");
        int memberCount = DatabaseConnection.getCount("member");



        if (bookCount == 0 || memberCount == 0) {
            if (bookCount == 0) {
                System.out.println();
                System.out.println("Error: There are no books in the library!");

            }
            if (memberCount == 0){
                System.out.println();
                System.out.println("Error: There are no members registered!");

            }
            System.out.println();
            System.out.println("-----------");
        } else {

          try {

              int ranval = generateLoanID();

              System.out.println();
              System.out.println("Available Books: ");
              int count = 0;
              List<Book> books = getBooksNotOnLoan();
              for (Book book : books) {
                  System.out.println(book);
                  count++;
              }
              if (count == 0) {
                  System.out.println("There are currently no available books to loan! Please try again later :)");
                  System.out.println();
                  System.out.println("-----------");
                  return;
              }

              Book selectedBook = selectedBook(scanner);

              String bookName = null;
              int bookID = 0;
              if (selectedBook != null) {
                  System.out.println("You selected: " + selectedBook);
                  bookName = selectedBook.getTitle();
                  bookID = selectedBook.getBookID();
              } else {
                  System.out.println("Invalid Book ID. Please try again.");
                  System.out.println();
                  return;
              }

              System.out.println();
              System.out.println("Members: ");
              for (Member member : Member.memberList) {
                  System.out.println(member);
              }

              Member selectedMember = selectMember(scanner);
              String memName = null;
              int memID = 0;

              if (selectedMember != null) {
                  System.out.println("You selected: " + selectedMember);
                  memName = selectedMember.getName();
                  memID = selectedMember.getMembershipID();
              } else {
                  System.out.println("Invalid Member ID. Please try again.");
                  System.out.println();
                  return;
              }


              Loan newLoan = new Loan(ranval, bookID, bookName, memID, memName);
              loanList.add(newLoan);


              for (Book book: Book.bookList) {
                  if (book.getBookID() == bookID) {
                      book.setOnLoan(true);
                      book.setOnLoanTo(memName);
                  }
              }

              System.out.println();
              System.out.println("New Loan added: " + newLoan);
              System.out.println();
              System.out.println("--------------");
          }catch (InputMismatchException e) {
              System.out.println("Error: Invalid input. Please enter a valid number.");
          } catch (Exception e) {
              System.out.println("An unexpected error occurred: " + e.getMessage());
          }
        }

    }
}