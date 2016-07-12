
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.bluetooth.*;
import javax.bluetooth.UUID;
import javax.microedition.io.*;
import java.util.*;

/*
 * This class connects with node servers around it and sends text to them
 */
public class WifiClient implements Runnable {

    private ArrayList<String> hostNames = new ArrayList<String>();
    private int portNumber = 9001;
    private int maxNumAttempts;
    private ArrayList<String> delQ;

    private ArrayList<String> outQ = new ArrayList<String>();
    private Thread t;
    
    WifiClient() {
       	/*
    	 * make sure only the line with your name at the end of it has "//" at the beginning of it
    	 */
    	//hostNames.add(""); //Colby
    	//hostNames.add(""); //Natalie
    	hostNames.add("BAH5CG621142N"); //Andrew
    	//hostNames.add(""); //Dylan
    	//hostNames.add("BAHCND6206GP1"); //Evan
    	//hostNames.add(""); //Will
    	/*
    	 * END
    	 */
    	maxNumAttempts = 3;
    }
    
    public void addToOutQ(String s) throws IOException {
    	outQ.add(s);
    }
    public void broadcast(String s) {
    	for(String hostName : hostNames) {
    		try {
				send(hostName, s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    public void start() {
    	t = new Thread(this, "bluetooth client");
		t.start();
    }
    public void run() {
    	System.out.println("running client");
    	while(true) {
    		delQ = new ArrayList<String>();
    		if(!outQ.isEmpty()) {
				for(String s : outQ) {
					broadcast(s);
					delQ.add(s);
				}
				for(String s : delQ) {
					outQ.remove(s);
				}
    		}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void send(String hostName, String s) throws IOException {
    	for(int numAttempts = 0; numAttempts < maxNumAttempts; numAttempts++) {
    		System.out.println("attempt");
    		
    		try (
		        Socket echoSocket = new Socket(hostName, portNumber);
		        PrintWriter out =
		            new PrintWriter(echoSocket.getOutputStream(), true);
		        BufferedReader in =
		            new BufferedReader(
		                new InputStreamReader(echoSocket.getInputStream()));
		        BufferedReader stdIn =
		            new BufferedReader(
		                new InputStreamReader(System.in))
    		) {
    			out.println(s);
		    } catch (UnknownHostException e) {
		        System.err.println("Don't know about host " + hostName);
		        //System.exit(1);
		    } catch (IOException e) {
		        System.err.println("Couldn't get I/O for the connection to " +
		            hostName);
		        //System.exit(1);
		    } 
    	}
    }
}
