import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class NodeGUI extends Program {

	/** Number of characters for each of the text input fields */
	public static final int TEXT_FIELD_SIZE = 15;
	public static final int MAXMOVE = 50;
	public static final int MAXSIZE = 800;
	public static final int MAXSIZEW = 800;

	/* Private instance variables */
	public JTextField diffString;
	public JTextField numNodes;
	public JTextField commRadString;
	public JTextField Send;
	public JTextField Receive;
	public JTextField messageText;
	public JTextField randMessage;
	public JTextField removeNode;
	public JTextField setMyNode;
	public JTextField resourceRequester;
	public JTextField resourceAmount;
	public JTextField resourceType;
	public JTextField resourceCategory;
	public JTextField acceptNumber;

	public ArrayList<SimulationNode> networkNodes = new ArrayList<SimulationNode>();

	public int nodeIDCounter = 0;

	public int difficulty = 5;
	public int numberOfNodes = 10;
	public int communicationRadius = 300;
	public int OFFSET = 15;

	public int messageNumber = 1;

	public Canvas canvas = new Canvas();
	public Random rand = new Random();
	public Graphics g = this.canvas.getGraphics();

	public Node myNode = null;

	/**
	 * This method has the responsibility for initializing the interactors in
	 * the application, and taking care of any other initialization that needs
	 * to be performed.
	 */
	@Override
	public void init() {
		this.setSize(new Dimension(2000, 850));

		// fields on the West Side of the screen
		generateWestFrame();

		// Action listeners
		addActionListeners();
		addListeners();

		add(this.canvas);
	}

	private void addListeners() {
		// this.messageText.addActionListener(this);
		this.setMyNode.addActionListener(this);
		this.resourceType.addActionListener(this);
		this.resourceAmount.addActionListener(this);
		this.resourceCategory.addActionListener(this);
	}

	private void generateWestFrame() {
		add(new JButton("Begin Simulation"), WEST);
		add(new JButton("Generate Nodes"), WEST);

		// // Joining a group
		// add(new JLabel("Enter Message Below"), WEST); // space holder
		// this.messageText = new JTextField(TEXT_FIELD_SIZE);
		// add(this.messageText, WEST);
		// this.Send = new JTextField(TEXT_FIELD_SIZE);
		// this.Receive = new JTextField(TEXT_FIELD_SIZE);
		// add(new JLabel("Enter Sender Below"), WEST);
		// add(this.Send, WEST);
		// add(new JLabel("Enter Receiver Below"), WEST);
		// add(this.Receive, WEST);
		// add(new JButton("Send Message"), WEST);

		add(new JLabel("Enter resource Requester"), WEST); // space holder
		this.resourceRequester = new JTextField(TEXT_FIELD_SIZE);
		add(this.resourceRequester, WEST);
		add(new JLabel("Enter the Supply"), WEST);
		this.resourceType = new JTextField(TEXT_FIELD_SIZE);
		add(this.resourceType, WEST);
		add(new JLabel("Enter the Amount"), WEST);
		this.resourceAmount = new JTextField(TEXT_FIELD_SIZE);
		add(this.resourceAmount, WEST);
		add(new JLabel("Enter the Category"), WEST);
		this.resourceCategory = new JTextField(TEXT_FIELD_SIZE);
		add(this.resourceCategory, WEST);
		add(new JButton("Request Resources"), WEST);

		add(new JLabel("Accept Number"), WEST);
		this.acceptNumber = new JTextField(TEXT_FIELD_SIZE);
		add(this.acceptNumber, WEST);
		add(new JButton("Accept Request"), WEST);

		// Remove a node
		this.removeNode = new JTextField(TEXT_FIELD_SIZE);
		add(this.removeNode, WEST);
		add(new JButton("Remove Node"), WEST);
		//
		// // // Creating a new Group
		// add(new JLabel("Random Message Below"), WEST); // space holder;
		// add(new JButton("Random Message"), WEST);

		// to move the location of the nodes
		add(new JButton("Move Nodes"), WEST);

		// // to move the location of the nodes
		// add(new JButton("Reset Nodes"), WEST);
		//
		// // to move the location of the nodes
		// add(new JButton("Input New Lines"), WEST);

		// // to ping everybody
		// add(new JButton("Ping Every Node"), WEST);

		this.setMyNode = new JTextField(TEXT_FIELD_SIZE);
		add(this.setMyNode, WEST);
		add(new JButton("Set My Node"), WEST);

//		add(new JButton("Input New Lines"), WEST);

		add(new JButton("Check Accepted"), WEST);
		
		add(new JButton("Global View"), WEST);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Lookup button is clicked
		if (e.getActionCommand().equals("Difficulty") && !this.diffString.getText().equals("")) {
			this.difficulty = Integer.parseInt(this.diffString.getText());
			//System.out.println("Your difficulty is: " + this.difficulty);
		} else if (e.getActionCommand().equals("Number of Nodes")
				|| e.getSource() == this.numNodes && !this.numNodes.getText().equals("")) {
			this.numberOfNodes = Integer.parseInt(this.numNodes.getText());
			//System.out.println("Your numberOfNodes is: " + this.numberOfNodes);
		}

		else if (e.getActionCommand().equals("Remove Node")
				|| e.getSource() == this.commRadString && !this.commRadString.getText().equals("")) {
			removeNode(this.removeNode.getText());
		}

		else if (e.getActionCommand().equals("Comm radius")
				|| e.getSource() == this.commRadString && !this.commRadString.getText().equals("")) {
			this.communicationRadius = Integer.parseInt(this.commRadString.getText());
			//System.out.println("Your communication radius is: " + this.communicationRadius);
		} else if (e.getActionCommand().equals("Random Message")
				|| e.getSource() == this.commRadString && !this.commRadString.getText().equals("")) {
			try {
				generateRandomMessages();
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Send Message")
				|| e.getSource() == this.messageText && !this.messageText.getText().equals("")) {
			try {
				//System.out.println("I should be getting here");
				sendMessage(this.messageText.getText(), this.Send.getText(), this.Receive.getText());
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Random Message")
				|| e.getSource() == this.randMessage && !this.randMessage.getText().equals("")) {
		} else if (e.getActionCommand().equals("Reset Nodes")) {
			resetNodesCommunicationLines();
		} else if (e.getActionCommand().equals("Input New Lines")) {
			generateCommunicationLines();
			generateLineToFriends();
		} else if (e.getActionCommand().equals("Begin Simulation")) {
			beginSimulation();
		} else if (e.getActionCommand().equals("Generate Nodes")) {
			try {
				//System.out.println("Hello I am here");
				generateNodes();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Move Nodes")) {
			moveNodes();
		} else if (e.getActionCommand().equals("Ping Every Node")) {
			globalPingCreation();
		} else if (e.getActionCommand().equals("Set My Node")) {
			try {
				setMyNode();
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Request Resources")) {
			try {
				generateResourceRequest();
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Accept Request")) {
			acceptResourceRequest();

		} else if (e.getActionCommand().equals("Check Accepted")) {
			generateAcceptedMessages();
		} else if (e.getActionCommand().equals("Global View")) {
			globalView();
		}
	}

	private void globalView() {
		drawMessages();
	}

	private void generateAcceptedMessages() {
		generateAcceptedMessageBoard();
		g.setColor(Color.WHITE);
		if (((SimulationNode) this.myNode).getAcceptedMessages() != null) {
			for (int i = 0; i < ((SimulationNode) this.myNode).getAcceptedMessages().size(); i++) {
				String messageNumber = "" + ((Resource) (((SimulationNode) this.myNode)
						.getAcceptedMessages().get(0)).getMessageData()).messageNumber;
				String resourceRequested = ((Resource) (((SimulationNode) this.myNode)
						.getAcceptedMessages().get(0)).getMessageData()).type;
				String resourceAmount = "" + ((Resource) (((SimulationNode) this.myNode)
						.getAcceptedMessages().get(0)).getMessageData()).getAmount();
				String destination = ((Resource) (((SimulationNode) this.myNode)
						.getAcceptedMessages().get(0)).getMessageData()).getOwnerName();
				g.drawString(messageNumber, MAXSIZE + 5, 40 + i * 20);
				g.drawString(resourceRequested, MAXSIZE + 5 + MAXSIZE / 4, 40 + i * 20);
				g.drawString(resourceAmount, MAXSIZE + 5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(destination, MAXSIZE + 5 + 3 * MAXSIZE / 4, 40 + i * 20);
			}
		}
	}

	private void generateAcceptedMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString("Message Number", MAXSIZE + 5, 20);
		g.drawString("Resource Requested", MAXSIZE + 5 + MAXSIZE / 4, 20);
		g.drawString("Amount Requested", MAXSIZE + 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Destination", MAXSIZE + 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(MAXSIZE, 25, 2 * MAXSIZE, 25);
	}

	private void acceptResourceRequest() {
		int messageNum = Integer.parseInt(this.acceptNumber.getText());
		for (int i = 0; i < this.myNode.getMessages().size(); i++) {
			Message currentMessage = this.myNode.getMessages().get(i);
			if (((Resource) currentMessage.getMessageData()).getMessageNumber() == messageNum) {
				((SimulationNode) this.myNode).addAcceptedMessage(currentMessage);
				((SimulationNode) this.myNode).removeMessage(currentMessage);
				globalView();
			}
		}
	}

	private void generateResourceRequest() throws NoSuchAlgorithmException, NoSuchProviderException {
		SimulationNode nodeRequesting = null;
		for (int i = 0; i < this.networkNodes.size(); i++) {
			if (this.networkNodes.get(i).getNodeID().toString().equals(this.resourceRequester.getText())) {
				nodeRequesting = this.networkNodes.get(i);
			}
		}
		Resource newRequest = new Resource(Integer.parseInt(this.resourceAmount.getText()), this.resourceType.getText(),
				(double) nodeRequesting.getXCoord(), (double) nodeRequesting.getYCoord(),
				this.resourceCategory.getText(), nodeRequesting.getNodeID(), this.messageNumber);

		recolorNodes();
		Message currentMessage = null;
		nodeRequesting.setNodeValues(nodeRequesting.getXCoord(), nodeRequesting.getYCoord(), Color.RED,
				nodeRequesting.getWidth());
		nodeRequesting.Draw(g);
		currentMessage = new ResourceRequest(newRequest, null);
		nodeRequesting.createMessage(currentMessage);

		for (int o = 0; o < this.networkNodes.size(); o++) {
			SimulationNode currentNode = this.networkNodes.get(o);
			for (int p = 0; p < currentNode.getMessages().size(); p++) {
				if (currentNode.getMessages().get(p).getMessageData().toString()
						.equals(currentMessage.getMessageData().toString())) {

					if (!currentNode.equals(nodeRequesting)) {
						currentNode.setNodeValues(currentNode.getXCoord(), currentNode.getYCoord(), Color.GREEN,
								currentNode.getWidth());
						currentNode.Draw(g);
					}
				}

			}
		}
		this.messageNumber++;
		drawMessages();
	}

	public void drawMessages() {
		generateMessageBoard();
		g.setColor(Color.WHITE);
		for (int i = 0; i < this.myNode.getMessages().size(); i++) {
			String messageNumber = ""
					+ ((Resource) this.myNode.getMessages().get(i).getMessageData()).getMessageNumber();
			String resourceRequested = ((Resource) this.myNode.getMessages().get(i).getMessageData()).type;
			String resourceAmount = "" + ((Resource) this.myNode.getMessages().get(i).getMessageData()).getAmount();
			String originator = ((Resource) this.myNode.getMessages().get(i).getMessageData()).getOwnerName();
			g.drawString(messageNumber, MAXSIZE + 5, 40 + i * 20);
			g.drawString(resourceRequested, MAXSIZE + 5 + MAXSIZE / 4, 40 + i * 20);
			g.drawString(resourceAmount, MAXSIZE + 5 + 2 * MAXSIZE / 4, 40 + i * 20);
			g.drawString(originator, MAXSIZE + 5 + 3 * MAXSIZE / 4, 40 + i * 20);
		}
	}

	private void setMyNode() throws NoSuchAlgorithmException, NoSuchProviderException {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.networkNodes.size(); i++) {
			if (this.networkNodes.get(i).getNodeID().toString().equals(this.setMyNode.getText())) {
				this.myNode = this.networkNodes.get(i);
				((SimulationNode) this.myNode).setNodeValues(((SimulationNode) this.myNode).getXCoord(), 
						((SimulationNode) this.myNode).getYCoord(), Color.CYAN,
						((SimulationNode) this.myNode).getWidth());
				((SimulationNode) this.myNode).Draw(g);
				globalView();
			}
		}
	}

	private void beginSimulation() {
		// TODO Auto-generated method stub
		this.g = this.canvas.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		g.setColor(Color.BLACK);

		generateMessageBoard();
	}

	private void generateMessageBoard() {
		// TODO Auto-generated method stub
		g.setColor(Color.DARK_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE); // Here
		g.drawString("Message Number", MAXSIZE + 5, 20);
		g.drawString("Resource Requested", MAXSIZE + 5 + MAXSIZE / 4, 20);
		g.drawString("Amount Requested", MAXSIZE + 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Originator", MAXSIZE + 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(MAXSIZE, 25, 2 * MAXSIZE, 25);
	}

	private void generateRandomMessages() throws NoSuchAlgorithmException, NoSuchProviderException {
		// TODO Auto-generated method stub
		ArrayList<String> messageContext = new ArrayList<String>();
		messageContext.add("a");
		messageContext.add("b");
		messageContext.add("c");
		messageContext.add("d");
		messageContext.add("e");
		messageContext.add("f");
		messageContext.add("g");
		messageContext.add("h");
		messageContext.add("i");
		messageContext.add("j");
		for (int i = 0; i < messageContext.size(); i++) {
			int random1 = rand.nextInt(this.networkNodes.size() - 1);
			int random2 = rand.nextInt(this.networkNodes.size() - 1);
			this.sendMessage(messageContext.get(i), this.networkNodes.get(random1).nodeID,
					this.networkNodes.get(random2).nodeID);
		}
	}

	private void globalPingCreation() {
		for (int i = 0; i < networkNodes.size(); i++) {
			//System.out.println("I created a pings");
			this.networkNodes.get(i).createPing();
		}
	}

	private void removeNode(String text) {
		Node nodeToRemove = null;
		System.out.println("I am to remove: " + text);
		for (int i = 0; i < networkNodes.size(); i++) {
			if (networkNodes.get(i).getNodeID().equals(text)) {
				nodeToRemove = this.networkNodes.get(i);
			}
		}
		networkNodes.remove(nodeToRemove);
		resetNodesCommunicationLines();
		generateCommunicationLines();
		generateLineToFriends();
		recolorNodes();
	}

	private void resetNodesCommunicationLines() {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		for (int i = 0; i < this.networkNodes.size(); i++) {
			this.networkNodes.get(i).getFriends().clear();
			this.networkNodes.get(i).Draw(g);
		}
		recolorNodes();
	}

	private void moveNodes() {
		for (int i = 0; i < this.networkNodes.size(); i++) {
			this.networkNodes.get(i).moveNode(this.MAXSIZE, this.OFFSET, this.MAXMOVE, this.g);
		}
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		resetNodesCommunicationLines();
		generateCommunicationLines();
		generateLineToFriends();
		// recolorNodes();
	}

	private void sendMessage(String message, String sender, String receiver)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		recolorNodes();
		SimulationNode senderNode = null;
		SimulationNode receiverNode = null;
		Message currentMessage = null;
		for (int i = 0; i < networkNodes.size(); i++) {
			for (int j = 0; j < networkNodes.size(); j++) {
				String nodeNameSend = "Node" + networkNodes.get(i).nodeID;
				String nodeNameRec = "Node" + networkNodes.get(j).nodeID;
				if (networkNodes.get(i).nodeID.equals(sender) && networkNodes.get(j).nodeID.equals(receiver)) {
					senderNode = networkNodes.get(i);
					receiverNode = networkNodes.get(j);
					senderNode.setNodeValues(senderNode.getXCoord(), senderNode.getYCoord(), Color.RED,
							senderNode.getWidth());
					senderNode.Draw(g);
					receiverNode.setNodeValues(receiverNode.getXCoord(), receiverNode.getYCoord(), Color.YELLOW,
							receiverNode.getWidth());
					receiverNode.Draw(g);

					currentMessage = new TextMessage(message, receiverNode);
					senderNode.createMessage(currentMessage);
				}
			}
		}
		for (int o = 0; o < this.networkNodes.size(); o++) {
			SimulationNode currentNode = this.networkNodes.get(o);
			for (int p = 0; p < currentNode.getMessages().size(); p++) {
				if (currentNode.getMessages().get(p).getMessageData().toString()
						.equals(currentMessage.getMessageData().toString())) {

					if (!currentNode.equals(senderNode) && !currentNode.equals(receiverNode)) {
						currentNode.setNodeValues(currentNode.getXCoord(), currentNode.getYCoord(), Color.GREEN,
								currentNode.getWidth());
						//System.out.println("I should be green now");
						currentNode.Draw(g);
					}
				}

			}
		}
	}

	private void recolorNodes() {
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).setColor(Color.BLUE);
			networkNodes.get(i).Draw(g);
		}
	}

	private void generateNodes() throws NoSuchAlgorithmException, NoSuchProviderException {
		//System.out.println("hey printed");
		SimulationNode n;
		this.g = this.canvas.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		//System.out.println("I should generate some nodes");
		for (int i = 0; i < this.numberOfNodes; i++) {
			int randomX = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			int randomY = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			n = new SimulationNode("Node" + nodeIDCounter);
			//System.out.println("Or before");
			n.setBlockChainDifficulty(this.difficulty);
			n.setNodeValues(randomX, randomY, Color.BLUE, OFFSET);
			n.start();
			networkNodes.add(n);
			this.nodeIDCounter++;
			n.Draw(g);
		}
		this.recolorNodes();
		generateCommunicationLines();
		generateLineToFriends();
	}

	private void generateLineToFriends() {
		for (int i = 0; i < this.networkNodes.size(); i++) {
			this.networkNodes.get(i).drawLinesToFriends(this.g);
		}

	}

	private void checkFriends() {
		for (int i = 0; i < networkNodes.size(); i++) {
			for (int j = 0; j < networkNodes.get(i).getFriends().size(); j++) {
				System.out.println("I am " + this.networkNodes.get(i).nodeID + " my friend is: "
						+ this.networkNodes.get(i).getFriends().get(j).nodeID);
			}
		}
	}

	private void generateCommunicationLines() {
		for (int i = 0; i < this.networkNodes.size(); i++) {
			SimulationNode currentNode = this.networkNodes.get(i);
			int xLoc = currentNode.getXCoord();
			int yLoc = currentNode.getYCoord();
			for (int j = 0; j < this.networkNodes.size(); j++) {
				if (i != j) {
					SimulationNode targetNode = this.networkNodes.get(j);
					int targetXLoc = targetNode.getXCoord();
					int targetYLoc = targetNode.getYCoord();
					int euclidDistance = calculateDistance(xLoc, yLoc, targetXLoc, targetYLoc);
					if (euclidDistance < this.communicationRadius) {
						currentNode.addFriend(targetNode);
					}
				} else {
					// do nothing
				}
			}
		}
	}

	private int calculateDistance(int xLoc, int yLoc, int targetXLoc, int targetYLoc) {
		double distance = Math.sqrt(Math.pow((targetXLoc - xLoc), 2) + Math.pow((targetYLoc - yLoc), 2));
		return (int) distance;
	}

	private void generateNodeNetwork() {
		Node n;
		this.g = this.canvas.getGraphics();
		for (int i = 0; i < this.numberOfNodes; i++) {
			int randomX = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			int randomY = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			g.setColor(Color.BLUE);
			g.fillOval(randomX, randomY, OFFSET, OFFSET);
		}
	}
}
