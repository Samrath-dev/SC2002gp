import java.util.*;

public class Application 
{
    private String applicant;
    private String nric;
    private int age;
    private String maritalStatus;
    private String flatType;
    private String projectDetails;
    private String status;  // e.g., Pending, Booked, Successful, Withdrawn

    public Application(String applicant, String nric, int age, String maritalStatus,String flatType, String projectDetails)             
	{
        this.applicant = applicant;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.flatType = flatType;
        this.projectDetails = projectDetails;
        this.status = "Pending";
    }

    public static void submit(Application app) 
	{
        DataStore.submitApplication(app); // changed to use datastore
        System.out.println("Application submitted.");
    }

    public static Application getByNric(String nric) 
	{
        for (Application a : DataStore.getAllApplications()) // changed to use datastore
		{
            if (a.nric.equals(nric)) return a;
        }
        return null;
    }

    public static List<Application> getAllApplications() 
	{
        return DataStore.getAllApplications(); // chanegd to use datastore
    }

    public void updateStatus(String newStatus) 
	{
        this.status = newStatus;
        System.out.println("Status updated to: " + newStatus);
    }

    public void withdraw() {
        this.status = "Withdrawn";
        System.out.println("Application withdrawn successfully.");
    }

    public void updateFlatType(String flatType) {
        this.flatType = flatType;
    }

    public void setProjectDetails(String details) {
        this.projectDetails = details;
    }

    // Some more getters I added for recipt gen class

    public String getName()
	{
		return applicant;
	}
    public String getNric()
	{
		return nric; 
	}
    public int getAge()
	{
		return age; 
	}
    public String getMaritalStatus()
    { 
		return maritalStatus; 
	}
    public String getFlatType() 
	{ 
		return flatType; 
	}
    public String getProjectDetails()
	{
		return projectDetails; 
	}
    public String getStatus() 
	{
		return status; 
	}
}