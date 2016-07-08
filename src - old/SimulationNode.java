import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

/*
 * Subclass of Node, simulation specific
 * Parameters: ID string
 */

/* TODO
 * When I am sending a message, I should try and verify the header of the message
 * but I am not sure if that would work or not because then someone could
 * possible alter the contents of it without having to alter the signature.
 */

public class SimulationNode extends Node implements Serializable {

	// Variables
	public int xCoordinate = 0;
	public int yCoordinate = 0;
	public Color color = Color.BLUE;
	public int WIDTH = 0;
	public int BidNumber = 1;
	public ArrayList<Message> acceptedMessages = new ArrayList<Message>();
	public ArrayList<Message> submittedBids = new ArrayList<Message>();
	public ArrayList<Ping> pingsReceived = new ArrayList<Ping>();
	public ArrayList<SimulationNode> networkNodes = new ArrayList<SimulationNode>();

	// Constructor
	public SimulationNode(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		super(id);
	}
	
	public void sendDirectMessage(Node node1, Message message) throws NoSuchAlgorithmException, NoSuchProviderException{
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

	public void addNetworkNode(SimulationNode node) {
		networkNodes.add(node);
	}

	public void addAcceptedMessage(Message msg) {
		this.acceptedMessages.add(msg);
	}

	public ArrayList<Message> getAcceptedMessages() {
		return this.acceptedMessages;
	}
	
	public void createTextMessage(String data) {

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
			Message text = new TextMessage(data, this.getNodeID(), networkNodes.get(receiverNum).getNodeID());

			this.blockChain.add(text);

			localMSG.add(text);
			this.distributeMessage(text); // distribute message to friend nodes
		} // (they will propagate to their
			// friends)
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
	
	
	// Utility
	public void Draw(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.xCoordinate, this.yCoordinate, this.WIDTH, this.WIDTH);
	}

	public void drawLinesToFriends(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < this.networkNodes.size(); i++) {
			SimulationNode friend = (SimulationNode) networkNodes.get(i);
			g.drawLine(this.xCoordinate + this.WIDTH / 2, this.yCoordinate + this.WIDTH / 2,
					friend.getXCoord() + this.WIDTH / 2, friend.getYCoord() + this.WIDTH / 2);
		}
	}

	public void removeBid(Message currentMessage) {
		// TODO Auto-generated method stub
		this.submittedBids.remove(currentMessage);
	}

	public void removeAcceptedMessage(Message message) {
		// TODO Auto-generated method stub
		this.acceptedMessages.remove(message);
	}
	public void addNodes(ArrayList<Node> newNodes) { // add a group of friend
		// nodes
		for (int i = 0; i < newNodes.size(); i++) {
			networkNodes.add(newNodes.get(i));
		}
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
