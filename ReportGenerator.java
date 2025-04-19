import java.util.*;
import java.time.LocalDate;

public class ReportGenerator implements ReportGenerationInterface {

    public static class Booking {
        private String applicantId;
        private String projectId;
        private String flatType;
        private LocalDate bookingDate;
        private String maritalStatus;
        private int age;

        public Booking(String applicantId, String projectId, String flatType, String maritalStatus, int age) {
            this.applicantId = applicantId;
            this.projectId = projectId;
            this.flatType = flatType;
            this.maritalStatus = maritalStatus;
            this.age = age;
            this.bookingDate = LocalDate.now();
        }

        public String getApplicantId() { return applicantId; }
        public String getProjectId() { return projectId; }
        public String getFlatType() { return flatType; }
        public LocalDate getBookingDate() { return bookingDate; }
        public String getMaritalStatus() { return maritalStatus; }
        public int getAge() { return age; }
    }

    @Override
    public void generateReport(String reportType, String filter) {
        switch (reportType.toLowerCase()) {
            case "booking":
                System.out.println(generateBookingReport(filter));
                break;
            case "filtered":
                System.out.println(generateFilteredReport(filter));
                break;
            case "project":
                System.out.println(generateProjectReport());
                break;
            default:
                System.out.println("Unsupported report type: " + reportType);
        }
    }

    public void addBooking(Booking booking) 
    {
        // Optional: Add to DataStore if needed
        System.out.println("Bookings are now derived from applications. Manual add not needed.");
    }

    public void addProject(BTOProject project) 
    {
        DataStore.addProject(project);
    }

    private String generateBookingReport(String filter) 
    {
        StringBuilder report = new StringBuilder();
        report.append("=== BTO BOOKING REPORT ===\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");

        List<Application> apps = DataStore.getAllApplications();
        for (Application a : apps) {
            if (a.getStatus().equalsIgnoreCase("Booked")) {
                report.append("Applicant: ").append(a.getName()).append("\n")
                      .append("Project: ").append(a.getProjectDetails()).append("\n")
                      .append("Flat Type: ").append(a.getFlatType()).append("\n")
                      .append("Status: ").append(a.getMaritalStatus()).append("\n")
                      .append("Age: ").append(a.getAge()).append("\n")
                      .append("Date: ").append(LocalDate.now()).append("\n\n");
            }
        }

        report.append("Total Bookings: ").append(apps.size());
        return report.toString();
    }

    private String generateFilteredReport(String filter) {
        StringBuilder report = new StringBuilder();
        report.append("=== FILTERED BTO REPORT ===\n");
        report.append("Filter: ").append(filter).append("\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");

        int count = 0;
        for (Application a : DataStore.getAllApplications()) {
            if (a.getStatus().equalsIgnoreCase("Booked") &&
                (a.getMaritalStatus().equalsIgnoreCase(filter) || a.getFlatType().equalsIgnoreCase(filter))) {
                report.append("Applicant: ").append(a.getName()).append("\n")
                      .append("Project: ").append(a.getProjectDetails()).append("\n")
                      .append("Flat Type: ").append(a.getFlatType()).append("\n")
                      .append("Date: ").append(LocalDate.now()).append("\n\n");
                count++;
            }
        }

        report.append("Total Matching Bookings: ").append(count);
        return report.toString();
    }

    private String generateProjectReport() // fixed this also to now work with flat objects.
    {
        StringBuilder report = new StringBuilder();
        report.append("=== PROJECT STATUS REPORT ===\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
    
        for (BTOProject project : DataStore.getAllProjects()) {
            report.append("Project ID: ").append(project.getProjectId()).append("\n")
                  .append("Name: ").append(project.getName()).append("\n")
                  .append("Location: ").append(project.getLocation()).append("\n")
                  .append("Status: ").append(project.isVisible() ? "Visible" : "Hidden").append("\n")
                  .append("Available Flats:\n");
    
            Map<String, Integer> typeCounts = new HashMap<>();
    
            for (Flat f : project.getFlats()) {
                if (f.checkAvailability(f.getFlatId())) {
                    typeCounts.put(f.getFlatType(), typeCounts.getOrDefault(f.getFlatType(), 0) + 1);
                }
            }
    
            for (String type : typeCounts.keySet()) 
            {
                report.append("- ").append(type).append(": ").append(typeCounts.get(type)).append(" available\n");
            }
    
            report.append("\n");
        }
    
        return report.toString();
    }
}