

public interface BTOProjectInterface 
{
    void createProject(String name, String location, String[] flatTypes, int availableFlats);
    void editProject(String projectId, String newName, String newLocation);
    void deleteProject(String projectId);
    void toggleVisibility(String projectId, boolean isVisible);

}