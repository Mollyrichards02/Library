

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Book {

    private String title;
    private String author;
    private int yearPublished;

    private int bookID;

    private boolean onLoan;
    private String onLoanTo;

    static ArrayList<Book> bookList = new ArrayList<>();
    public Book(int bookID, String title, String author, int yearPublished, boolean onLoan, String onLoanTo){
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.bookID = bookID;
        this.onLoan = onLoan;
        this.onLoanTo = onLoanTo;
    }


    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author=author;
    }
    public int getYearPublished(){
        return yearPublished;
    }
    public void setYearPublished(int yearPublished){
        this.yearPublished = yearPublished;
    }

    public int getBookID() {
        return bookID;
    }
    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public boolean isOnLoan() {
        return onLoan;
    }

    public void setOnLoan(boolean onLoan) {
        this.onLoan = onLoan;
    }

    public String getOnLoanTo() {
        return onLoanTo;
    }

    public void setOnLoanTo(String onLoanTo) {
        this.onLoanTo = onLoanTo;
    }

    @Override
    public String toString() {
        return String.format("BookID: %d, Title: %s, Author: %s, Year Published: %d, On Loan: %s, On Loan To: %s", getBookID(), getTitle(), getAuthor(), getYearPublished(), isOnLoan(), getOnLoanTo());
    }

    public static int generateRandomBookID() {
        Random newbookID = new Random();
        int newID;

        while(true) {
            newID = newbookID.nextInt(101);
            boolean exists = false;

            for (Book book : Book.bookList) {
                if (book.getBookID() == newID) {
                    exists = true;
                    break;
                }
            }
            // If the ID is unique, break the loop
            if (!exists) {
                break;
            }
        }
        return newID;// Generates a random number between 0 and 100
    }

    public static void createBook() {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter book title:");
        String bookTitle = scanner.nextLine();
        System.out.println("Please enter the author's name:");
        String bookAuthor = scanner.nextLine();
        System.out.println("Please enter the year the book was published:");
        int yearPub = scanner.nextInt();

        int ranval = generateRandomBookID();
        boolean onLoan = false;
        String onLoanTo = "N/A";

        Book newbook = new Book(ranval, bookTitle, bookAuthor, yearPub, onLoan, onLoanTo);
        bookList.add(newbook);
        System.out.println();
        System.out.println("Book added: " + newbook);
        System.out.println();
        System.out.println("--------------");

    }

    public static void viewAllBooks() {
        int bookCount = Book.bookList.size();
        System.out.println();

        if (bookCount == 0) {
            System.out.println("Error: There are no books in the library!");
            System.out.println();
        }else{
            System.out.println("-----------");
            System.out.println();
            System.out.println("Books: ");
            for (Book book : bookList) {
                System.out.println(book);
            }
            System.out.println();

            }
    }
}