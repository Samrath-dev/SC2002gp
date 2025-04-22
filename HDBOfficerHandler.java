import java.util.Scanner;
/**
 * Handles the interaction between the HDB officer and the system through a menu-driven interface.
 */
public class HDBOfficerHandler 
{
    private HDBOfficer officer;
    private Scanner sc = new Scanner(System.in);
    /**
     * Constructs a handler for the given HDB officer.
     * 
     * @param officer The HDB officer associated with this handler.
     */
    public HDBOfficerHandler(HDBOfficer officer) {
        this.officer = officer;
    }
    /**
     * Starts the menu loop for officer operations until logout is selected.
     */
    public void start() {
        int choice;
        do {
            showMenu();
            choice = getChoice();
            handleChoice(choice);
        } while (choice != 15);
    }
    /**
     * Displays the available menu options to the officer.
     */
    private void showMenu() {
        System.out.println("\n=== HDB Officer Menu ===");
        System.out.println("1. View Projects");
        System.out.println("2. Filter Projects");
        System.out.println("3. View Project Enquiries");
        System.out.println("4. Reply to Enquiry");
        System.out.println("5. Generate Receipt");
        System.out.println("6. Apply for a project as Officer");
        System.out.println("7. View my Project applications");
        System.out.println("8. Update Flat Availability");
        System.out.println("9. View Successful Applications for My Project");
        System.out.println("10. Book Flat for Applicant");
        System.out.println();
        System.out.println(" ===Applicant and User Features=== ");
        System.out.println("11. Change password");
        System.out.println("12. Apply for Flat as Applicant");
        System.out.println("13. View my Flat Application status");
        System.out.println("14 Request Withdrawal from my Flat Application");
        System.out.println("15. Logout");
        System.out.print("Enter your choice: ");
    }
    /**
     * Retrieves the officer's menu choice input.
     * 
     * @return The menu option selected by the officer, or -1 if input is invalid.
     */
    private int getChoice() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
    /**
     * Executes the appropriate action based on the officer's selected menu choice.
     * 
     * @param choice The menu option selected by the officer.
     */
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
            case 12 -> 
            {
                System.out.print("Enter flat type: ");
                String flatType = sc.nextLine();
                System.out.print("Enter project ID: ");
                String projectId = sc.nextLine();
                officer.applyAsApplicant(flatType, projectId);
            }
            case 13->
            {
                officer.viewMyFlatApplications();
            }

            case 14 -> 
            {
                System.out.print("Enter project ID to withdraw from: ");
                String proj = sc.nextLine();
                officer.withdrawApplication(officer.getNric(), proj);
            }
            case 15 -> officer.logout();
            default -> System.out.println("Invalid choice.");
        }
    }
}
