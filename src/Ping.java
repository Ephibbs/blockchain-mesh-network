
public class Ping {
	
	public Node originator = null;
	public Node relayer= null;
	public int count = 0;
	
	public Ping(Node initialCreator) {
		originator = initialCreator;
		relayer = initialCreator;
	}
	public void setRelayer(Node relayNode) {
		this.relayer = relayNode;
	}
	public Node getOriginator(){
		return this.originator;
	}
	public Node getRelayer() {
		return this.relayer;
	}
	public void addCount(){
		this.count++;
	}
	public int getCount(){
		return this.count;
	}

}
