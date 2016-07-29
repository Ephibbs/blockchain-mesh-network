
import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;

public class WifiServer implements Runnable {
    
    private boolean isExit = false;
    
    private Thread t;
    private WifiManager wm;
    private int portNumber = 9001;
    private boolean running = false;
    private boolean verbose = false;
    
    WifiServer(WifiManager wm, boolean verbose) {
    	this.wm = wm;
    	this.verbose = verbose;
    }
    
    public void start() throws BluetoothStateException {
		t = new Thread(this, "wifi server");
		t.start();
	}
    
    public void run() {
        try {
			startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void startServer() throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
    	running = true;
    	if(verbose) System.out.println("running server...");
    	while (running) {
	    	try (
	            ServerSocket serverSocket =
	                new ServerSocket(portNumber);
	            Socket clientSocket = serverSocket.accept();     
	            PrintWriter out =
	                new PrintWriter(clientSocket.getOutputStream(), true);                   
	            BufferedReader in = new BufferedReader(
	                new InputStreamReader(clientSocket.getInputStream()));
	        ) {
	            String line;
	        	if((line = in.readLine()) != null) wm.addReceived(line);
	        	Thread.sleep(500);
	        } catch (IOException | InterruptedException e) {
	            System.out.println("Exception caught when trying to listen on port "
	                + portNumber + " or listening for a connection");
	            System.out.println(e.getMessage());
	        }
    	}
    	if(verbose) System.out.println("shutting down server...");
    }
}

