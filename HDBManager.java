import java.util.*;

public class HDBManager extends User implements PermissionInterface
{

    private String projectManaged;

    public HDBManager(String nric, String password, int age, String maritalStatus, String name) 
    {
        super(nric, password, age, maritalStatus, name);
        this.projectManaged = null;
    }
    // fix for double hashing
    public HDBManager(String nric, String password, int age, String maritalStatus, String name, boolean isHashed) 
    {
        super(nric, password, age, maritalStatus, name, isHashed);
    }
    public HDBManager(String nric, String password, int age, String maritalStatus, String name, boolean isHashed, String projectManaged) 
     {
          super(nric, password, age, maritalStatus, name, isHashed);
          this.projectManaged = projectManaged;
     } 
    public void createProject(String name, String location, String startDate, String endDate, Map<String, Integer> flatTypeMap) 
    {
       
        if (this.projectManaged != null) 
        {
            System.out.println("You are already managing a project and cannot create another.");
            return;
        }
        
        BTOProject project = new BTOProject();
        project.createProject(name, location, startDate, endDate, flatTypeMap);
    
        // Set manager and store project ID
        project.setManagerInCharge(this.nric);
        this.projectManaged = project.getProjectId();
    
        DataStore.registerProject(project);
    
        System.out.println("Project created with ID: " + project.getProjectId());
    }

    public void editProject(String projectId, String newName, String newLocation) {
        BTOProject p = getProjectById(projectId);
        if (p != null) {
            p.editProject(projectId, newName, newLocation);
            System.out.println("Project updated successfully.");
        }
    }

    public void deleteProject(String projectId) {
        BTOProject p = getProjectById(projectId);
        if (p != null) {
            p.deleteProject(projectId);
            System.out.println("Project deleted successfully.");
        }
    }

    public void toggleProjectVisibility(String projectId, boolean isVisible) {
        BTOProject p = getProjectById(projectId);
        if (p != null) {
            p.toggleVisibility(projectId, isVisible);
            System.out.println("Project visibility updated.");
        }
    }

    // now folling the flow 
    public void approveApplication(String applicantId, String flatType) 
    {
        Application app = DataStore.getApplicationByNric(applicantId);
        if (app != null && app.getStatus().equalsIgnoreCase("Pending")) {
            BTOProject p = getProjectById(app.getProjectDetails());
            if (p != null) {
                // Check if at least one flat of that type is available (without booking) to handle missing cases
                boolean available = p.getFlats().stream()
                    .anyMatch(f -> f.getFlatType().equalsIgnoreCase(flatType) && f.checkAvailability(f.getFlatId()));
                if (available) {
                    app.updateStatus("Successful");
                    app.updateFlatType(flatType);
                    System.out.println("Application approved. Status: Successful.");
                } else {
                    System.out.println("No available flats of type " + flatType);
                }
            }
        } else {
            System.out.println("No pending application found for that applicant.");
        }
    }

    public void rejectApplication(String applicantId) 
    {
        Application app = DataStore.getApplicationByNric(applicantId); // now dynamic
        if (app != null) {
            app.updateStatus("Unsuccessful");
            System.out.println("Application rejected.");
        }
    }

    public void approveWithdrawal(String applicantId, String projectId) 
    {
        Application app = DataStore.getApplicationByNric(applicantId); // now dynamic
        if (app != null && app.getProjectDetails().equals(projectId)) {
            app.withdraw();
            System.out.println("Withdrawal approved.");
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    public void replyToEnquiry(int enquiryId, String responseMessage) {
        List<Enquiry> all = DataStore.getEnquiriesByProject(this.projectManaged); // now dynamic list
        for (Enquiry e : all) {
            if (e.getEnquiryId() == enquiryId) {
                e.setReply(responseMessage);
                System.out.println("Enquiry " + enquiryId + " updated.");
                return;
            }
        }
        System.out.println("Enquiry ID not found.");
    }

    public void generateBookingReport() {
        ReportGenerator rep = new ReportGenerator();
        rep.generateReport("booking", "all");
    }

    public void generateFilteredReport(String filterType) {
        ReportGenerator rep = new ReportGenerator();
        rep.generateReport("filtered", filterType);
    }

    private BTOProject getProjectById(String id) {
        List<BTOProject> all = DataStore.getAllProjects(); // implemented this class 
        for (BTOProject p : all) {
            if (p.getProjectId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    @Override
    public void viewProjects() {
        System.out.println("Manager viewing all projects. (placeholder)");
    }


    @Override
    public String hashPassword(String password) {
        return EncryptionUtil.hashPassword(password);
    }

    @Override
    public boolean verifyPassword(String hashedPassword, String password) {
        return EncryptionUtil.verifyPassword(hashedPassword, password);
    }
    // i HAVE NOW IMPLEMENTED PERMISSIONS
    @Override
    public boolean HasPermission(String role) 
    {
        return role.equalsIgnoreCase("HDBManager");
    }
    // Filtering for HDBManager too
    @Override
    public void filterProjects(String location, String flatType) 
    {
        List<BTOProject> all = DataStore.getAllProjects();
        for (BTOProject p : all) 
        {
            if (p.getLocation().equalsIgnoreCase(location)) 
            {
                for (Flat f : p.getFlats()) 
                {
                    if (f.getFlatType().equalsIgnoreCase(flatType) && f.checkAvailability(f.getFlatId())) 
                    {
                        System.out.println("Match: " + p.getName() + " in " + p.getLocation() + " with " + f.getFlatType());
                        break; // now works with flat objects
                    }
                }
            }
        }
    }
    //
    public String getPassword() 
    {
        return this.password;
    }
    public String getProjectManaged()
    {
        return projectManaged;
    }
    // officer approval or reject and view pending
    public void approveOfficerApplication(String officerNric, String projectId) 
    {
    for (OfficerApplication app : DataStore.getAllOfficerApplications()) {
        if (app.getOfficerNric().equals(officerNric) && app.getProjectId().equals(projectId)) {
            if (!app.getStatus().equalsIgnoreCase("Pending")) {
                System.out.println("Application already processed.");
                return;
            }

            app.setStatus("Approved");
            BTOProject project = DataStore.getProjectById(projectId);
            if (project != null) {
                project.addOfficer(officerNric);

                // Also update the officer's record
                User user = DataStore.getUserByNric(officerNric);
                if (user instanceof HDBOfficer officer) {
                    officer.setManagingProject(projectId);
                    System.out.println("Officer approved and assigned to project: " + projectId);
                } else {
                    System.out.println("Error: Officer user not found.");
                }
            } else {
                System.out.println("Project not found.");
            }
            return;
        }
    }
    System.out.println("Application not found.");
    }

    public void rejectOfficerApplication(String officerNric, String projectId) 
    {
        for (OfficerApplication app : DataStore.getAllOfficerApplications()) {
            if (app.getOfficerNric().equals(officerNric) && app.getProjectId().equals(projectId)) {
                if (!app.getStatus().equals("Pending")) {
                    System.out.println("Application already processed.");
                    return;
                }
                app.setStatus("Rejected");
                System.out.println("Officer application rejected.");
                return;
            }
        }
        System.out.println("Application not found.");
    }

    public void viewPendingOfficerApplications() 
    {
        System.out.println("--- Pending Officer Applications ---");
        for (OfficerApplication app : DataStore.getAllOfficerApplications()) {
            if (app.getStatus().equalsIgnoreCase("Pending")) {
                System.out.println("Officer NRIC: " + app.getOfficerNric() + " | Project ID: " + app.getProjectId());
            }
        }
    }


}