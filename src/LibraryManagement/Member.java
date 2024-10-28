package LibraryManagement;


import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Member {
    private String name;
    private int membershipID;
    private String membershipType;


    // Constructor
    public Member(String name, int membershipID, String membershipType) {
        this.name = name;
        this.membershipID = membershipID;
        this.membershipType = membershipType;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMembershipID() {
        return membershipID;
    }

    public void setMembershipID(int membershipID) {
        this.membershipID = membershipID;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    @Override
    public String toString() {
        return String.format("Member: %s, MembershipID: %d, Membership Type: %s", getName(), getMembershipID(), getMembershipType());
    }
    public static int generateMemberID() {
        Random memID = new Random();
        int membID;
        while (true) {
            membID = memID.nextInt(101);
            boolean exists = false;
            if (!exists) {
                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String query = "SELECT 1 FROM Member WHERE membershipID = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, membID);
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

            if (!exists) {
                break;
            }
        }
        return membID;
    }

    public static String selectMembershipType(Scanner scanner) {
        int choice = 0;
        String memType = null;
        do {
            System.out.println();
            System.out.println("Please select your membership type:");
            System.out.println("1. Child");
            System.out.println("2. Student");
            System.out.println("3. Adult");
            System.out.println("4. Senior");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    memType = "Child";
                    break;
                case 2:
                    memType = "Student";
                    break;
                case 3:
                    memType = "Adult";
                    break;
                case 4:
                    memType = "Senior";
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice < 1 || choice > 4);

        return memType;
    }

    private static void insertMemberIntoDatabase(Member member) {
        try {
            // Get a connection from the LibraryManagement.DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();

            // Create the SQL insert statement
            String insertSQL = "INSERT INTO Member (membershipID, name, membershipType) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, member.getMembershipID());
            preparedStatement.setString(2, member.getName());
            preparedStatement.setString(3, member.getMembershipType());

            // Execute the insert
            preparedStatement.executeUpdate();

            // Close the connections
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createMember() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Please enter new member name:");
        String memName = scanner.nextLine();

        int ranval = generateMemberID();
        System.out.println();
        System.out.println(String.format("You Membership ID number is: %d", ranval));

        String memType = selectMembershipType(scanner);

        System.out.println("You selected: " + memType);

        Member newMember = new Member(memName, ranval, memType);

        insertMemberIntoDatabase(newMember);


        System.out.println();
        System.out.println("New Member added: " + newMember);
        System.out.println();
        System.out.println("--------------");

    }

    public static void fetchAndPrintAllMembers() {
        try {
            // Get a connection from the LibraryManagement.DatabaseConnection class
            Connection connection = DatabaseConnection.getConnection();

            // Create the SQL select statement
            String query = "SELECT * FROM Member";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set is empty
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Error: There are no members in the database!");
                System.out.println();
            } else {
                // Process the results
                while (resultSet.next()) {
                    int membershipID = resultSet.getInt("membershipID");
                    String name = resultSet.getString("name");
                    String membershipType = resultSet.getString("membershipType");

                    System.out.println("MembershipID: " + membershipID);
                    System.out.println("Name: " + name);
                    System.out.println("Membership Type: " + membershipType);
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
    }

