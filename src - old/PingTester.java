import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;

public class PingTester {

	public static void main(String[] args)
			throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException {
		PingTester pingTest = new PingTester();
		testOne();
		// testTwo();

	}

	private static void testTwo() throws NoSuchAlgorithmException, NoSuchProviderException {
		NetworkNode node1 = new NetworkNode("node1");
		NetworkNode node2 = new NetworkNode("node2");
		NetworkNode node3 = new NetworkNode("node3");
		NetworkNode node4 = new NetworkNode("node4");
		NetworkNode node5 = new NetworkNode("node5");
		NetworkNode node6 = new NetworkNode("node6");
		NetworkNode node7 = new NetworkNode("node7");

		// node1.addFriend(node2);
		// node1.addFriend(node4);
		// node2.addFriend(node3);
		// node3.addFriend(node4);
		// node4.addFriend(node5);
		// node5.addFriend(node6);
		// node6.addFriend(node7);
		// node7.addFriend(node1);
		//
		// node1.createPing();
		// node1.updateRouteTable();
		// node2.updateRouteTable();
		// node3.updateRouteTable();
		// node4.updateRouteTable();
		// node5.updateRouteTable();
		// node6.updateRouteTable();
		// node7.updateRouteTable();
		//
		// Message text = new TextMessage("help", node6, node1);
		// node6.sendDirectMessage(node1, text);

		// String messageContents = ((TextMessage)
		// node1.getDirectMessages().get(0)).getMessageData().toString();

		// System.out.println("message contents: " + messageContents);

	}

	private static void testOne() throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException {
		NetworkNode node1 = new NetworkNode("node1");
		NetworkNode node2 = new NetworkNode("node2");
		NetworkNode node3 = new NetworkNode("node3");
		NetworkNode node4 = new NetworkNode("node4");
		NetworkNode node5 = new NetworkNode("node5");

		node1.addNode(node2);
		node1.addNode(node3);
		node1.addNode(node4);
		node1.addNode(node5);
		node2.addNode(node1);
		node2.addNode(node3);
		node2.addNode(node4);
		node2.addNode(node5);
		node3.addNode(node1);
		node3.addNode(node2);
		node3.addNode(node4);
		node3.addNode(node5);
		node4.addNode(node1);
		node4.addNode(node2);
		node4.addNode(node3);
		node4.addNode(node5);
		node5.addNode(node1);
		node5.addNode(node2);
		node5.addNode(node3);
		node5.addNode(node4);

		node2.createPing();
		// node3.createPing();
		// node4.createPing();
		// node5.createPing();

		System.out.println("now for direct messaging stuff");
		System.out.println(node1.getNodeInfoList().toString());
		NodeInfo node2Info = node1.getNodeInfoList().get("node2");
		Location node2Loc = node2Info.getMyLocation();
		System.out.println("Node 2: Time: " + node2Info.getLastPingTime() + " and locx: " + node2Loc.getX()
				+ " and locy: " + node2Loc.getY());

//		NodeInfo node3Info = node1.getNodeInfoList().get("node3");
//		Location node3Loc = node3Info.getMyLocation();
//		System.out.println("Node 3: Time: " + node3Info.getLastPingTime() + " and locx: " + node3Loc.getX()
//				+ " and locy: " + node3Loc.getY());
//
//		NodeInfo node4Info = node1.getNodeInfoList().get("node4");
//		Location node4Loc = node4Info.getMyLocation();
//		System.out.println("Node 4: Time: " + node4Info.getLastPingTime() + " and locx: " + node4Loc.getX()
//				+ " and locy: " + node4Loc.getY());
//
//		NodeInfo node5Info = node1.getNodeInfoList().get("node5");
//		Location node5Loc = node5Info.getMyLocation();
//		System.out.println("Node 5: Time: " + node5Info.getLastPingTime() + " and locx: " + node5Loc.getX()
//				+ " and locy: " + node5Loc.getY());
//
 		Thread.sleep(1000);

		node2.createPing();
		System.out.println("trying to change node 2");
		node2Info = node1.getNodeInfoList().get("node2");
		node2Loc = node2Info.getMyLocation();
		System.out.println("Node 2: Time: " + node2Info.getLastPingTime() + " and locx: " + node2Loc.getX()
				+ " and locy: " + node2Loc.getY());
	}
}
