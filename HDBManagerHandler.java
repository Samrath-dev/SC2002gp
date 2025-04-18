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
        while (choice != 10);
    }

    private void showMenu() {
        System.out.println("\n=== HDB Manager Menu ===");
        System.out.println("1. Create Project");
        System.out.println("2. Edit Project");
        System.out.println("3. Delete Project");
        System.out.println("4. Toggle Project Visibility");
        System.out.println("5. Application Handling");
        System.out.println("6. Reply to Enquiry");
        System.out.println("7. Generate Report");
        System.out.println("8.View Projects");
        System.out.println("9.Change password");
        System.out.println("10. Logout");
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
            case 3 -> {
                System.out.print("Project ID to delete: ");
                String id = sc.nextLine();
                manager.deleteProject(id);
            }
            case 4 -> {
                System.out.print("Project ID to toggle: ");
                String id = sc.nextLine();
                System.out.print("Visibility (true/false): ");
                boolean vis = Boolean.parseBoolean(sc.nextLine());
                manager.toggleProjectVisibility(id, vis);
            }
            case 5 -> applicationMenu();
            case 6 -> {
                System.out.print("Enquiry ID: ");
                int id = Integer.parseInt(sc.nextLine());
                System.out.print("Reply message: ");
                String msg = sc.nextLine();
                manager.replyToEnquiry(id, msg);
            }
            case 7 -> {
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
            case 8 -> 
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
            case 9->
            {
                System.out.print("Old password: ");
                String oldPw = sc.nextLine();
                System.out.print("New password: ");
                String newPw = sc.nextLine();
                manager.changePassword(oldPw, newPw);  
            }
            case 10 -> manager.logout();
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
