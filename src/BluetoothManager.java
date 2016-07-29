
import javax.bluetooth.*;
import javax.bluetooth.UUID;
import javax.microedition.io.*;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.*;


public class BluetoothManager {
	private BluetoothClient client;
	private BluetoothServer server;
	private Node node;
	private boolean verbose = true;

	BluetoothManager(Node node) {
		this.node = node;
		client = new BluetoothClient();
		server = new BluetoothServer(this);
	}
	
	BluetoothManager() {
		client = new BluetoothClient();
		server = new BluetoothServer(this);
	}
	
	void start() throws IOException {
		server.start();
		client.start();
	}
	
	void broadcast(Message m) throws IOException {
		if(verbose) System.out.println("added to Q");
		client.addToOutQ(m);
	}

    /** Write the object to a Base64 string. */
    
    public void addReceived(Message s) throws ClassNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
    	if(verbose) System.out.println("BluetoothManager has received message with id: "+s.getID());
    	if(s != null && s.getType().equals("Message")) {
    		node.addMessage(s);
    	} else {
    		System.err.println("Uh Oh: received something else...");
    	}
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, ClassNotFoundException {
    	BluetoothManager bm = new BluetoothManager();
    	bm.start();
    	BlockRequest b = new BlockRequest("1","e");
    	bm.broadcast(b);
    }
	        
}
