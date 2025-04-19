import java.util.*;

public class MainDriver {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Load everything at startup
            FileHandler.loadApplicants("applicants.csv");
            FileHandler.loadOfficers("officers.csv");
            FileHandler.loadManagers("managers.csv");
            FileHandler.loadProjects("projects.csv");
            FileHandler.loadApplications("applications.csv");
            FileHandler.loadEnquiries("enquiries.csv");
            FileHandler.loadFlats("flats.csv");
            FileHandler.loadOfficerApplications("officer_applications.csv");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        while (true) {
            try {
                System.out.println("\nWelcome to BTO Management System");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        RegisterDriver.registerUser();
                        break;
                    case 2:
                        User user = LoginDriver.login();
                        if (user instanceof Applicant a) {
                            new ApplicantHandler(a).start();
                        } else if (user instanceof HDBOfficer o) {
                            new HDBOfficerHandler(o).start();
                        } else if (user instanceof HDBManager m) {
                            new HDBManagerHandler(m).start();
                        }

                        // saving after every user session
                        try {
                            FileHandler.saveApplicants("applicants.csv");
                            FileHandler.saveOfficers("officers.csv");
                            FileHandler.saveManagers("managers.csv");
                            FileHandler.saveProjects("projects.csv");
                            FileHandler.saveApplications("applications.csv");
                            FileHandler.saveEnquiries("enquiries.csv");
                        } catch (Exception e) {
                            System.out.println("Error saving after logout: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("Saving all data and exiting...");
                        try {
                            FileHandler.saveApplicants("applicants.csv");
                            FileHandler.saveOfficers("officers.csv");
                            FileHandler.saveManagers("managers.csv");
                            FileHandler.saveProjects("projects.csv");
                            FileHandler.saveApplications("applications.csv");
                            FileHandler.saveEnquiries("enquiries.csv");
                            FileHandler.saveFlats("flats.csv");
                            FileHandler.saveOfficerApplications("officer_applications.csv");
                        } catch (Exception e) {
                            System.out.println("Error saving before exit: " + e.getMessage());
                        }
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                sc.nextLine(); // Clear buffer just in case
            }
        }
    }
}