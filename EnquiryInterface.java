/**
 * Interface defining operations related to applicant enquiries
 * for BTO projects, including creation, viewing, editing, and deletion.
 */
public interface EnquiryInterface 
{   
    /**
     * Creates a new enquiry from an applicant.
     *
     * @param applicantId The NRIC or identifier of the applicant.
     * @param message The message or enquiry text.
     */
    void createEnquiry(String applicantId, String message);
    /**
     * Displays all enquiries made by a specific applicant.
     *
     * @param applicantId The NRIC or identifier of the applicant.
     */
    void viewEnquiries(String applicantId);
    /**
     * Edits an existing enquiry using its ID.
     *
     * @param enquiryId The ID of the enquiry to be edited.
     * @param updatedMessage The new message to update the enquiry with.
     */
    void editEnquiry(String enquiryId, String updatedMessage);
    /**
     * Deletes an enquiry based on its ID.
     *
     * @param enquiryId The ID of the enquiry to be deleted.
     */
    void deleteEnquiry(String enquiryId);
}