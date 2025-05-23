import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptGenerator implements ReceiptInterface {

    public static void generateReceipts() {
        List<Application> applicants = DataStore.getAllApplications(); // upgraded to use DataStore

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        System.out.println("=======================================");
        System.out.println("           BTO Booking Receipt        ");
        System.out.println("=======================================");

        for (Application applicant : applicants) {
            System.out.println("---------------------------------------");
            System.out.println("Timestamp: " + timestamp);
            System.out.println("Applicant Name: " + applicant.getName());
            System.out.println("NRIC: " + applicant.getNric());
            System.out.println("Age: " + applicant.getAge());
            System.out.println("Marital Status: " + applicant.getMaritalStatus());
            System.out.println("Flat Type: " + applicant.getFlatType());
            System.out.println("Project Details: " + applicant.getProjectDetails());
        }
    }
    @Override
    public void generateReceipt(String applicantId, String projectId, String flatType) 
    {
        Application app = Application.getByNric(applicantId);
        if (app != null && app.getProjectDetails().equalsIgnoreCase(projectId) && app.getFlatType().equalsIgnoreCase(flatType)) 
        {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            System.out.println("=======================================");
            System.out.println("           BTO Booking Receipt        ");
            System.out.println("=======================================");
            System.out.println("Timestamp: " + timestamp);
            System.out.println("Applicant Name: " + app.getName());
            System.out.println("NRIC: " + app.getNric());
            System.out.println("Age: " + app.getAge());
            System.out.println("Marital Status: " + app.getMaritalStatus());
            System.out.println("Flat Type: " + app.getFlatType());
            System.out.println("Project Details: " + app.getProjectDetails());
        } 
        else 
        {
            System.out.println("No matching application found for receipt generation.");
        }
    }

    public static void generateReceipts(List<Application> applicants) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
    
        System.out.println("=======================================");
        System.out.println("           BTO Booking Receipt        ");
        System.out.println("=======================================");
    
        for (Application applicant : applicants) {
            System.out.println("---------------------------------------");
            System.out.println("Timestamp: " + timestamp);
            System.out.println("Applicant Name: " + applicant.getName());
            System.out.println("NRIC: " + applicant.getNric());
            System.out.println("Age: " + applicant.getAge());
            System.out.println("Marital Status: " + applicant.getMaritalStatus());
            System.out.println("Flat Type: " + applicant.getFlatType());
            System.out.println("Project Details: " + applicant.getProjectDetails());
        }
    }
}