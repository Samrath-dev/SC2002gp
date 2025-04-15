import java.util.Scanner;

public abstract class User implements userInterface, AuthenticationInterface , FilterInterface, EncryptionInterface
{
    protected String nric;
    protected String password;
    protected int age;
    protected String maritalStatus;
    protected String name;
   
    public User(String nric, String password, int age, String maritalStatus,String name) 
    {   
        this.name = name;
        this.nric = nric;
        this.password = EncryptionUtil.hashPassword(password); 
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    
    public boolean login(String nric, String password) 
    {
        if (this.nric.equals(nric) && EncryptionUtil.verifyPassword(this.password, password))
        {
            System.out.println("Logged in Successfully");
            return true;
        }
        System.out.println("Invalid credentials.");
        return false;
    }
    public String getName() 
    {
        return name;
    }
  
    public void changePassword(String oldPassword, String newPassword)
    {
        if (EncryptionUtil.verifyPassword(this.password, oldPassword))
        {
            this.password = EncryptionUtil.hashPassword(newPassword);
            System.out.println("Password changed successfully.");
        } 
        else 
        {
            System.out.println("Incorrect old password.");
        }
    }
    
    
    public void logout() 
    {
        System.out.println("User logged out.");
    }
    
    // I have now implemented permissions and filtering in all 
    public abstract void filterProjects(String location, String flatType);
    public abstract void viewProjects();
    
}