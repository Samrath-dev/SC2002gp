public interface EnquiryInterface 
{
    void createEnquiry(String applicantId, String message);
    void viewEnquiries(String applicantId);
    void editEnquiry(String enquiryId, String updatedMessage);
    void deleteEnquiry(String enquiryId);
}