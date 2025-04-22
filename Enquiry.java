import java.util.ArrayList;
import java.util.List;
/**
 * Represents an enquiry made by an applicant regarding a BTO project.
 * Implements functionality to create, view, edit, and delete enquiries.
 */
public class Enquiry implements EnquiryInterface {
    private static int counter = 1;
    private int enquiryId;
    private String applicantName;
    private String project;
    private String enquiryText;
    private String reply;
    /**
     * Constructs a new enquiry and automatically adds it to the DataStore.
     *
     * @param applicantName The name or NRIC of the applicant.
     * @param project       The name or ID of the project.
     * @param enquiryText   The content of the enquiry.
     */
    public Enquiry(String applicantName, String project, String enquiryText) {
        this.enquiryId = counter++;
        this.applicantName = applicantName;
        this.project = project;
        this.enquiryText = enquiryText;
        this.reply = "No reply yet.";
        DataStore.addEnquiry(this); // replaced static list with DataStore
    }
    /**
     * @return The ID of the enquiry.
     */
    public int getEnquiryId() { return enquiryId; }
    /**
     * @return The applicant's name or NRIC.
     */
    public String getApplicantName() { return applicantName; }
    /**
     * @return The project associated with the enquiry.
     */
    public String getProject() { return project; }
    /**
     * @return The content of the enquiry.
     */
    public String getEnquiryText() { return enquiryText; }
    /**
     * @return The reply to the enquiry.
     */
    public String getReply() { return reply; }
    /**
     * Sets the enquiry text.
     *
     * @param enquiryText The new enquiry content.
     */
    public void setEnquiryText(String enquiryText) {
        this.enquiryText = enquiryText;
    }
    /**
     * Sets the reply to the enquiry.
     *
     * @param reply The reply message.
     */
    public void setReply(String reply) {
        this.reply = reply;
    }
    /**
     * Displays all enquiries stored in the DataStore.
     */
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
    /**
     * Updates the enquiry text.
     *
     * @param newText The updated enquiry message.
     */
    public void editEnquiry(String newText) {
        this.enquiryText = newText;
        System.out.println("Enquiry updated successfully.");
    }
    /**
     * Deletes the enquiry from the DataStore.
     */
    public void deleteEnquiry() {
        DataStore.removeEnquiry(this); // replaced
        System.out.println("Enquiry deleted successfully.");
    }
    /**
     * Retrieves all enquiries for a specific project.
     *
     * @param project The project name or ID.
     * @return A list of matching enquiries.
     */
    public static List<Enquiry> getEnquiriesByProject(String project) {
        List<Enquiry> projectEnquiries = new ArrayList<>();
        for (Enquiry e : DataStore.getAllEnquiries()) { // replaced
            if (e.getProject().equalsIgnoreCase(project)) {
                projectEnquiries.add(e);
            }
        }
        return projectEnquiries;
    }
    /**
     * Creates a new enquiry with a default project.
     *
     * @param applicantId The applicant’s NRIC.
     * @param message     The enquiry message.
     */
    // All interface methods...
    @Override
    public void createEnquiry(String applicantId, String message) 
    {
        new Enquiry(applicantId, "UnknownProject", message); // or allow project to be passed in another way
    }
    /**
     * Views all enquiries made by a specific applicant.
     *
     * @param applicantId The NRIC of the applicant.
     */
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
    /**
     * Edits an existing enquiry by ID.
     *
     * @param enquiryId      The ID of the enquiry.
     * @param updatedMessage The updated enquiry message.
     */
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
    /**
     * Deletes an enquiry based on its ID.
     *
     * @param enquiryId The ID of the enquiry to delete.
     */
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