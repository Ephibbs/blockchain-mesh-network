import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class Utils {
	static Random rand = new Random();
	static int LENGTH = 5;
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
		//return Integer.toString(rand.nextInt(1000000000));
		
		String messageNum = "";
		ArrayList<String> charPossibilities = new ArrayList<String>();
		charPossibilities.add("a");
		charPossibilities.add("b");
		charPossibilities.add("c");
		charPossibilities.add("d");
		charPossibilities.add("e");
		charPossibilities.add("f");
		charPossibilities.add("g");
		charPossibilities.add("h");
		charPossibilities.add("i");
		charPossibilities.add("j");
		charPossibilities.add("k");
		//charPossibilities.add("l");
		charPossibilities.add("m");
		charPossibilities.add("n");
		//charPossibilities.add("o");
		charPossibilities.add("p");
		charPossibilities.add("q");
		charPossibilities.add("r");
		charPossibilities.add("s");
		charPossibilities.add("t");
		charPossibilities.add("u");		
		charPossibilities.add("v");
		charPossibilities.add("w");
		charPossibilities.add("x");
		charPossibilities.add("y");
		charPossibilities.add("z");
		//charPossibilities.add("0");
		//charPossibilities.add("1");
		charPossibilities.add("2");
		charPossibilities.add("3");
		charPossibilities.add("4");
		charPossibilities.add("5");
		charPossibilities.add("6");
		charPossibilities.add("7");
		charPossibilities.add("8");
		charPossibilities.add("9");
		
		for(int i = 0; i < LENGTH;i++){
			int ranNum = rand.nextInt(charPossibilities.size());
			messageNum = messageNum + charPossibilities.get(ranNum);
		}
		return messageNum;
	}
}
