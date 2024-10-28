package LibraryManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;


public class Librarian {
    private String name;
    private int employeeID;


    static ArrayList<Librarian> librarianList = new ArrayList<>();

    // Constructor
    public Librarian(String name, int employeeID) {
        this.name = name;
        this.employeeID = employeeID;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    @Override
    public String toString() {
        return String.format("Librarian: %s, LibrarianID: %d", getName(), getEmployeeID());
    }

    public static void fetchAndPrintAllLibrarians() {
        try {
            // Get a connection from the LibraryManagement.DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();
            // Create the SQL select statement
            String query = "SELECT * FROM Librarian";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if the result set is empty
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Error: There are no librarians in the database!");
                System.out.println();
            } else {
                // Process the results
                while (resultSet.next()) {
                    int employeeID = resultSet.getInt("employeeID");
                    String name = resultSet.getString("name");
                    System.out.println("EmployeeID: " + employeeID);
                    System.out.println("Name: " + name);
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
    private static void insertLibrarianIntoDatabase(Librarian librarian) {
        try {
            // Get a connection from the LibraryManagement.DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();
            // Create the SQL insert statement
            String insertSQL = "INSERT INTO Librarian (employeeID, name) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, librarian.getEmployeeID());
            preparedStatement.setString(2, librarian.getName());
            // Execute the insert
            preparedStatement.executeUpdate();
            // Close the connections
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static int generateEmployeeID() {
        Random empID = new Random();
        int newEmpID;
        while (true) {
            newEmpID = empID.nextInt(101);
            boolean exists = false;
            if (!exists) {
                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String query = "SELECT 1 FROM Librarian WHERE employeeID = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, newEmpID);
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
        return newEmpID; // Generates a random number between 0 and 100
    }


    public static void createLibrarian() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Please enter new librarian name:");
        String librarianName = scanner.nextLine();


        int ranval = generateEmployeeID();

        System.out.printf("You Employee ID number is: %d%n", ranval);

        Librarian newlibrarian = new Librarian(librarianName, ranval);


        insertLibrarianIntoDatabase(newlibrarian);

        System.out.println();
        System.out.println("New Librarian added: " + newlibrarian);
        System.out.println();
        System.out.println("--------------");
    }
}

