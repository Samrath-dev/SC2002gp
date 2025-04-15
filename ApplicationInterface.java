public interface ApplicationInterface 
{
    boolean submitApplication(String applicantId, String projectId);
    boolean withdrawApplication(String applicantId, String projectId);
    String getApplicationStatus(String applicantId);
}