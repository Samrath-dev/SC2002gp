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
    // this new constructor stops csv loaded people from rehashing
    protected User(String nric, String hashedPassword, int age, String maritalStatus, String name, boolean isHashed) 
    {
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.name = name;
        this.password = isHashed ? hashedPassword : hashPassword(hashedPassword);
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
    //new password change option
    public void changePassword(String oldPw, String newPw) 
    {
        if (verifyPassword(this.password, oldPw)) 
        {
            this.password = hashPassword(newPw);
            System.out.println("Password changed successfully.");
        } 
        else 
        {
            System.out.println("Incorrect old password.");
        }
    }
    //getter?
    public String getNric()
     {
        return nric;
     }
     public String getMaritalStatus()
     {
        return maritalStatus;
     }
    
    public void logout() 
    {
        System.out.println("User logged out.");
    }
    public int getAge()
    {
        return age;
    }
    
    
    // I have now implemented permissions and filtering in all 
    public abstract void filterProjects(String location, String flatType);
    public abstract void viewProjects();
    
}