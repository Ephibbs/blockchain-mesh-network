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

public class ClientGUI extends Program {

	public static final int TEXT_FIELD_SIZE = 15;
	public static final int MAXMOVE = 50;
	public static final int MAXSIZE = 800;
	public static final int MAXSIZEW = 800;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	public JTextField nodeName;
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
	private boolean nodeCreated = false; // prevent multiple clicks

	/**
	 * This method has the responsibility for initializing the interactors in
	 * the application, and taking care of any other initialization that needs
	 * to be performed.
	 */
	@Override
	public void init() {
		this.setSize(new Dimension(1900, 850));
		generateWestFrame();
		addActionListeners();
		addListeners();
		add(this.canvas);
	}

	// Don't worry about these
	private void addListeners() {
		this.resourceType.addActionListener(this);
		this.resourceAmount.addActionListener(this);
		this.resourceCategory.addActionListener(this);
		this.bidNumber.addActionListener(this);
		// this.amount.addActionListener(this);
		this.eta.addActionListener(this);
		this.viewResources.addActionListener(this);
		this.sentResource.addActionListener(this);
	}

	// Just the window, don't worry about it
	private void generateWestFrame() {
		add(new JLabel("Enter your Node Name"), WEST);
		this.nodeName = new JTextField(TEXT_FIELD_SIZE);
		add(this.nodeName, WEST);
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
		// add(new JLabel("Amount"), WEST);
		// this.amount = new JTextField(TEXT_FIELD_SIZE);
		// add(this.amount, WEST);
		add(new JButton("Generate Bid"), WEST);

		add(new JLabel("Bid Number"), WEST);
		this.bidNumber = new JTextField(TEXT_FIELD_SIZE);
		add(this.bidNumber, WEST);

		add(new JButton("Accept Bid"), WEST);

		// this.removeNode = new JTextField(TEXT_FIELD_SIZE);
		// add(this.removeNode, WEST);
		// add(new JButton("Remove Node"), WEST);
		//
		// add(new JButton("Move Nodes"), WEST);

		add(new JButton("Check Accepted"), WEST);
		add(new JButton("Check Bids"), WEST);

		add(new JButton("Check Requests"), WEST);

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

		add(new JButton("Total Messages"), WEST);
		add(new JButton("View Blocks"), WEST);
		this.shortestPathTo = new JTextField(TEXT_FIELD_SIZE);
		add(this.shortestPathTo, NORTH);
		add(new JButton("Show Fastest Path"), NORTH);

		add(new JButton("Ping Everybody"), WEST);

		add(new JButton("Draw Nodes"), WEST);
		add(new JButton("Create Ping"), WEST);
	}

	// initialize
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start My Node")) {
			if (nodeCreated) { // check for multiple clicks
				System.out.println("Node already created");
			} else {
				nodeCreated = true;
				try {
					myNode = new NetworkNode(this.nodeName.getText());
				} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
					e1.printStackTrace();
				}
				beginSimulation();
				myNode.start();	
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
			checkAccepts();
		} else if (e.getActionCommand().equals("Check Bids")) {
			checkBids();
		} else if (e.getActionCommand().equals("Check Requests")) {
			checkRequests();
		} else if (e.getActionCommand().equals("Send Resource")) {
			sendResource();
		} else if (e.getActionCommand().equals("Receive Resource")) {
			receiveResource();
		} else if (e.getActionCommand().equals("Check Resources")) {
			receiveResource();
		} else if (e.getActionCommand().equals("Total Messages")) {
			generateTotalMessages();
			this.myNode.printTotalMessages(this.g, MAXSIZE);
		} else if (e.getActionCommand().equals("View Blocks")) {
			generateBlockView();
		} else if (e.getActionCommand().equals("Draw Nodes")) {
			drawNodes();
		} else if (e.getActionCommand().equals("Create Ping")) {
			createPing();
		} else if (e.getActionCommand().equals("View Resources")) {
			viewNodeResources();
		} else if (e.getActionCommand().equals("Put Initial Resources")) {
			putInitResources();
		}
	}

	private void createPing() {
		this.myNode.createPingToBroadcast();
	}

	private void putInitResources() {
		// this.myNode.myResources
		ArrayList<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource(500, "water", null));
		resources.add(new Resource(20, "medical supplies", null));
		resources.add(new Resource(300, "food", null));
		resources.add(new Resource(100, "blankets", null));
		resources.add(new Resource(50, "tents", null));
		resources.add(new Resource(40, "gre", null));
		resources.add(new Resource(50, "radios", null));
		resources.add(new Resource(100, "laptops", null));
		resources.add(new Resource(6, "raspberry pis", null));
		resources.add(new Resource(14, "tons of coffee", null));
		this.myNode.getNodeInfoList().put(this.myNode.getNodeID(),
				new NodeInfo(this.myNode.getNodeID(), myNode.pubKey, myNode.getLocation(), resources, null));
	}

	private void viewNodesResources(String nodeName) {
		generateNodesResourcesBoard();
		for (int i = 0; i < this.networkNodes.size(); i++) {
			if (this.networkNodes.get(i).getNodeID().equals(nodeName)) {
				HashMap<String, Integer> nodeResources = this.networkNodes.get(i).getResources();
				Set<String> nodesR = nodeResources.keySet();
				int o = 0;
				for (String key : nodesR) {
					String resourceName = key;
					String resourceAmount = "" + nodeResources.get(key);
					g.drawString(resourceName, 5, 75 + o * 25);
					g.drawString(resourceAmount, 10 + 0 / 5, 75 + o * 25);
					g.drawLine(0, 78 + o * 25, MAXSIZE, 78 + o * 25);
					o++;
				}
			}
		}
	}

	public void setMyNode(NetworkNode newNode) {
		this.myNode = newNode;
	}

	// Message Generation
	private void generateResourceRequest() throws NoSuchAlgorithmException, NoSuchProviderException {
		ResourceRequest newRequest = new ResourceRequest(Integer.parseInt(this.resourceAmount.getText()),
				this.resourceType.getText(), myNode.getNodeID());
		myNode.addMessage(newRequest);
		checkRequests();
	}

	private void generateBid() {
		String messageID = this.acceptNumber.getText();
		int eta = Integer.parseInt(this.eta.getText());
		// int amount = Integer.parseInt(this.amount.getText());
		int amount = 100;
		ResourceRequestBid newBid = new ResourceRequestBid(messageID, eta, amount, myNode.getNodeID());

		myNode.addMessage(newBid);
		checkBids();
	}

	private void acceptBid() {
		String bidID = this.bidNumber.getText();
		ResourceAgreement ra = new ResourceAgreement(bidID, myNode.getNodeID());
		myNode.addMessage(ra);
	}

	private void sendResource() {
		String MessageID = this.sentResource.getText();
		ResourceSent rs = null;

		for (int i = 0; i < myNode.openRequests.size(); i++) {
			ResourceRequest req = (ResourceRequest) this.myNode.openRequests.get(i);
			if (req.getID().equals(MessageID)) {
				rs = new ResourceSent(MessageID, myNode.getNodeID(), req.getType(), req.getAmount());
			}
		}

		// ResourceSent rs = new ResourceSent(MessageID, myNode.getNodeID());
		// for(int i = 0; i < thisNode.)

		// String typ =

		// ResourceSent rs = new ResourceSent(sendResourceID,
		// myNode.getNodeID(), need type, need amount);

		myNode.addMessage(rs);
	}

	private void receiveResource() {
		String MessageID = this.sentResource.getText();
		ResourceReceived rr = null;

		for (int i = 0; i < myNode.openRequests.size(); i++) {
			ResourceRequest req = (ResourceRequest) this.myNode.openRequests.get(i);
			if (req.getID().equals(MessageID)) {
				rr = new ResourceReceived(MessageID, myNode.getNodeID(), req.getType(), req.getAmount());
			}
		}
		// ResourceReceived rr = new ResourceReceived(sendID,
		// myNode.getNodeID());
		// ResourceReceived rr = new ResourceReceived(sendID,
		// myNode.getNodeID(), need type, need amount);
		myNode.addMessage(rr);
	}

	// GUI generation
	private void checkBids() {
		generateBidMessageBoard();
		g.setColor(Color.WHITE);
		if (myNode.getBidsToMyRequests() != null) {
			for (int i = 0; i < myNode.getBidsToMyRequests().size(); i++) {
				ResourceRequestBid rrbid = (ResourceRequestBid) myNode.getBidsToMyRequests().get(i);
				String bidID = rrbid.getID();
				String eta = "" + rrbid.eta;
				String resourceAmount = "" + rrbid.amount;
				String bidder = rrbid.author;
				g.drawString(bidID, 5, 40 + i * 20);
				g.drawString(eta, 5 + MAXSIZE / 4, 40 + i * 20);
				g.drawString(resourceAmount, 5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(bidder, 5 + 3 * MAXSIZE / 4, 40 + i * 20);
			}
		}
	}

	private void checkRequests() {
		generateMessageBoard();
		g.setColor(Color.WHITE);
		ArrayList<Message> availableMessages = myNode.getOpenRequests();
		for (int i = 0; i < availableMessages.size(); i++) {
			ResourceRequest rr = (ResourceRequest) availableMessages.get(i);
			String requestID = rr.id;
			String resourceRequested = rr.resourceType;
			String resourceAmount = "" + rr.amount;
			String originator = rr.getAuthor();
			g.drawString(requestID, 5, 40 + i * 20);
			g.drawString(resourceRequested, 5 + MAXSIZE / 4, 40 + i * 20);
			g.drawString(resourceAmount, 5 + 2 * MAXSIZE / 4, 40 + i * 20);
			g.drawString(originator, 5 + 3 * MAXSIZE / 4, 40 + i * 20);
		}
	}

	private void checkAccepts() {
		generateAcceptedMessageBoard();
		g.setColor(Color.WHITE);
		if (myNode.getMyResourceAgreements() != null) {
			ArrayList<Message> resourceAgreements = new ArrayList<Message>(myNode.getMyResourceAgreements());
			for (int i = 0; i < resourceAgreements.size(); i++) {
				ResourceAgreement rrAgree = (ResourceAgreement) resourceAgreements.get(i);
				ResourceRequestBid rrbid = (ResourceRequestBid) myNode.msgMap.get(rrAgree.resourceBidID);
				if (rrbid == null) {
					System.out.println("incorrect bid id");
					return;
				}
				ResourceRequest rr = (ResourceRequest) myNode.msgMap.get(rrbid.requestID);
				String messageNumber = rr.getID();
				String resourceRequested = rr.resourceType;
				String resourceAmount = "" + rr.amount;
				String destination = rr.author;
				g.drawString(messageNumber, 5, 40 + i * 20);
				g.drawString(resourceRequested, 5 + MAXSIZE / 4, 40 + i * 20);
				g.drawString(resourceAmount, 5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(destination, 5 + 3 * MAXSIZE / 4, 40 + i * 20);
			}
		}
	}

	
	private void generateBlockView() {
		generateBlockBoard();
		g.setColor(Color.WHITE);
		ArrayList<Block> blocks = new ArrayList<Block>(myNode.getBlockchain());
		for (int i=0;i<blocks.size();i++) {
			Block b = blocks.get(i);
			String s;
			if(b.getPrevHash().length() > 46){ 
				s = b.getPrevHash().substring(0, 10);
			} else {
				s = b.getPrevHash();
			}
			g.drawString(b.getMyHash().substring(0, 10),  5, 40 + i * 20);
			g.drawString(s,  5 + MAXSIZE / 4, 40 + i * 20);
			g.drawString(Integer.toString(b.getMsgs().size()),  5 + 2 * MAXSIZE / 4, 40 + i * 20);
			g.drawString(b.getNonce(),  5 + 3 * MAXSIZE / 4, 40 + i * 20);
		}
	}

	private void generateBlockBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 , 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE); // Here
		g.drawString("Hash ID",  5, 20);
		g.drawString("Previous Hash", 5 + MAXSIZE / 4, 20);
		g.drawString("Number of Messages", 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Nonce", 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(0, 25, MAXSIZE, 25);
	}
	
	private void generateBidMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);

		g.drawRect(0, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString("Bid Number", 5, 20);
		g.drawString("Time of Arrival", 5 + MAXSIZE / 4, 20);
		g.drawString("Amount Can Send", 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Bidder", 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(0, 25, 1 * MAXSIZE, 25);
	}

	private void generateAcceptedMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString("Message Number", 5, 20);
		g.drawString("Resource Requested", 5 + MAXSIZE / 4, 20);
		g.drawString("Amount Requested", 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Destination", 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(0, 25, 1 * MAXSIZE, 25);
	}

	private void generateNodesResourcesBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 5, MAXSIZE);
		g.drawRect(MAXSIZE / 5, 0, 4 * MAXSIZE / 5, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString(this.viewResources.getText() + "'s resources at Location ", 5 + 2 * MAXSIZE / 5, 20);
		g.drawString("Resource Name", 5, 45);
		g.drawString("Quantity", 5 + MAXSIZE / 5, 45);
		g.drawLine(0, 25, MAXSIZE, 25);
		g.drawLine(0, 50, MAXSIZE, 50);

	}

	private void generateTotalMessages() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 3, MAXSIZE);
		g.drawRect(MAXSIZE / 3, 0, MAXSIZE / 3, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 3, 0, MAXSIZE / 3, MAXSIZE);
		// g.drawRect(3 * MAXSIZE / 5, 0, MAXSIZE / 5, MAXSIZE);
		// g.drawRect(4 * MAXSIZE / 5, 0, MAXSIZE / 5, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE); // Here
		g.drawString("Message Type", 5, 20);
		g.drawString("Message ID", 5 + MAXSIZE / 3, 20);
		g.drawString("Author", 5 + 2 * MAXSIZE / 3, 20);
		// g.drawString("Originator", 5 + 3 * MAXSIZE / 5, 20);
		// g.drawString("Message Type", 5 + 4 * MAXSIZE / 5, 20);
		g.drawLine(0, 25, MAXSIZE, 25);
	}

	private void generateMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE); // Here
		g.drawString("Message Number", 5, 20);
		g.drawString("Resource Requested", 5 + MAXSIZE / 4, 20);
		g.drawString("Amount Requested", 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Originator", 5 + 3 * MAXSIZE / 4, 20);
		g.drawLine(0, 25, MAXSIZE, 25);
	}

	public void beginSimulation() {
		this.g = this.canvas.getGraphics();
		g.setColor(Color.BLACK);

		generateNodeMap();
		generateMessageBoard();
	}

	private void drawNodes() {
		g.setColor(Color.BLACK);
		this.myNode.drawNodes(this.g, MAXSIZE, WIDTH, HEIGHT);
		// this.myNode.drawTemps(this.g, MAXSIZE, WIDTH, HEIGHT);
	}

	private void generateNodeMap() {
		this.g = this.canvas.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);
		drawNodes();
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
				// currentMessage = new TextMessage(message,
				// receiverNode.getNodeID());
				myNode.addMessage(currentMessage);
			}
		}
	}

	public void viewNodeResources() {
		String nodeToView = this.viewResources.getText();
		NodeInfo nodeInfo = this.myNode.getNodeInfoList().get(nodeToView);
		ArrayList<Resource> resources = nodeInfo.getResourceList();
		Location nodeLoc = nodeInfo.getMyLocation();
		int nodeX = nodeLoc.getX();
		int nodeY = nodeLoc.getY();
		generateNodeResourceWindow(nodeToView, nodeX, nodeY);
		displayResources(resources);
	}

	private void displayResources(ArrayList<Resource> resources) {
		for (int i = 0; i < resources.size(); i++) {
			String amount = String.valueOf(resources.get(i).getAmount());
			String type = resources.get(i).getType();
			g.drawString(amount, 5, 70 + i * 25);
			g.drawString(type, 5 + MAXSIZE/5, 70+i*25);
			g.drawLine(0, 73+i*25, MAXSIZE, 73+i*25);
		}
	}

	private void generateNodeResourceWindow(String nodeToView, int nodeX, int nodeY) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 5, MAXSIZE);
		g.drawRect(MAXSIZE / 5, 0, 4 * MAXSIZE / 5, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString(nodeToView + "'s resources at Location X:  " + nodeX + " Y: " + nodeY, 5 + 3 * MAXSIZE / 10, 20);
		g.drawString("Quantity", 5, 45);
		g.drawString("Name", 5 + MAXSIZE / 5, 45);
		g.drawLine(0, 25, MAXSIZE, 25);
		g.drawLine(0, 50, MAXSIZE, 50);
	}

}