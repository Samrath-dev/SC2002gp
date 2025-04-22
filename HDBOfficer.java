import java.util.*;
/**
 * Represents an HDB Officer who manages a BTO project and handles related functionalities
 * such as application approvals, enquiries, and flat bookings.
 */
public class HDBOfficer extends User implements ApplicationInterface, EnquiryInterface, PermissionInterface, FlatInterface, ReceiptInterface {

    private String managing_project = null;

    /**
     * Constructs an HDB Officer with required details.
     * 
     * @param nric The officer's NRIC.
     * @param password The officer's password.
     * @param age The officer's age.
     * @param maritalStatus The officer's marital status.
     * @param name The officer's name.
     */
    // User pre hashes so no need to rehash.In the user constructor...
    public HDBOfficer(String nric, String password, int age, String maritalStatus, String name) 
    {
        super(nric, password, age, maritalStatus, name);
    }

    /**
     * Constructs an HDB Officer with optional password hashing.
     * 
     * @param nric The officer's NRIC.
     * @param password The officer's password.
     * @param age The officer's age.
     * @param maritalStatus The officer's marital status.
     * @param name The officer's name.
     * @param isHashed Whether the password is already hashed.
     */
    // fix for double hashing
    public HDBOfficer(String nric, String password, int age, String maritalStatus, String name, boolean isHashed) {
        super(nric, password, age, maritalStatus, name, isHashed);
    }
     
    
     /**
     * Approves an application.
     * 
     * @param ApplicationID The ID of the application to approve.
     */
    public void approveApplications(int ApplicationID) 
    {
        // updateStatus(ApplicationID, true); // Assuming updateStatus is defined somewhere else
        System.out.println("Application with ID " + ApplicationID + " approved. (Dummy flow)");
    }
    /**
     * Displays all enquiries for the officer's managed project.
     */
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
    /**
     * Replies to a specific enquiry.
     * 
     * @param enquiryId The ID of the enquiry to reply to.
     */
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
    /**
     * Generates receipts for all booked applicants.
     */
    //proper generating reciepts..
    public void generateReceipt() 
    {
        List<Application> all = DataStore.getAllApplications();
    
        for (Application app : all) {
            if (app.getStatus().equalsIgnoreCase("Booked") &&
                app.getProjectDetails().equalsIgnoreCase(this.managing_project)) {
    
                System.out.println("--- RECEIPT ---");
                System.out.println("Applicant: " + app.getName());
                System.out.println("NRIC: " + app.getNric());
                System.out.println("Project: " + app.getProjectDetails());
                System.out.println("Flat Type: " + app.getFlatType());
                System.out.println("Status: " + app.getStatus());
                System.out.println();
            }
        }
    }
    
    /**
     * Views the officer's managed project and all other projects.
     */
    @Override
    public void viewProjects() 
    {
        System.out.println("=== Your Assigned Project ===");
        if (managing_project != null && !managing_project.equalsIgnoreCase("Project_id")) 
        {
            BTOProject assigned = DataStore.getProjectById(managing_project);
            if (assigned != null) 
            {
                System.out.println("Project ID: " + assigned.getProjectId());
                System.out.println("Name: " + assigned.getName());
                System.out.println("Location: " + assigned.getLocation());
                System.out.println("Visible: " + assigned.isVisible());
                System.out.println("Flats:");
                for (Flat f : assigned.getFlats())
                {
                    System.out.println("  - " + f.getFlatId() + " (" + f.getFlatType() + ") : " +
                            (f.checkAvailability(f.getFlatId()) ? "Available" : "Booked"));
                }
                System.out.println();
            } 
            else 
            {
                System.out.println("You are not managing a valid project currently.");
            }
        } 
        else 
        {
            System.out.println("You are not currently managing any project.");
        }
    
        System.out.println("=== All Available Projects ===");
        List<BTOProject> all = DataStore.getAllProjects();
        for (BTOProject p : all) {
            System.out.println("Project ID: " + p.getProjectId());
            System.out.println("Name: " + p.getName());
            System.out.println("Location: " + p.getLocation());
            System.out.println("Visible: " + p.isVisible());
            System.out.println("Flats:");
            for (Flat f : p.getFlats()) {
                System.out.println("  - " + f.getFlatId() + " (" + f.getFlatType() + ") : " +
                        (f.checkAvailability(f.getFlatId()) ? "Available" : "Booked"));
            }
            System.out.println();
        }
    }
     /**
     * Dummy method since officers do not create enquiries.
     */
    /*
     * interface fixes
     */
    @Override
    public void createEnquiry(String applicantId, String message) 
    {
        // 
        System.out.println("Officers do not create enquiries.");
    }
    /**
     * Views enquiries (delegates to viewProjectEnquiries).
     * 
     * @param applicantId The applicant's ID.
     */
    @Override
    public void viewEnquiries(String applicantId) 
    {
        // 
        viewProjectEnquiries();
    }
    /**
     * Updates flat availability (stub).
     * 
     * @param flatType The flat type.
     * @param change The number of units to change by.
     */

    @Override
    public void updateFlatAvailability(String flatType, int change) 
    {
        // 
        System.out.println("Updated availability for " + flatType + " by " + change + " units.");
    }
    /**
     * Generates a receipt (stub).
     * 
     * @param applicantId The applicant's ID.
     * @param projectId The project ID.
     * @param flatType The flat type.
     */
    @Override
    public void generateReceipt(String applicantId, String projectId, String flatType)
    {
        // 
        System.out.println("Receipt generated for " + applicantId + " on project " + projectId + " for flat " + flatType);
    }
    /**
     * Verifies a password.
     * 
     * @param hashedPassword The hashed password.
     * @param password The plain password.
     * @return true if password matches hash, false otherwise.
     */
    @Override
    public boolean verifyPassword(String hashedPassword, String password) 
    {
        return EncryptionUtil.verifyPassword(hashedPassword, password);
    }
    /**
     * Checks if user has permission based on role.
     * 
     * @param role The role name.
     * @return true if the role is "HDBOfficer".
     */
    @Override
    public boolean HasPermission(String role) 
    {
        return role.equalsIgnoreCase("HDBOfficer");
    }
    /**
     * Hashes a password.
     * 
     * @param password The password to hash.
     * @return The hashed password.
     */
    @Override
    public String hashPassword(String password) 
    {
        return EncryptionUtil.hashPassword(password);
    }
    /**
     * Gets the application status.
     * 
     * @param applicantId The applicant's ID.
     * @return The application status or "Not found".
     */
    @Override
    public String getApplicationStatus(String applicantId) 
    {
        Application app = Application.getByNric(applicantId);
        return app != null ? app.getStatus() : "Not found";
    }
    /**
     * Gets the project this officer manages.
     * 
     * @return The project ID.
     */
    public String getManagingProject()
    {
        return this.managing_project;
    }
    /**
     * Sets the project this officer manages.
     * 
     * @param projectId The project ID.
     */
    public void setManagingProject(String projectId) {
        this.managing_project = projectId;
    }
    /**
     * Submits an application on behalf of an applicant.
     * 
     * @param applicantId The applicant ID.
     * @param projectId The project ID.
     * @return true if submission is successful.
     */
    @Override
    public boolean submitApplication(String applicantId, String projectId) 
    {
        System.out.println("Officer submitting application for " + applicantId + " to project " + projectId);
        return true;
    }
    /**
     * Overloaded bookFlat with suppression message.
     */
    @Override
    public boolean bookFlat(String applicantId, String flatType, boolean suppressMessage) 
    {
    return bookFlat(applicantId, flatType); // fallback to default behavior
    }
    /**
     * Displays successful applications for officer's managed project.
     */
    // now viewing successful
    public void viewSuccessfulApplications() 
    {
        List<Application> all = DataStore.getAllApplications();
        boolean found = false;
        System.out.println("Officer you are in charge of project: " + this.managing_project);
        for (Application app : all) {
            if (app.getProjectDetails().equalsIgnoreCase(this.managing_project) &&
                app.getStatus().equalsIgnoreCase("Successful")) {
    
                System.out.println("Applicant: " + app.getNric() + ", Name: " + app.getName()
                    + ", Flat Type: " + app.getFlatType() + ", Status: " + app.getStatus());
                found = true;
            }
        }
    
        if (!found) System.out.println("No successful applications found.");
    }
    /**
     * Books a flat for a specific applicant.
     * 
     * @param applicantId The applicant's ID.
     */
    public void bookFlatForApplicant(String applicantId) 
    {
        Application app = DataStore.getApplicationByNric(applicantId);
    
        if (app == null || !app.getStatus().equalsIgnoreCase("Successful")) 
        {
            System.out.println("No successful application found for this applicant.");
            return;
        }
    
        if (!app.getProjectDetails().equalsIgnoreCase(this.managing_project)) 
        {
            System.out.println("This application does not belong to your project.");
            return;
        }
    
        BTOProject project = DataStore.getProjectById(this.managing_project);
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }
    
        String flatType = app.getFlatType();
    
        for (Flat flat : project.getFlats()) {
            if (flat.getFlatType().equalsIgnoreCase(flatType) && flat.checkAvailability(flat.getFlatId())) 
            {
                flat.bookFlat(applicantId, flat.getFlatId());
                app.updateStatus("Booked");
                System.out.println("Flat " + flat.getFlatId() + " booked successfully for " + applicantId);
                return;
            }
        }
    
        System.out.println("No available flats of type " + flatType + " in this project.");
    }
    /**
     * Books a flat for a applicant.
     * 
     * @param applicantId The applicant's ID.
     * @param flatType The flat type.
     * @return true if booked.
     */
    @Override
    public boolean bookFlat(String applicantId, String flatType) 
    {
        System.out.println("Booked flat of type " + flatType + " for applicant " + applicantId);
        return true;
    }
    /**
     * Deletes an enquiry.
     * 
     * @param enquiryId The ID of the enquiry to delete.
     */
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
    /**
     * Edits an enquiry message.
     * 
     * @param enquiryId The enquiry ID.
     * @param updatedMessage The new enquiry message.
     */
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
    /**
     * Checks availability of a flat.
     * 
     * @param flatId The flat ID.
     * @return true if available.
     */
    @Override
    public boolean checkAvailability(String flatId) 
    {
        System.out.println("Checked availability of flat: " + flatId);
        return true; 
    }
    /**
     * Filters BTO projects by location and flat type and print them
     * 
     * @param location The desired location.
     * @param flatType The desired flat type.
     */
    // Now adding filtering
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
    /**
     * Gets the officer's password.
     * 
     * @return The password string.
     */
    //file handling fixes
    public String getPassword()
    {
        return this.password;
    }
    /**
     * Applies to a specific project to be a officer
     * 
     * @param projectId The ID of the project.
     */
    // officer applying
    public void applyToProject(String projectId) 
    {    
        if (this.managing_project != null && this.managing_project.equalsIgnoreCase(projectId)) {
            System.out.println("You are already managing this project.");
            return;
        }
        if (this.managing_project != null && !this.managing_project.equalsIgnoreCase("null") && !this.managing_project.isEmpty()) {
            System.out.println("Registration failed: You are already managing another project.");
            return;
        }
        // Check for duplicate
        for (OfficerApplication app : DataStore.getAllOfficerApplications()) {
            if (app.getOfficerNric().equals(this.nric) && app.getProjectId().equals(projectId)) 
            {
                System.out.println("You have already applied for this project.");
                return;
            }
        }
        OfficerApplication newApp = new OfficerApplication(this.nric, projectId);
        DataStore.submitOfficerApplication(newApp);
        System.out.println("Application to project submitted. Awaiting manager approval.");
    }
    /**
     * Displays all applications status of the officer's applications to be a officer for a project.
     */
    public void viewMyProjectApplications() 
    {
        List<OfficerApplication> apps = DataStore.getAllOfficerApplications();
        boolean found = false;
        for (OfficerApplication app : apps) {
            if (app.getOfficerNric().equals(this.nric)) 
            {
                System.out.println("Project ID: " + app.getProjectId() + " | Status: " + app.getStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("You have not applied to any projects.");
        }
    }
    /**
     * Applies for a specific flat type for a project as an officer
     * 
     * @param flatType The type of flat.
     * @param projectDetails The ID of the project.
     */
    // hdbofficer now applying
    public void applyAsApplicant(String flatType, String projectDetails) 
    {
        Application existing = DataStore.getApplicationByNric(this.nric);
        
        if (existing != null && 
            !existing.getStatus().equalsIgnoreCase("Unsuccessful") &&
            !existing.getStatus().equalsIgnoreCase("Withdrawn")) 
        {
            System.out.println("You already have an active application.");
            return;
        }
    
        if (this.managing_project != null && this.managing_project.equalsIgnoreCase(projectDetails)) {
            System.out.println("You cannot apply to the project you are managing.");
            return;
        }
        String maritalStatus = this.maritalStatus;
        int age = this.age;
        boolean eligible = false;
        if (maritalStatus.equalsIgnoreCase("Single"))
         {
            eligible = flatType.equalsIgnoreCase("2Room") && age >= 35;
        } 
        else if (maritalStatus.equalsIgnoreCase("Married")) 
        {
            eligible = (flatType.equalsIgnoreCase("2Room") || flatType.equalsIgnoreCase("3Room")) && age >= 21;
        }
    
        if (!eligible) 
        {
            System.out.println("You are not eligible to apply for this flat type.");
            return;
        }
    
        Application newApp = new Application(this.name, this.nric, this.age, this.maritalStatus, flatType, projectDetails);
        DataStore.submitApplication(newApp);
        System.out.println("Flat application submitted as officer.");
    }
    /**
     * Withdraw an application submitted by the officer himself
     * 
     * @param applicantId The ID of the officer.
     * @param projectId The ID of the project.
     */
    @Override
    public boolean withdrawApplication(String applicantId, String projectId) {
        Application app = DataStore.getApplicationByNric(this.nric); // Officer’s own application
        if (app != null && app.getProjectDetails().equalsIgnoreCase(projectId)) {
            if (app.getStatus().equalsIgnoreCase("Booked")) {
                System.out.println("You cannot withdraw a booked application.");
                return false;
            }
            app.updateStatus("Withdraw Requested");
            System.out.println("Withdrawal request submitted.");
            return true;
        }
        System.out.println("Application not found or doesn't match project.");
        return false;
    }
    /**
     * Views flat application status for the officer.
     */
    public void viewMyFlatApplications() 
    {
        List<Application> all = DataStore.getAllApplications();
        boolean found = false;
    
        for (Application app : all) {
            if (app.getNric().equalsIgnoreCase(this.nric)) {
                System.out.println("Project: " + app.getProjectDetails() +
                                   ", Flat Type: " + app.getFlatType() +
                                   ", Status: " + app.getStatus());
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("You have not applied for any flats.");
        }
    }
}
	