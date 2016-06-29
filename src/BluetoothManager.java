import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.*;

public class BluetoothManager {
	BluetoothClient client;
	BluetoothServer server;
	NetworkNode node;
	
	BluetoothManager(NetworkNode node) {
		client = new BluetoothClient();
		server = new BluetoothServer();
		this.node = node;
	}
	
	void start() throws IOException {
		server.init();
		server.start();
	}
	
	boolean broadcastBlock(Block b) throws BluetoothStateException {
		
	}
	boolean broadcastMessage(Message m) {
		
	}
}
