import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Member {
    private String name;
    private int membershipID;
    private String membershipType;

    static ArrayList<Member> memberList = new ArrayList<>();

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

            for (Member member : Member.memberList) {
                if (member.getMembershipID() == membID) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                break;
            }
        }
        return membID;// Generates a random number between 0 and 100
    }

    public static String selectMembershipType(Scanner scanner) {
        int choice = 0;
        String memType = null;
        do {
            System.out.println();            System.out.println("Please select your membership type:");
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
        memberList.add(newMember);
        System.out.println();
        System.out.println("New Member added: " + newMember);
        System.out.println();
        System.out.println("--------------");

    }
        public static void viewAllMembers() {
            int memberCount = Member.memberList.size();
            System.out.println();
            if (memberCount == 0) {
                System.out.println("Error: There are no members registered!");
                System.out.println();
            }else{
                    System.out.println();
                    System.out.println("Members: ");
                    for (Member member : memberList) {
                        System.out.println(member);
                    }
                    System.out.println();
                }


        }
    }

