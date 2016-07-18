
import javax.bluetooth.*;
import javax.bluetooth.UUID;
import javax.microedition.io.*;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.*;


public class WifiManager {
	private WifiClient client;
	private WifiServer server;
	private Node node;
	private boolean verbose = true;
	
	WifiManager(Node node) {
		this.node = node;
		client = new WifiClient(verbose);
		server = new WifiServer(this, verbose);
	}
	
	WifiManager() {
		client = new WifiClient(verbose);
		server = new WifiServer(this,verbose);
	}
	
	void start() throws IOException {
		server.start();
		client.start();
	}
	
	void broadcast(Serializable b) throws IOException {
		if(verbose) System.out.println("added to Q");
		String s = toString(b);
		client.addToOutQ(s);
	}
	
	void send(String hostName, String s) throws IOException {
		client.send(hostName, s);
	}
	
	private Object fromString( String s ) throws IOException , ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
	}

    /** Write the object to a Base64 string. */
    private String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
    
    public void addReceived(String s) throws ClassNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
    	if(verbose) System.out.print("Received string: ");
    	if(verbose) System.out.println(s);
    	Sendable o = (Sendable) fromString(s);
    	System.out.println(o.getType());
    	if(o != null && o.getType().equals("Block")) {
    		if(verbose) System.out.print("received Block");
    		node.addBlock((Block) o);
    	} else if(o != null && o.getType().equals("Message")) {
    		if(verbose) System.out.print("received Message");
    		node.addMessage((Message) o);
    	}
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, ClassNotFoundException {
    	WifiManager wm = new WifiManager();
    	wm.start();
    	ResourceRequest b = new ResourceRequest(0, "ehy", "ya", "asdf");
    	wm.broadcast(b);
    }
	    
}
