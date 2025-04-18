public class OfficerApplication 
{
    private String officerNric;
    private String projectId;
    private String status; // "Pending", "Approved", "Rejected"

    public OfficerApplication(String officerNric, String projectId) {
        this.officerNric = officerNric;
        this.projectId = projectId;
        this.status = "Pending";
    }

    public String getOfficerNric() {
        return officerNric;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getStatus() {
        return status;
    }

    public void approve() {
        this.status = "Approved";
    }

    public void reject() {
        this.status = "Rejected";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}