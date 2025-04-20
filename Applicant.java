import java.util.List;
import java.util.Scanner;

public class Applicant extends User implements ApplicationInterface, EnquiryInterface , PermissionInterface
{
    protected boolean visibility;
    private String applicationStatus;

    public Applicant(String nric, String password, int age, String maritalStatus, String name) 
    {
        super(nric, password, age, maritalStatus, name);
        visibility = false;
        applicationStatus = "No";
    }
    // this is for the rehashing issue
    public Applicant(String nric, String hashedPw, int age, String maritalStatus, String name, boolean isHashed) 
    {
            super(nric, hashedPw, age, maritalStatus, name, isHashed);
            this.visibility = false;
            this.applicationStatus = "No"; // fixed null bug also 
        
    }

    public void applyForProject(String flatType, String projectDetails) 
  {
    Application app = DataStore.getApplicationByNric(this.nric);

    if (app != null && !app.getStatus().equalsIgnoreCase("Unsuccessful") && !app.getStatus().equalsIgnoreCase("Withdrawn")) 
    {
        System.out.println("You cannot have more than one application at the same time.");
        return;
    }

    if (!isEligibleFor(flatType)) {
        System.out.println("You are not eligible to apply for this flat type.");
        return;
    }

        Application newApp = new Application(this.name, this.nric, this.age, this.maritalStatus, flatType, projectDetails);
        DataStore.submitApplication(newApp);
        this.applicationStatus = "Pending";
        System.out.println("Application complete.");
   }  

    public void viewApplicationStatus() {
        Application app = DataStore.getApplicationByNric(this.nric);
        if (app != null) 
        {
            System.out.println(app.getStatus());
        } 
        else 
        {
            System.out.println(this.applicationStatus);
        }
    }

    public boolean submitApplication(String applicantId, String projectId)
    {
        return false;
    }
    //fixed placeholder logic for view projects for applicants 
    @Override
    public void viewProjects() 
    {
        List<BTOProject> allProjects = DataStore.getAllProjects();
    
        for (BTOProject p : allProjects) {
            if (!p.isVisible() || p.isExpired()) continue;
    
            boolean shown = false;
            for (Flat f : p.getFlats()) {
                if (f.checkAvailability(f.getFlatId()) && isEligibleFor(f.getFlatType())) 
                {
                    if (!shown) {
                        System.out.println("Project ID: " + p.getProjectId());
                        System.out.println("Name: " + p.getName());
                        System.out.println("Location: " + p.getLocation());
                        System.out.println("Eligible Flats:");
                        shown = true;
                    }
                    System.out.println("  - " + f.getFlatId() + " (" + f.getFlatType() + ")");
                }
            }
            if (shown) System.out.println(); // spacer after each project
        }
    }

    @Override
    public boolean withdrawApplication(String applicantId, String projectId) 
    {
        Application app = DataStore.getApplicationByNric(this.nric);
        if (app != null) 
        {
            if (app.getStatus().equalsIgnoreCase("Booked")) 
            {
                System.out.println("You cannot withdraw an already booked application.");
                return false;
            }
    
            app.updateStatus("Withdraw Requested");
            this.applicationStatus = "Withdraw Requested";
            System.out.println("Withdrawal request submitted.");
            return true;
        }
        System.out.println("No application found to withdraw.");
        return false;
    }

    public String getApplicationStatus(String applicantId) 
    {
        return applicationStatus;
    }

    public boolean login(String nric, String password) 
    {
        return super.login(nric, password);
    }

    public void logout() 
    {
        super.logout();
    }

    public void changePassword(String oldPassword, String newPassword) 
    {
        super.changePassword(oldPassword, newPassword);
    }
    
  
   
    // fixed placeholder logic for createenquiry now with unlimited ones
    public void createEnquiry(String applicantId, String message) 
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter project ID to enquire about: ");
        String projectId = sc.nextLine();
    
        BTOProject project = DataStore.getProjectById(projectId);
    
        if (project == null || project.isExpired() || !project.isVisible()) {
            System.out.println("Invalid or expired project.");
            return;
        }
    
        new Enquiry(applicantId, projectId, message);
        System.out.println("Enquiry submitted to project " + projectId);
    }

        public void viewEnquiries(String applicantId) 
        {
            Enquiry.viewEnquiries();
        }

        public void editEnquiry(String enquiryId, String updatedMessage) 
        {
            try {
                int id = Integer.parseInt(enquiryId);
                Enquiry e = DataStore.getEnquiryById(id);
        
                if (e == null || !e.getApplicantName().equals(this.nric)) {
                    System.out.println("Enquiry not found or you don't own it.");
                    return;
                }
        
                if (e.getReply() != null && !e.getReply().isBlank() && !e.getReply().equalsIgnoreCase("No reply yet."))
                {
                    System.out.println("This enquiry has already been replied to and cannot be edited.");
                    return;
                }
        
                e.setEnquiryText(updatedMessage);
                System.out.println("Enquiry updated successfully.");
        
            } catch (Exception ex) {
                System.out.println("Invalid enquiry ID.");
            }
        }
               
	
        public void deleteEnquiry(String enquiryId)
         {
            try {
                int id = Integer.parseInt(enquiryId.trim());
                Enquiry e = DataStore.getEnquiryById(id);
        
                if (e == null || !e.getApplicantName().equals(this.nric)) {
                    System.out.println("Enquiry not found or you don't own it.");
                    return;
                }
        
                if (e.getReply() != null && !e.getReply().isBlank()) {
                    System.out.println("This enquiry has already been replied to and cannot be deleted.");
                    return;
                }
        
                e.deleteEnquiry();
                System.out.println("Enquiry deleted successfully.");
        
            } catch (Exception ex) {
                System.out.println("Invalid enquiry ID.");
            }
        }


    @Override
    public String hashPassword(String password) 
	{
        return EncryptionUtil.hashPassword(password);
    }

    @Override
    public boolean verifyPassword(String hashedPassword, String password) 
	{
        return EncryptionUtil.verifyPassword(hashedPassword, password);
    }
    // I have now implemented permissions
    @Override
     public boolean HasPermission(String role) 
     {
        return role.equalsIgnoreCase("Applicant");
     }

    
    // this is for filtering 
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
                if (
                    f.getFlatType().equalsIgnoreCase(flatType) &&
                    f.checkAvailability(f.getFlatId()) &&
                    this.isEligibleFor(f.getFlatType()) // eligibility check 
                ) 
                {
                    System.out.println("Match: " + p.getName() + " in " + p.getLocation() + " with " + f.getFlatType());
                    break;
                }
            }
        }
    }
    }
    // this for eligibility
    public boolean isEligibleFor(String flatType) 
    {
        if (maritalStatus.equalsIgnoreCase("Single")) 
        {
            return flatType.equalsIgnoreCase("2Room") && age >= 35;
        } 
        else if (maritalStatus.equalsIgnoreCase("Married")) 
        {
            return (flatType.equalsIgnoreCase("2Room") || flatType.equalsIgnoreCase("3Room")) && age >= 21;
        }
        return false;
    }
    // filehandling getters
    public String getPassword()
    {
        return this.password;
    }
    public int getAge()
    {
        return age;
    }
    // added viewing projects after bug fixes
    
}
 
	

