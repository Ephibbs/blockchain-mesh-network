import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/*
 * Class for the nodes in a mesh network
 * Parameters: ID string
 */

/* TODO
 * When I am sending a message, I should try and verify the header of the message
 * but I am not sure if that would work or not because then someone could
 * possible alter the contents of it without having to alter the signature.
 */

public class Node implements Serializable {

	// Variables
	public String nodeID = null;
	public ArrayList<Message> localMSG = new ArrayList<Message>();
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public ArrayList<PublicKey> publicKeySet = new ArrayList<PublicKey>();
	public KeyPairGenerator keyGen = null;
	public SecureRandom random = null;
	public Signature dsa = null;
	public KeyPair pair = null;
	public PrivateKey privKey = null;
	public PublicKey pubKey = null;
	public byte[] byteArray = new byte[1024];
	public Blockchain blockChain = new Blockchain(this);
	public Random rand = new Random();
	
	public Hashtable<Node,Node> routeTable = new Hashtable<Node,Node>();

	public int xCoordinate = 0;
	public int yCoordinate = 0;
	public Color color = Color.BLUE;
	public int WIDTH = 0;
	
	private Ping ping;

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
		// this.blockChain = new Blockchain(this);
		// distributePublicKey()
	}

	// Accessors
	public String getNodeID() {
		return nodeID;
	}

	public PublicKey getPublicKey() {
		return this.pubKey;
	}

	// Mutators
	public void createMessage(Object data) {

		distributePublicKey(this.getPublicKey()); // distribute public key to
													// friend nodes (they will
													// propagate it to their
													// friends)
		//System.out.println("I distributed my public key");

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
		System.out.println("I distributed my public key");

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
		if (this.localMSG.contains(text)) { // if message is unique, add and
											// distribute
			// do nothing
		} else {
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
			// System.out.println("signature verifies: " + verifies);
			// System.out.println("the public key I verified with was: " +
			// publicKeySet.get(i).toString());
			// System.out.println("I am node: " + this.getNodeID());

			if (verifies == true) { // if verified and unique, add to localMSG
									// and distribute friend nodes
				if (!this.localMSG.contains(text)) {
					localMSG.add(text);
					this.distributeSignedMessage(realSig, byteArray2, text);
					System.out.println("I sent the message out");
					return;
				}
			} else {
				// System.out.println("I broke the program");
				// return;
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
		// TODO Auto-generated method stub
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

	// Utility
	public void start() {
		//System.out.println(nodeID);
		blockChain.start();
	}

	public void printNodes() {
		for (int i = 0; i < networkNodes.size(); i++) { // print out friend
														// nodes
			System.out.println(networkNodes.get(i).getNodeID());
		}
	}

	public boolean isOnline() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setBlockChainDifficulty(int difficulty) {
		blockChain.setDifficulty(difficulty);
	}

	public void setNodeValues(int xVal, int yVal, Color myColor, int width)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		this.xCoordinate = xVal;
		this.yCoordinate = yVal;
		this.color = myColor;
		this.WIDTH = width;
	}

	public void Draw(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.xCoordinate, this.yCoordinate, this.WIDTH, this.WIDTH);
	}

	public void drawLinesToFriends(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < this.networkNodes.size(); i++) {
			Node friend = networkNodes.get(i);
			g.drawLine(this.xCoordinate + this.WIDTH / 2, this.yCoordinate + this.WIDTH / 2,
					friend.getXCoord() + this.WIDTH / 2, friend.getYCoord() + this.WIDTH / 2);
		}
	}
	
	public void createPing(){
		this.ping = new Ping(this);
		distributePing(this.ping);
	}
	
	public void distributePing(Ping receivedPing){
		for(int i = 0; i < this.networkNodes.size();i++){
			networkNodes.get(i).receivePing(receivedPing);
		}
	}

	private void receivePing(Ping ping) {
		// TODO Auto-generated method stub
		if(this.routeTable.containsKey(ping.getOriginator())) {
			//do nothing
		} else {
			ping.setRelayer(this);
			distributePing(ping);
		}
		
	}

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

	public void setColor(Color myColor) {
		this.color = myColor;
	}

	public ArrayList<Node> getFriends() {
		return this.networkNodes;
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
			System.out.println("I got here");
			this.yCoordinate = this.yCoordinate - 40;
			// this.yCoordinate = this.yCoordinate +
			// (randomY-(maxChecker-this.yCoordinate));
		}
		if ((this.yCoordinate + randomY) < 100) {
			System.out.println("I got here");
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
}
