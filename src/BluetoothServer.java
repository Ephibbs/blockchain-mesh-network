import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.ArrayList;

public class BluetoothServer implements Runnable {
    
    private static final String SERVICE_NAME        = "NetworkNode";
    
    /*
     * setting this node's service id
     */
    private String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC1"; //Colby
    //private String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC9"; //Natalie
    //private String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC3"; //Andrew
    //private String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC4"; //Dylan
    //private String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC5"; //Evan
    //private String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC6"; //Will
    /*
     * END
     **/
    
    private UUID SERVICE_UUID          = new UUID(SERVICE_UUID_STRING, false);
    
    private LocalDevice localDevice;
    
    private boolean isExit = false;
    
    private Thread t;
    private BluetoothManager bm;
    
    BluetoothServer(BluetoothManager bm) {
    	this.bm = bm;
    }
    
    public void start() throws BluetoothStateException {
    	localDevice = LocalDevice.getLocalDevice();
		t = new Thread(this, "bluetooth server");
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
    	String connUrl = "btspp://localhost:" + SERVICE_UUID_STRING + ";" + "name=" + SERVICE_NAME;
        StreamConnectionNotifier scn = (StreamConnectionNotifier) Connector.open(connUrl);

        System.out.println("Ready to accept connections");
        StreamConnection sc = scn.acceptAndOpen();
        
        while (!isExit) {
        	
        	RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(sc);
        	String remoteAddress = remoteDevice.getBluetoothAddress();
        	
        	System.out.println("Connection from " + remoteAddress);
        	
        	String remoteName = "NetworkNode";
        	try {
                remoteName = remoteDevice.getFriendlyName(false);
            }
            catch (IOException e) {
                System.err.println("Unable to get remote device name");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(sc.openDataInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(sc.openDataOutputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
            	System.out.println(line);
            	bm.addReceived(line);
            }
        	sc = scn.acceptAndOpen();
        }
        sc.close();
    }
}

