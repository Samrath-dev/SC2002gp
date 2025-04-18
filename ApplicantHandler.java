import java.util.Scanner;

public class ApplicantHandler {
    private Applicant applicant;
    private Scanner sc = new Scanner(System.in);

    public ApplicantHandler(Applicant applicant) {
        this.applicant = applicant;
    }

    public void start() {
        int choice;
        do {
            showMenu();
            choice = getChoice();
            handleChoice(choice);
        } while (choice != 7);
    }

    private void showMenu() {
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. Apply for Project");
        System.out.println("2. View Application Status");
        System.out.println("3. Withdraw Application");
        System.out.println("4. Manage Enquiries");
        System.out.println("5. Filter Projects");
        System.out.println("6. Change Password");
        System.out.println("7. View Projects ");
        System.out.println("8. Logout");
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
            case 1 -> {
                System.out.print("Enter flat type: ");
                String flatType = sc.nextLine();
                System.out.print("Enter project ID: ");
                String project = sc.nextLine();
                applicant.applyForProject(flatType, project);
            }
            case 2 -> applicant.viewApplicationStatus();
            case 3 -> {
                System.out.print("Enter project ID to withdraw from: ");
                String projectId = sc.nextLine();
                applicant.withdrawApplication(applicant.getNric(), projectId);
            }
            case 4 -> enquiryMenu();
            case 5 -> {
                System.out.print("Enter location: ");
                String loc = sc.nextLine();
                System.out.print("Enter flat type: ");
                String type = sc.nextLine();
                applicant.filterProjects(loc, type);
            }
            case 6 -> 
            {
                System.out.print("Old password: ");
                String oldPw = sc.nextLine();
                System.out.print("New password: ");
                String newPw = sc.nextLine();
                applicant.changePassword(oldPw, newPw);
            }
            case 7->applicant.viewProjects();

            case 8 -> applicant.logout();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void enquiryMenu() {
        System.out.println("\n--- Enquiry Management ---");
        System.out.println("1. Create Enquiry");
        System.out.println("2. View My Enquiries");
        System.out.println("3. Edit Enquiry");
        System.out.println("4. Delete Enquiry");
        System.out.print("Choose: ");
        int ch = getChoice();

        switch (ch) {
            case 1 -> {
                System.out.print("Message: ");
                String msg = sc.nextLine();
                applicant.createEnquiry(applicant.getNric(), msg);
            }
            case 2 -> applicant.viewEnquiries(applicant.getNric());
            case 3 -> {
                System.out.print("Enquiry ID: ");
                String id = sc.nextLine();
                System.out.print("New message: ");
                String updated = sc.nextLine();
                applicant.editEnquiry(id, updated);
            }
            case 4 -> {
                System.out.print("Enquiry ID to delete: ");
                String id = sc.nextLine();
                applicant.deleteEnquiry(id);
            }
            default -> System.out.println("Invalid option.");
        }
    }
}
