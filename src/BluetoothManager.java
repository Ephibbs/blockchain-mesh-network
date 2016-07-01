import javax.bluetooth.*;
import javax.bluetooth.UUID;
import javax.microedition.io.*;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;


public class BluetoothManager {
	static BluetoothClient client;
	static BluetoothServer server;
	Node node;
	
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
		client.init();
	}
	
	void broadcast(Serializable b) throws IOException {
		String s = toString(b);
		client.broadcast(s);
	}
	
	static void send(String s) throws IOException {
		client.send(new UUID("5F6C6A6E1CFA49B49C831E0D1C9B9DC2", false), s);
	}
	
	private static Object fromString( String s ) throws IOException , ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
	}

    /** Write the object to a Base64 string. */
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }
    
    public void addReceived(String s) throws ClassNotFoundException, IOException {
    	System.out.print("Received string: ");
    	System.out.println(s);
    	Sendable o = (Sendable) fromString(s);
    	if(o != null && o.getType().equals("Block")) {
    		System.out.print("received Block");
    		//node.addBlock((Block) o);
    	} else if(o != null && o.getType().equals("Message")) {
    		System.out.print("received Message");
    		//node.addMessage((Message) o);
    	}
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, ClassNotFoundException {
    	BluetoothManager bm = new BluetoothManager();
    	bm.start();
    	//bm.broadcast("Hey there");
    	//TextMessage b = new TextMessage("Hi", new Node("Evan"));
    	Block b = new Block();
    	System.out.println(b);
    	bm.broadcast(b);
    }
	    
}
