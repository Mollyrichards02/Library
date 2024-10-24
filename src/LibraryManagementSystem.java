import java.util.Scanner;

public class LibraryManagementSystem {

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        String name;

        // Loop for valid name input
        while (true) {
            System.out.println("Hello! Please enter your name:");
            name = scanner.nextLine();

            try {
                // Check if the name contains only letters
                if (name.matches("[a-zA-Z]+")) {
                    break; // Exit loop if valid
                } else {
                    throw new IllegalArgumentException("Invalid input. Please enter a name containing only letters.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Welcome to the library management system, " + name + "!");

        int choice;

        do {
            System.out.println("1. Add a new book");
            System.out.println("2. Add a new member");
            System.out.println("3. Add a new loan");
            System.out.println("4. Add a new librarian");
            System.out.println("5. View all books");
            System.out.println("6. View all members");
            System.out.println("7. View all loans");
            System.out.println("8. View all librarians");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    Book.createBook();
                    break;
                case 2:
                    Member.createMember();
                    break;
                case 3:
                    Loan.createLoan();
                    break;
                case 4:
                    Librarian.createLibrarian();
                case 5:
                    Book.viewAllBooks();
                    break;
                case 6:
                    Member.viewAllMembers();
                    break;
                case 7:
                    Loan.viewAllLoans();
                    break;
                case 8:
                    Librarian.viewAllLibrarians();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);

        scanner.close();
    }


}




