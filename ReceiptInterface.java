/**
 * Interface defining the contract for generating BTO booking receipts.
 */
public interface ReceiptInterface 

{
    /**
     * Generates a receipt for a specific applicant based on their NRIC, project ID, and flat type.
     *
     * @param applicantId The NRIC of the applicant.
     * @param projectId The ID of the project the applicant applied for.
     * @param flatId The type of flat applied for.
     */
    void generateReceipt(String applicantId, String projectId, String flatId);
}