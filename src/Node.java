import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Node {

	public String nodeID = null;
	public ArrayList<Message> localMSG = new ArrayList<Message>();
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public ArrayList<PublicKey> publicKeySet = new ArrayList<PublicKey>();
	KeyPair pair = null;
	PrivateKey privKey = null;
	PublicKey pubKey = null;
	
	// These are the constructors for the node class
	public Node(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.nodeID = id;
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(1024, random);
		this.pair = keyGen.generateKeyPair();
		this.privKey = pair.getPrivate();
		this.pubKey = pair.getPublic();
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
		
		distributePublicKey();
		System.out.println("I distributed my public key");
		
		Random rand = new Random();
		int receiverNum = rand.nextInt(networkNodes.size());

		Message text = new TextMessage(data, this, networkNodes.get(receiverNum));

		localMSG.add(text);
		this.distributeMessage(text);
	}

	private void distributePublicKey() {
		// TODO Auto-generated method stub
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).addPublicKey(this.getPublicKey());
		}
	}

	private void addPublicKey(PublicKey publicKey) {
		// TODO Auto-generated method stub
		if(this.localMSG.contains(publicKey)) {
			// do nothing
		}
		else {
			this.publicKeySet.add(publicKey);
			this.publicKeySet.add(publicKey);
		}
	}

	private PublicKey getPublicKey() {
		// TODO Auto-generated method stub
		return this.pubKey;
	}

	// THis is how the message is distributed to its friends
	private void distributeMessage(Message text) {
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).addMessage(text);
		}
	}

	// Adding a message to a nodes list of messages it has received
	private void addMessage(Message text) {
		if(this.localMSG.contains(text)) {
			// do nothing
		}
		else {
			this.localMSG.add(text);
			this.distributeMessage(text);
		}
	}
}
