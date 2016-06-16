import java.util.ArrayList;
import java.util.Random;

public class Node {
	
	public String nodeID = null;
	public ArrayList<Message> localMSG = new ArrayList<Message>();
	public ArrayList<Node> networkNodes = new ArrayList<Node>();
	public Main main = new Main();
	
	public Node(String id) {
		this.nodeID = id;
	}

	public Node(String string, Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
	}

	public String getNodeID() {
		return nodeID;
	}
	
	public void addNodes(ArrayList<Node> newNodes){
		for(int i = 0; i < newNodes.size(); i++) {
			networkNodes.add(newNodes.get(i));
		}
	}
	
	public void printNodes(){
		for(int i = 0; i < networkNodes.size(); i++) {
			System.out.println(networkNodes.get(i).getNodeID());
		}
	}
	
	public void createMessage(Object data) {
		Random rand = new Random();
		int receiverNum = rand.nextInt(4);
		if(this.nodeID.equals(networkNodes.get(receiverNum))){
			this.createMessage(data);
			System.out.println("I reran the function");
		}
		else {
			Message text = new TextMessage("hello ", networkNodes.get(receiverNum));
			localMSG.add(text);
			main.addMSG(text);
		}
	}

	public void verifyMessages() {
		for(int i = 0; i < main.getGlobalMSG().size();i++) {
			if(!localMSG.contains(main.getGlobalMSG().get(i))){
				localMSG.add(main.getGlobalMSG().get(i));
				System.out.println("I updated " + this.nodeID + " local message bank");
			}
			else {
				// do nothing
			}
		}
	}
}
