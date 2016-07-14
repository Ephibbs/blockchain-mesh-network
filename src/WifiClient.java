
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    private int maxNumAttempts = 1;

    private ArrayList<String> outQ = new ArrayList<String>();
    private Thread t;
    private boolean verbose = false;
    
    WifiClient(boolean verbose) {
       	/*
    	 * make sure only the line with your name at the end of it has "//" at the beginning of it
    	 */
    	//hostNames.add("");
    	//hostNames.add("BAH5CG621140Y"); //Colby
    	//hostNames.add("BAH5CG621142S"); //Natalie
    	//hostNames.add("BAH5CG621142N"); //Andrew
    	//hostNames.add("BAH5CG62113G8"); //Dylan
    	//hostNames.add("BAHCND6206GP1"); //Evan
    	//hostNames.add("BAH5CG62113Z0"); //Will
    	/*
    	 * END
    	 */
		ArrayList<String> names = new ArrayList<String>();
		names.add("Colby");
		names.add("Natalie");
		names.add("Andrew");
		names.add("Dylan");
		names.add("Evan");
		names.add("Will");
		names.add("Jerry");
		
		
		String myName="";
		try {
			for (String line : Files.readAllLines(Paths.get("../../SPECS.txt"))) {
			    if(names.contains(line)) {
			    	myName = line;
			    } else {
			    	String[] lines = line.split(":");
			    	if(!lines[0].substring(1).equals(myName) && lines[0].substring(0, 1).equals("*")) {
			    		hostNames.add(lines[1]);
			    	}
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(hostNames.isEmpty()) {
			hostNames.add("");
		}
		System.out.println(myName);
		System.out.println(hostNames);
    	this.verbose = verbose;
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
    	t = new Thread(this, "wifi client");
		t.start();
    }
    public void run() {
    	String s;
    	if(verbose) System.out.println("running client");
    	while(true) {
    		if(!outQ.isEmpty()) {
				for(int i=outQ.size()-1;i>-1;i--) {
					s = outQ.get(i);
					broadcast(s);
					if(verbose) System.out.println("sent & removed");
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
    	if(verbose) System.out.println("start sending...");
    	for(int numAttempts = 0; numAttempts < maxNumAttempts; numAttempts++) {
    		if(verbose) System.out.println("attempt");
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
    			break;
		    } catch (UnknownHostException e) {
		        System.err.println("Don't know about host " + hostName);
		        //System.exit(1);
		    } catch (IOException e) {
		        System.err.println("Couldn't get I/O for the connection to " +
		            hostName);
		        //System.exit(1);
		    } 
    		try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}

