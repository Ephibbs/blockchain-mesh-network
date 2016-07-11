import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.sql.Time;
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

	// Encryption Set
	public ArrayList<PublicKey> publicKeySet = new ArrayList<PublicKey>();
	public KeyPairGenerator keyGen = null;
	public SecureRandom random = null;
	public Signature dsa = null;
	public KeyPair pair = null;
	public PrivateKey privKey = null;
	public PublicKey pubKey = null;
	public byte[] byteArray = new byte[1024];

	// Blockchain
	public Blockchain blockChain;
	public ArrayList<String> blockRequestIDs = new ArrayList<String>();

	// keep track of messages
	public ArrayList<Message> openRequests = new ArrayList<Message>();
	public ArrayList<Message> bidsToMyRequests = new ArrayList<Message>();
	public ArrayList<Message> myResourceAgreements = new ArrayList<Message>();
	public ArrayList<Message> myResourceSents = new ArrayList<Message>();
	public ArrayList<Message> myResourceReceives = new ArrayList<Message>();
	public ArrayList<TextMessage> myTextMsgs = new ArrayList<TextMessage>();
	public ArrayList<String> allMsgIDs = new ArrayList<String>();
	public HashMap<String, Message> msgMap = new HashMap<String, Message>();

	public Location myLocation = new Location().createRandomLocation();

	// ping stuff
	public ArrayList<String> localConnections = new ArrayList<String>();
	public Hashtable<String, String> routeTable = new Hashtable<String, String>();
	public Hashtable<String, ArrayList<Ping>> pingHash = new Hashtable<String, ArrayList<Ping>>();
	public HashMap<String, Integer> countHash = new HashMap<String, Integer>();

	public ArrayList<NetworkNode> tempNodes = new ArrayList<NetworkNode>();

	public HashMap<String, NodeInfo> nodeInfoMap = new HashMap<String, NodeInfo>();

	public BluetoothManager bm;

	NetworkNode(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		nodeID = id;
		blockChain = new Blockchain(this);
		this.bm = new BluetoothManager(this);
		// this.myLocation = this.myLocation.createRandomLocation();
	}

	private Location generateRandomLocation() {
		// TODO Auto-generated method stub
		return null;
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
		blockChain.start();
		try {
			bm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void makeBlockRequest(String hash) {
		// BlockRequest br = new BlockRequest(hash, nodeID);
		// try {
		// bm.broadcast(br);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void broadcastBlock(Block b) {
		try {
			bm.broadcast(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public HashMap<String, Integer> getResources() {
		return myResources;
	}

	@Override
	public String getNodeID() {
		return nodeID;
	}

	@Override
	public ArrayList<Message> getOpenRequests() {
		return openRequests;
	}

	@Override
	public ArrayList<Message> getBidsToMyRequests() {
		return bidsToMyRequests;
	}

	@Override
	public ArrayList<Message> getMyResourceAgreements() {
		return myResourceAgreements;
	}

	@Override
	public ArrayList<Message> getMyResourceSents() {
		return myResourceSents;
	}

	@Override
	public ArrayList<Message> getMyResourceReceives() {
		return myResourceReceives;
	}

	@Override
	public void addResource(String type, int amount) {
		// using Integer because myResources.get may return null;
		Integer am = myResources.get(type);
		if (am != null) {
			myResources.put(type, amount + am);
		}
	}

	@Override
	public void addMessage(Message msg) {
		// if message is unique, add and
		// distribute
		// do nothing
		if (msg != null && !this.msgMap.containsKey(msg.getID())) {
			blockChain.add(msg);
			switch (msg.getMessageType()) {
			case "ResourceRequest":
				System.out.println("received resource request");
				openRequests.add(msg);
				break;
			case "ResourceRequestBid":
				System.out.println("received resource bid");
				bidsToMyRequests.add(msg);
				break;
			case "ResourceAgreement":
				System.out.println("received resource agreement");
				myResourceAgreements.add(msg);
				break;
			case "ResourceSent":
				System.out.println("received resource sent");
				myResourceSents.add(msg);
				break;
			case "ResourceReceived":
				System.out.println("received resource receives");
				myResourceReceives.add(msg);
				break;
			case "Ping":
				receivePing((Ping) msg);
				break;
			}
			msgMap.put(msg.getID(), msg);
			distributeMessage(msg);
		}
	}

	private void receivePing(Ping msg) {
		if (msg.getOriginator().equals(this.getNodeID())) {
			return;
		}
		if (this.pingHash.get(msg.getOriginator()) != null) {
			if (this.pingHash.get(msg.getOriginator()).contains(msg)) {
				return;
			}
			else if (this.pingHash.containsKey(msg.getOriginator())) {
				if (this.pingHash.get(msg.getOriginator()).contains(msg)) {
					return;
				} else {
					this.pingHash.get(msg.getOriginator()).add(msg);
				}
			}
		} 
//		else if (this.pingHash.containsKey(msg.getOriginator())) {
//			if (this.pingHash.get(msg.getOriginator()).contains(msg)) {
//			} else {
//				this.pingHash.get(msg.getOriginator()).add(msg);
//			}
//		} 
		else {
			ArrayList<Ping> newArrayList = new ArrayList<Ping>();
			newArrayList.add(msg);
			this.pingHash.put(msg.getOriginator(), newArrayList);
		}

		Set<String> nodeInfoKeys = nodeInfoMap.keySet();
		String pingOriginator = msg.getOriginator();
		ArrayList<String> nodeInfoKeyAL = new ArrayList<String>();
		for (String key : nodeInfoKeys) {
			nodeInfoKeyAL.add(key);
		}
		if (!nodeInfoKeyAL.contains(pingOriginator)) {
			Location newLocation = msg.getLocation();
			Time newTime = msg.getTimeSent();
			// System.out.println("newTime " + newTime.toString());
			System.out.println("I created a new Ping");
			NodeInfo newNodeInfo = new NodeInfo(pingOriginator, msg.getPublicKey(), msg.getLocation(),
					new ArrayList<Message>(), newTime);
			// System.out.println("newTIme from nodeinfo " +
			// newNodeInfo.getLastPingTime().toString());
			this.nodeInfoMap.put(pingOriginator, newNodeInfo);
		} else if (nodeInfoKeyAL.contains(pingOriginator)) {
			NodeInfo currentNodeInfo = nodeInfoMap.get(pingOriginator);
			currentNodeInfo.setLastPingTime(new Time(System.currentTimeMillis()));
			currentNodeInfo.setMyLocation(msg.getLocation());
		}
		updateRouteTable();
		sendToTempNodes(msg);
	}

	public void removeOutdatedPings() {
		Set<String> nodeInfoKeys = nodeInfoMap.keySet();
		ArrayList<String> nodeInfoKeyAL = new ArrayList<String>();
		long someValue = 1000000;
		for (String key : nodeInfoKeys) {
			nodeInfoKeyAL.add(key);
			// if(nodeInfoMap.get(key).getLatestPing() < (new
			// Time(System.currentTimeMillis()).getTime()-someValue)){
			// nodeInfoMap.remove(key);
			// }
		}
	}

	private void updateRouteTable() {
		Set<String> pingSet = pingHash.keySet();
		// System.out.println("My node is: " + this.getNodeID());
		for (String key : pingSet) {
			ArrayList<Ping> nodePings = pingHash.get(key);
			int min = 1000000;
			Node nodeToRouteTo = null;
			for (int i = 0; i < nodePings.size(); i++) {
				String originator = nodePings.get(i).getOriginator();
				String relayer = nodePings.get(i).getRelayer();
				// System.out.println("The relayer was: " + relayer);
				if (i == 0) {
					min = nodePings.get(i).getCount();
					this.routeTable.put(originator, relayer);
					this.countHash.put(originator, min);
					// System.out.println("I should have put something in: " +
					// relayer);
				} else if (nodePings.get(i).getCount() < min) {

					min = nodePings.get(i).getCount();
					if (this.countHash.get(originator) < min) {
						this.routeTable.remove(originator);
						this.routeTable.put(originator, relayer);
					}
				} else {
				}
			}
		}
		// System.out.println("before calling again");
	}

	public Hashtable<String, String> getRouteTable() {
		return this.routeTable;
	}

	@Override
	public void setBlockChainDifficulty(int difficulty) {
		blockChain.setDifficulty(difficulty);
	}

	@Override
	public void receiveBlock(Block b) {
	}

	@Override
	public void requestBlock(String hash) {
	}

	@Override
	public boolean isOnline() {
		return true;
	}

	@Override
	public void addBlock(Block b) {
		blockChain.add(b);
	}

	public void createPing() {
		Ping newPing = new Ping(this.getNodeID(), this.getNodeID(), this.pubKey);
		newPing.setLocation(new Location().createRandomLocation());
		newPing.setTime(new Time(System.currentTimeMillis()));
		sendToTempNodes(newPing);
	}

	public void sendToTempNodes(Ping newPing) {
		for (int i = 0; i < this.tempNodes.size(); i++) {
			newPing.setRelayer(this.getNodeID());
			this.tempNodes.get(i).addMessage(newPing);
		}
	}

	public Location getLocation() {
		return this.myLocation;
	}

	public void receiveConnection(String newConnectionID) {
		this.localConnections.add(newConnectionID);
	}

	public void addNode(NetworkNode newNode) {
		this.tempNodes.add(newNode);
	}

	public void drawNodes(Graphics g, int MAXSIZE, int width, int height) {
		g.setColor(Color.BLACK);
		g.fillOval(MAXSIZE + this.myLocation.getX(), this.myLocation.getY(), width, height);
	}

	public void drawTemps(Graphics g, int maxsize, int width, int height) {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.tempNodes.size(); i++) {
			this.tempNodes.get(i).drawNodes(g, maxsize, width, height);
		}
	}

	public HashMap<String, NodeInfo> getNodeInfoList() {
		return this.nodeInfoMap;
	}
}