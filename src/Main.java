import java.util.ArrayList;
import java.util.Random;

public class Main {
	public Node node1, node2, node3, node4, node5, node6, node7, node8,node9;
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public ArrayList<Message> globalMSG = new ArrayList<Message>();
	
	public Main() {
		node1 = new Node("Steve", this);
		node2 = new Node("Damian", this);
		node3 = new Node("Lawrence", this);
		node4 = new Node("Jeffrey", this);
		node5 = new Node("Evan", this);
		node6 = new Node("Natalie", this);
		node7 = new Node("Will", this);
		node8 = new Node("Dylan", this);
		node9 = new Node("Colby", this);
		
		networkNodes.add(node1);
		networkNodes.add(node2);
		networkNodes.add(node3);
		networkNodes.add(node4);
		networkNodes.add(node5);
		networkNodes.add(node6);
		networkNodes.add(node7);
		networkNodes.add(node8);
		networkNodes.add(node9);
		
		//generateCommunicationLines();
	}
	
	private void generateCommunicationLines() {
		// TODO Auto-generated method stub
		for(int i = 0; i <networkNodes.size();i++){
			Node nodeToGetFriends = networkNodes.get(i);
			Random rand = new Random();
			int numberOfFriends = rand.nextInt(networkNodes.size());
			
			
//			if(this.nodeID.equals(networkNodes.get(receiverNum))){
//				this.createMessage(data);
//				System.out.println("I reran the function");
//			}
//			else {
//				Message text = new TextMessage("hello ", networkNodes.get(receiverNum));
//				localMSG.add(text);
//				main.addMSG(text);
//			}
		}
		
	}

	public void run() {
		node1.createMessage("I like me");
	}
	
	public void addMSG(Message msg) {
		this.globalMSG.add(msg);
		this.verifyMessageDistribution();
	}
	private void verifyMessageDistribution() {
		for(int i = 0; i < networkNodes.size();i++) {
			networkNodes.get(i).verifyMessages();
		}
	}

	public ArrayList<Message> getGlobalMSG() {
		return this.globalMSG;
	}

}
