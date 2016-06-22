
public class Ping {
	
	public Node originator = null;
	public Node relayer= null;
	
	public Ping(Node initialCreator) {
		originator = initialCreator;
		relayer = initialCreator;
	}
	
	public void relayPing(Node relayNode) {
		this.relayer = relayNode;
	}
	
	public Node getOriginator(){
		return this.originator;
	}
	
	public Node getRelayer() {
		return this.relayer;
	}

}
