package LibraryManagement;

import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;




public class Loan {
    private int loanID;
    private String bookTitle;
    private int memberID;
    private static String memberName;

    private Date dueDate;
    private int BookID;


    // Constructor
    public Loan(int loanID, int bookID, String bookTitle, int memberID, String memberName) {
        this.loanID = loanID;
        this.BookID = bookID;
        this.bookTitle = bookTitle;
        this.memberID = memberID;
        this.memberName = memberName;
        this.dueDate = calculateDueDate();
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

    public static void fetchAndPrintAllLoans() {
        try {
            // Get a connection from the LibraryManagement.DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();
            // Create the SQL select statement
            String query = "SELECT * FROM Loan";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if the result set is empty
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Error: There are no loans in the database!");
                System.out.println();
            } else {
                // Process the results
                while (resultSet.next()) {
                    int loanID = resultSet.getInt("loanID");
                    String bookTitle = resultSet.getString("bookTitle");
                    int membershipID = resultSet.getInt("membershipID");
                    String memberName = resultSet.getString("memberName");
                    Date dueDate = resultSet.getDate("dueDate");
                    int bookID = resultSet.getInt("BookID");
                    System.out.println("LoanID: " + loanID);
                    System.out.println("Book Title: " + bookTitle);
                    System.out.println("MembershipID: " + membershipID);
                    System.out.println("Member Name: " + memberName);
                    System.out.println("Due Date: " + dueDate);
                    System.out.println("BookID: " + bookID);
                    System.out.println("---------------------------");
                }
            }
            // Close the connections
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public static int generateLoanID() {
        Random loanID = new Random();
        int newloanID;
        while (true) {
            newloanID = loanID.nextInt(101);
            boolean exists = false;
            if (!exists) {
                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String query = "SELECT 1 FROM Loan WHERE loanID = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, newloanID);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        exists = true;
                    }
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
            // If the ID is unique, break the loop
            if (!exists) {
                break;
            }
        }
        return newloanID; // Generates a random number between 0 and 100
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

    public static Member getMemberById(int memberID) throws Exception {
        String query = "SELECT * FROM Member WHERE membershipID = " + memberID;
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return new Member(
                        rs.getString("name"),
                        rs.getInt("membershipID"),
                        rs.getString("membershipType") // Make sure this matches your table structure
                );
            }
        }
        return null; // Return null if the member is not found
    }

    public static Member selectMember(Scanner scanner) throws Exception {
        System.out.println();
        System.out.println("Please select a member by typing in the memberID:");

        int choiceTwo = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Member selectedMember = getMemberById(choiceTwo);

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

    public static List<Member> getAllMembers() throws Exception {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM Member";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Member member = new Member(
                        rs.getString("name"),
                        rs.getInt("membershipID"),
                        rs.getString("membershipType") // Correct field name
                );

                members.add(member);
            }
        }
        return members;
    }

    public static void updateBook(int bookID, String memName) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "UPDATE Book SET onLoan = ?, onLoanTo = ? WHERE bookID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, memName);
            preparedStatement.setInt(3, bookID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book with ID " + bookID + " has been updated to on loan.");
            } else {
                System.out.println("No book found with ID " + bookID + ".");
            }
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertLoanIntoDatabase(Loan loan) {
        try {
            // Get a connection from the LibraryManagement.DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();
            // Create the SQL insert statement
            String insertSQL = "INSERT INTO Loan (loanID, bookTitle, membershipID, memberName, dueDate, BookID) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, loan.getLoanID());
            preparedStatement.setString(2, loan.getBookTitle());
            preparedStatement.setInt(3, loan.getMemberID());
            preparedStatement.setString(4, loan.getMemberName());

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(loan.getDueDate().getTime());
            preparedStatement.setDate(5, sqlDate);

            preparedStatement.setInt(6, loan.getBookID());
            // Execute the insert
            preparedStatement.executeUpdate();
            // Close the connections
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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
              List<Member> members = getAllMembers(); // Fetch members from the database
              for (Member member : members) {
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

              insertLoanIntoDatabase(newLoan);

              updateBook(bookID, memName);

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