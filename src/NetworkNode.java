import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by 585728 on 6/28/2016.
 */

/*
 * NetworkNode class that uses BluetoothManager
 */

public class NetworkNode extends Node {
	NetworkNode(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		super(id);
	}
}