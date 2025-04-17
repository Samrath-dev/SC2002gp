// this handles login and goes forward
import java.util.Scanner;

public class LoginDriver 
{
    public static User login() 
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter NRIC: ");
        String nric = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        User user = DataStore.getUserByNric(nric);
        if (user != null && user.login(nric, password)) 
        {
            return user;
        }
        System.out.println("Login failed.");
        return null;
    }
}
