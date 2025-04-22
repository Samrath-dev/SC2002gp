import java.util.*;
/**
 * Represents an application for a BTO flat project.
 * Contains applicant details, flat preferences, and application status.
 */
public class Application 
{
    private String applicant;
    private String nric;
    private int age;
    private String maritalStatus;
    private String flatType;
    private String projectDetails;
    private String status;  // e.g., Pending, Booked, Successful, Withdrawn

    /**
     * Constructs a new Application instance.
     *
     * @param applicant The name of the applicant.
     * @param nric The NRIC of the applicant.
     * @param age The age of the applicant.
     * @param maritalStatus The marital status of the applicant.
     * @param flatType The type of flat the applicant is applying for.
     * @param projectDetails The details of the project the applicant is applying to.
     */
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
    /**
     * Submits the application to the data store.
     *
     * @param app The application to submit.
     */
    public static void submit(Application app) 
	{
        DataStore.submitApplication(app); // changed to use datastore
        System.out.println("Application submitted.");
    }
    /**
     * Retrieves an application by the applicant's NRIC.
     *
     * @param nric The NRIC of the applicant.
     * @return The matching Application, or null if not found.
     */
    public static Application getByNric(String nric) 
	{
        for (Application a : DataStore.getAllApplications()) // changed to use datastore
		{
            if (a.nric.equals(nric)) return a;
        }
        return null;
    }
    /**
     * Retrieves all applications from the data store.
     *
     * @return A list of all applications.
     */
    public static List<Application> getAllApplications() 
	{
        return DataStore.getAllApplications(); // chanegd to use datastore
    }
    /**
     * Updates the status of the application.
     *
     * @param newStatus The new status to set.
     */
    public void updateStatus(String newStatus) 
	{
        this.status = newStatus;
    }
    /**
     * Withdraws the application and updates its status.
     */
    public void withdraw() {
        this.status = "Withdrawn";
        System.out.println("Application withdrawn successfully.");
    }
    /**
     * Updates the flat type of the application.
     *
     * @param flatType The new flat type.
     */
    public void updateFlatType(String flatType) {
        this.flatType = flatType;
    }
    /**
     * Sets the project details for the application.
     *
     * @param details The new project details.
     */
    public void setProjectDetails(String details) {
        this.projectDetails = details;
    }

    // Some more getters I added for recipt gen class
    /**
     * @return The name of the applicant.
     */
    public String getName()
	{
		return applicant;
	}
    /**
     * @return The NRIC of the applicant.
     */
    public String getNric()
	{
		return nric; 
	}
    /**
     * @return The age of the applicant.
     */
    public int getAge()
	{
		return age; 
	}
    /**
     * @return The marital status of the applicant.
     */
    public String getMaritalStatus()
    { 
		return maritalStatus; 
	}
    /**
     * @return The flat type selected in the application.
     */
    public String getFlatType() 
	{ 
		return flatType; 
	}
    /**
     * @return The project details associated with the application.
     */
    public String getProjectDetails()
	{
		return projectDetails; 
	}
    /**
     * @return The current status of the application.
     */
    public String getStatus() 
	{
		return status; 
	}
}