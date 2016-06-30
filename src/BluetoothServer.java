import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.ArrayList;

public class BluetoothServer implements Runnable {
    
    private static final String SERVICE_NAME        = "NetworkNode";
    
    /*
     * setting this node's service id
     */
    //private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC1"; //Colby
    //private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC2"; //Natalie
    //private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC3"; //Andrew
    //private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC4"; //Dylan
    private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC5"; //Evan
    //private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC6"; //Will
    /*
     * END
     **/
    
    private static final UUID SERVICE_UUID          = new UUID(SERVICE_UUID_STRING, false);
    
    private ArrayList<String> receivedLines = new ArrayList<String>();
    
    private LocalDevice localDevice;
    
    private boolean isExit = false;
    
    private Thread t;
    private BluetoothManager bm;
    
    BluetoothServer(BluetoothManager bm) {
    	this.bm = bm;
    }
    
    public void start() throws BluetoothStateException {
    	localDevice = LocalDevice.getLocalDevice();
		t = new Thread(this, "puzzleSolver");
		t.start();
	}

   /*
    * This is the old method for echoing back messages to clients - for reference
    */
//    public void start() throws BluetoothStateException, IOException {
//        String connUrl = "btspp://localhost:" + SERVICE_UUID_STRING + ";" + "name=" + SERVICE_NAME;
//        StreamConnectionNotifier scn = (StreamConnectionNotifier) Connector.open(connUrl);
//
//        System.out.println("Ready to accept connections");
//
//        while (true) {
//            StreamConnection sc = scn.acceptAndOpen();
//
//            RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(sc);
//            String remoteAddress = remoteDevice.getBluetoothAddress();
//
//            System.out.println("Connection from " + remoteAddress);
//
//            String remoteName = "NetworkNode";
//            try {
//                remoteName = remoteDevice.getFriendlyName(false);
//            }
//            catch (IOException e) {
//                System.err.println("Unable to get remote device name");
//            }
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(sc.openDataInputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(sc.openDataOutputStream()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(remoteName + " (" + remoteAddress + "): " + line);
//                writer.println(line);
//                writer.flush();
//            }
//
//            sc.close();
//
//            System.out.println("Connection from " + remoteAddress + " closed");
//        }
//    }
    
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

        BufferedReader reader = new BufferedReader(new InputStreamReader(sc.openDataInputStream()));
        String line;
        //PrintWriter writer = new PrintWriter(new OutputStreamWriter(sc.openDataOutputStream()));
        
        while (!isExit) {

//            RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(sc);
//            String remoteAddress = remoteDevice.getBluetoothAddress();
//
//            System.out.println("Connection from " + remoteAddress);
//
//            String remoteName = "NetworkNode";
//            try {
//                remoteName = remoteDevice.getFriendlyName(false);
//            }
//            catch (IOException e) {
//                System.err.println("Unable to get remote device name");
//            }

            while ((line = reader.readLine()) != null) {
            	bm.addReceived(line);
            }
        }
        sc.close();
    }
}

