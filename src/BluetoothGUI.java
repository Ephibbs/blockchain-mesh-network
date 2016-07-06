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

public class BluetoothGUI extends Program{

	public static final int TEXT_FIELD_SIZE = 15;
	public static final int MAXMOVE = 50;
	public static final int MAXSIZE = 800;
	public static final int MAXSIZEW = 800;

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
	public JTextField shortestPathTo;

	public ArrayList<NetworkNode> networkNodes = new ArrayList<NetworkNode>();

	public Canvas canvas = new Canvas();
	public Random rand = new Random();
	public Graphics g = this.canvas.getGraphics();
	public NetworkNode myNode = null;

	/**
	 * This method has the responsibility for initializing the interactors in
	 * the application, and taking care of any other initialization that needs
	 * to be performed.
	 */
	@Override
	public void init() {
		this.setSize(new Dimension(1800, 850));
		generateWestFrame();
		addActionListeners();
		addListeners();
		add(this.canvas);
		try {
			myNode = new NetworkNode("Me");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Don't worry about these
	private void addListeners() {
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
		add(new JButton("Start My Node"), WEST);

		add(new JLabel("Enter resource Requester"), WEST);
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
		
		this.shortestPathTo = new JTextField(TEXT_FIELD_SIZE);
		add(this.shortestPathTo, NORTH);
		add(new JButton("Show Fastest Path"), NORTH);
		
		add(new JButton("Ping Everybody"), WEST);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start My Node")) {
			beginSimulation();
			myNode.start();
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
		} else if (e.getActionCommand().equals("Check Bids")) {
			checkBids();
		} else if (e.getActionCommand().equals("View Resources")) {
			viewNodesResources(this.viewResources.getText());
		} else if (e.getActionCommand().equals("Send Resource")) {
			sendResource();
		} else if (e.getActionCommand().equals("Receive Resource")) {
			receiveResource();
		} 
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
					g.drawString(resourceName, 5 , 75 + o*25);
					g.drawString(resourceAmount,  10 + 0/5, 75+o*25);
					g.drawLine(0, 78 + o*25, MAXSIZE, 78 + o*25);
					o++;
				}
			}
		}
	}

	private void displayBids() {
		g.setColor(Color.WHITE);
		if (myNode.getBids() != null) {
			for (int i = 0; i < myNode.getBids().size(); i++) {
				String bidNumber = ""
						+ ((Bid) (myNode.getBids().get(i)).getMessageData()).getBidNumber();
				String eta = "" + ((Bid) (myNode.getBids().get(i)).getMessageData()).getETA();
				String resourceAmount = ""
						+ ((Bid) (myNode.getBids().get(i)).getMessageData()).getAmount();
				String bidder = ((Bid) (myNode.getBids().get(i)).getMessageData()).getBidder()
						.getNodeID();
				g.drawString(bidNumber,  5, 40 + i * 20);
				g.drawString(eta,  5 + MAXSIZE / 4, 40 + i * 20);
				g.drawString(resourceAmount,  5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(bidder, 5 + 3 * MAXSIZE / 4, 40 + i * 20);
			}
		}
	}

	private void checkBids() {
		generateBidMessageBoard();
		displayBids();
	}

	private void receiveResource() {
		int sendResourceNumber = Integer.parseInt(this.sentResource.getText());
		if(myNode.getAcceptedMessages() != null){
			for(int i = 0; i < myNode.getAcceptedMessages().size(); i++){
				Resource thisResource = (Resource) myNode.getAcceptedMessages().get(i).getMessageData();
				if(thisResource.getMessageNumber() == sendResourceNumber){
					myNode.removeAcceptedMessage(myNode.getAcceptedMessages().get(i));
					myNode.addResource(thisResource.getType(), (+1*thisResource.getAmount()));
				}
			}
		}
		viewNodesResources(myNode.getNodeID());
		
	}

	private void sendResource() {
		int sendResourceNumber = Integer.parseInt(this.sentResource.getText());
		NetworkNode thisNode = (NetworkNode) this.myNode;
		if(myNode.getAcceptedMessages() != null){
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
	
	private void acceptBid() {
		int messageNum = Integer.parseInt(this.bidNumber.getText());
		for (int i = 0; i < myNode.getBids().size(); i++) {
			Message currentMessage = myNode.getBids().get(i);
			Bid bidObject = ((Bid) currentMessage.getMessageData());
			if (((Bid) currentMessage.getMessageData()).getBidNumber() == messageNum) {
				myNode.removeBid(currentMessage);
				String bidder = ((Bid) currentMessage.getMessageData()).getBidder().getNodeID();
				int messNum = ((Bid) currentMessage.getMessageData()).getMessageNumber();
				Message acceptedMessage = null;
				for (int o = 0; o < this.networkNodes.size(); o++) {
					for (int k = 0; k < this.networkNodes.get(o).getMessages().size(); k++) {
						Resource mess = ((Resource) ((NetworkNode) this.networkNodes.get(o)).getMessages().get(k)
								.getMessageData());
						NetworkNode simNode = ((NetworkNode) this.networkNodes.get(o));
						if (mess.getMessageNumber() == messNum) {
							if (bidObject.getBidder().getNodeID().equals(this.networkNodes.get(o).getNodeID())) {
								simNode.addAcceptedMessage(simNode.getMessages().get(k));
								myNode.addAcceptedMessage(simNode.getMessages().get(k));
								//simNode.removeGlobalMessage(simNode.getMessages().get(k));
							}
						}
					}
				}
				//myNode.removeGlobalMessage(currentMessage);
			}
		}
	}

	private void generateAcceptedMessages() {
		generateAcceptedMessageBoard();
		g.setColor(Color.WHITE);
		if (myNode.getAcceptedMessages() != null) {
			for (int i = 0; i < myNode.getAcceptedMessages().size(); i++) {
				String messageNumber = "" + ((Resource) (myNode.getAcceptedMessages().get(i))
						.getMessageData()).messageNumber;
				String resourceRequested = ((Resource) (myNode.getAcceptedMessages().get(i))
						.getMessageData()).type;
				String resourceAmount = ""
						+ ((Resource) (myNode.getAcceptedMessages().get(0)).getMessageData())
								.getAmount();
				String destination = ((Resource) (myNode.getAcceptedMessages().get(i))
						.getMessageData()).getOwnerName();
				g.drawString(messageNumber,  5, 40 + i * 20);
				g.drawString(resourceRequested,  5 + MAXSIZE / 4, 40 + i * 20);
				g.drawString(resourceAmount,  5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(destination,  5 + 3 * MAXSIZE / 4, 40 + i * 20);
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
						NetworkNode requestingNode = this.networkNodes.get(j);
						Resource oldResource = ((Resource) currentMessage.getMessageData());
						Bid newBid = new Bid(this.myNode, requestingNode, Integer.parseInt(this.eta.getText()),
								Integer.parseInt(this.amount.getText()), oldResource.getMessageNumber());
						ResourceRequestBid newRequestBid = new ResourceRequestBid(newBid, this.myNode);
						requestingNode.addBid(newRequestBid);
					}
				}
			}
		}
	}

	private void generateResourceRequest() throws NoSuchAlgorithmException, NoSuchProviderException {
		NetworkNode nodeRequesting = null;
		for (int i = 0; i < this.networkNodes.size(); i++) {
			if (this.networkNodes.get(i).getNodeID().toString().equals(myNode.getNodeID())) {
				nodeRequesting = this.networkNodes.get(i);
			}
		}
		Resource newRequest = new Resource(Integer.parseInt(this.resourceAmount.getText()), this.resourceType.getText(),
				(double) nodeRequesting.getXCoord(), (double) nodeRequesting.getYCoord(),
				this.resourceCategory.getText(), nodeRequesting.getNodeID(), rand.nextInt(100000000));
		
		Message currentMessage = null;
		nodeRequesting.setNodeValues(nodeRequesting.getXCoord(), nodeRequesting.getYCoord(), Color.RED,
				nodeRequesting.getWidth());
		//nodeRequesting.Draw(g);
		currentMessage = new ResourceRequest(newRequest, null);
		nodeRequesting.createMessage(currentMessage);

		for (int o = 0; o < this.networkNodes.size(); o++) {
			NetworkNode currentNode = this.networkNodes.get(o);
			for (int p = 0; p < currentNode.getMessages().size(); p++) {
				if (currentNode.getMessages().get(p).getMessageData().toString()
						.equals(currentMessage.getMessageData().toString())) {

					if (!currentNode.equals(nodeRequesting)) {
						currentNode.setNodeValues(currentNode.getXCoord(), currentNode.getYCoord(), Color.GREEN,
								currentNode.getWidth());
					}
				}

			}
		}
	}

	private void generateNodesResourcesBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 , 0, MAXSIZE / 5, MAXSIZE);
		g.drawRect(MAXSIZE / 5, 0, 4 * MAXSIZE / 5, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString(this.viewResources.getText() + "'s resources at Location ", 5 + 2 * MAXSIZE / 5, 20);
		g.drawString("Resource Name", 5, 45);
		g.drawString("Quantity", 5 + MAXSIZE / 5, 45);
		g.drawLine(0, 25,  MAXSIZE, 25);
		g.drawLine(0, 50,  MAXSIZE, 50);

	}

	private void generateMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 + 0, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4 + 0, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4 + 0, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE); // Here
		g.drawString("Message Number", 5, 20);
		g.drawString("Resource Requested", 5 + MAXSIZE / 4, 20);
		g.drawString("Amount Requested", 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Originator", 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(0, 25, MAXSIZE, 25);
	}

	private void beginSimulation() {
		this.g = this.canvas.getGraphics();
//		g.setColor(Color.LIGHT_GRAY);
//		g.fillRect(0, 0, MAXSIZE, MAXSIZE);
		g.setColor(Color.BLACK);

		generateMessageBoard();
	}

	private void sendMessage(String message, String sender, String receiver)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		NetworkNode senderNode = myNode;
		NetworkNode receiverNode = null;
		Message currentMessage = null;
		for (int i = 0; i < networkNodes.size(); i++) {
			String nodeNameRec = "Node" + networkNodes.get(i).nodeID;
			if (networkNodes.get(i).nodeID.equals(receiver)) {
				receiverNode = networkNodes.get(i);
				currentMessage = new TextMessage(message, receiverNode);
				myNode.addMessage(currentMessage);
			}
		}
	}

}