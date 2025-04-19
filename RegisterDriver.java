// This handles new users. Goes back to main screen after registered.
import java.util.InputMismatchException;
import java.util.Scanner;

public class RegisterDriver {
    public static void registerUser() {
        Scanner sc = new Scanner(System.in);
    try {
        System.out.print("Enter NRIC: ");
        String nric = sc.nextLine();
       
        if (!nric.matches("^[ST]\\d{7}[A-Z]$")) 
        {
            System.out.println("Invalid NRIC format. Registration failed.");
            return;
        }
        if (DataStore.getUserByNric(nric) != null) 
        {
            System.out.println("NRIC already registered. Registration failed.");
            return;
        }
        String password = "password";
        System.out.print("Enter age: ");
        int age = sc.nextInt(); sc.nextLine();
        System.out.print("Enter marital status: ");
        String maritalStatus = sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.println("Register as:");
        System.out.println("1. Applicant\n2. HDB Officer\n3. HDB Manager");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                Applicant a = new Applicant(nric, password, age, maritalStatus, name);
                DataStore.registerUser(a);
                System.out.println("Applicant registered.");
                break;
            case 2:
                HDBOfficer o = new HDBOfficer(nric, password, age, maritalStatus, name);
                DataStore.registerUser(o);
                System.out.println("HDB Officer registered.");
                break;
            case 3:
                HDBManager m = new HDBManager(nric, password, age, maritalStatus, name);
                DataStore.registerUser(m);
                System.out.println("HDB Manager registered.");
                break;
            default:
                System.out.println("Invalid option.");
        }

    } 
    catch (NumberFormatException e) 
    {
        System.out.println("Invalid numeric input. Registration failed.");
    } 
    catch (InputMismatchException e) 
        {
            System.out.println("Invalid input type. Registration failed.");
            sc.nextLine(); 
        }
    catch (Exception e) 
    {
        System.out.println("Unexpected error: " + e.getMessage());
    }
    }
}


