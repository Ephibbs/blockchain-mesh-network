import java.awt.Color;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
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
	public int TIMEING = 10;
	public int currentMessageCount = 0;

	// Blockchain
	public Blockchain blockChain;
	public ArrayList<String> blockRequestIDs = new ArrayList<String>();

	// keep track of messages
	public ArrayList<Message> totalMessages = new ArrayList<Message>();
	public ArrayList<Message> directMessages = new ArrayList<Message>();
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

	public WifiManager wm;

	NetworkNode(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		nodeID = id;
		blockChain = new Blockchain(this);
		this.wm = new WifiManager(this);
		// this.myLocation = this.myLocation.createRandomLocation();

		this.nodeID = id;
		this.keyGen = KeyPairGenerator.getInstance("DSA", "SUN"); // create key
		// generator
		// object
		this.random = SecureRandom.getInstance("SHA1PRNG", "SUN"); // random var
		// for
		// random
		// generation
		this.dsa = Signature.getInstance("SHA1withDSA", "SUN"); // create
		// signature
		// object
		keyGen.initialize(1024, random); // initialize and generate random key
		// pair
		this.pair = keyGen.generateKeyPair();
		this.privKey = pair.getPrivate();
		this.pubKey = pair.getPublic();
		this.blockChain = new Blockchain(this);
	}

	public void distributeMessage(Message text) {
		try {
			wm.broadcast(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void distributeBlock(Block b) {
		try {
			wm.broadcast(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		blockChain.start();
		try {
			wm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		makeBlockRequest("latest");
	}

	public void broadcastBlock(Block b) {
		try {
			wm.broadcast(b);
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
	public void addMessage(Message msg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		// TODO: later, implement signature checking mechanism for messages
		// if message is unique, add and
		// distribute
		// else do nothing

		if(msg.getMessageType().equals("MySignedObject")) {
			MySignedObject m = (MySignedObject) msg;
			if(verifyMessage((MySignedObject) msg)==false){
				return;
			}
		}

		if (!this.totalMessages.contains(msg)) {
			System.out.println(msg.getType());
			System.out.println(msg.getID());
			totalMessages.add(msg);
		}
		if (msg != null && !this.msgMap.containsKey(msg.getID())) {
			switch (msg.getMessageType()) {
			case "ResourceRequest":
				System.out.println("received resource request");
				openRequests.add(msg);
				blockChain.add(msg);
				distributeMessage(msg);
				break;
			case "ResourceRequestBid":
				System.out.println("received resource bid");
				bidsToMyRequests.add(msg);
				blockChain.add(msg);
				distributeMessage(msg);
				break;
			case "ResourceAgreement":
				System.out.println("received resource agreement");
				myResourceAgreements.add(msg);
				blockChain.add(msg);
				distributeMessage(msg);
				break;
			case "ResourceSent":
				System.out.println("received resource sent");
				myResourceSents.add(msg);
				updateNodeInfo((ResourceSent) msg);
				blockChain.add(msg);
				distributeMessage(msg);
				break;
			case "ResourceReceived":
				System.out.println("received resource receives");
				myResourceReceives.add(msg);
				updateNodeInfo((ResourceReceived) msg);
				blockChain.add(msg);
				distributeMessage(msg);
				break;
			case "Ping":
				receivePing((Ping) msg);
				distributeMessage(msg);
				break;
			case "DirectMessage":
				sendDirectMessage(msg);
				distributeMessage(msg);
				break;
			case "InitialPing":
				System.out.println("I received an initial ping");
				receivePing((InitialPing) msg);
				break;
			case "BlockRequest":
				BlockRequest br = (BlockRequest) msg;
				receiveBlockRequest(br);
				break;
			case "BlockDelivery":
				BlockDelivery bd = (BlockDelivery) msg;
				receiveBlockDelivery(bd);
				break;
			}
			msgMap.put(msg.getID(), msg);
			this.currentMessageCount++;
			if (this.currentMessageCount % this.TIMEING == 0) {
				this.createPingToBroadcast();
			}
		}
	}

	private boolean verifyMessage(MySignedObject msg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		// do nothing
		Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
		byte[] realSig = msg.getSig();
		byte[] byteArray2 = msg.getByteArray();
		for (int i = 0; i < publicKeySet.size(); i++) { // find public key that
			// can verify message
			sig.initVerify(publicKeySet.get(i));
			sig.update(byteArray2);
			boolean verifies = sig.verify(realSig);

			if (verifies == true) {
				return true;
			} else {
			}
		}
		return false;
	}

	public void addMessage(MySignedObject message) {
		// TODO: later, implement signature checking mechanism for messages
		// if message is unique, add and
		// distribute
		// else do nothing

		Message msg = convertToMessage(message);

		if (!this.totalMessages.contains(msg)) {
			System.out.println(msg.getType());
			System.out.println(msg.getID());
			totalMessages.add(msg);
		}
		if (msg != null && !this.msgMap.containsKey(msg.getID())) {
			switch (msg.getMessageType()) {
			case "ResourceRequest":
				break;
			case "ResourceRequestBid":
				break;
			case "ResourceAgreement":
				break;
			case "ResourceSent":
				break;
			case "ResourceReceived":
				break;
			case "Ping":
				break;
			case "DirectMessage":
				break;
			case "InitialPing":
				break;
			case "BlockRequest":
				break;
			case "BlockDelivery":
				break;
			}
			msgMap.put(msg.getID(), msg);
			this.currentMessageCount++;
			if (this.currentMessageCount % this.TIMEING == 0) {
				this.createPingToBroadcast();
			}
		}
	}

	// private Message convertToMessage( msg) {
	// byte[] byteArray = msg.getByteArray();
	// byte[] sig = msg.getSig();
	// //String Author = "";
	// String messageType = msg.getMessageType();
	// System.out.println("The message type is: " + messageType);
	//
	// return null;
	// }

	private Message convertToMessage(MySignedObject msg) {
		// TODO Auto-generated method stub
		// msg.get
		switch (msg.getMessageType()) {
		case "ResourceRequest":
			break;
		case "ResourceRequestBid":
			break;
		case "ResourceAgreement":
			break;
		case "ResourceSent":
			break;
		case "ResourceReceived":
			break;
		case "Ping":
			break;
		case "DirectMessage":
			break;
		case "InitialPing":
			break;
		case "BlockRequest":
			break;
		case "BlockDelivery":
			break;
		}
		msgMap.put(msg.getID(), msg);
		this.currentMessageCount++;
		if (this.currentMessageCount % this.TIMEING == 0) {
			this.createPingToBroadcast();
		}
		return null;
	}

	private void receivePing(InitialPing msg) {
		// System.out.println("Ping Author " + msg.getAuthor());
		if (msg.getOriginator().equals(this.getNodeID())) {
			// System.out.println("I didn't do anything");
			return;
		}
		// if (this.pingHash.get(msg.getOriginator()) != null) {
		// if (this.pingHash.get(msg.getOriginator()).contains(msg)) {
		// return;
		// } else if (this.pingHash.containsKey(msg.getOriginator())) {
		// if (this.pingHash.get(msg.getOriginator()).contains(msg)) {
		// return;
		// } else {
		// this.pingHash.get(msg.getOriginator()).add(msg);
		// }
		// }
		// } else {
		// // System.out.println("I should be creating a new arrayList of
		// // pings");
		// ArrayList<Ping> newArrayList = new ArrayList<Ping>();
		// newArrayList.add(msg);
		// this.pingHash.put(msg.getOriginator(), newArrayList);
		// }

		Set<String> nodeInfoKeys = nodeInfoMap.keySet();
		String pingOriginator = msg.getOriginator();
		ArrayList<String> nodeInfoKeyAL = new ArrayList<String>();
		for (String key : nodeInfoKeys) {
			nodeInfoKeyAL.add(key);
		}
		if (!nodeInfoKeyAL.contains(pingOriginator)) {
			System.out.println("I dont have a node so I am making a new one in initial ping");
			Location newLocation = msg.getLocation();
			Time newTime = msg.getTimeSent();
			// System.out.println("I should be making a new NodeInfo List");

			NodeInfo newNodeInfo = new NodeInfo(pingOriginator, msg.getPublicKey(), msg.getLocation(),
					msg.getCurrentResources(), newTime);
			System.out.println("I made the new node info in initial ping");
			this.nodeInfoMap.put(pingOriginator, newNodeInfo);
		} else if (nodeInfoKeyAL.contains(pingOriginator)) {
			// System.out.println("I already had this in here");
			NodeInfo currentNodeInfo = nodeInfoMap.get(pingOriginator);
			currentNodeInfo.setLastPingTime(new Time(System.currentTimeMillis()));
			currentNodeInfo.setMyLocation(msg.getLocation());
		}
		updateRouteTable();
		// sendToTempNodes(msg);
	}

	private ArrayList<Resource> convertToArrayList(HashMap<String, Integer> currentResources) {
		// TODO Auto-generated method stub
		Set<String> nodeInfoKeys = nodeInfoMap.keySet();
		// String pingOriginator = msg.getOriginator();
		// ArrayList<String> nodeInfoKeyAL = new ArrayList<String>();

		ArrayList<Resource> resourceArrayList = new ArrayList<Resource>();

		for (String key : nodeInfoKeys) {
			resourceArrayList.add(new Resource(currentResources.get(key), key, null));
		}

		return resourceArrayList;
	}

	private void updateNodeInfo(ResourceReceived msg) {
		System.out.println("I should be adding more resource received");
		String NodeID = msg.getAuthor();
		NodeInfo currentInfo = nodeInfoMap.get(NodeID);
		String resourceType = msg.getResourceType();
		int resourceAmt = msg.getAmount();
		updateResourceInfo(resourceType, 1 * resourceAmt, currentInfo);
	}

	private void updateResourceInfo(String resourceType, int resourceAmt, NodeInfo currentInfo) {
		System.out.println("I got here");
		for (int i = 0; i < currentInfo.getResourceList().size(); i++) {
			Resource cResource = currentInfo.getResourceList().get(i);
			// System.out.println("resourceType: " + resourceType);
			// System.out.println("cResource.getType(): " +
			// cResource.getType());
			if (cResource.getType().equals(resourceType)) {
				cResource.setAmount(cResource.getAmount() + resourceAmt);
				System.out.println("I was able to change a resource value by: " + resourceAmt);
			}
		}
	}

	private void updateNodeInfo(ResourceSent msg) {
		String NodeID = msg.getAuthor();
		NodeInfo currentInfo = nodeInfoMap.get(NodeID);
		String resourceType = msg.getType();
		int resourceAmt = msg.getAmount();
		updateResourceInfo(resourceType, -1 * resourceAmt, currentInfo);
	}

	private void sendDirectMessage(Message msg) {
		// this is not finished yet
		String recipient = msg.getRecipient();
		if (this.nodeID.equals(msg.getRecipient())) {
			this.directMessages.add(msg);
		} else {
			String nextReceiver = routeTable.get(recipient);
			// send to the receiver only not everybody. so dont do
			// distributeMessage
			// but it will be something similar
		}
	}

	private void receivePing(Ping msg) {
		// System.out.println("Ping Author " + msg.getAuthor());
		if (msg.getOriginator().equals(this.getNodeID())) {
			// System.out.println("I didn't do anything");
			return;
		}
		if (this.pingHash.get(msg.getOriginator()) != null) {
			if (this.pingHash.get(msg.getOriginator()).contains(msg)) {
				return;
			} else if (this.pingHash.containsKey(msg.getOriginator())) {
				if (this.pingHash.get(msg.getOriginator()).contains(msg)) {
					return;
				} else {
					this.pingHash.get(msg.getOriginator()).add(msg);
				}
			}
		} else {
			// System.out.println("I should be creating a new arrayList of
			// pings");
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
			// System.out.println("I should be making a new NodeInfo List");
			NodeInfo newNodeInfo = new NodeInfo(pingOriginator, msg.getPublicKey(), msg.getLocation(),
					new ArrayList<Resource>(), newTime);
			this.nodeInfoMap.put(pingOriginator, newNodeInfo);
		} else if (nodeInfoKeyAL.contains(pingOriginator)) {
			// System.out.println("I already had this in here");
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
		for (String key : pingSet) {
			ArrayList<Ping> nodePings = pingHash.get(key);
			int min = 1000000;
			Node nodeToRouteTo = null;
			for (int i = 0; i < nodePings.size(); i++) {
				String originator = nodePings.get(i).getOriginator();
				String relayer = nodePings.get(i).getRelayer();
				if (i == 0) {
					min = nodePings.get(i).getCount();
					this.routeTable.put(originator, relayer);
					this.countHash.put(originator, min);
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
	}

	public Hashtable<String, String> getRouteTable() {
		return this.routeTable;
	}

	@Override
	public void setBlockChainDifficulty(int difficulty) {
		blockChain.setDifficulty(difficulty);
	}

	@Override
	public void makeBlockRequest(String hash) {
		BlockRequest br = new BlockRequest(hash, nodeID);
		try {
			wm.broadcast(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receiveBlockDelivery(BlockDelivery bd) {
		if (blockRequestIDs.contains(bd.getBlockHash() + bd.getRecipient())) {
			blockRequestIDs.remove(bd.getBlockHash() + bd.getRecipient());
			if (nodeID == bd.getRecipient()) { // if the delivery is for me
				blockChain.add(bd.getBlock());
			} else {
				distributeMessage(bd);
			}
		}
	}

	@Override
	public void receiveBlockRequest(BlockRequest br) {
		if (br.getBlockHash().equals("latest")) {
			Block b = blockChain.getLastBlock();
			BlockDelivery bd = new BlockDelivery(b, nodeID, br.getAuthor()); // Author
																				// of
																				// request
																				// is
																				// the
																				// block
																				// recipient
			distributeMessage(bd);
		} else if (!blockRequestIDs.contains(br.getBlockHash() + br.getAuthor())) { // If
																					// I
																					// haven't
																					// received
																					// this
																					// request
			Block b = blockChain.getBlock(br.getBlockHash());
			if (b != null) { // if I have the block, make a block delivery
				BlockDelivery bd = new BlockDelivery(b, nodeID, br.getAuthor()); // Author
																					// of
																					// request
																					// is
																					// the
																					// block
																					// recipient
				distributeMessage(bd);
			} else {
				blockRequestIDs.add(br.getBlockHash() + br.getAuthor());
				distributeMessage(br);
			}
		}
	}

	@Override
	public boolean isOnline() {
		return true;
	}

	@Override
	public void addBlock(Block b) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		// add any messages that are in b but have not been received to their
		// respective lists
		for (Message m : b.getMsgs()) {
			addMessage(m);
		}
		blockChain.add(b);
	}

	public void createPing() {
		Ping newPing = new Ping(this.getNodeID(), this.getNodeID(), this.pubKey);
		newPing.setLocation(new Location().createRandomLocation());
		newPing.setTime(new Time(System.currentTimeMillis()));
		sendToTempNodes(newPing);
	}

	public void createPingToBroadcast() {
		Ping newPing = new Ping(this.getNodeID(), this.getNodeID(), this.pubKey);
		newPing.setLocation(new Location().createRandomLocation());
		newPing.setTime(new Time(System.currentTimeMillis()));
		distributeMessage(newPing);
	}

	public void sendToTempNodes(Ping newPing) {
//		for (int i = 0; i < this.tempNodes.size(); i++) {
//			newPing.setRelayer(this.getNodeID());
//			this.tempNodes.get(i).addMessage(newPing);
//		}
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
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.RED);
		g.fillOval(MAXSIZE + this.myLocation.getX(), this.myLocation.getY(), width, height);
		g.setColor(Color.BLACK);

		// need to get the locations of other nodes
		// need to put them on the screen
		// for()
		// need to get the keyset from nodeInfoMap
		// iterate over it
		// print a node in the location from the Location within Nodeinfo
		Set<String> nodeInfoKeys = nodeInfoMap.keySet();
		int x = 0;
		int y = 0;
		// System.out.println("I drew something");
		for (String key : nodeInfoKeys) {
			x = nodeInfoMap.get(key).getMyLocation().getX();
			y = nodeInfoMap.get(key).getMyLocation().getY();
			if (!this.nodeID.equals(key)) {
				g.fillOval(MAXSIZE + x, y, width, height);
			}
			// System.out.println("I should have drawn other nodes");
		}

	}

	public void drawTemps(Graphics g, int maxsize, int width, int height) {
		for (int i = 0; i < this.tempNodes.size(); i++) {
			this.tempNodes.get(i).drawNodes(g, maxsize, width, height);
		}
	}

	public HashMap<String, NodeInfo> getNodeInfoList() {
		return this.nodeInfoMap;
	}

	public ArrayList<Block> getBlockchain() {
		return blockChain.getBlockchain();
	}

	public void updateMyResources(String type, int amount) {
		// TODO Auto-generated method stub
		int currentAmount = this.myResources.get(type);
		this.myResources.remove(type);
		this.myResources.put(type, currentAmount + amount);
		System.out.println("I should have changed my resources");
	}

	public void createInitialPingToBroadcast() {
		System.out.println("my Pub Key: " + this.pubKey);
		InitialPing newPing = new InitialPing(this.getNodeID(), this.getNodeID(), this.pubKey,
				this.getNodeInfoList().get(this.nodeID).getResourceList());
		newPing.setLocation(new Location().createRandomLocation());
		newPing.setTime(new Time(System.currentTimeMillis()));
		distributeMessage(newPing);
	}

	public void sendMessage(Message newMess)
			throws InvalidKeyException, SignatureException, IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(newMess);
		os.close();

		byteArray = out.toByteArray();

		byte[] realSig = new byte[1024];
		dsa.initSign(this.privKey); // sign message with private key
		dsa.update(byteArray);
		realSig = dsa.sign();

		Message newMessage = new MySignedObject(this.nodeID, byteArray, realSig);

		this.addMessage(newMessage);
	}
}
