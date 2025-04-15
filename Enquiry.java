import java.util.ArrayList;
import java.util.List;

public class Enquiry implements EnquiryInterface {
    private static int counter = 1;
    private int enquiryId;
    private String applicantName;
    private String project;
    private String enquiryText;
    private String reply;

    public Enquiry(String applicantName, String project, String enquiryText) {
        this.enquiryId = counter++;
        this.applicantName = applicantName;
        this.project = project;
        this.enquiryText = enquiryText;
        this.reply = "No reply yet.";
        DataStore.addEnquiry(this); // replaced static list with DataStore
    }

    public int getEnquiryId() { return enquiryId; }
    public String getApplicantName() { return applicantName; }
    public String getProject() { return project; }
    public String getEnquiryText() { return enquiryText; }
    public String getReply() { return reply; }

    public void setEnquiryText(String enquiryText) {
        this.enquiryText = enquiryText;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public static void viewEnquiries() {
        List<Enquiry> enquiries = DataStore.getAllEnquiries(); // replaced
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }
        for (Enquiry e : enquiries) {
            System.out.println("ID: " + e.getEnquiryId() + ", Applicant: " + e.getApplicantName() +
                               ", Project: " + e.getProject() + ", Enquiry: " + e.getEnquiryText() +
                               ", Reply: " + e.getReply());
        }
    }

    public void editEnquiry(String newText) {
        this.enquiryText = newText;
        System.out.println("Enquiry updated successfully.");
    }

    public void deleteEnquiry() {
        DataStore.removeEnquiry(this); // replaced
        System.out.println("Enquiry deleted successfully.");
    }

    public static List<Enquiry> getEnquiriesByProject(String project) {
        List<Enquiry> projectEnquiries = new ArrayList<>();
        for (Enquiry e : DataStore.getAllEnquiries()) { // replaced
            if (e.getProject().equalsIgnoreCase(project)) {
                projectEnquiries.add(e);
            }
        }
        return projectEnquiries;
    }

    // All interface methods...
    @Override
    public void createEnquiry(String applicantId, String message) 
    {
        new Enquiry(applicantId, "UnknownProject", message); // or allow project to be passed in another way
    }

    @Override
    public void viewEnquiries(String applicantId) 
    {
        List<Enquiry> enquiries = DataStore.getAllEnquiries(); // replaced
        boolean found = false;
        for (Enquiry e : enquiries) 
        {
            if (e.applicantName.equalsIgnoreCase(applicantId)) 
            {
                System.out.println("ID: " + e.getEnquiryId() + ", Project: " + e.getProject() + ", Enquiry: " + e.getEnquiryText() + ", Reply: " + e.getReply());
                found = true;
            }
        }
        if (!found) System.out.println("No enquiries found for applicant: " + applicantId);
    }

    @Override
    public void editEnquiry(String enquiryId, String updatedMessage) 
    {
        try {
            int id = Integer.parseInt(enquiryId);
            for (Enquiry e : DataStore.getAllEnquiries()) // replaced
            {
                if (e.enquiryId == id) 
                {
                    e.editEnquiry(updatedMessage);
                    return;
                }
            }
            System.out.println("Enquiry ID not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format.");
        }
    }

    @Override
    public void deleteEnquiry(String enquiryId) 
    {
        try {
            int id = Integer.parseInt(enquiryId);
            List<Enquiry> enquiries = DataStore.getAllEnquiries(); // replaced
            for (int i = 0; i < enquiries.size(); i++)
            {
                if (enquiries.get(i).enquiryId == id) {
                    enquiries.get(i).deleteEnquiry();
                    return;
                }
            }
            System.out.println("Enquiry ID not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format.");
        }
    }
}