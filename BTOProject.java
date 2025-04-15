import java.util.List;  //for method return types
import java.util.ArrayList;

//BTOProject class//
public class BTOProject implements BTOProjectInterface {
    private String projectId;
    private String name;
    private String location;
    private boolean isVisible;
    private List<FlatType> flatTypes;
    private static int projectCounter = 0;
    private List<String> assignedOfficers;
    private static final int MAX_OFFICERS = 10;
    private String managerInCharge;

     //represents a flat type with available quantity
    public static class FlatType {
        private String type;
        private int quantity;

        public FlatType(String type, int quantity) {
            this.type = type;
            this.quantity = quantity;
        }

        public String getType() { return type; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public BTOProject() {
        this.projectId = "PROJ_" + (++projectCounter);
        this.flatTypes = new ArrayList<>();
        this.assignedOfficers = new ArrayList<>();
        this.isVisible = true;  // added to ensure default visibility
    }

 // Interface method implementations
    @Override
    public void createProject(String name, String location, String[] flatTypes, int availableFlats) {
        this.name = name;
        this.location = location;
        this.isVisible = true;
        
        for (String type : flatTypes) {
            this.flatTypes.add(new FlatType(type, availableFlats));
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

   
     //books a flat of specified type,
     //parameter type->Flat type (2-Room/3-Room),
     //return true if booking is successful
    public boolean bookFlat(String flatType) {
        for (FlatType type : flatTypes) {
            if (type.getType().equals(flatType) && type.getQuantity() > 0) {
                type.setQuantity(type.getQuantity() - 1);
                return true;
            }
        }
        return false;
    }

     //adds an officer to the project, and also checks the nric(whether its valid or not)
     //return true if adding the officer is successful
    public boolean addOfficer(String officerId) {
        if (isValidNric(officerId) && assignedOfficers.size() < MAX_OFFICERS 
            && !assignedOfficers.contains(officerId)) {
            assignedOfficers.add(officerId);
            return true;
        }
        return false;
    }

    private boolean isValidNric(String nric) {
        return nric != null && nric.matches("^[ST]\\d{7}[A-Z]$"); //nric format
    }


    //getters
    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public boolean isVisible() { return isVisible; }
    public List<FlatType> getFlatTypes() { return flatTypes; }
    public List<String> getAssignedOfficers() { return assignedOfficers; }
    public int getAvailableOfficerSlots() { return MAX_OFFICERS - assignedOfficers.size(); }
    public String getManagerInCharge() { return managerInCharge; }

    //setters
    public void setManagerInCharge(String managerId) { 
        this.managerInCharge = managerId; 
    }
}
