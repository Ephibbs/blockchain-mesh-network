import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utils {
	static Random rand = new Random();
	public static boolean checkHash(Block b, int difficulty) {
		int diff = b.getDifficulty();
    	if (diff < 1) {
    		diff = 1;
        } else if (diff > 32) {
        	diff = 32;
        }
		
		String hash="";
		try {
			hash = sha256(b.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        //System.out.println(hash);
        
        // Verified?
        int i;
        for (i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) != '0') {
                break;
            }
        }
        if(i >= difficulty) {
        	return true;
        }
        	return false;
    }
	public static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }
	public static String getRandID() {
		return Integer.toString(rand.nextInt(1000000000));
	}
}
