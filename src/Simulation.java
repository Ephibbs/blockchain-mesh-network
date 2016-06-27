import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Random;

/*
 * The Simulation class simulates a mesh network and tests functionality
 * Executed via SimulationHandler
 */

public class Simulation {

	// Nodes, network, and blockchains
	//public Node node1, node2, node3, node4, node5, node6, node7, node8, node9;
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public Blockchain chain1, chain2, chain3, chain4, chain5, chain6, chain7, chain8, chain9;
	public int difficulty;
	public int numberOfInitialNodes;
	public NetworkGUI gui = new NetworkGUI();
	
	// Set up the network
		public Simulation(int numberOfInitialNodes, int difficulty) throws NoSuchAlgorithmException, NoSuchProviderException {
			this.difficulty = difficulty;
			this.numberOfInitialNodes = numberOfInitialNodes;
			// Initialize each node with a name
			generateInitialNodes(numberOfInitialNodes);

			generateCommunicationLines(); // each node will randomly add friend nodes
			distributePublicKeys(); // distribute public key throughout network
			runWithBlockChain(); // create and send a message
		}

	private void generateInitialNodes(int numberOfInitialNodes) throws NoSuchAlgorithmException, NoSuchProviderException {
			// TODO Auto-generated method stub
			Node n;
			for(int i = 0; i < numberOfInitialNodes; i++){
				n = new Node("" + i);
				n.setBlockChainDifficulty(difficulty);
				networkNodes.add(n);
			}
		}
	private void distributePublicKeys() {
		// TODO Auto-generated method stub
		for(int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).distributePublicKey(networkNodes.get(i).getPublicKey());
		}
	}
	/*
	 * This method is for giving each node the other nodes that it can talk to.
	 * Doing it this way allows us to simulate a distribution because each node
	 * can only talk to a maximum of n/2 nodes.
	 */
	private void generateCommunicationLines() {
		// Do not worry about this part it is just defining a class so i can use
		// it later
		Random rand = new Random();
		
		// just defining the number of friends that each node can have
		Double n = (double) networkNodes.size();
		Double maxFriendsDouble = Math.ceil(n/2);
		int maxFriends = maxFriendsDouble.intValue();
		
		//this first forloop is only to make sure that each node gets friends to talk to
		for(int i = 0; i < n.intValue();i++){
			Node nodeToGiveFriends = networkNodes.get(i);
			ArrayList<Node> possibleFriends = (ArrayList<Node>) networkNodes.clone();
			possibleFriends.remove(i);
			
			// This next for loop is the one that actually gives nodes their friends
			// it goes through and makes sure you dont give the same node more then 2 friends
			for(int j = 0; j < maxFriends-1; j++) {
				int randomFriend = rand.nextInt(possibleFriends.size());
				nodeToGiveFriends.addFriend(possibleFriends.get(randomFriend));
				possibleFriends.remove(randomFriend);
			}
		}
	}
	
	public void runWithBlockChain() {
		for(Node n : networkNodes) {
			n.start();
		}
		this.networkNodes.get(0).createMessage("help");
	}

	/*
	 * This method is to just test run making messages
	 */
	public void run() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
		
		this.networkNodes.get(0).createMessageWithSignature("Colby and Dylan are watching snapchats");
		
		System.out.println("Size of node 5 public keyset = " + this.networkNodes.get(4).publicKeySet.size());
		
		//System.out.println("A list of all of node 1's friends");
		for(int i = 0; i < this.networkNodes.get(0).networkNodes.size();i++){
			//System.out.println("Friend" + (1+i) + " is: " + node1.networkNodes.get(i).getNodeID());
		}
		
		System.out.println("A list of who all has a message: ");
		for(int j = 0; j < this.networkNodes.size();j++) {
			if(this.networkNodes.get(j).localMSG.size()>0) {
				System.out.println("I have a message: " + this.networkNodes.get(j).getNodeID());
			}
		}
		
		System.out.println(this.networkNodes.get(0).blockChain.getLastTreeNode().getMyHash());
	}
}

