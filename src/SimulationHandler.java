import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;

/*
 * Handler for Simulation class, runs the simulation
 */

public class SimulationHandler {
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
		Simulation sim = new Simulation();
		//sim.run();
		sim.runWithBlockChain();
	}
}
