/**
 * Interface defining application-related operations.
 */
public interface ApplicationInterface 
{   
    /**
     * Submits an application for a given applicant and project.
     * 
     * @param applicantId The ID (NRIC) of the applicant.
     * @param projectId The ID of the project to apply for.
     * @return true if submission was successful, false otherwise.
     */
    boolean submitApplication(String applicantId, String projectId);
    /**
     * Withdraws an existing application for a given applicant and project.
     * 
     * @param applicantId The ID (NRIC) of the applicant.
     * @param projectId The ID of the project from which to withdraw.
     * @return true if withdrawal was successful, false otherwise.
     */
    boolean withdrawApplication(String applicantId, String projectId);
    /**
     * Retrieves the status of the application for the given applicant.
     * 
     * @param applicantId The ID (NRIC) of the applicant.
     * @return The current status of the application as a String.
     */
    String getApplicationStatus(String applicantId);
}