import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HDBManagerHandler {
    private HDBManager manager;
    private Scanner sc = new Scanner(System.in);

    public HDBManagerHandler(HDBManager manager) {
        this.manager = manager;
    }

    public void start() 
    {
        int choice;

        do 
        {
            showMenu();
            choice = getChoice();
            handleChoice(choice);
        } 
        while (choice != 13);
    }

    private void showMenu() 
    {
        System.out.println("\n=== HDB Manager Menu ===");
        System.out.println("1. Create Project");
        System.out.println("2. Edit Project");
        System.out.println("3. Delete Project");
        System.out.println("4. Toggle Project Visibility");
        System.out.println("5. View all aplications to my projects");
        System.out.println("6. Application Handling");
        System.out.println("7. Reply to Enquiry");
        System.out.println("8. Generate Report");
        System.out.println("9.View Projects");
        System.out.println("10. Filter projects");
        System.out.println("11. Officer applications");
        System.out.println("12.Change password");
        System.out.println("13. Logout");
        System.out.print("Enter your choice: ");
    }

    private int getChoice() 
    {
        try 
        {
            return Integer.parseInt(sc.nextLine());
        } 
        catch (Exception e) 
        {
            return -1;
        }
    }

    private void handleChoice(int choice) 
    {
        switch (choice) {
            
                case 1 -> {
                    
                    if (manager.getProjectManaged() != null) 
                    {
                        System.out.println("You are already managing a project and cannot create another.");
                        return;
                    }
                

                    System.out.print("Project name: ");
                    String name = sc.nextLine();
                
                    System.out.print("Location: ");
                    String loc = sc.nextLine();
                
                    System.out.print("Start date (yyyy-MM-dd): ");
                    String start = sc.nextLine();
                
                    System.out.print("End date (yyyy-MM-dd): ");
                    String end = sc.nextLine();
                
                    System.out.print("Enter flat types (comma-separated): ");
                    String[] types = sc.nextLine().split(",");
                
                    Map<String, Integer> flatTypeMap = new HashMap<>();
                    for (String type : types) {
                        type = type.trim(); 
                        System.out.print("Enter number of " + type + " flats: ");
                        int qty = Integer.parseInt(sc.nextLine());
                        flatTypeMap.put(type, qty);
                    }
                
                    manager.createProject(name, loc, start, end, flatTypeMap);
                }
            case 2 -> {
                System.out.print("Project ID to edit: ");
                String id = sc.nextLine();
                System.out.print("New name: ");
                String name = sc.nextLine();
                System.out.print("New location: ");
                String loc = sc.nextLine();
                manager.editProject(id, name, loc);
            }
            case 3 -> 
            {
                System.out.print("Project ID to delete: ");
                String id = sc.nextLine();
            
                if (DataStore.deleteProjectById(id, manager.getNric())) {
                    System.out.println("Project " + id + " deleted successfully. Please logout to create another project");
                } else {
                    System.out.println("Project not found or you're not authorized to delete it.");
                }
            }
            case 4 -> {
                System.out.print("Project ID to toggle: ");
                String id = sc.nextLine();
                System.out.print("Visibility (true/false): ");
                boolean vis = Boolean.parseBoolean(sc.nextLine());
                manager.toggleProjectVisibility(id, vis);
            }

            case 5->
            {
                manager.viewAllApplicationsToMyProject();
            }
            case 6 -> applicationMenu();
            case 7 -> 
            {    
                List<Enquiry> allEnquiries = DataStore.getEnquiriesByProject(manager.getProjectManaged());

                if (allEnquiries.isEmpty()) {
                    System.out.println("No enquiries found for your project.");
                    break;
                }
            
                System.out.println("Enquiries for your project:");
                for (Enquiry e : allEnquiries) {
                    System.out.println("ID: " + e.getEnquiryId() + " | From: " + e.getApplicantName() + 
                        " | Message: " + e.getEnquiryText() + " | Reply: " + (e.getReply().isEmpty() ? "No reply" : e.getReply()));
                }

                System.out.print("Enquiry ID (enter 0 to exit or -1 to delete): ");
                int id = Integer.parseInt(sc.nextLine());
                if (id == 0)
                {
                   System.out.println("Returning to menu.");
                   break;
                }
                else if(id==-1)
                {
                    System.out.println("Enter enquiry id to delete: ");
                    int deleteId = Integer.parseInt(sc.nextLine());
                    DataStore.deleteEnquiry(deleteId);  
                    System.out.println("Enquiry deleted.");
                    break;
                }
                System.out.print("Reply message: ");
                String msg = sc.nextLine();
                manager.replyToEnquiry(id, msg);
            }
            case 8 -> {
                System.out.print("Report type (booking/filtered): ");
                String type = sc.nextLine();
                if (type.equalsIgnoreCase("filtered")) 
                {
                    System.out.print("Filter (married/single/2-Room/...): ");
                    String filter = sc.nextLine();
                    manager.generateFilteredReport(filter);
                } else {
                    manager.generateBookingReport();
                }
            }
            case 9 -> 
            {
                List<BTOProject> projects = DataStore.getAllProjects();
                if (projects.isEmpty()) {
                    System.out.println("No projects found.");
                } else {
                    for (BTOProject p : projects) {
                        System.out.println("===========================================");
                        System.out.println("Project ID   : " + p.getProjectId());
                        System.out.println("Name         : " + p.getName());
                        System.out.println("Location     : " + p.getLocation());
                        System.out.println("Start Date   : " + p.getStartDate());
                        System.out.println("End Date     : " + p.getEndDate());
                        System.out.println("Status       : " + (p.isExpired() ? "Expired" : "Active"));
                        System.out.println("Visible      : " + p.isVisible());
                        System.out.println("Flats        :");
                        for (Flat f : p.getFlats()) {
                            System.out.println("  - " + f.getFlatId() + " (" + f.getFlatType() + ") : " + 
                                               (f.checkAvailability(f.getFlatId()) ? "Available" : "Booked"));
                        }
                        System.out.println("===========================================");
                    }
                }
            }
            case 10 -> 
            {
                System.out.print("Enter location to filter: ");
                String loc = sc.nextLine();
                System.out.print("Enter flat type to filter (e.g., 2Room, 3Room): ");
                String type = sc.nextLine();
                manager.filterProjects(loc, type);
            }
            case 11 -> 
            {
                String managerProjectId = manager.getProjectManaged();
            
                if (managerProjectId == null) {
                    System.out.println("You are not managing any project.");
                    break;
                }
            
                List<OfficerApplication> apps = DataStore.getAllOfficerApplications();
                boolean anyFound = false;
            
                for (OfficerApplication app : apps)
                {
                    if (app.getStatus().equalsIgnoreCase("Pending") &&
                        app.getProjectId().equalsIgnoreCase(managerProjectId)) 
                    {
                        anyFound = true;
                        System.out.println("Officer NRIC: " + app.getOfficerNric() + ", Project ID: " + app.getProjectId());
                        System.out.print("Approve this application? (yes/no): ");
                        String input = sc.nextLine();
            
                        if (input.equalsIgnoreCase("yes")) 
                        {
                            manager.approveOfficerApplication(app.getOfficerNric(), app.getProjectId());
                        } 
                        else 
                        {
                            manager.rejectOfficerApplication(app.getOfficerNric(), app.getProjectId());
                        }
                    }
                }
            
                if (!anyFound) {
                    System.out.println("No pending officer applications for your project.");
                }
            }
            case 12->
            {
                System.out.print("Old password: ");
                String oldPw = sc.nextLine();
                System.out.print("New password: ");
                String newPw = sc.nextLine();
                manager.changePassword(oldPw, newPw);  
            }
            case 13 -> manager.logout();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void applicationMenu() {
        System.out.println("--- Application Handling ---");
        System.out.println("1. Approve Application");
        System.out.println("2. Reject Application");
        System.out.println("3. Approve Withdrawal");
        int ch = getChoice();

        switch (ch) {
            case 1 -> {
                System.out.print("Applicant ID: ");
                String id = sc.nextLine();
                System.out.print("Flat type: ");
                String type = sc.nextLine();
                manager.approveApplication(id, type);
            }
            case 2 -> {
                System.out.print("Applicant ID: ");
                String id = sc.nextLine();
                manager.rejectApplication(id);
            }
            case 3 -> {
                System.out.print("Applicant ID: ");
                String id = sc.nextLine();
                System.out.print("Project ID: ");
                String proj = sc.nextLine();
                manager.approveWithdrawal(id, proj);
            }
            default -> System.out.println("Invalid application option.");
        }
    }
}
