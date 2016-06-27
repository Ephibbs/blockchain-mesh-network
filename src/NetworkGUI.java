import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class NetworkGUI extends Program {

	/** Number of characters for each of the text input fields */
	public static final int TEXT_FIELD_SIZE = 15;
	public static final int MAXMOVE = 50;
	public static final int MAXSIZE = 800;

	/* Private instance variables */
	public JTextField diffString;
	public JTextField numNodes;
	public JTextField commRadString;
	public JTextField Send;
	public JTextField Receive;
	public JTextField messageText;
	public JTextField randMessage;
	public JTextField removeNode;

	public ArrayList<Node> networkNodes = new ArrayList<Node>();

	public int nodeIDCounter = 0;
	public int difficulty = 5;
	public int numberOfNodes = 100;
	public int communicationRadius = 100;
	public int OFFSET = 15;

	public Canvas canvas = new Canvas();
	public Random rand = new Random();
	public Graphics g = this.canvas.getGraphics();

	/**
	 * This method has the responsibility for initializing the interactors in
	 * the application, and taking care of any other initialization that needs
	 * to be performed.
	 */
	@Override
	public void init() {
		this.setSize(new Dimension(1100, 850));
		// fields on the West Side of the screen
		generateWestFrame();

		// Action listeners
		addActionListeners();
		addListeners();

		add(this.canvas);
	}

	private void addListeners() {
		this.diffString.addActionListener(this);
		this.numNodes.addActionListener(this);
		this.commRadString.addActionListener(this);
		//this.randMessage.addActionListener(this);
		this.messageText.addActionListener(this);
	}

	private void generateWestFrame() {
		add(new JLabel("Enter the Hash Difficulty below"), WEST);
		this.diffString = new JTextField(TEXT_FIELD_SIZE);
		add(this.diffString, WEST);
		add(new JButton("Difficulty"), WEST);

		add(new JLabel("Number of Nodes in Network Below"), WEST); // space
																	// holder
		this.numNodes = new JTextField(TEXT_FIELD_SIZE);
		add(this.numNodes, WEST);
		add(new JButton("Number of Nodes"), WEST);

		add(new JLabel("Communication Radius Below"), WEST); // space holder
		this.commRadString = new JTextField(TEXT_FIELD_SIZE);
		add(this.commRadString, WEST);
		add(new JButton("Comm radius"), WEST);

		add(new JButton("Generate Nodes"), WEST);

		// Joining a group
		add(new JLabel("Enter Message Below"), WEST); // space holder
		this.messageText = new JTextField(TEXT_FIELD_SIZE);
		add(this.messageText, WEST);
		this.Send = new JTextField(TEXT_FIELD_SIZE);
		this.Receive = new JTextField(TEXT_FIELD_SIZE);
		add(new JLabel("Enter Sender Below"), WEST);
		add(this.Send, WEST);
		add(new JLabel("Enter Receiver Below"), WEST);
		add(this.Receive, WEST);
		add(new JButton("Send Message"), WEST);

		// Remove a node
		this.removeNode = new JTextField(TEXT_FIELD_SIZE);
		add(this.removeNode, WEST);
		add(new JButton("Remove Node"), WEST);
//
//		// Creating a new Group
//		add(new JLabel("Random Message Below"), WEST); // space holder
//		this.randMessage = new JTextField(TEXT_FIELD_SIZE);
//		add(this.randMessage, WEST);
//		add(new JButton("Random Message"), WEST);

		// to move the location of the nodes
		add(new JButton("Move Nodes"), WEST);

		// to move the location of the nodes
		add(new JButton("Reset Nodes"), WEST);

		// to move the location of the nodes
		add(new JButton("Input New Lines"), WEST);

		// to ping everybody
		add(new JButton("Ping Every Node"), WEST);
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
			System.out.println("Your difficulty is: " + this.difficulty);
		}
		else if (e.getActionCommand().equals("Number of Nodes")
				|| e.getSource() == this.numNodes && !this.numNodes.getText().equals("")) {
			this.numberOfNodes = Integer.parseInt(this.numNodes.getText());
			System.out.println("Your numberOfNodes is: " + this.numberOfNodes);
		}

		else if (e.getActionCommand().equals("Remove Node")
				|| e.getSource() == this.commRadString && !this.commRadString.getText().equals("")) {
			removeNode(this.removeNode.getText());
		}

		else if (e.getActionCommand().equals("Comm radius")
				|| e.getSource() == this.commRadString && !this.commRadString.getText().equals("")) {
			this.communicationRadius = Integer.parseInt(this.commRadString.getText());
			System.out.println("Your communication radius is: " + this.communicationRadius);
		}
		else if (e.getActionCommand().equals("Send Message")
				|| e.getSource() == this.messageText && !this.messageText.getText().equals("")) {
			try {
				System.out.println("I should be getting here");
				sendMessage(this.messageText.getText(), this.Send.getText(), this.Receive.getText());
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getActionCommand().equals("Random Message")
				|| e.getSource() == this.randMessage && !this.randMessage.getText().equals("")) {
		} else if (e.getActionCommand().equals("Reset Nodes")) {
			resetNodesCommunicationLines();
		} else if (e.getActionCommand().equals("Input New Lines")) {
			generateCommunicationLines();
			generateLineToFriends();
		}

		else if (e.getActionCommand().equals("Generate Nodes")) {
			try {
				System.out.println("Hello I am here");
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
		}
	}

	private void globalPingCreation() {
		for (int i = 0; i < networkNodes.size(); i++) {
			System.out.println("I created a pings");
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
		//recolorNodes();
	}

	private void sendMessage(String message, String sender, String receiver)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		recolorNodes();
		Node senderNode = null;
		Node receiverNode = null;
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
			Node currentNode = this.networkNodes.get(o);
			for (int p = 0; p < currentNode.getMessages().size(); p++) {
				if (currentNode.getMessages().get(p).getMessageData().toString()
						.equals(currentMessage.getMessageData().toString())) {

					if (!currentNode.equals(senderNode) && !currentNode.equals(receiverNode)) {
						currentNode.setNodeValues(currentNode.getXCoord(), currentNode.getYCoord(), Color.GREEN,
								currentNode.getWidth());
						System.out.println("I should be green now");
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
		System.out.println("hey printed");
		Node n;
		this.g = this.canvas.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		System.out.println("I should generate some nodes");
		for (int i = 0; i < this.numberOfNodes; i++) {
			int randomX = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			int randomY = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			n = new Node("Node" + nodeIDCounter);
			System.out.println("Or before");
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
		// checkFriends();
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
			Node currentNode = this.networkNodes.get(i);
			int xLoc = currentNode.getXCoord();
			int yLoc = currentNode.getYCoord();
			for (int j = 0; j < this.networkNodes.size(); j++) {
				if (i != j) {
					Node targetNode = this.networkNodes.get(j);
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
		// TODO Auto-generated method stub
		double distance = Math.sqrt(Math.pow((targetXLoc - xLoc), 2) + Math.pow((targetYLoc - yLoc), 2));
		return (int) distance;
	}

	private void generateNodeNetwork() {
		Node n;
		Graphics g = this.canvas.getGraphics();
		for (int i = 0; i < this.numberOfNodes; i++) {
			int randomX = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			int randomY = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			g.setColor(Color.BLUE);
			g.fillOval(randomX, randomY, OFFSET, OFFSET);
		}
	}
}
