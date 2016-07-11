import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class PingTester {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException{
		PingTester pingTest = new PingTester();
		testOne();
		//testTwo();
		
	}

	private static void testTwo() throws NoSuchAlgorithmException, NoSuchProviderException {
		NetworkNode node1 = new NetworkNode("node1");
		NetworkNode node2 = new NetworkNode("node2");
		NetworkNode node3 = new NetworkNode("node3");
		NetworkNode node4 = new NetworkNode("node4");
		NetworkNode node5 = new NetworkNode("node5");
		NetworkNode node6 = new NetworkNode("node6");
		NetworkNode node7 = new NetworkNode("node7");
		
		
//		node1.addFriend(node2);
//		node1.addFriend(node4);
//		node2.addFriend(node3);
//		node3.addFriend(node4);
//		node4.addFriend(node5);
//		node5.addFriend(node6);
//		node6.addFriend(node7);
//		node7.addFriend(node1);
//		
//		node1.createPing();
//		node1.updateRouteTable();
//		node2.updateRouteTable();
//		node3.updateRouteTable();
//		node4.updateRouteTable();
//		node5.updateRouteTable();
//		node6.updateRouteTable();
//		node7.updateRouteTable();
//		
//		Message text = new TextMessage("help", node6, node1);
//		node6.sendDirectMessage(node1, text);
		
		//String messageContents = ((TextMessage) node1.getDirectMessages().get(0)).getMessageData().toString();
		
		//System.out.println("message contents: " + messageContents);
		
	}

	private static void testOne() throws NoSuchAlgorithmException, NoSuchProviderException {
		NetworkNode node1 = new NetworkNode("node1");
		NetworkNode node2 = new NetworkNode("node2");
		NetworkNode node3 = new NetworkNode("node3");
		NetworkNode node4 = new NetworkNode("node4");
		NetworkNode node5 = new NetworkNode("node5");
		
		node1.addNode(node2);
		node1.addNode(node3);
		node2.addNode(node4);
		node3.addNode(node5);
		
		node1.createPing();
		System.out.println(node2.getRouteTable().get("node1"));
		System.out.println(node3.getRouteTable().get("node1"));
		System.out.println(node4.getRouteTable().get("node1"));
		System.out.println(node5.getRouteTable().get("node1"));
		node2.createPing();
		System.out.println(node4.getRouteTable().get("node2"));
		
//		BluetoothGUI blueGUI = new BluetoothGUI();
//		blueGUI.setMyNode(node1);
//		blueGUI.beginSimulation();
		
		
//		node2.createPing();
//		node3.createPing();
//		node4.createPing();
//		node5.createPing();
		
//		node1.addFriend(node2);
//		node2.addFriend(node3);
//		node3.addFriend(node4);
//		node4.addFriend(node5);
//		node5.addFriend(node1);
//		
//		node1.createPing();
//		//node2.createPing();
//		node1.updateRouteTable();
//		node2.updateRouteTable();
//		node3.updateRouteTable();
//		node4.updateRouteTable();
//		node5.updateRouteTable();
//		//node1.updateRouteTable();
		
//		Message text = new TextMessage("help", node2, node1);
//		node4.sendDirectMessage(node1, text);
		
		//String messageContents = ((TextMessage) node1.getDirectMessages().get(0)).getMessageData().toString();
		
		//System.out.println("message contents: " + messageContents);
		
	}

}
