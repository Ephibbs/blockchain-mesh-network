import java.util.ArrayList;

public class Main {
	public Node node1 = new Node("Steve", this);
	public Node node2 = new Node("Damian", this);
	public Node node3 = new Node("Lawrence", this);
	public Node node4 = new Node("Jeffrey", this);
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public ArrayList<Message> globalMSG = new ArrayList<Message>();
	
	public Main() {
		networkNodes.add(node1);
		networkNodes.add(node2);
		networkNodes.add(node3);
		networkNodes.add(node4);
		
		node1.addNodes(networkNodes);
		node2.addNodes(networkNodes);
		node3.addNodes(networkNodes);
		node4.addNodes(networkNodes);
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
