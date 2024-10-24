import java.text.SimpleDateFormat;
import java.util.*;


public class Loan {
    private int loanID;
    private String bookTitle;
    private static String memberName;
    private Date dueDate;
    private int BookID;

    static ArrayList<Loan> loanList = new ArrayList<>();

    // Constructor
    public Loan(int loanID, int bookID, String bookTitle, String memberName) {
        this.loanID = loanID;
        this.BookID = bookID;
        this.bookTitle = bookTitle;
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

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("LoanID: %d, BookID: %d, Book Title: %s, Member: %s, Due Date: %s",
                getLoanID(), getBookID(), getBookTitle(), getMemberName(), sdf.format(getDueDate()));
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

    public static Book selectedBook(Scanner scanner) {

        System.out.println();
        System.out.println("Please select a book by typing in the BookID:");


        int choice = scanner.nextInt();
        String bookName = null;
        scanner.nextLine();

        Book selectedBook = null;
        for (Book book : Book.bookList) {
            if (book.getBookID() == choice) {
                selectedBook = book;
                break;
            }
        }

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

    public static void createLoan() {
        Scanner scanner = new Scanner(System.in);

        int bookCount = Book.bookList.size();
        int memberCount = Member.memberList.size();

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
              for (Book book : Book.bookList) {
                  if (!book.isOnLoan()) {
                      System.out.println(book);
                      count++;
                  }
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

              if (selectedMember != null) {
                  System.out.println("You selected: " + selectedMember);
                  memName = selectedMember.getName();
              } else {
                  System.out.println("Invalid Member ID. Please try again.");
                  System.out.println();
                  return;
              }


              Loan newLoan = new Loan(ranval, bookID, bookName, memName);
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