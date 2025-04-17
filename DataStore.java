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

    //  Projects
    private static List<BTOProject> projects = new ArrayList<>();

    public static void registerProject(BTOProject project)
    {
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

    public static void addProject(BTOProject project) {
        projects.add(project);
    }

    // Applications
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

    // === ENQUIRIES ===
    private static List<Enquiry> enquiries = new ArrayList<>();

    public static void addEnquiry(Enquiry e) {
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

    public static Enquiry getEnquiryById(int id) {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryId() == id) return e;
        }
        return null;
    }

    public static void deleteEnquiry(int id) {
        enquiries.removeIf(e -> e.getEnquiryId() == id);
    }

    // filehandler methods
    private static List<List<String>> rawData = new ArrayList<>();

    public static void setData(List<List<String>> data) 
    {
        rawData = data;
    }

    public static List<List<String>> getData() {
        return rawData;
    }

    // applicant filehandling
    public static List<List<String>> getApplicantDataForWrite() {
        List<List<String>> data = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Applicant a) {
                List<String> row = new ArrayList<>();
                row.add(a.getName());
                row.add(a.getNric());
                row.add(String.valueOf(a.getAge()));
                row.add(a.getMaritalStatus());
                row.add(a.getPassword());  // password is hashed
                data.add(row);
            }
        }
        return data;
    }
    
    public static void loadApplicantsFromData(List<List<String>> data) {
        for (List<String> row : data) {
            if (row.size() >= 5) {
                String name = row.get(0);
                String nric = row.get(1);
                int age = Integer.parseInt(row.get(2));
                String maritalStatus = row.get(3);
                String hashedPw = row.get(4);
                Applicant app = new Applicant(nric, hashedPw, age, maritalStatus, name, true);
                registerUser(app);
            }
        }
    }

    //officer filehandler
    public static List<List<String>> getOfficerDataForWrite() {
        List<List<String>> data = new ArrayList<>();
        for (User u : users) {
            if (u instanceof HDBOfficer o) {
                List<String> row = new ArrayList<>();
                row.add(o.getName());
                row.add(o.getNric());
                row.add(String.valueOf(o.getAge()));
                row.add(o.getMaritalStatus());
                row.add(o.getPassword());  // already hashed
                data.add(row);
            }
        }
        return data;
    }

    public static void loadOfficersFromData(List<List<String>> data) {
        for (List<String> row : data) {
            if (row.size() >= 5) {
                String name = row.get(0);
                String nric = row.get(1);
                int age = Integer.parseInt(row.get(2));
                String maritalStatus = row.get(3);
                String hashedPw = row.get(4);
                HDBOfficer o = new HDBOfficer(nric, hashedPw, age, maritalStatus, name, true);
                registerUser(o);
            }
        }
    }

    //manager filehandler
    public static List<List<String>> getManagerDataForWrite() {
        List<List<String>> data = new ArrayList<>();
        for (User u : users) {
            if (u instanceof HDBManager m) {
                List<String> row = new ArrayList<>();
                row.add(m.getName());
                row.add(m.getNric());
                row.add(String.valueOf(m.getAge()));
                row.add(m.getMaritalStatus());
                row.add(m.getPassword());
                data.add(row);
            }
        }
        return data;
    }

    public static void loadManagersFromData(List<List<String>> data) {
        for (List<String> row : data) {
            if (row.size() >= 5) {
                String name = row.get(0);
                String nric = row.get(1);
                int age = Integer.parseInt(row.get(2));
                String maritalStatus = row.get(3);
                String hashedPw = row.get(4);
                HDBManager m = new HDBManager(nric, hashedPw, age, maritalStatus, name, true);
                registerUser(m);
            }
        }
    }

    // project writer 
    public static List<List<String>> getProjectDataForWrite() {
        List<List<String>> data = new ArrayList<>();
        for (BTOProject p : projects) {
            List<String> row = new ArrayList<>();
            row.add(p.getProjectId());
            row.add(p.getName());
            row.add(p.getLocation());
            row.add(String.valueOf(p.isVisible()));
            row.add(p.getManagerInCharge());
            data.add(row);
        }
        return data;
    }
    //flat writer
    public static List<List<String>> getFlatDataForProject(BTOProject p) {
        List<List<String>> data = new ArrayList<>();
        for (Flat f : p.getFlats()) {
            List<String> row = new ArrayList<>();
            row.add(f.getFlatId());
            row.add(f.getFlatType());
            row.add(String.valueOf(f.checkAvailability(f.getFlatId())));
            data.add(row);
        }
        return data;
    }
    // project reader
    public static void loadProjectsFromData(List<List<String>> data) {
        for (List<String> row : data) {
            if (row.size() >= 5) {
                BTOProject p = new BTOProject();
    
                try {
                    // Set projectId
                    var pid = BTOProject.class.getDeclaredField("projectId");
                    pid.setAccessible(true);
                    pid.set(p, row.get(0));
    
                    // Set name
                    var name = BTOProject.class.getDeclaredField("name");
                    name.setAccessible(true);
                    name.set(p, row.get(1));
    
                    // Set location
                    var loc = BTOProject.class.getDeclaredField("location");
                    loc.setAccessible(true);
                    loc.set(p, row.get(2));
    
                    // Set visibility
                    var vis = BTOProject.class.getDeclaredField("isVisible");
                    vis.setAccessible(true);
                    vis.set(p, Boolean.parseBoolean(row.get(3)));
    
                    // Set manager
                    var mgr = BTOProject.class.getDeclaredField("managerInCharge");
                    mgr.setAccessible(true);
                    mgr.set(p, row.get(4));
    
                    projects.add(p);
    
                } catch (Exception e) {
                    System.out.println("Error loading project: " + e.getMessage());
                }
            }
        }
    }
    //flat reader
    public static void loadFlatsForProject(BTOProject p, List<List<String>> data) {
        for (List<String> row : data) {
            if (row.size() >= 3) {
                Flat f = new Flat(row.get(0), row.get(1));
                if (!Boolean.parseBoolean(row.get(2))) {
                    f.bookFlat("NA", row.get(0));  // mark as booked
                }
                p.getFlats().add(f);
            }
        }
    }
    //enquiries
    public static List<List<String>> getEnquiryDataForWrite() {
        List<List<String>> data = new ArrayList<>();
        for (Enquiry e : enquiries) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(e.getEnquiryId()));
            row.add(e.getApplicantName());
            row.add(e.getProject());
            row.add(e.getEnquiryText());
            row.add(e.getReply());
            data.add(row);
        }
        return data;
    }

    public static void loadEnquiriesFromData(List<List<String>> data) {
        for (List<String> row : data) {
            if (row.size() >= 5) {
                try {
                    int id = Integer.parseInt(row.get(0));
                    Enquiry e = new Enquiry(row.get(1), row.get(2), row.get(3));
                    e.setReply(row.get(4));

                    var counterField = Enquiry.class.getDeclaredField("counter");
                    counterField.setAccessible(true);
                    counterField.setInt(null, Math.max(counterField.getInt(null), id + 1));

                    enquiries.add(e);
                } catch (Exception ex) {
                    System.out.println("Error loading enquiry: " + ex.getMessage());
                }
            }
        }
    }
   //applications
   public static List<List<String>> getApplicationDataForWrite() {
    List<List<String>> data = new ArrayList<>();
    for (Application app : applications) 
    {
        List<String> row = new ArrayList<>();
        row.add(app.getName());
        row.add(app.getNric());
        row.add(String.valueOf(app.getAge()));
        row.add(app.getMaritalStatus());
        row.add(app.getFlatType());
        row.add(app.getProjectDetails());
        row.add(app.getStatus());
        data.add(row);
    }
    return data;
}

public static void loadApplicationsFromData(List<List<String>> data) {
    for (List<String> row : data) {
        if (row.size() >= 7) {
            String name = row.get(0);
            String nric = row.get(1);
            int age = Integer.parseInt(row.get(2));
            String maritalStatus = row.get(3);
            String flatType = row.get(4);
            String projectDetails = row.get(5);
            String status = row.get(6);
            Application app = new Application(name, nric, age, maritalStatus, flatType, projectDetails);
            app.updateStatus(status);
            applications.add(app);
        }
    }
}
    // flats filehandling
    public static List<List<String>> getAllFlatDataForWrite() 
    {
        List<List<String>> data = new ArrayList<>();
        for (BTOProject p : projects) {
            for (Flat f : p.getFlats()) {
                List<String> row = new ArrayList<>();
                row.add(p.getProjectId()); // Link to project
                row.add(f.getFlatId());
                row.add(f.getFlatType());
                row.add(String.valueOf(f.checkAvailability(f.getFlatId())));
                data.add(row);
            }
        }
        return data;
    }
    
}
