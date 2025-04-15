public interface EncryptionInterface
 {
    String hashPassword(String password);
    boolean verifyPassword(String hashedPassword, String inputPassword);
}