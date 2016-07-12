
import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WifiServer implements Runnable {
    
    private boolean isExit = false;
    
    private Thread t;
    private WifiManager wm;
    private int portNumber = 9001;
    private boolean running = false;
    
    WifiServer(WifiManager wm) {
    	this.wm = wm;
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
		}
    }
    
    public void startServer() throws IOException, ClassNotFoundException {
    	running = true;
    	System.out.println("running server...");
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
            while (running) {
            	line = in.readLine();
                wm.addReceived(line);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}

