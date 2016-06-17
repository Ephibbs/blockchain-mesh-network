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
import java.util.Random;

/*
 * When I am sending a message, I should try and verify the header of the message
 * but I am not sure if that would work or not because then someone could
 * possible alter the contents of it without having to alter the signature.
 */
public class Node implements Serializable {

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
	public Blockchain blockChain = null;

	// These are the constructors for the node class
	public Node(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		
		this.nodeID = id;

		this.keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
		this.random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		this.dsa = Signature.getInstance("SHA1withDSA", "SUN");
		keyGen.initialize(1024, random);
		this.pair = keyGen.generateKeyPair();
		this.privKey = pair.getPrivate();
		this.pubKey = pair.getPublic();
		this.blockChain = new Blockchain(this);
		// distributePublicKey()
	}
	
	public void run() {
		System.out.println(nodeID);
		blockChain.run();
	}

	// This allows other classes to get the name of the node
	public String getNodeID() {
		return nodeID;
	}

	// This is one mechanism so we are able to add friends to nodes
	// this one is a group of friends at a time.
	public void addNodes(ArrayList<Node> newNodes) {
		for (int i = 0; i < newNodes.size(); i++) {
			networkNodes.add(newNodes.get(i));
		}
	}

	// This is another mechanism so we are able to add friends to nodes
	// this is one friend at a time
	public void addFriend(Node node) {
		networkNodes.add(node);
	}

	// This prints the nodes that each node is friends with
	public void printNodes() {
		for (int i = 0; i < networkNodes.size(); i++) {
			System.out.println(networkNodes.get(i).getNodeID());
		}
	}

	// This is the method for creating a message and adding a random recipient
	// from a nodes friend list. The message is then distribute to this nodes
	// friend
	public void createMessage(Object data) {

		distributePublicKey(this.getPublicKey());
		System.out.println("I distributed my public key");

		Random rand = new Random();
		int receiverNum = rand.nextInt(networkNodes.size());

		Message text = new TextMessage(data, this, networkNodes.get(receiverNum));

		// byteArray = text.getMessageData().toString().getBytes();
		
		this.blockChain.addMessage(text);

		localMSG.add(text);
		this.distributeMessage(text);
	}

	// This is the method for creating a message and adding a random recipient
	// from a nodes friend list. The message is then distribute to this nodes
	// friend
	public void createMessageWithSignature(Object data) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {

		// distributePublicKey(this.getPublicKey());
		System.out.println("I distributed my public key");

		Random rand = new Random();
		int receiverNum = rand.nextInt(networkNodes.size());

		Message text = new TextMessage(data, this, networkNodes.get(receiverNum));

		byteArray = text.getMessageData().toString().getBytes();

		byte[] realSig = new byte[1024];

		dsa.initSign(this.privKey);
		dsa.update(byteArray);
		realSig = dsa.sign();

		localMSG.add(text);
		this.distributeSignedMessage(realSig, byteArray, text);
	}

	public void distributeSignedMessage(byte[] realSig, byte[] byteArray2, Message text) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
		// TODO Auto-generated method stub
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).addSignedMessage(realSig, byteArray2, text);
		}
	}

	public void addSignedMessage(byte[] realSig, byte[] byteArray2, Message text) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
		for (int i = 0; i < publicKeySet.size(); i++) {
			sig.initVerify(publicKeySet.get(i));
			sig.update(byteArray2);
			boolean verifies = sig.verify(realSig);
			//System.out.println("signature verifies: " + verifies);
			//System.out.println("the public key I verified with was: " + publicKeySet.get(i).toString());
			//System.out.println("I am node: " + this.getNodeID());

			if (verifies == true) {
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

	public void distributePublicKey(PublicKey publicKey) {
		// TODO Auto-generated method stub
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).addPublicKey(publicKey);
		}
	}

	public void addPublicKey(PublicKey publicKey) {
		// TODO Auto-generated method stub
		if (this.publicKeySet.contains(publicKey)) {
			// do nothing
		} else {
			this.publicKeySet.add(publicKey);
			this.distributePublicKey(publicKey);
		}
	}

	public PublicKey getPublicKey() {
		// TODO Auto-generated method stub
		return this.pubKey;
	}

	// THis is how the message is distributed to its friends
	public void distributeMessage(Message text) {
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).addMessage(text);
		}
	}

	// Adding a message to a nodes list of messages it has received
	public void addMessage(Message text) {
		if (this.localMSG.contains(text)) {
			// do nothing
		} else {
			this.localMSG.add(text);
			this.distributeMessage(text);
		}
	}

	public boolean isOnline() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void distributeBlock(Block b) {
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).blockChain.receiveBlock(b);
		}
	}

	public void receiveChain(Blockchain chain1) {
		// TODO Auto-generated method stub
		this.blockChain = chain1;
	}
}
