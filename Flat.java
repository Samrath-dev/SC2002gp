public class Flat implements FlatInterface 
{
    private String flatId;
    private boolean availability;

    public Flat(String flatId) 
    {
        this.flatId = flatId;
        this.availability = true;
    }
    private String flatType;

   public Flat(String flatId, String flatType) 
    {
        this.flatId = flatId;
        this.flatType = flatType;
        this.availability = true;
    }
    public String getFlatType() 
    {
    return flatType;
    }
    public String getFlatId() 
    {
        return flatId;
    }
    @Override
    public boolean checkAvailability(String flatId) {
        return this.flatId.equals(flatId) && availability;
    }

    @Override
    public boolean bookFlat(String applicantId, String flatId, boolean suppressMessage) 
    {
        if (this.flatId.equals(flatId) && availability) 
        {
            availability = false;
            if (suppressMessage) 
            {
                System.out.println("Book successfully.");
            }
            return true;
        } 
        else 
        {
            if (suppressMessage) {
                System.out.println("It has been booked.");
            }
            return false;
        }
    }
    @Override // this is wrapping for backward compatibility
    public boolean bookFlat(String applicantId, String flatId) 
    {
    return bookFlat(applicantId, flatId, false);
    }
    
    @Override
    public void updateFlatAvailability(String flatId, int newAvailability) {
        if (this.flatId.equals(flatId)) {
            this.availability = newAvailability > 0;
            System.out.println("Flat availability updated.");
        }
    }
}
