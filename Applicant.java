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

    public boolean withdrawApplication(String applicantId, String projectId) 
    {
        Application app = DataStore.getApplicationByNric(this.nric);
        if (app != null) 
        {
            app.withdraw();
            this.applicationStatus = "Withdrawn";
            return true;
        }
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
    //fixed placeholder logic for view projects for applicants 
    @Override
    public void viewProjects() 
    {
    System.out.println("\n--- Available Projects Based on Your Eligibility ---");
    boolean found = false;

                    for (BTOProject project : DataStore.getAllProjects()) 
                    {
                        if (!project.isVisible()) continue;

                    for (Flat flat : project.getFlats()) 
                    {
                            if (flat.checkAvailability(flat.getFlatId()) && isEligibleFor(flat.getFlatType())) 
                            {
                                System.out.println("Project ID: " + project.getProjectId());
                                System.out.println("Name: " + project.getName());
                                System.out.println("Location: " + project.getLocation());
                                System.out.println("Flat: " + flat.getFlatId() + " (" + flat.getFlatType() + ") - Available");
                                System.out.println("------------------------------");
                                found = true;
                                break; // Found a valid flat for this project, no need to print more from it
                            }
                    }
                    }

            if (!found) {
                    System.out.println("No eligible projects currently available.");
                        }
    }
    // fixed placeholder logic for createenquiry
    public void createEnquiry(String applicantId, String message) 
    {
        Application app = DataStore.getApplicationByNric(this.nric);
        if (app == null) 
        {
            System.out.println("You have no active application.");
            return;
        }

        String projectId = app.getProjectDetails();
        new Enquiry(applicantId, projectId, message);
        System.out.println("Enquiry submitted for project: " + projectId);
    }

        public void viewEnquiries(String applicantId) 
        {
            Enquiry.viewEnquiries();
        }

        public void editEnquiry(String enquiryId, String updatedMessage) 
        {
            try 
            {
            // fixed generic project placeholder
            Application app = DataStore.getApplicationByNric(this.nric);
                if (app == null) 
                {
                System.out.println("You have no active application.");
                return;
                }
                String projectId = app.getProjectDetails();
                
                int id = Integer.parseInt(enquiryId.trim());
                Enquiry e = Enquiry.getEnquiriesByProject(projectId).stream()
                    .filter(enq -> enq.getEnquiryId() == id)
                    .findFirst().orElse(null);
                if (e != null && e.getApplicantName().equals(this.nric)) 
                {
                    e.setEnquiryText(updatedMessage);
                    System.out.println("Enquiry updated successfully.");
                } 
                else
                {
                    System.out.println("Enquiry ID not found.");
                }
            } 
            catch (Exception ex) 
            {
                System.out.println("Invalid enquiry ID.");
            }
        }
	
	public void deleteEnquiry(String enquiryId) 
	{
		
        try 
        {
            // fixed generic project placeholder
            Application app = DataStore.getApplicationByNric(this.nric);
            if (app == null) 
            {
                System.out.println("You have no active application.");
                return;
            }
            String projectId = app.getProjectDetails();

			int id = Integer.parseInt(enquiryId.trim());
			Enquiry e = Enquiry.getEnquiriesByProject(projectId).stream()
				.filter(enq -> enq.getEnquiryId() == id)
				.findFirst().orElse(null);
			if (e != null && e.getApplicantName().equals(this.nric)) 
			{
				e.deleteEnquiry();
			} 
			else 
			{
				System.out.println("Enquiry ID not found.");
			}
		} catch (Exception ex) 
		{
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
 
	

