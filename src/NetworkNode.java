import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.*;

/**
 * Created by 585728 on 6/28/2016.
 */

/*
 * NetworkNode class that uses BluetoothManager
 */

public class NetworkNode implements Node {
	// Variables
	public String nodeID = null;
	public Random rand = new Random();
	public HashMap<String, Integer> myResources = new HashMap<String, Integer>();
	
	//Encryption Set
	public ArrayList<PublicKey> publicKeySet = new ArrayList<PublicKey>();
	public KeyPairGenerator keyGen = null;
	public SecureRandom random = null;
	public Signature dsa = null;
	public KeyPair pair = null;
	public PrivateKey privKey = null;
	public PublicKey pubKey = null;
	public byte[] byteArray = new byte[1024];
	
	//Blockchain
	public Blockchain blockChain;
	public ArrayList<String> blockRequestIDs = new ArrayList<String>();
	
	//keep track of messages
	public ArrayList<Message> openRequests = new ArrayList<Message>();
	public ArrayList<Message> bidsToMyRequests = new ArrayList<Message>();
	public ArrayList<Message> myResourceAgreements = new ArrayList<Message>();
	public ArrayList<Message> myResourceSents = new ArrayList<Message>();
	public ArrayList<Message> myResourceReceives = new ArrayList<Message>();
	public ArrayList<TextMessage> myTextMsgs = new ArrayList<TextMessage>();
	public ArrayList<String> allMsgIDs = new ArrayList<String>();
	public HashMap<String, Message> msgMap = new HashMap<String, Message>();
	
	public BluetoothManager bm;
	
	NetworkNode(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		nodeID = id;
	}
	public void distributeMessage(Message text) {
		try {
			bm.broadcast(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void distributeBlock(Block b) {
		try {
			bm.broadcast(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public void start() {
		this.bm = new BluetoothManager(this);
		try {
			bm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void makeBlockRequest(String hash) {
		BlockRequest br = new BlockRequest(hash);
		try {
			bm.broadcast(br);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void broadcastBlock(Block b) {
		try {
			bm.broadcast(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public HashMap<String, Integer> getResources() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getNodeID() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Message> getOpenRequests() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Message> getBidsToMyRequests() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void addResource(String type, int amount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setBlockChainDifficulty(int difficulty) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void receiveBlock(Block b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void requestBlock(String hash) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void addBlock(Block b) {
		// TODO Auto-generated method stub
		
	}
}
