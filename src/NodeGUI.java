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
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import javax.swing.*;

public class NodeGUI extends Program {

	public static final int TEXT_FIELD_SIZE = 15;
	public static final int MAXMOVE = 50;
	public static final int MAXSIZE = 800;
	public static final int MAXSIZEW = 800;

	public JTextField removeNode;
	public JTextField setMyNode;
	public JTextField resourceRequester;
	public JTextField resourceAmount;
	public JTextField resourceType;
	public JTextField resourceCategory;
	public JTextField acceptNumber;
	public JTextField bidNumber;
	public JTextField amount;
	public JTextField eta;
	public JTextField viewResources;
	public JTextField sentResource;
	public JTextField receiveResource;

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
		generateWestFrame();
		addActionListeners();
		addListeners();
		add(this.canvas);
	}

	// Don't worry about these
	private void addListeners() {
		this.setMyNode.addActionListener(this);
		this.resourceType.addActionListener(this);
		this.resourceAmount.addActionListener(this);
		this.resourceCategory.addActionListener(this);
		this.bidNumber.addActionListener(this);
		this.amount.addActionListener(this);
		this.eta.addActionListener(this);
		this.viewResources.addActionListener(this);
		this.sentResource.addActionListener(this);
	}

	// Just the window, don't worry about it
	private void generateWestFrame() {
		add(new JButton("Begin Simulation"), WEST);
		add(new JButton("Generate Nodes"), WEST);

		this.setMyNode = new JTextField(TEXT_FIELD_SIZE);
		add(this.setMyNode, WEST);
		add(new JButton("Set My Node"), WEST);

		add(new JLabel("Enter resource Requester"), WEST);
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

		add(new JLabel("Message Number"), WEST);
		this.acceptNumber = new JTextField(TEXT_FIELD_SIZE);
		add(this.acceptNumber, WEST);
		add(new JLabel("ETA"), WEST);
		this.eta = new JTextField(TEXT_FIELD_SIZE);
		add(this.eta, WEST);
		add(new JLabel("Amount"), WEST);
		this.amount = new JTextField(TEXT_FIELD_SIZE);
		add(this.amount, WEST);
		add(new JButton("Generate Bid"), WEST);

		add(new JLabel("Bid Number"), WEST);
		this.bidNumber = new JTextField(TEXT_FIELD_SIZE);
		add(this.bidNumber, WEST);

		add(new JButton("Accept Bid"), WEST);

//		this.removeNode = new JTextField(TEXT_FIELD_SIZE);
//		add(this.removeNode, WEST);
//		add(new JButton("Remove Node"), WEST);
//
//		add(new JButton("Move Nodes"), WEST);

		add(new JButton("Check Accepted"), WEST);
		add(new JButton("Check Bids"), WEST);

		add(new JButton("Global View"), WEST);

//		this.viewResources = new JTextField(TEXT_FIELD_SIZE);
//		add(this.viewResources, WEST);
//		add(new JButton("View Resources"), WEST);
		
		add(new JButton("Put Initial Resources"), NORTH);
		
		this.viewResources = new JTextField(TEXT_FIELD_SIZE);
		add(this.viewResources, NORTH);
		add(new JButton("View Resources"), NORTH);
		
		this.sentResource = new JTextField(TEXT_FIELD_SIZE);
		add(this.sentResource, NORTH);
		add(new JButton("Send Resource"), NORTH);
		
		this.receiveResource = new JTextField(TEXT_FIELD_SIZE);
		add(this.receiveResource, NORTH);
		add(new JButton("Receive Resource"), NORTH);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Reset Nodes")) {
			resetNodesCommunicationLines();
		} else if (e.getActionCommand().equals("Input New Lines")) {
			generateCommunicationLines();
			generateLineToFriends();
		} else if (e.getActionCommand().equals("Begin Simulation")) {
			beginSimulation();
		} else if (e.getActionCommand().equals("Generate Nodes")) {
			try {
				generateNodes();
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Move Nodes")) {
			moveNodes();
		} else if (e.getActionCommand().equals("Set My Node")) {
			try {
				setMyNode();
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Request Resources")) {
			try {
				generateResourceRequest();
			} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Generate Bid")) {
			generateBid();

		} else if (e.getActionCommand().equals("Accept Bid")) {
			acceptBid();
		} else if (e.getActionCommand().equals("Check Accepted")) {
			generateAcceptedMessages();
		} else if (e.getActionCommand().equals("Global View")) {
			globalView();
		} else if (e.getActionCommand().equals("Check Bids")) {
			checkBids();
		} else if (e.getActionCommand().equals("View Resources")) {
			viewNodesResources(this.viewResources.getText());
		} else if (e.getActionCommand().equals("Send Resource")) {
			sendResource();
		} else if (e.getActionCommand().equals("Put Initial Resources")) {
			putInitResources();
		} else if (e.getActionCommand().equals("Receive Resource")) {
			receiveResource();
		}
	}

	
	private void receiveResource() {
		int sendResourceNumber = Integer.parseInt(this.sentResource.getText());
		SimulationNode thisNode = (SimulationNode) this.myNode;
		if(((SimulationNode) this.myNode).getAcceptedMessages() != null){
			for(int i = 0; i < thisNode.getAcceptedMessages().size(); i++){
				Resource thisResource = (Resource) thisNode.getAcceptedMessages().get(i).getMessageData();
				if(thisResource.getMessageNumber() == sendResourceNumber){
					thisNode.removeAcceptedMessage(thisNode.getAcceptedMessages().get(i));
					thisNode.addResource(thisResource.getType(), (+1*thisResource.getAmount()));
				}
			}
		}
		viewNodesResources(thisNode.getNodeID());
		
	}

	private void putInitResources() {
		for(int i = 0; i < this.networkNodes.size();i++){
			SimulationNode thisNode = (SimulationNode) this.networkNodes.get(i);
			thisNode.getResources().put("water", 500);
			thisNode.getResources().put("medical supplies", 20);
			thisNode.getResources().put("food", 300);
			thisNode.getResources().put("blankets", 100);
			thisNode.getResources().put("tents", 50);
			
		}
	}

	private void sendResource() {
		int sendResourceNumber = Integer.parseInt(this.sentResource.getText());
		SimulationNode thisNode = (SimulationNode) this.myNode;
		if(((SimulationNode) this.myNode).getAcceptedMessages() != null){
			for(int i = 0; i < thisNode.getAcceptedMessages().size(); i++){
				Resource thisResource = (Resource) thisNode.getAcceptedMessages().get(i).getMessageData();
				if(thisResource.getMessageNumber() == sendResourceNumber){
					thisNode.removeAcceptedMessage(thisNode.getAcceptedMessages().get(i));
					thisNode.addResource(thisResource.getType(), (-1*thisResource.getAmount()));					
				}
			}
		}
		viewNodesResources(thisNode.getNodeID());		
	}

	private void viewNodesResources(String nodeName) {
		generateNodesResourcesBoard();
		for (int i = 0; i < this.networkNodes.size(); i++) {
			if (this.networkNodes.get(i).getNodeID().equals(nodeName)) {
				HashMap<String, Integer> nodeResources = this.networkNodes.get(i).getResources();
				Set<String> nodesR = nodeResources.keySet();
				int o = 0;
				for (String key: nodesR) {
					String resourceName = key;
					String resourceAmount = "" + nodeResources.get(key);
					g.drawString(resourceName,MAXSIZE + 5 , 75 + o*25);
					g.drawString(resourceAmount, MAXSIZE + 10 + MAXSIZE/5, 75+o*25);
					g.drawLine(MAXSIZE, 78 + o*25, 2 * MAXSIZE, 78 + o*25);
					o++;
				}
			}
		}
	}

	private void generateNodesResourcesBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 + MAXSIZE, 0, MAXSIZE / 5, MAXSIZE);
		g.drawRect(MAXSIZE / 5 + MAXSIZE, 0, 4 * MAXSIZE / 5, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString(this.viewResources.getText() + "'s resources at Location ", MAXSIZE + 5 + 2 * MAXSIZE / 5, 20);
		g.drawString("Resource Name", MAXSIZE + 5, 45);
		g.drawString("Quantity", MAXSIZE + 5 + MAXSIZE / 5, 45);
		g.drawLine(MAXSIZE, 25, 2 * MAXSIZE, 25);
		g.drawLine(MAXSIZE, 50, 2 * MAXSIZE, 50);

	}

	private void checkBids() {
		generateBidMessageBoard();
		displayBids();
	}

	private void displayBids() {
		g.setColor(Color.WHITE);
		if (((SimulationNode) this.myNode).getBids() != null) {
			for (int i = 0; i < ((SimulationNode) this.myNode).getBids().size(); i++) {
				String bidNumber = ""
						+ ((Bid) (((SimulationNode) this.myNode).getBids().get(i)).getMessageData()).getBidNumber();
				String eta = "" + ((Bid) (((SimulationNode) this.myNode).getBids().get(i)).getMessageData()).getETA();
				String resourceAmount = ""
						+ ((Bid) (((SimulationNode) this.myNode).getBids().get(i)).getMessageData()).getAmount();
				String bidder = ((Bid) (((SimulationNode) this.myNode).getBids().get(i)).getMessageData()).getBidder()
						.getNodeID();
				g.drawString(bidNumber, MAXSIZE + 5, 40 + i * 20);
				g.drawString(eta, MAXSIZE + 5 + MAXSIZE / 4, 40 + i * 20);
				g.drawString(resourceAmount, MAXSIZE + 5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(bidder, MAXSIZE + 5 + 3 * MAXSIZE / 4, 40 + i * 20);
			}
		}
	}

	private void acceptBid() {
		int messageNum = Integer.parseInt(this.bidNumber.getText());
		for (int i = 0; i < ((SimulationNode) this.myNode).getBids().size(); i++) {
			Message currentMessage = ((SimulationNode) this.myNode).getBids().get(i);
			Bid bidObject = ((Bid) currentMessage.getMessageData());
			if (((Bid) currentMessage.getMessageData()).getBidNumber() == messageNum) {
				((SimulationNode) this.myNode).removeBid(currentMessage);
				String bidder = ((Bid) currentMessage.getMessageData()).getBidder().getNodeID();
				int messNum = ((Bid) currentMessage.getMessageData()).getMessageNumber();
				Message acceptedMessage = null;
				for (int o = 0; o < this.networkNodes.size(); o++) {
					for (int k = 0; k < this.networkNodes.get(o).getMessages().size(); k++) {
						Resource mess = ((Resource) ((SimulationNode) this.networkNodes.get(o)).getMessages().get(k)
								.getMessageData());
						SimulationNode simNode = ((SimulationNode) this.networkNodes.get(o));
						//System.out.println("but at least i got here");
						if (mess.getMessageNumber() == messNum) {
							if (bidObject.getBidder().getNodeID().equals(this.networkNodes.get(o).getNodeID())) {
								//System.out.println("I got here");
								simNode.addAcceptedMessage(simNode.getMessages().get(k));
								((SimulationNode) this.myNode).addAcceptedMessage(simNode.getMessages().get(k));
								simNode.removeGlobalMessage(simNode.getMessages().get(k));
							}
						}
					}
				}
				((SimulationNode) this.myNode).removeGlobalMessage(currentMessage);
				globalView();
			}
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
				String messageNumber = "" + ((Resource) (((SimulationNode) this.myNode).getAcceptedMessages().get(i))
						.getMessageData()).messageNumber;
				String resourceRequested = ((Resource) (((SimulationNode) this.myNode).getAcceptedMessages().get(i))
						.getMessageData()).type;
				String resourceAmount = ""
						+ ((Resource) (((SimulationNode) this.myNode).getAcceptedMessages().get(0)).getMessageData())
								.getAmount();
				String destination = ((Resource) (((SimulationNode) this.myNode).getAcceptedMessages().get(i))
						.getMessageData()).getOwnerName();
				g.drawString(messageNumber, MAXSIZE + 5, 40 + i * 20);
				g.drawString(resourceRequested, MAXSIZE + 5 + MAXSIZE / 4, 40 + i * 20);
				g.drawString(resourceAmount, MAXSIZE + 5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(destination, MAXSIZE + 5 + 3 * MAXSIZE / 4, 40 + i * 20);
			}
		}
	}

	private void generateBidMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4 + MAXSIZE, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString("Bid Number", MAXSIZE + 5, 20);
		g.drawString("Time of Arrival", MAXSIZE + 5 + MAXSIZE / 4, 20);
		g.drawString("Amount Can Send", MAXSIZE + 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Bidder", MAXSIZE + 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(MAXSIZE, 25, 2 * MAXSIZE, 25);
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

	private void generateBid() {
		int messageNum = Integer.parseInt(this.acceptNumber.getText());
		for (int i = 0; i < this.myNode.getMessages().size(); i++) {
			Message currentMessage = this.myNode.getMessages().get(i);
			if (((Resource) currentMessage.getMessageData()).getMessageNumber() == messageNum) {
				String ownerName = ((Resource) currentMessage.getMessageData()).getOwnerName();
				for (int j = 0; j < this.networkNodes.size(); j++) {
					if (this.networkNodes.get(j).getNodeID().equals(ownerName)) {
						SimulationNode requestingNode = this.networkNodes.get(j);
						Resource oldResource = ((Resource) currentMessage.getMessageData());
						Bid newBid = new Bid(this.myNode, requestingNode, Integer.parseInt(this.eta.getText()),
								Integer.parseInt(this.amount.getText()), oldResource.getMessageNumber());
						ResourceRequestBid newRequestBid = new ResourceRequestBid(newBid, this.myNode);
						requestingNode.addBid(newRequestBid);
					}
				}
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

	private void drawMessages() {
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
		for (int i = 0; i < this.networkNodes.size(); i++) {
			if (this.networkNodes.get(i).getNodeID().toString().equals(this.setMyNode.getText())) {
				this.myNode = this.networkNodes.get(i);
				((SimulationNode) this.myNode).setNodeValues(((SimulationNode) this.myNode).getXCoord(),
						((SimulationNode) this.myNode).getYCoord(), Color.CYAN,
						((SimulationNode) this.myNode).getWidth());
				((SimulationNode) this.myNode).Draw(g);
			}
		}
		globalView();
	}

	private void beginSimulation() {
		this.g = this.canvas.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		g.setColor(Color.BLACK);

		generateMessageBoard();
	}

	private void generateMessageBoard() {
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
		SimulationNode n;
		this.g = this.canvas.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		for (int i = 0; i < this.numberOfNodes; i++) {
			int randomX = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			int randomY = rand.nextInt(MAXSIZE - OFFSET) + OFFSET / 2;
			n = new SimulationNode("Node" + nodeIDCounter);
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