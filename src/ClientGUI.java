import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ClientGUI extends Program {

	public static final int TEXT_FIELD_SIZE = 15;
	public static final int MAXMOVE = 50;
	public static int MAX = 800;
	//public static int MAXSIZE = 800;
	public static final int MAXSIZEW = 800;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final int FONTSIZE = 30;
	public int MAXSIZEIMAGE = 400;
	public int IMAGEY = (800-MAXSIZEIMAGE)/2;
	public int IMAGEX = (800-MAXSIZEIMAGE)/2;
	public int MAXSIZE = MAX + IMAGEX;
	

	public JTextField nodeName;
	public JTextField resourceAmount;
	public JTextField resourceType;
	public JTextField requestReason;
	public JTextField acceptNumber;
	public JTextField bidNumber;
	public JTextField amount;
	public JTextField eta;
	public JTextField viewResources;
	public JTextField sentResource;
	public JTextField receiveResource;
	public JTextField shortestPathTo;
	public JTextField nodeIDTextField;

	public ArrayList<NetworkNode> networkNodes = new ArrayList<NetworkNode>();

	public Canvas canvas = new Canvas();
	public Random rand = new Random();
	public Graphics g = this.canvas.getGraphics();
	public NetworkNode myNode = null;
	private boolean nodeCreated = false; // prevent multiple clicks
	public Thread t;
	public int openTabID = 0; //the id of the tab currently open
	
	class GUIRefresher implements Runnable {
		public void run() {
			drawNodes();
//			System.out.println("Current Tab: "+openTabID);
			switch(openTabID) {
				case 0:
					printTotalMessages();
					break;
				case 1:
					checkRequests();
					break;
				case 2:
					checkBids();
					break;
				case 3:
					checkAccepts();
					break;
				case 4:
					generateBlockView();
					break;
				case 5:
					viewNodeResources();
					break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			run();
		}
	}

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
		this.requestReason.addActionListener(this);
		this.bidNumber.addActionListener(this);
		this.eta.addActionListener(this);
		this.viewResources.addActionListener(this);
		this.sentResource.addActionListener(this);
	}

	// Just the window, don't worry about it
	private void generateWestFrame() {
		
		// Node name
		add(new JLabel("Enter your Node Name"), WEST);
		this.nodeName = new JTextField(TEXT_FIELD_SIZE);
		add(this.nodeName, WEST);
		add(new JButton("Start My Node"), WEST);

		
		// Request resources
		add(new JLabel("________________________"), WEST);		
		add(new JLabel("Enter the Supply"), WEST);
		this.resourceType = new JTextField(TEXT_FIELD_SIZE);
		add(this.resourceType, WEST);
		add(new JLabel("Enter the Amount"), WEST);
		this.resourceAmount = new JTextField(TEXT_FIELD_SIZE);
		add(this.resourceAmount, WEST);
		add(new JLabel("Enter a Reason"), WEST);
		this.requestReason = new JTextField(TEXT_FIELD_SIZE);
		add(this.requestReason, WEST);
		add(new JButton("Request Resources"), WEST);

		
		// Bidding
		add(new JLabel("________________________"), WEST);		
		add(new JLabel("Message Number"), WEST);
		this.acceptNumber = new JTextField(TEXT_FIELD_SIZE);
		add(this.acceptNumber, WEST);
		add(new JLabel("ETA"), WEST);
		this.eta = new JTextField(TEXT_FIELD_SIZE);
		add(this.eta, WEST);
		add(new JButton("Generate Bid"), WEST);

		add(new JLabel("Bid Number"), WEST);
		this.bidNumber = new JTextField(TEXT_FIELD_SIZE);
		add(this.bidNumber, WEST);
		
		add(new JButton("Accept Bid"), WEST);
		
		
		// Actions
		add(new JLabel("________________________"), WEST);
		add(new JLabel("Actions"), WEST);
		add(new JButton("Draw Nodes"), WEST);
		add(new JButton("Create Ping"), WEST);
		
		
		// Messages
		add(new JLabel("Messages"), WEST);
		add(new JButton("Total Messages"), WEST);
		add(new JButton("Check Requests"), WEST);
		add(new JButton("Check Bids"), WEST);
		add(new JButton("Check Accepted"), WEST);
		
		this.nodeIDTextField = new JTextField(TEXT_FIELD_SIZE);
		add(nodeIDTextField, WEST);
		add(new JButton("View Blocks"), WEST);

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
		
	}

	// initialize
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start My Node")) {
			if (nodeCreated) { // check for multiple clicks
				System.err.println("Node already created");
			} else {
				nodeCreated = true;
				try {
					myNode = new NetworkNode(this.nodeName.getText());
				} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
					e1.printStackTrace();
				}
				beginSimulation();
				myNode.start();
				t = new Thread(new GUIRefresher(), "refresher");
				t.start();
			}
		} else if (!nodeCreated) { // don't go below unless node is created
			System.err.println("Must create node first!");
		} else if (e.getActionCommand().equals("Request Resources")) {

			if (resourceType.getText().isEmpty() || resourceAmount.getText().isEmpty() || requestReason.getText().isEmpty()) {
				System.err.println("Aborting request: one or more fields are empty");
			} else if (!resourceAmount.getText().matches("\\d+")) { // check if amount is an integer
				System.err.println("Error: Supply amount is not a valid integer");
			} else {
				try {
					try {
						generateResourceRequest();
					} catch (IOException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (InvalidKeyException | SignatureException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getActionCommand().equals("Generate Bid")) {
			if (acceptNumber.getText().isEmpty() || eta.getText().isEmpty()) {
				System.err.println("Cannot generate bid: one or more fields are empty");
			} else if (!eta.getText().matches("\\d+")) { // eta has to be an integer
				System.err.println("Error: ETA is not a valid integer");
			} else {
				try {
					generateBid();
				} catch (InvalidKeyException | SignatureException | ClassNotFoundException | NoSuchAlgorithmException
						| NoSuchProviderException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if (e.getActionCommand().equals("Accept Bid")) {
			try {
				acceptBid();
			} catch (InvalidKeyException | SignatureException | ClassNotFoundException | NoSuchAlgorithmException
					| NoSuchProviderException | IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Check Accepted")) {
			checkAccepts();
		} else if (e.getActionCommand().equals("Check Bids")) {
			checkBids();
		} else if (e.getActionCommand().equals("Check Requests")) {
			checkRequests();
		} else if (e.getActionCommand().equals("Send Resource")) {
			try {
				sendResource();
			} catch (InvalidKeyException | SignatureException | ClassNotFoundException | NoSuchAlgorithmException
					| NoSuchProviderException | IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Receive Resource")) {
			try {
				receiveResource();
			} catch (InvalidKeyException | SignatureException | ClassNotFoundException | NoSuchAlgorithmException
					| NoSuchProviderException | IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Check Resources")) {
			try {
				receiveResource();
			} catch (InvalidKeyException | SignatureException | ClassNotFoundException | NoSuchAlgorithmException
					| NoSuchProviderException | IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("Total Messages")) {
			printTotalMessages();
		} else if (e.getActionCommand().equals("View Blocks")) {
			generateBlockView();
		} else if (e.getActionCommand().equals("Draw Nodes")) {
			drawNodes();
		} else if (e.getActionCommand().equals("Create Ping")) {
			createPing();
		} else if (e.getActionCommand().equals("View Resources")) {
			System.out.println("failure called from here");
			viewNodeResources();
		} 
//		else if (e.getActionCommand().equals("Put Initial Resources")) {
//			putInitResources();
//		}
	}

	private void createPing() {
		openTabID = 0; // all messages
		this.myNode.createPingToBroadcast();
	}

	public void setMyNode(NetworkNode newNode) {
		this.myNode = newNode;
	}

	// Message Generation
	private void generateResourceRequest() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException {
		ResourceRequest newRequest = new ResourceRequest(Integer.parseInt(this.resourceAmount.getText()),
				this.resourceType.getText(), myNode.getNodeID(), requestReason.getText());
		myNode.sendMessage(newRequest);
		//myNode.addMessage(newRequest);
		checkRequests();
	}

	private void generateBid() throws InvalidKeyException, SignatureException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String requestID = this.acceptNumber.getText();
		int eta = Integer.parseInt(this.eta.getText());
		//int amount = Integer.parseInt(this.amount.getText());
		if(myNode.msgMap.containsKey(requestID)
				&& myNode.msgMap.get(requestID).messageType.equals("ResourceRequest")) {
			ResourceRequestBid newBid = new ResourceRequestBid(requestID, eta, myNode.getNodeID());
			myNode.sendMessage(newBid);
			//myNode.addMessage(newBid);
			checkBids();
		} else {
			System.err.println("no request with that id");
		}
	}

	private void acceptBid() throws InvalidKeyException, SignatureException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String bidID = this.bidNumber.getText();
		if(myNode.msgMap.containsKey(bidID) && myNode.msgMap.get(bidID).messageType.equals("ResourceRequestBid")) {
			ResourceAgreement ra = new ResourceAgreement(bidID, myNode.getNodeID());
			//myNode.addMessage(ra);
			myNode.sendMessage(ra);
			checkAccepts();
		} else {
			System.err.println("no bid with that id");
		}
	}

	private void sendResource() throws InvalidKeyException, SignatureException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String MessageID = this.sentResource.getText();
		if(myNode.msgMap.containsKey(MessageID) && myNode.msgMap.get(MessageID).messageType.equals("ResourceAgreement")) {
			ResourceAgreement agree = (ResourceAgreement) myNode.msgMap.get(MessageID);
			ResourceRequestBid bid = (ResourceRequestBid) myNode.msgMap.get(agree.resourceBidID);
			ResourceRequest req = (ResourceRequest) myNode.msgMap.get(bid.requestID);
			ResourceSent rs = new ResourceSent(MessageID, myNode.getNodeID(), req.resourceType, req.amount);
			System.out.println(rs.amount);
			//myNode.addMessage(rs);
			myNode.sendMessage(rs);
		} else {
			System.err.println("no resource agreement with that id");
		}
	}

	private void receiveResource() throws InvalidKeyException, SignatureException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String MessageID = this.receiveResource.getText();
		if(myNode.msgMap.containsKey(MessageID) && myNode.msgMap.get(MessageID).messageType.equals("ResourceSent")) {
			ResourceSent rs = (ResourceSent) myNode.msgMap.get(MessageID);
			ResourceReceived rr = new ResourceReceived(MessageID, myNode.getNodeID(), rs.getResourceType(), rs.getAmount());
			//myNode.addMessage(rr);
			myNode.sendMessage(rr);
		} else {
			System.err.println("no resource sent with that id");
		}
	}

	// GUI data drawing
	public void printTotalMessages() {
		openTabID = 0;
		generateTotalMessages();
		g.setColor(Color.WHITE);
		if (myNode.totalMessages != null) {
			for (int i = 0; i < myNode.totalMessages.size(); i++) {
				Message msg = myNode.totalMessages.get(i);
				String type = msg.getMessageType();
				String id = msg.getID();
				String author = msg.getAuthor();
				g.drawString(type, 5, (FONTSIZE*2) + i * (FONTSIZE+5));
				g.drawString(id, 5 + MAXSIZE / 3, (FONTSIZE*2) + i * (FONTSIZE+5));
				g.drawString(author, 5 + 2 * MAXSIZE / 3, (FONTSIZE*2) + i * (FONTSIZE+5));
				g.drawLine(0, (2*FONTSIZE+5) + i * (FONTSIZE+5), MAXSIZE, (2*FONTSIZE+5) + i * (FONTSIZE+5));
			}
		}
	}
	
	private void checkRequests() {
		openTabID = 1;
		generateMessageBoard();
		g.setColor(Color.WHITE);
		ArrayList<Message> availableMessages = myNode.getOpenRequests();
		for (int i = 0; i < availableMessages.size(); i++) {
			ResourceRequest rr = (ResourceRequest) availableMessages.get(i);
			String requestID = rr.id;
			String resourceRequested = rr.resourceType;
			String resourceAmount = "" + rr.amount;
			String originator = rr.getAuthor();
			g.drawString(requestID, 5, 2*FONTSIZE + i * FONTSIZE);
			g.drawString(resourceRequested, 5 + MAXSIZE / 4, 2*FONTSIZE + i * FONTSIZE);
			g.drawString(resourceAmount, 5 + 2 * MAXSIZE / 4, 2*FONTSIZE + i * FONTSIZE);
			g.drawString(originator, 5 + 3 * MAXSIZE / 4, 2*FONTSIZE + i * FONTSIZE);
		}
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
					g.drawString(resourceName, 5, 3*FONTSIZE+15 + o * (FONTSIZE+5));
					g.drawString(resourceAmount, 10 + 0 / 5, 75 + o * (FONTSIZE+5));
					g.drawLine(0, 3*FONTSIZE+18 + o * (FONTSIZE+5), MAXSIZE, 78 + o * (FONTSIZE+5));
					o++;
				}
			}
		}
	}

	private void checkBids() {
		openTabID = 2;
		generateBidMessageBoard();
		g.setColor(Color.WHITE);
		if (myNode.getBidsToMyRequests() != null) {
			for (int i = 0; i < myNode.getBidsToMyRequests().size(); i++) {
				ResourceRequestBid rrbid = (ResourceRequestBid) myNode.getBidsToMyRequests().get(i);
				String bidID = rrbid.getID();
				String eta = "" + rrbid.eta;
				//String resourceAmount = "" + rrbid.amount;
				String bidder = rrbid.author;
				g.drawString(bidID, 5, 2*FONTSIZE + i * FONTSIZE);
				g.drawString(eta, 5 + MAXSIZE / 3, 2*FONTSIZE + i * FONTSIZE);
				//g.drawString(resourceAmount, 5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(bidder, 5 + 2 * MAXSIZE / 3, 2*FONTSIZE + i * FONTSIZE);
			}
		}
	}

	private void checkAccepts() {
		openTabID = 3;
		generateAcceptedMessageBoard();
		g.setColor(Color.WHITE);
		if (myNode.getMyResourceAgreements() != null) {
			ArrayList<Message> resourceAgreements = new ArrayList<Message>(myNode.getMyResourceAgreements());
			for (int i = 0; i < resourceAgreements.size(); i++) {
				ResourceAgreement rrAgree = (ResourceAgreement) resourceAgreements.get(i);
				ResourceRequestBid rrbid = (ResourceRequestBid) myNode.msgMap.get(rrAgree.resourceBidID);
				ResourceRequest rr = (ResourceRequest) myNode.msgMap.get(rrbid.requestID);
				String messageNumber = rrAgree.getID();
				String resourceRequested = rr.resourceType;
				//String resourceAmount = "" + rr.amount;
				String destination = rr.author;
				g.drawString(messageNumber, 5, 2*FONTSIZE + i * FONTSIZE);
				g.drawString(resourceRequested, 5 + MAXSIZE / 3, 2*FONTSIZE + i * FONTSIZE);
				//g.drawString(resourceAmount, 5 + 2 * MAXSIZE / 4, 40 + i * 20);
				g.drawString(destination, 5 + 2 * MAXSIZE / 3, 2*FONTSIZE + i * FONTSIZE);
			}
		}
	}

	private void generateBlockView() {
		openTabID = 4;
		String nodeID = nodeIDTextField.getText();
		generateBlockBoard();
		g.setColor(Color.WHITE);
		ArrayList<Block> blocks;
		if(nodeID.equals(myNode.getNodeID())) {
			blocks = new ArrayList<Block>(myNode.getBlockchain());
		} else {
			NodeInfo nodeInfo = this.myNode.getNodeInfoList().get(nodeID);
			if(nodeInfo != null) {
				blocks = new ArrayList<Block>(nodeInfo.getBlockchain());
			} else {
				blocks = new ArrayList<Block>();
			}
		}
		for (int i=0;i<blocks.size();i++) {
			Block b = blocks.get(i);
			String s;
			if(b.getPrevHash().length() > 46){ 
				s = b.getPrevHash().substring(0, 10);
			} else {
				s = b.getPrevHash();
			}
			g.drawString(b.getMyHash().substring(0, 10),  5, 2*FONTSIZE + i * FONTSIZE);
			g.drawString(s,  5 + MAXSIZE / 4, 2*FONTSIZE + i * FONTSIZE);
			g.drawString(Integer.toString(b.getMsgs().size()),  5 + 2 * MAXSIZE / 4, 2*FONTSIZE + i * FONTSIZE);
			g.drawString(b.getNonce(),  5 + 3 * MAXSIZE / 4, 2*FONTSIZE + i * FONTSIZE);
		}
	}

	//GUI background drawing
	private void generateBlockBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0 , 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, FONTSIZE));
		g.setColor(Color.WHITE); // Here
		g.drawString("Hash ID",  5, FONTSIZE);
		g.drawString("Previous Hash", 5 + MAXSIZE / 4, FONTSIZE);
		g.drawString("Number of Messages", 5 + 2 * MAXSIZE / 4, FONTSIZE);
		g.drawString("Nonce", 5 + 3 * MAXSIZE / 4, FONTSIZE);
		g.drawLine(0, FONTSIZE+5, MAXSIZE, FONTSIZE+5);
	}
	
	private void generateBidMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);

		g.drawRect(0, 0, MAXSIZE / 3, MAXSIZE);
		g.drawRect(MAXSIZE / 3, 0, MAXSIZE / 3, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 3, 0, MAXSIZE / 3, MAXSIZE);
		//g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, FONTSIZE));
		g.setColor(Color.WHITE);
		g.drawString("Bid Number", 5, FONTSIZE);
		g.drawString("Time of Arrival", 5 + MAXSIZE / 3, FONTSIZE);
		//g.drawString("Amount Can Send", 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Bidder", 5 + 2 * MAXSIZE / 3, FONTSIZE);
		g.drawLine(0, FONTSIZE+5, 1 * MAXSIZE, FONTSIZE+5);
	}

	private void generateAcceptedMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 3, MAXSIZE);
		g.drawRect(MAXSIZE / 3, 0, MAXSIZE / 3, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 3, 0, MAXSIZE / 3, MAXSIZE);
		//g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, FONTSIZE));
		g.setColor(Color.WHITE);
		g.drawString("Message Number", 5, FONTSIZE);
		g.drawString("Resource Requested", 5 + MAXSIZE / 3, FONTSIZE);
		//g.drawString("Amount Requested", 5 + 2 * MAXSIZE / 4, 20);
		g.drawString("Destination", 5 + 2 * MAXSIZE / 3, FONTSIZE);
		g.drawLine(0, FONTSIZE+5, 1 * MAXSIZE, FONTSIZE+5);
	}

	private void generateNodesResourcesBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 5, MAXSIZE);
		g.drawRect(MAXSIZE / 5, 0, 4 * MAXSIZE / 5, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, FONTSIZE));
		g.setColor(Color.WHITE);
		//g.drawString("hi", 400, 50);
		g.drawString(this.viewResources.getText() + "'s resources at Location ", 5 + 2 * MAXSIZE / 5, (FONTSIZE));
		g.drawString("Resource Name", 5, 2*FONTSIZE+5);
		g.drawString("Quantity", 5 + MAXSIZE / 5, 2*FONTSIZE+5);
		g.drawLine(0, FONTSIZE+5, MAXSIZE, FONTSIZE+5);
		g.drawLine(0, FONTSIZE+10, MAXSIZE, FONTSIZE+10);

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

		g.setFont(new Font("TimesRoman", Font.PLAIN, FONTSIZE));
		g.setColor(Color.WHITE); // Here
		g.drawString("Message Type", 5, FONTSIZE);
		g.drawString("Message ID", 5 + MAXSIZE / 3, FONTSIZE);
		g.drawString("Author", 5 + 2 * MAXSIZE / 3, FONTSIZE);
		// g.drawString("Originator", 5 + 3 * MAXSIZE / 5, 20);
		// g.drawString("Message Type", 5 + 4 * MAXSIZE / 5, 20);
		g.drawLine(0, FONTSIZE+5, MAXSIZE, FONTSIZE+5);
	}

	private void generateMessageBoard() {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(2 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);
		g.drawRect(3 * MAXSIZE / 4, 0, MAXSIZE / 4, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, FONTSIZE));
		g.setColor(Color.WHITE); // Here
		g.drawString("Message Number", 5, FONTSIZE);
		g.drawString("Resource Requested", 5 + MAXSIZE / 4, FONTSIZE);
		g.drawString("Amount Requested", 5 + 2 * MAXSIZE / 4, FONTSIZE);
		g.drawString("Originator", 5 + 3 * MAXSIZE / 4, FONTSIZE);
		g.drawLine(0, FONTSIZE+5, MAXSIZE, FONTSIZE+5);
	}

	public void beginSimulation() {
		this.g = this.canvas.getGraphics();
		g.setColor(Color.BLACK);

		generateNodeMap();
		generateMessageBoard();
	}

	private void drawNodes() {
//		System.out.println("Nodes drawn");
		g.setColor(Color.BLACK);
		this.myNode.drawNodes(this.g, MAXSIZE, WIDTH, HEIGHT);
		// this.myNode.drawTemps(this.g, MAXSIZE, WIDTH, HEIGHT);
	}

	private void generateNodeMap() {
		this.g = this.canvas.getGraphics();
		
		String fName = "Tuck" + MAXSIZEIMAGE + ".png";
		//System.out.println(fName);
		File cFile = new File(fName);
		
		//File cFile = new File("Tuck.png");
		String filePath = cFile.getAbsolutePath();
		String newFilePath = filePath.replace("bin\\", "");
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(newFilePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.drawImage(image, MAXSIZE, 0, null);
		
		//g.setColor(Color.LIGHT_GRAY);
		//g.fillRect(MAXSIZE, 0, MAXSIZE, MAXSIZE);
		
		
		// here
		drawNodes();
	}

	private void sendMessage(String message, String sender, String receiver)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, ClassNotFoundException, IOException {
		NetworkNode senderNode = myNode;
		NetworkNode receiverNode = null;
		Message currentMessage = null;
		for (int i = 0; i < networkNodes.size(); i++) {
			String nodeNameRec = "Node" + networkNodes.get(i).nodeID;
			if (networkNodes.get(i).nodeID.equals(receiver)) {
				receiverNode = networkNodes.get(i);
				// currentMessage = new TextMessage(message,
				// receiverNode.getNodeID());
				//myNode.addMessage(currentMessage);
				myNode.sendMessage(currentMessage);
			}
		}
	}

	public void viewNodeResources() {
		openTabID = 5;
		String nodeToView = this.viewResources.getText();
		//System.out.println("here is the failure");
		NodeInfo nodeInfo = this.myNode.getNodeInfoList().get(nodeToView);
		if(nodeInfo != null) {
			ArrayList<Resource> resources = nodeInfo.getResourceList();
			Location nodeLoc = nodeInfo.getMyLocation();
			int nodeX = nodeLoc.getX();
			int nodeY = nodeLoc.getY();
			generateNodeResourceWindow(nodeToView, nodeX, nodeY);
			displayResources(resources);
		} else {
			System.err.println("No node with that id found.");
		}
	}

	private void displayResources(ArrayList<Resource> resources) {
		for (int i = 0; i < resources.size(); i++) {
			String amount = String.valueOf(resources.get(i).getAmount());
			String type = resources.get(i).getType();
			g.drawString(amount, 5, (FONTSIZE*3 + 10) + i * (FONTSIZE + 5));
			g.drawString(type, 5 + MAXSIZE/5, (FONTSIZE*3 + 10)+i*(FONTSIZE+5));
			g.drawLine(0, (FONTSIZE*3 + 13)+i*(FONTSIZE+5), MAXSIZE, (FONTSIZE*3 + 13)+i*(FONTSIZE+5));
		}
	}

	private void generateNodeResourceWindow(String nodeToView, int nodeX, int nodeY) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, MAXSIZE, MAXSIZE);

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, MAXSIZE / 5, MAXSIZE);
		g.drawRect(MAXSIZE / 5, 0, 4 * MAXSIZE / 5, MAXSIZE);

		g.setFont(new Font("TimesRoman", Font.PLAIN, FONTSIZE));
		g.setColor(Color.WHITE);
		g.drawString(nodeToView + "'s resources at Location X:  " + nodeX + " Y: " + nodeY, 5 + 3 * MAXSIZE / 10, (FONTSIZE));
		g.drawString("Quantity", 5, 2*FONTSIZE+5);
		g.drawString("Name", 5 + MAXSIZE / 5, 2*FONTSIZE+5);
		g.drawLine(0, FONTSIZE+5, MAXSIZE, FONTSIZE+5);
		g.drawLine(0, 2*FONTSIZE+10, MAXSIZE, 2*FONTSIZE+10);
	}

}