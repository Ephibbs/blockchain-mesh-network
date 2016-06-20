import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Random;

public class BlockChainTesting {
	
	public Node node1, node2, node3, node4;//, node5, node6, node7, node8,node9;
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public Blockchain blockChain = null;
	
	//public Blockchain chain1,chain2,chain3,chain4,chain5,chain6,chain7,chain8,chain9;
	
	/*
	 * This function is to basically just give each node a name and add the
	 * nodes to an arraylist to similar the network
	 */
	public BlockChainTesting() throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException {
		node1 = new Node("Steve");
		node2 = new Node("Damian");
		node3 = new Node("Lawrence");
		node4 = new Node("Jeffrey");
		
		//adding the nodes to the "network"
		networkNodes.add(node1);
		networkNodes.add(node2);
		networkNodes.add(node3);
		networkNodes.add(node4);
		
		blockChain = new Blockchain(node1);
		
		//This is a method call to the generate communication lines method
		generateCommunicationLines();
		distributePublicKeys();
		
	}
	
	public void runWithBlockChain() throws InterruptedException {
		
		this.blockChain.run();

		System.out.println("look at me");
		
		Message text = new TextMessage("help", node1, networkNodes.get(2));
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.add(text);
		
		System.out.println("look at me now");
		
		Block newBlock = new Block("whatishappening", messages);
		
		System.out.println(node1.getNodeID());
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

}
