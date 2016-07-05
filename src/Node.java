import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

/**
 * Created by 585728 on 6/28/2016.
 */

/*
 * class to provide structure for the nodes in a network
 */
public class Node implements Serializable {
	// Variables
	public String nodeID = null;
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public ArrayList<Message> localMSG = new ArrayList<Message>();
	public ArrayList<PublicKey> publicKeySet = new ArrayList<PublicKey>();
	public KeyPairGenerator keyGen = null;
	public SecureRandom random = null;
	public Signature dsa = null;
	public KeyPair pair = null;
	public PrivateKey privKey = null;
	public PublicKey pubKey = null;
	public byte[] byteArray = new byte[1024];
	public Blockchain blockChain;
	public HashMap<Node, Node> routeTable = new HashMap<Node, Node>();
	public Random rand = new Random();
	private Ping ping;
	public ArrayList<String> blockRequestIDs = new ArrayList<String>();
	public HashMap<String, Integer> myResources = new HashMap<String, Integer>();
	public HashMap<String, ArrayList<Ping>> pingHash = new HashMap<String, ArrayList<Ping>>();
	public ArrayList<Message> directMessages = new ArrayList<Message>();
	public HashMap<Node, Integer> countHash = new HashMap<Node, Integer>();
	
	public int xCoordinate = 0;
	public int yCoordinate = 0;
	public Color color = Color.BLUE;
	public int WIDTH = 0;
	public int BidNumber = 1;
	public ArrayList<Message> acceptedMessages = new ArrayList<Message>();
	public ArrayList<Message> submittedBids = new ArrayList<Message>();
	public ArrayList<Ping> pingsReceived = new ArrayList<Ping>();
	public int LENGTH = 10;

	// Constructor
	public Node(String id) throws NoSuchAlgorithmException, NoSuchProviderException {

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

	public void addResource(String type, int amount) {
		String resourceType = type.toLowerCase();
		if (myResources.containsKey(resourceType)) {
			myResources.replace(resourceType, amount + myResources.get(resourceType));
		} else {
			myResources.put(resourceType, amount);
		}
	}

	public HashMap<String, Integer> getResources() {
		return this.myResources;
	}

	// Accessors
	public String getNodeID() {
		return nodeID;
	}

	public PublicKey getPublicKey() {
		return this.pubKey;
	}

	public ArrayList<Node> getFriends() {
		return this.networkNodes;
	}

	public ArrayList<Message> getMessages() {
		// TODO Auto-generated method stub
		return this.localMSG;
	}

	// Mutators
	public void createMessage(Message text) {

		distributePublicKey(this.getPublicKey()); // distribute public key to
		// friend nodes (they will
		// propagate it to their
		// friends)

		this.blockChain.add(text);

		localMSG.add(text);
		this.distributeMessage(text); // distribute message to friend nodes

	}

	public void createMessage(Object data) {

		distributePublicKey(this.getPublicKey()); // distribute public key to
		// friend nodes (they will
		// propagate it to their
		// friends)
		// System.out.println("I distributed my public key");

		Random rand = new Random(); // create a message with a random friend
		// node as the recipient
		if (networkNodes.size() > 0) {
			int nodesSize = networkNodes.size();
			int receiverNum = rand.nextInt(nodesSize);
			Message text = new TextMessage(data, this, networkNodes.get(receiverNum));

			this.blockChain.add(text);

			localMSG.add(text);
			this.distributeMessage(text); // distribute message to friend nodes
		} // (they will propagate to their
			// friends)
	}

	public void createMessageWithSignature(Object data)
			throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {

		distributePublicKey(this.getPublicKey());
		// System.out.println("I distributed my public key");

		Random rand = new Random(); // create a message with a random friend
		// node as the recipient
		int receiverNum = rand.nextInt(networkNodes.size());
		Message text = new TextMessage(data, this, networkNodes.get(receiverNum));

		byteArray = text.getMessageData().toString().getBytes(); // convert
		// message
		// to series
		// of bytes
		byte[] realSig = new byte[1024];

		dsa.initSign(this.privKey); // sign message with private key
		dsa.update(byteArray);
		realSig = dsa.sign();

		localMSG.add(text);
		this.distributeSignedMessage(realSig, byteArray, text); // distribute
		// message to
		// friend nodes
		// (they will
		// propagate to
		// their
		// friends)
	}

	public void addNodes(ArrayList<Node> newNodes) { // add a group of friend
		// nodes
		for (int i = 0; i < newNodes.size(); i++) {
			networkNodes.add(newNodes.get(i));
		}
	}

	public void addFriend(Node node) { // add a single friend node
		networkNodes.add(node);
	}

	public void addMessage(Message text) {
		// if message is unique, add and
		// distribute
		// do nothing
		if (text != null && !this.localMSG.contains(text)) {
			this.blockChain.add(text);
			this.localMSG.add(text);
			this.distributeMessage(text);
		}
	}

	public void addSignedMessage(byte[] realSig, byte[] byteArray2, Message text)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
		for (int i = 0; i < publicKeySet.size(); i++) { // find public key that
			// can verify message
			sig.initVerify(publicKeySet.get(i));
			sig.update(byteArray2);
			boolean verifies = sig.verify(realSig);

			if (verifies == true) { // if verified and unique, add to localMSG
				// and distribute friend nodes
				if (!this.localMSG.contains(text)) {
					localMSG.add(text);
					this.distributeSignedMessage(realSig, byteArray2, text);
					return;
				}
			} else {
			}
		}
	}

	public void addPublicKey(PublicKey publicKey) {
		// TODO Auto-generated method stub
		if (this.publicKeySet.contains(publicKey)) { // if public key is unique,
			// add and distribute to
			// friend nodes
			// do nothing
		} else {
			this.publicKeySet.add(publicKey);
			this.distributePublicKey(publicKey);
		}
	}

	public void setBlockChainDifficulty(int difficulty) {
		blockChain.setDifficulty(difficulty);
	}
	
	public HashMap<Node, Node> getRouteTable(){
		return this.routeTable;
	}

	public void createPing() {
		this.ping = new Ping(this);
		distributePing(this.ping);
	}
	
	public void distributePing(Ping receivedPing) {
		Ping pingToSend = new Ping(receivedPing.getOriginator(), this, receivedPing.getCount() + 1);
		for (int i = 0; i < this.networkNodes.size(); i++) {
			this.networkNodes.get(i).receivePing(pingToSend);
		}
	}

	private void receivePing(Ping ping) {
		if(ping.getOriginator().getNodeID().equals(this.getNodeID())){
			return;
		}
		else if (this.pingHash.containsKey(ping.getOriginator().getNodeID())) {
			if (this.pingHash.get(ping.getOriginator().getNodeID()).contains(ping)) {
			} else {
				this.pingHash.get(ping.getOriginator().getNodeID()).add(ping);
			}
		} else {
			ArrayList<Ping> newArrayList = new ArrayList<Ping>();
			newArrayList.add(ping);
			this.pingHash.put(ping.getOriginator().getNodeID(), newArrayList);
			this.distributePing(ping);
		}
	}
	
	public void updateRouteTable(){
		Set<String> pingSet = pingHash.keySet();
		for(String key:pingSet){
			ArrayList<Ping> nodePings = pingHash.get(key);
			int min = 1000000;
			Node nodeToRouteTo = null;
			for(int i = 0; i < nodePings.size(); i++){
				if(i ==0){
					min = nodePings.get(i).getCount();
					Node originator = nodePings.get(i).getOriginator();
					Node relayer = nodePings.get(i).getRelayer();
					routeTable.put(originator, relayer);
					this.countHash.put(originator, min);
				}
				else if(nodePings.get(i).getCount() < min){
					min = nodePings.get(i).getCount();
					Node originator = nodePings.get(i).getOriginator();
					Node relayer = nodePings.get(i).getRelayer();
					if(this.countHash.get(originator) < min) {
						routeTable.remove(originator);
						routeTable.put(originator, relayer);
					}
					//routeTable.remove(originator);
					//routeTable.put(originator, relayer);
				}
				else{
					// do nothing
				}
			}
		}
	}
	
	public void sendDirectMessage(Node node1, Message message) throws NoSuchAlgorithmException, NoSuchProviderException{
		System.out.println(this.getNodeID());
		Node nodeToSendTo = routeTable.get(node1);
		if(this.getNodeID().equals(node1.getNodeID())){
			System.out.println("The message made it to me");
			directMessages.add(message);
			return;
		}
		else {
			nodeToSendTo.sendDirectMessage(node1, message);
		}
	}
	
	public ArrayList<Message> getDirectMessages(){
		return this.directMessages;
	}

	public void receiveBlock(Block b, String nodeID) {
		if (blockRequestIDs.contains(b.getMyHash() + nodeID)) {
			blockRequestIDs.remove(b.getMyHash() + nodeID);
			if (nodeID == this.nodeID) {
				blockChain.add(b);
			} else {
				broadcastBlock(b, nodeID);
			}
		}
	}

	public void requestBlock(String hash, String id) {
		if (!blockRequestIDs.contains(hash + id)) {
			blockRequestIDs.add(hash + id);
			Block b = blockChain.getBlock(hash);
			if (b != null) {
				broadcastBlock(b, id);
			} else {
				makeBlockRequest(hash, id);
			}
		}
	}

	public void makeBlockRequest(String hash, String id) {
		for (int i = 0; i < networkNodes.size(); i++) { // distribute
			// blockrequest to
			// friend nodes (they
			// will propagate to
			// their friends if they
			// cannot resolve)
			networkNodes.get(i).requestBlock(hash, id);
		}
	}

	public void broadcastBlock(Block b, String nodeID) {
		for (int i = 0; i < networkNodes.size(); i++) { // distribute
			// blockrequest to
			// friend nodes (they
			// will propagate to
			// their friends if they
			// cannot resolve)
			networkNodes.get(i).receiveBlock(b, nodeID);
		}
	}

	// Utility
	public void start() {
		blockChain.start();
	}

	public void printNodes() {
		for (int i = 0; i < networkNodes.size(); i++) { // print out friend
			// nodes
			System.out.println(networkNodes.get(i).getNodeID());
		}
	}

	public boolean isOnline() {
		return true;
	}

	public void distributeMessage(Message text) {
		for (int i = 0; i < networkNodes.size(); i++) { // distribute message to
			// friend nodes (they
			// will propagate to
			// their friends)
			networkNodes.get(i).addMessage(text);
		}
	}

	public void distributeSignedMessage(byte[] realSig, byte[] byteArray2, Message text)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
		// TODO Auto-generated method stub
		for (int i = 0; i < networkNodes.size(); i++) { // give each friend node
			// the signed message,
			// saved in their
			// localMSG
			networkNodes.get(i).addSignedMessage(realSig, byteArray2, text);
		}
	}

	public void distributePublicKey(PublicKey publicKey) {
		for (int i = 0; i < networkNodes.size(); i++) { // add public key to all
			// friend nodes (they
			// will propagate to
			// their friends)
			networkNodes.get(i).addPublicKey(publicKey);
		}
	}

	public void distributeBlock(Block b) {
		for (int i = 0; i < networkNodes.size(); i++) { // add block to friend
			// node's blockchain
			networkNodes.get(i).blockChain.add(b);
		}
	}

	public void addBlock(Block b) {
		blockChain.add(b);
	}
	
	public void sendDirectMessageSim(Node node1, Message message) throws NoSuchAlgorithmException, NoSuchProviderException{
		System.out.println(this.getNodeID());
		Node nodeToSendTo = routeTable.get(node1);
		System.out.println(node1.getNodeID());
		if(this.getNodeID().equals(node1.getNodeID())){
			this.setNodeValues(this.getXCoord(), this.getYCoord(), Color.ORANGE,
					this.getWidth());
			System.out.println("The message made it to me");
			directMessages.add(message);
			return;
		}
		else {
			this.setNodeValues(this.getXCoord(), this.getYCoord(), Color.MAGENTA,
					this.getWidth());
			nodeToSendTo.sendDirectMessage(node1, message);
		}
	}

	// Accessors
	public Color getColor() {
		return this.color;
	}

	public int getXCoord() {
		return this.xCoordinate;
	}

	public int getYCoord() {
		return this.yCoordinate;
	}

	public int getWidth() {
		return this.WIDTH;
	}

	public void setNodeValues(int xVal, int yVal, Color myColor, int width)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		this.xCoordinate = xVal;
		this.yCoordinate = yVal;
		this.color = myColor;
		this.WIDTH = width;
	}

	public void setColor(Color myColor) {
		this.color = myColor;
	}

	public void moveNode(int maxSize, int offset, int movement, Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(this.xCoordinate, this.yCoordinate, this.WIDTH, this.WIDTH);

		int randomX = rand.nextInt(movement);
		int randomY = rand.nextInt(movement);
		int direction = rand.nextInt(4);
		int maxChecker = maxSize - offset;
		if ((this.xCoordinate + randomX) > maxChecker) {
			this.xCoordinate = this.xCoordinate - 40;
			// System.out.println("I got here");
			// this.xCoordinate = this.xCoordinate +
			// (randomX-(maxChecker-this.xCoordinate));
		}
		if ((this.xCoordinate - randomX) < 20) {
			this.xCoordinate = this.xCoordinate + 25;
			// System.out.println("I got here");
			// this.xCoordinate = this.xCoordinate - (randomX-(this.xCoordinate
			// - offset));
		}
		if ((this.yCoordinate + randomY) > maxChecker) {
			// System.out.println("I got here");
			this.yCoordinate = this.yCoordinate - 40;
			// this.yCoordinate = this.yCoordinate +
			// (randomY-(maxChecker-this.yCoordinate));
		}
		if ((this.yCoordinate + randomY) < 100) {
			// System.out.println("I got here");
			this.yCoordinate = this.yCoordinate + 25;
			// this.yCoordinate = this.yCoordinate - (randomY-(this.yCoordinate
			// - offset));
		} else {
			if (direction == 0) {
				this.xCoordinate = this.xCoordinate + randomX;
				this.yCoordinate = this.yCoordinate + randomY;
			} else if (direction == 1) {
				this.xCoordinate = this.xCoordinate - randomX;
				this.yCoordinate = this.yCoordinate + randomY;
			} else if (direction == 2) {
				this.xCoordinate = this.xCoordinate + randomX;
				this.yCoordinate = this.yCoordinate - randomY;
			} else {
				this.xCoordinate = this.xCoordinate - randomX;
				this.yCoordinate = this.yCoordinate - randomY;
			}
		}
	}


	public void addAcceptedMessage(Message msg) {
		this.acceptedMessages.add(msg);
	}

	public ArrayList<Message> getAcceptedMessages() {
		return this.acceptedMessages;
	}

	public void removeMessage(Message text) {
		if (this.localMSG.contains(text)) {
			this.localMSG.remove(text);
			
		} else {
			// do nothing
		}
	}
	
	public void addBid(Message bid){
		System.out.println("submitted Bids Length: " + this.submittedBids.size());
		((Bid) bid.getMessageData()).setBidNumber(this.BidNumber);
		this.BidNumber++;
		this.submittedBids.add(bid);
		System.out.println("submitted Bids Length after: " + this.submittedBids.size());
	}
	
	public ArrayList<Message> getBids(){
		return this.submittedBids;
	}

	public void removeBid(Message currentMessage) {
		// TODO Auto-generated method stub
		this.submittedBids.remove(currentMessage);
	}

	public void removeAcceptedMessage(Message message) {
		// TODO Auto-generated method stub
		this.acceptedMessages.remove(message);
	}
	
	public String randomMessageNumberGenerator(){
		String messageNum = "";
		ArrayList<String> charPossibilities = new ArrayList<String>();
		charPossibilities.add("a");
		charPossibilities.add("b");
		charPossibilities.add("c");
		charPossibilities.add("d");
		charPossibilities.add("e");
		charPossibilities.add("f");
		charPossibilities.add("g");
		charPossibilities.add("h");
		charPossibilities.add("i");
		charPossibilities.add("j");
		charPossibilities.add("k");
		charPossibilities.add("l");
		charPossibilities.add("m");
		charPossibilities.add("n");
		charPossibilities.add("o");
		charPossibilities.add("p");
		charPossibilities.add("q");
		charPossibilities.add("r");
		charPossibilities.add("s");
		charPossibilities.add("t");
		charPossibilities.add("u");		
		charPossibilities.add("v");
		charPossibilities.add("w");
		charPossibilities.add("x");
		charPossibilities.add("y");
		charPossibilities.add("z");
		charPossibilities.add("0");
		charPossibilities.add("1");
		charPossibilities.add("2");
		charPossibilities.add("3");
		charPossibilities.add("4");
		charPossibilities.add("5");
		charPossibilities.add("6");
		charPossibilities.add("7");
		charPossibilities.add("8");
		charPossibilities.add("9");
		
		for(int i = 0; i < LENGTH;i++){
			int ranNum = rand.nextInt(charPossibilities.size());
			messageNum = messageNum + charPossibilities.get(i);
		}
		//System.out.println(messageNum);
		return messageNum;
	}
}
