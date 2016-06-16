import java.util.ArrayList;
import java.util.Random;

public class Main {
	// These nodes are available nodes in the global network.
	public Node node1, node2, node3, node4, node5, node6, node7, node8,node9;
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	
	/*
	 * This function is to basically just give each node a name and add the
	 * nodes to an arraylist to similar the network
	 */
	public Main() {
		node1 = new Node("Steve");
		node2 = new Node("Damian");
		node3 = new Node("Lawrence");
		node4 = new Node("Jeffrey");
		node5 = new Node("Evan");
		node6 = new Node("Natalie");
		node7 = new Node("Will");
		node8 = new Node("Dylan");
		node9 = new Node("Colby");
		
		//adding the nodes to the "network"
		networkNodes.add(node1);
		networkNodes.add(node2);
		networkNodes.add(node3);
		networkNodes.add(node4);
		networkNodes.add(node5);
		networkNodes.add(node6);
		networkNodes.add(node7);
		networkNodes.add(node8);
		networkNodes.add(node9);
		
		//This is a method call to the generate communication lines method
		generateCommunicationLines();
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

	/*
	 * This method is to just test run making messages
	 */
	public void run() {
		node1.createMessage("I like tacos");
		
		System.out.println("A list of all of node 1's friends");
		for(int i = 0; i < node1.networkNodes.size();i++){
			System.out.println("Friend" + (1+i) + " is: " + node1.networkNodes.get(i).getNodeID());
		}
		
		System.out.println("A list of who all has a message: ");
		for(int j = 0; j < this.networkNodes.size();j++) {
			if(this.networkNodes.get(j).localMSG.size()>0) {
				System.out.println("I have a message: " + this.networkNodes.get(j).getNodeID());
			}
		}
		
		System.out.println("The message was sent from: " + node4.localMSG.get(0).getAuthor().getNodeID());
		System.out.println("The message was sent to: " + node4.localMSG.get(0).getRecipient().getNodeID());
		System.out.println("The message said: " + node4.localMSG.get(0).getMessageData().toString());
	}
}
