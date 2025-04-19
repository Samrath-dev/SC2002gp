public interface FlatInterface 
{
    boolean checkAvailability(String flatId);
    boolean bookFlat(String applicantId, String flatId);
    void updateFlatAvailability(String flatId, int newAvailability);
    boolean bookFlat(String applicantId, String flatId, boolean suppressMessage); 
}