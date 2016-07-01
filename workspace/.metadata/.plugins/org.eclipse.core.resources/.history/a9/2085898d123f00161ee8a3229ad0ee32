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
	
	void broadcast(String b) throws IOException {
		//String s = toString(b);
		client.broadcast(b);
	}
	
	static void send(String s) throws IOException {
		client.send(new UUID("5F6C6A6E1CFA49B49C831E0D1C9B9DC9", false), s);
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
    
    public void addReceived(String s) throws ClassNotFoundException, IOException {
    	System.out.print("Received string: ");
    	System.out.println(s);
    	Sendable o = (Sendable) fromString(s);
    	if(o != null && o.getType() == "Block") {
    		System.out.print("received Block");
    		//node.addBlock((Block) o);
    	} else if(o != null && o.getType() == "Message") {
    		System.out.print("received Message");
    		//node.addMessage((Message) o);
    	}
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
    	BluetoothManager bm = new BluetoothManager();
    	bm.start();
    	//bm.broadcast("Hey there");
    	//bm.send("Hey there");
    	//bm.send(new Block());
    }
	    
}
