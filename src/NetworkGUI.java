
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

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

	/* Private instance variables */
	public JTextField diffString;
	public JTextField numNodes;
	public JTextField commRadString;
	public JTextField Send;
	public JTextField Receive;
	public JTextField messageText;
	public JTextField randMessage;

	public ArrayList<Node> networkNodes = new ArrayList<Node>();

	public int nodeIDCounter = 0;
	public int difficulty = 1;
	public int numberOfNodes = 10;
	public int communicationRadius = 10;
	public int MAXSIZE = 800;
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
		this.randMessage.addActionListener(this);
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

		add(new JButton("Generate Network Graphic"), WEST);

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

		// Creating a new Group
		add(new JLabel("Random Message Below"), WEST); // space holder
		this.randMessage = new JTextField(TEXT_FIELD_SIZE);
		add(this.randMessage, WEST);
		add(new JButton("Random Message"), WEST);
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
		// Change Status is clicked or user clicked enter after entering a
		// status in the text field
		else if (e.getActionCommand().equals("Number of Nodes")
				|| e.getSource() == this.numNodes && !this.numNodes.getText().equals("")) {
			this.numberOfNodes = Integer.parseInt(this.numNodes.getText());
			System.out.println("Your numberOfNodes is: " + this.numberOfNodes);

		}
		// Change Picture is clicked or user clicked enter after entering
		// picture name into the text field
		else if (e.getActionCommand().equals("Comm radius")
				|| e.getSource() == this.commRadString && !this.commRadString.getText().equals("")) {
			System.out.println("Radius is as big as your mom");// extractButtonFunctionality();
			this.communicationRadius = Integer.parseInt(this.commRadString.getText());
			System.out.println("Your communication radius is: " + this.communicationRadius);
		}
		// Add Friend is clicked or user clicked enter after entering a friends
		// name into the text field
		else if (e.getActionCommand().equals("Send Message")
				|| e.getSource() == this.messageText && !this.messageText.getText().equals("")) {
			// System.out.println("Send
			// Message");//addFriendFunctionality(enteredName);
			try {
				System.out.println("I should be getting here");
				sendMessage(this.messageText.getText(), this.Send.getText(), this.Receive.getText());
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// Create a group is clicked or user clicked enter after entering a
		// group name into the text field
		else if (e.getActionCommand().equals("Random Message")
				|| e.getSource() == this.randMessage && !this.randMessage.getText().equals("")) {
			System.out.println("You are a Random");// createGroupFunctionality();
		} else if (e.getActionCommand().equals("Generate Network Graphic")) {
			// generateNodeNetwork();
			try {
				generateNodes();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		}
	}
	private void sendMessage(String message, String sender, String receiver) throws NoSuchAlgorithmException, NoSuchProviderException {
		recolorNodes();
		Node senderNode = null;
		Node receiverNode = null;
		for (int i = 0; i < networkNodes.size(); i++) {
			for (int j = 0; j < networkNodes.size(); j++) {
				String nodeNameSend = "Node" + networkNodes.get(i).nodeID;
				String nodeNameRec = "Node" + networkNodes.get(j).nodeID;
				if (networkNodes.get(i).nodeID.equals(sender) && networkNodes.get(j).nodeID.equals(receiver)) {
					senderNode = networkNodes.get(i);
					receiverNode = networkNodes.get(j);
					senderNode.setNodeValues(senderNode.getXCoord(), senderNode.getYCoord(), 
							Color.RED, senderNode.getWidth());
					senderNode.Draw(g);
					receiverNode.setNodeValues(receiverNode.getXCoord(), receiverNode.getYCoord(), 
							Color.YELLOW, receiverNode.getWidth());
					receiverNode.Draw(g);
				}
			}
		}
	}
	private void recolorNodes() {
		// TODO Auto-generated method stub
		for (int i = 0; i < networkNodes.size(); i++) {
			networkNodes.get(i).setColor(Color.BLUE);
			networkNodes.get(i).Draw(g);
		}
	}
	private void generateNodes() throws NoSuchAlgorithmException, NoSuchProviderException {
		Node n;
		this.g = this.canvas.getGraphics();
		for (int i = 0; i < this.numberOfNodes; i++) {
			int randomX = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			int randomY = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			n = new Node("Node" + nodeIDCounter);
			n.setBlockChainDifficulty(this.difficulty);
			n.setNodeValues(randomX, randomY, Color.BLUE, OFFSET);
			networkNodes.add(n);
			this.nodeIDCounter++;
			n.Draw(g);
		}
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
