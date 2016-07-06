import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class PingTester {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException{
		PingTester pingTest = new PingTester();
		//testOne();
		testTwo();
		
	}

	private static void testTwo() throws NoSuchAlgorithmException, NoSuchProviderException {
		SimulationNode node1 = new SimulationNode("node1");
		SimulationNode node2 = new SimulationNode("node2");
		SimulationNode node3 = new SimulationNode("node3");
		SimulationNode node4 = new SimulationNode("node4");
		SimulationNode node5 = new SimulationNode("node5");
		SimulationNode node6 = new SimulationNode("node6");
		SimulationNode node7 = new SimulationNode("node7");
		
		
		node1.addFriend(node2);
		node1.addFriend(node4);
		node2.addFriend(node3);
		node3.addFriend(node4);
		node4.addFriend(node5);
		node5.addFriend(node6);
		node6.addFriend(node7);
		node7.addFriend(node1);
		
		node1.createPing();
		node1.updateRouteTable();
		node2.updateRouteTable();
		node3.updateRouteTable();
		node4.updateRouteTable();
		node5.updateRouteTable();
		node6.updateRouteTable();
		node7.updateRouteTable();
		
		Message text = new TextMessage("help", node6, node1);
		node6.sendDirectMessage(node1, text);
		
		//String messageContents = ((TextMessage) node1.getDirectMessages().get(0)).getMessageData().toString();
		
		//System.out.println("message contents: " + messageContents);
		
	}

	private static void testOne() throws NoSuchAlgorithmException, NoSuchProviderException {
		Node node1 = new Node("node1");
		Node node2 = new Node("node2");
		Node node3 = new Node("node3");
		Node node4 = new Node("node4");
		Node node5 = new Node("node5");
		
		
		node1.addFriend(node2);
		node2.addFriend(node3);
		node3.addFriend(node4);
		node4.addFriend(node5);
		node5.addFriend(node1);
		
		node1.createPing();
		//node2.createPing();
		node1.updateRouteTable();
		node2.updateRouteTable();
		node3.updateRouteTable();
		node4.updateRouteTable();
		node5.updateRouteTable();
		//node1.updateRouteTable();
		
		Message text = new TextMessage("help", node2, node1);
		node4.sendDirectMessage(node1, text);
		
		//String messageContents = ((TextMessage) node1.getDirectMessages().get(0)).getMessageData().toString();
		
		//System.out.println("message contents: " + messageContents);
		
	}

}
