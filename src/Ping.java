
public class Ping {
	
	public SimulationNode originator = null;
	public SimulationNode relayer= null;
	public int count = 0;
	
	public Ping(SimulationNode initialCreator) {
		originator = initialCreator;
		relayer = initialCreator;
	}
	public void setRelayer(SimulationNode relayNode) {
		this.relayer = relayNode;
	}
	public SimulationNode getOriginator(){
		return this.originator;
	}
	public SimulationNode getRelayer() {
		return this.relayer;
	}
	public void addCount(){
		this.count++;
	}
	public int getCount(){
		return this.count;
	}

}
