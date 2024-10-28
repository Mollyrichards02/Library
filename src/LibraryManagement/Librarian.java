package LibraryManagement;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Librarian {
    private String name;
    private int employeeID;
    private String shift;

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

    public static void viewAllLibrarians() {
        int librarianCount = Librarian.librarianList.size();
        System.out.println();
        if (librarianCount == 0) {
            System.out.println("Error: There are no librarians registered!");
            System.out.println();
        } else {
            System.out.println();
            System.out.println("Librarians: ");
            for (Librarian librarian : librarianList) {
                System.out.println(librarian);
            }
            System.out.println();
        }
    }

    public static void createLibrarian() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Please enter new librarian name:");
        String librarianName = scanner.nextLine();

        Random libID = new Random();
        int ranval = libID.nextInt(101);

        System.out.printf("You Employee ID number is: %d%n", ranval);

        Librarian newlibrarian = new Librarian(librarianName, ranval);
        librarianList.add(newlibrarian);
        System.out.println();
        System.out.println("New Librarian added: " + newlibrarian);
        System.out.println();
        System.out.println("--------------");
    }
}

