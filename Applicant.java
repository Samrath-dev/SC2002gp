import java.util.List;
import java.util.Scanner;

public class Applicant extends User implements ApplicationInterface, EnquiryInterface , PermissionInterface
{
    protected boolean visibility;
    private String applicationStatus;

    public Applicant(String nric, String password, int age, String maritalStatus, String name) {
        super(nric, password, age, maritalStatus, name);
        visibility = false;
        applicationStatus = "No";
    }

    public void applyForProject(String flatType, String projectDetails) 
	{
        if (!applicationStatus.equals("No") && !applicationStatus.equals("Unsuccessful")) 
		{

            System.out.println("You cannot have more than one application at the same time.");
            return;
        }

        Application app = new Application(this.name, this.nric, this.age, this.maritalStatus, flatType, projectDetails);
        DataStore.submitApplication(app);
        this.applicationStatus = "Pending";
        System.out.println("Application complete.");
    }

    public void viewApplicationStatus() {
        Application app = DataStore.getApplicationByNric(this.nric);
        if (app != null) {
            System.out.println(app.getStatus());
        } else {
            System.out.println(this.applicationStatus);
        }
    }

    public boolean submitApplication(String applicantId, String projectId) {
        return false;
    }

    public boolean withdrawApplication(String applicantId, String projectId) 
    {
        Application app = DataStore.getApplicationByNric(this.nric);
        if (app != null) {
            app.withdraw();
            this.applicationStatus = "Withdrawn";
            return true;
        }
        return false;
    }

    public String getApplicationStatus(String applicantId) {
        return applicationStatus;
    }

    public boolean login(String nric, String password) 
    {
        return super.login(nric, password);
    }

    public void logout() {
        super.logout();
    }

    public void changePassword(String oldPassword, String newPassword) {
        super.changePassword(oldPassword, newPassword);
    }

    public void viewProjects() {
        System.out.println("You are not allowed to view the Projects.");
    }

    public void createEnquiry(String applicantId, String message) {
        new Enquiry(applicantId, "GenericProject", message);
    }

    public void viewEnquiries(String applicantId) {
        Enquiry.viewEnquiries();
    }

    public void editEnquiry(String enquiryId, String updatedMessage) 
	{
		try {
			int id = Integer.parseInt(enquiryId);
			Enquiry e = Enquiry.getEnquiriesByProject("GenericProject").stream()
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
		} catch (Exception ex) 
		{
			System.out.println("Invalid enquiry ID.");
		}
	}
	
	public void deleteEnquiry(String enquiryId) 
	{
		try {
			int id = Integer.parseInt(enquiryId);
			Enquiry e = Enquiry.getEnquiriesByProject("GenericProject").stream()
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
 
	

