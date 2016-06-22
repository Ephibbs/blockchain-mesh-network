import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;

/*
 * Handler for Main class, runs the simulation
 */

public class MainHandler {
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
		Main main = new Main();
		//main.run();
		main.runWithBlockChain();
	}
}
