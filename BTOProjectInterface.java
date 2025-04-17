import java.util.Map;

public interface BTOProjectInterface 
{
    void createProject(String name, String location, String[] flatTypes, int availableFlats);
    void editProject(String projectId, String newName, String newLocation);
    void deleteProject(String projectId);
    void toggleVisibility(String projectId, boolean isVisible);
    void createProject(String name, String location, Map<String, Integer> flatTypeMap);
}