import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;

/**
 * Created by 585728 on 6/28/2016.
 */

/*
 * NetworkNode class that uses BluetoothManager
 */

public class NetworkNode extends Node {
	public BluetoothManager bm;
	
	NetworkNode(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		super(id);
	}
	public void distributeMessage(Message text) {
		try {
			bm.broadcast(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void distributeBlock(Block b) {
		try {
			bm.broadcast(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void start() {
		super.start();
		this.bm = new BluetoothManager(this);
		try {
			bm.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Message> getOpenRequests() {
		 ArrayList<Message> list = super.blockChain.getMessages();
		 
		 ArrayList<Message> openRequests = new ArrayList<Message>();
		 HashMap<String, Message> msgs = new HashMap<String, Message>();
		 
		 for(Message m : list) {
			 if(m.getMessageType() == "ResourceRequest") {
				 openRequests.add(m);
				 //msgs.put(m.getMessageData().getMessageID(), m);
			 } else if(m.getMessageType() == "ResourceAgreement") {
				 //openRequests.remove(msgs.get(m.getMessageData().getMessageID()));
			 }
		 }
		 return openRequests;
	}
}
