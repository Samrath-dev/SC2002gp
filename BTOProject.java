import java.time.LocalDate;
import java.util.List;  //for method return types
import java.util.Map;
import java.util.ArrayList;
import java.time.LocalDate;


//BTOProject class//
public class BTOProject implements BTOProjectInterface {
    private String projectId;
    private String name;
    private String location;
    private boolean isVisible;
    private List<Flat> flats = new ArrayList<>(); // made new flat storage
    private static int projectCounter = 0;
    private List<String> assignedOfficers;
    private static final int MAX_OFFICERS = 10;
    private String managerInCharge;
    private LocalDate startDate; // new
    private LocalDate endDate;   // new

    public BTOProject() 
    {
        this.projectId = "PROJ_" + (++projectCounter);
        this.assignedOfficers = new ArrayList<>();
        this.isVisible = true;  // added to ensure default visibility
    }

 // Interface method implementations
    @Override
    public void createProject(String name, String location, String[] flatTypes, int availableFlats) 
    {
        this.name = name;
        this.location = location;
        this.isVisible = true;

        for (String type : flatTypes) {
            for (int i = 1; i <= availableFlats; i++) 
            {
                String flatId = this.projectId + "_" + type + "_" + i;
                flats.add(new Flat(flatId, type)); // 
            }
        }
    }

    
    @Override
    public void createProject(String name, String location, String startDate, String endDate, Map<String, Integer> flatTypeMap) {
    this.name = name;
    this.location = location;
    this.startDate = LocalDate.parse(startDate);
    this.endDate = LocalDate.parse(endDate);
    this.isVisible = true;

    for (Map.Entry<String, Integer> entry : flatTypeMap.entrySet()) {
        String type = entry.getKey();
        int quantity = entry.getValue();
        for (int i = 1; i <= quantity; i++) {
            String flatId = this.projectId + "_" + type + "_" + i;
            flats.add(new Flat(flatId, type));
        }
    }
}

    @Override
    public void editProject(String projectId, String newName, String newLocation) {
        if (this.projectId.equals(projectId)) {
            this.name = newName;
            this.location = newLocation;
        }
    }

    @Override
    public void deleteProject(String projectId) {
        if (this.projectId.equals(projectId)) {
            this.isVisible = false;
        }
    }

    @Override
    public void toggleVisibility(String projectId, boolean isVisible) {
        if (this.projectId.equals(projectId)) {
            this.isVisible = isVisible;
        }
    }
    public boolean isExpired() 
    {
        return LocalDate.now().isAfter(endDate);
    }
    //books a flat of specified type,
    //parameter type->Flat type (2-Room/3-Room),
    //return true if booking is successful
    public boolean bookFlat(String flatType) 
    {
        for (Flat f : flats) {
            if (f.getFlatType().equalsIgnoreCase(flatType) && f.checkAvailability(f.getFlatId())) {
                f.bookFlat("NA", f.getFlatId()); 
                return true;
            }
        }
        return false;
    }

    //adds an officer to the project, and also checks the nric(whether its valid or not)
    //return true if adding the officer is successful
    public boolean addOfficer(String officerId) 
    {
        if (isValidNric(officerId) && assignedOfficers.size() < MAX_OFFICERS 
            && !assignedOfficers.contains(officerId)) {
            assignedOfficers.add(officerId);
            return true;
        }
        return false;
    }

    private boolean isValidNric(String nric) 
    {
        return nric != null && nric.matches("^[ST]\\d{7}[A-Z]$"); //nric format
    }

    //getters
    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public boolean isVisible() { return isVisible; }
    public List<Flat> getFlats() { return flats; } // new getter for real flat
    public List<String> getAssignedOfficers() { return assignedOfficers; }
    public int getAvailableOfficerSlots() { return MAX_OFFICERS - assignedOfficers.size(); }
    public String getManagerInCharge() { return managerInCharge; }
    public LocalDate getStartDate() { return startDate; } // new
    public LocalDate getEndDate() { return endDate; }     // new

    //setters
     
    //
    public void setManagerInCharge(String managerId) 
    {
        if (this.managerInCharge != null && !this.managerInCharge.isEmpty()) {
            System.out.println("Project already has a manager in charge.");
            return;
        }
        this.managerInCharge = managerId;
    }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; } // new
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }         // new
}

