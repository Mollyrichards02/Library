import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.sql.ResultSet;

public class Book {

    private String title;
    private String author;
    private int yearPublished;

    private int bookID;

    private boolean onLoan;
    private String onLoanTo;

    static ArrayList<Book> bookList = new ArrayList<>();

    public Book(int bookID, String title, String author, int yearPublished, boolean onLoan, String onLoanTo) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.bookID = bookID;
        this.onLoan = onLoan;
        this.onLoanTo = onLoanTo;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
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

        while (true) {
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

        String onLoanTo = null;

        Book newbook = new Book(ranval, bookTitle, bookAuthor, yearPub, onLoan, onLoanTo);


        insertBookIntoDatabase(newbook);

        fetchAndPrintAllBooks();

        System.out.println();
        System.out.println("Book added: " + newbook);
        System.out.println();
        System.out.println("--------------");

    }

    private static void insertBookIntoDatabase(Book book) {
        try {
            // Get a connection from the DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();

            // Create the SQL insert statement
            String insertSQL = "INSERT INTO Book (bookID, title, author, yearPublished, onLoan, onLoanTo) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, book.getBookID());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setInt(4, book.getYearPublished());
            preparedStatement.setBoolean(5, book.isOnLoan());
            preparedStatement.setString(6, book.getOnLoanTo());

            // Execute the insert
            preparedStatement.executeUpdate();

            // Close the connections
            preparedStatement.close();
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fetchAndPrintAllBooks() {
        try {
            // Get a connection from the DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();

            // Create the SQL select statement
            String query = "SELECT * FROM Book";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the results
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Error: There are no books in the library!");
                System.out.println();
            } else {
                // Process the results
                while (resultSet.next()) {
                    int bookID = resultSet.getInt("bookID");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    int yearPublished = resultSet.getInt("yearPublished");
                    boolean onLoan = resultSet.getBoolean("onLoan");
                    String onLoanTo = resultSet.getString("onLoanTo");

                    System.out.println("BookID: " + bookID);
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Year Published: " + yearPublished);
                    System.out.println("On Loan: " + onLoan);
                    System.out.println("On Loan To: " + onLoanTo);
                    System.out.println("---------------------------");
                }
            }

                    // Close the connections
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
        }

    }
}