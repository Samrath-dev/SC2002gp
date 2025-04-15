import java.util.*;

public class HDBOfficer extends User implements ApplicationInterface, EnquiryInterface, PermissionInterface, FlatInterface, ReceiptInterface {

    private String managing_project = "Project_id";
    private String[] applied_projects = {"Project_1", "Project_2"};

    // User pre hashes so no need to rehash.In the user constructor...
    public HDBOfficer(String nric, String password, int age, String maritalStatus, String name) 
    {
        super(nric, password, age, maritalStatus, name);
    }

    public void registerToManage(String Project_id) 
    {
        if (managing_project.equals(Project_id)) {
            System.out.println("Registration failed: Already an officer for another project in the same period.");
        } 
        else if (Arrays.asList(applied_projects).contains(Project_id)) 
        {
            System.out.println("Registration failed: Applicant cannot register as an HDB Officer for a project they applied for.");
        } 
        else 
        {
            System.out.println("Registration successful: Waiting for HDB Manager approval.");
            // submitApplication("Project_id", "Managing"); // Assuming this is for dummy flow
        }
    }

    public void approveApplications(int ApplicationID) 
    {
        // updateStatus(ApplicationID, true); // Assuming updateStatus is defined somewhere else
        System.out.println("Application with ID " + ApplicationID + " approved. (Dummy flow)");
    }

    public void viewProjectEnquiries() {
        List<Enquiry> projectEnquiries = Enquiry.getEnquiriesByProject(this.managing_project);
        if (projectEnquiries.isEmpty()) {
            System.out.println("No enquiries found for project: " + this.managing_project);
            return;
        }
        for (Enquiry e : projectEnquiries) 
        {
            System.out.println("ID: " + e.getEnquiryId() + ", Applicant: " + e.getApplicantName() +
                               ", Enquiry: " + e.getEnquiryText() + ", Reply: " + e.getReply());
        }
    }

    public void replyEnquiry(int enquiryId) {
        List<Enquiry> projectEnquiries = DataStore.getEnquiriesByProject(this.managing_project); 

        if (projectEnquiries.isEmpty()) {
            System.out.println("No enquiries found for project: " + this.managing_project);
            return;
        }

        for (Enquiry e : projectEnquiries) {
            if (e.getEnquiryId() == enquiryId) {
                System.out.print("Enter your reply: ");
                Scanner scanner = new Scanner(System.in);
                String reply = scanner.nextLine();
                e.setReply(reply);
                System.out.println("Reply submitted successfully for Enquiry ID: " + enquiryId);
                return;
            }
        }
        System.out.println("Enquiry ID not found for project: " + this.managing_project);
    }

    public void generateReceipt() {
        List<Application> applicants = DataStore.getAllApplications();
        List<Application> filtered = new ArrayList<>();

        for (Application app : applicants) {
            if (app.getProjectDetails().equalsIgnoreCase(this.managing_project) && app.getStatus().equalsIgnoreCase("Booked")) {
                filtered.add(app);
            }
        }

        ReceiptGenerator.generateReceipts(filtered);
    }
    
    @Override
    public void viewProjects() {
        // Minimal dummy implementation
        System.out.println("HDB Officer viewing projects.");
    }


    /*
     * interface fixes
     */
    @Override
    public void createEnquiry(String applicantId, String message) 
    {
        // 
        System.out.println("Officers do not create enquiries.");
    }

    @Override
    public void viewEnquiries(String applicantId) 
    {
        // 
        viewProjectEnquiries();
    }

    @Override
    public void updateFlatAvailability(String flatType, int change) 
    {
        // 
        System.out.println("Updated availability for " + flatType + " by " + change + " units.");
    }

    @Override
    public void generateReceipt(String applicantId, String projectId, String flatType)
    {
        // 
        System.out.println("Receipt generated for " + applicantId + " on project " + projectId + " for flat " + flatType);
    }

    @Override
    public boolean verifyPassword(String hashedPassword, String password) 
    {
        return EncryptionUtil.verifyPassword(hashedPassword, password);
    }

    @Override
    public boolean HasPermission(String role) 
    {
        return role.equalsIgnoreCase("HDBOfficer");
    }

    @Override
    public String hashPassword(String password) 
    {
        return EncryptionUtil.hashPassword(password);
    }

    @Override
    public String getApplicationStatus(String applicantId) 
    {
        Application app = Application.getByNric(applicantId);
        return app != null ? app.getStatus() : "Not found";
    }

    @Override
    public boolean submitApplication(String applicantId, String projectId) 
    {
        System.out.println("Officer submitting application for " + applicantId + " to project " + projectId);
        return true;
    }

    @Override
    public boolean bookFlat(String applicantId, String flatType) 
    {
        System.out.println("Booked flat of type " + flatType + " for applicant " + applicantId);
        return true;
    }
    @Override
    public void deleteEnquiry(String enquiryId) {
        try {
            int id = Integer.parseInt(enquiryId);
            List<Enquiry> projectEnquiries = Enquiry.getEnquiriesByProject(this.managing_project);
            for (Enquiry e : projectEnquiries) {
                if (e.getEnquiryId() == id) {
                    e.deleteEnquiry();
                    System.out.println("Enquiry deleted successfully.");
                    return;
                }
            }
            System.out.println("Enquiry ID not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID.");
        }
    }
    
    @Override
    public void editEnquiry(String enquiryId, String updatedMessage) {
        try {
            int id = Integer.parseInt(enquiryId);
            List<Enquiry> projectEnquiries = Enquiry.getEnquiriesByProject(this.managing_project);
            for (Enquiry e : projectEnquiries) {
                if (e.getEnquiryId() == id) {
                    e.setEnquiryText(updatedMessage);
                    System.out.println("Enquiry updated successfully.");
                    return;
                }
            }
            System.out.println("Enquiry ID not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID.");
        }
    }
    
    @Override
    public boolean checkAvailability(String flatId) 
    {
        System.out.println("Checked availability of flat: " + flatId);
        return true; 
    }
    
    @Override
    public boolean withdrawApplication(String applicantId, String projectId) {
        Application app = Application.getByNric(applicantId);
        if (app != null && app.getProjectDetails().equalsIgnoreCase(projectId)) {
            app.withdraw();
            return true;
        }
        System.out.println("Application not found or doesn't match project.");
        return false;
    }
    // Now adding filtering
    @Override
        public void filterProjects(String location, String flatType) 
        {
            List<BTOProject> all = DataStore.getAllProjects();
            for (BTOProject p : all) 
            {
                if (p.getLocation().equalsIgnoreCase(location)) 
                {
                      for (BTOProject.FlatType ft : p.getFlatTypes()) 
                    {
                            if (ft.getType().equalsIgnoreCase(flatType)) 
                            {
                                System.out.println("Match: " + p.getName() + " in " + p.getLocation() + " with " + ft.getType());
                            }
                    }
                 }
            }
        }
}
	