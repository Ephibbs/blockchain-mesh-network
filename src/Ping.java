
public class Ping {
	
	public Node originator = null;
	public Node relayer= null;
	public int count = 0;
	
	public Ping(Node node) {
		originator = node;
		relayer = node;
	}
	public void setRelayer(Node node) {
		this.relayer = node;
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
