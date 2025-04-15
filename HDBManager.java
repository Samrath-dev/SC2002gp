import java.util.*;

public class HDBManager extends User implements PermissionInterface
{

    private String projectManaged;

    public HDBManager(String nric, String password, int age, String maritalStatus, String name) {
        super(nric, password, age, maritalStatus, name);
        this.projectManaged = null;
    }

    public void createProject(String name, String location, String[] flatTypes, int availableFlats) {
        BTOProject p = new BTOProject();
        p.createProject(name, location, flatTypes, availableFlats);
        p.setManagerInCharge(this.nric);
        this.projectManaged = p.getProjectId();
        DataStore.registerProject(p); // now dynamic
        System.out.println("Project created successfully. ID: " + this.projectManaged);
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

    public void approveApplication(String applicantId, String flatType) {
        Application app = DataStore.getApplicationByNric(applicantId); // now dynamic
        if (app != null && app.getStatus().equalsIgnoreCase("Pending")) {
            BTOProject p = getProjectById(app.getProjectDetails());
            if (p != null && p.bookFlat(flatType)) {
                app.updateStatus("Booked");
                app.updateFlatType(flatType);
                System.out.println("Application approved and flat booked.");
            } else {
                System.out.println("No available units to approve.");
            }
        }
    }

    public void rejectApplication(String applicantId) {
        Application app = DataStore.getApplicationByNric(applicantId); // now dynamic
        if (app != null) {
            app.updateStatus("Unsuccessful");
            System.out.println("Application rejected.");
        }
    }

    public void approveWithdrawal(String applicantId, String projectId) {
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