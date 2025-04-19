import java.util.Scanner;

public class HDBOfficerHandler 
{
    private HDBOfficer officer;
    private Scanner sc = new Scanner(System.in);

    public HDBOfficerHandler(HDBOfficer officer) {
        this.officer = officer;
    }

    public void start() {
        int choice;
        do {
            showMenu();
            choice = getChoice();
            handleChoice(choice);
        } while (choice != 12);
    }

    private void showMenu() {
        System.out.println("\n=== HDB Officer Menu ===");
        System.out.println("1. View Projects");
        System.out.println("2. Filter Projects");
        System.out.println("3. View Project Enquiries");
        System.out.println("4. Reply to Enquiry");
        System.out.println("5. Generate Receipt");
        System.out.println("6. Apply for a project");
        System.out.println("7. View my Project applications");
        System.out.println("8. Update Flat Availability");
        System.out.println("9.View Successful Applications");
        System.out.println("10. Book Flat for Applicant");
        System.out.println("11.Change password");
        System.out.println("12. Logout");
        System.out.print("Enter your choice: ");
    }

    private int getChoice() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> officer.viewProjects();
            case 2 -> {
                System.out.print("Enter location: ");
                String loc = sc.nextLine();
                System.out.print("Enter flat type: ");
                String type = sc.nextLine();
                officer.filterProjects(loc, type);
            }
            case 3 -> officer.viewEnquiries(officer.getNric());
            case 4 -> 
            {
                System.out.print("Enter enquiry ID to reply: ");
                int id = Integer.parseInt(sc.nextLine());
                officer.replyEnquiry(id);
            }
            case 5 -> officer.generateReceipt();
            case 6 ->
             {
                System.out.print("Enter project ID to apply to: ");
                String projectId = sc.nextLine();
                officer.applyToProject(projectId);
            }
            case 7->
            {
                officer.viewMyProjectApplications();
            }
            case 8 -> {
                System.out.print("Enter flat type: ");
                String flatType = sc.nextLine();
                System.out.print("Enter number of units to add (use negative for removal): ");
                int delta = Integer.parseInt(sc.nextLine());
                officer.updateFlatAvailability(flatType, delta);
            }
            case 9->
            {
                officer.viewSuccessfulApplications();
            }
            case 10->
            {
                System.out.print("Enter applicant NRIC to book flat: ");
                String id = sc.nextLine();
                officer.bookFlatForApplicant(id);
            }
            case 11->
            {
                System.out.print("Old password: ");
                String oldPw = sc.nextLine();
                System.out.print("New password: ");
                String newPw = sc.nextLine();
                officer.changePassword(oldPw, newPw);  
            }
            case 12 -> officer.logout();
            default -> System.out.println("Invalid choice.");
        }
    }
}
