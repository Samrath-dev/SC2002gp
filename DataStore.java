import java.util.*;

public class DataStore {

   
    // Users
   
    private static List<User> users = new ArrayList<>();

    public static void registerUser(User user) {
        users.add(user);
    }

    public static User getUserByNric(String nric) {
        for (User u : users) {
            if (u.nric.equalsIgnoreCase(nric)) return u;
        }
        return null;
    }

    public static List<User> getAllUsers() {
        return users;
    }

   
    // PROJECTS
   
    private static List<BTOProject> projects = new ArrayList<>();

    public static void registerProject(BTOProject project) {
        projects.add(project);
    }

    public static List<BTOProject> getAllProjects() {
        return projects;
    }

    public static BTOProject getProjectById(String projectId) {
        for (BTOProject p : projects) {
            if (p.getProjectId().equalsIgnoreCase(projectId)) return p;
        }
        return null;
    }
    public static void addProject(BTOProject project)
    {
        projects.add(project);
    }

  
    // APPLICATIONS
  
    private static List<Application> applications = new ArrayList<>();

    public static void submitApplication(Application app) {
        applications.add(app);
    }

    public static Application getApplicationByNric(String nric) {
        for (Application a : applications) {
            if (a.getNric().equalsIgnoreCase(nric)) return a;
        }
        return null;
    }

    public static List<Application> getAllApplications() {
        return applications;
    }

    
    // ENQUIRIES
    
    private static List<Enquiry> enquiries = new ArrayList<>();

    public static void addEnquiry(Enquiry e) 
    {
        enquiries.add(e);
    }

    public static List<Enquiry> getEnquiriesByProject(String project) 
    {
        List<Enquiry> result = new ArrayList<>();
        for (Enquiry e : enquiries) {
            if (e.getProject().equalsIgnoreCase(project)) 
            {
                result.add(e);
            }
        }
        return result;
    }
    public static void removeEnquiry(Enquiry e) 
    {
        enquiries.remove(e);
    }
    public static List<Enquiry> getAllEnquiries() 
    {
        return enquiries;
    }

    public static Enquiry getEnquiryById(int id) 
    {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryId() == id) return e;
        }
        return null;
    }

    public static void deleteEnquiry(int id) 
    {
        enquiries.removeIf(e -> e.getEnquiryId() == id);
    }
}