
// Samrath 
/* First obfuscates password with a particular seed then wraps the whole thing with a preset shift then finally reverses it
 * 100% deterministic 
 */

public class EncryptionUtil 
{
    private static final int SEED = 42;
    private static final int SHIFT = 7; 
    public static String hashPassword(String password) 
    {
        StringBuilder hashed = new StringBuilder();
        for (int i = 0; i < password.length(); i++) 
        {
            char c = password.charAt(i);
            int val = (c * SEED + i) % 95 + 32;  
            val = ((val - 32 + SHIFT) % 95) + 32;  
            hashed.append((char) val);
        }
        return hashed.reverse().toString(); 
    }

    public static boolean verifyPassword(String hashedPassword, String password) 
    {
        return hashedPassword.equals(hashPassword(password));
    }
}