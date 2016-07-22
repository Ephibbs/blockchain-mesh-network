import java.security.PublicKey;
import java.sql.Time;
import java.util.ArrayList;

public class Ping extends Message {
	
	public int count = 0;
	
	public String originatorString = null;
	public String relayerString = null;
	public Location currentLocation = null;
	public PublicKey publicKey = null;
	public Time timeSent = null;
	public ArrayList<Block> blockchainArr = new ArrayList<Block>();
	
	public Ping(String sender, String receiver, PublicKey pubKey){
		// should create a public key whenever you start a node
		// when i create a ping I need to set its location
		super(sender, receiver);
		this.messageType = "Ping";
		this.originatorString = sender;
		this.relayerString = receiver;
		this.publicKey = pubKey;
	}

	public void setRelayer(String relayer) {
		this.relayerString = relayer;
	}
	public String getOriginator(){
		return this.originatorString;
	}
	public String getRelayer() {
		return this.relayerString;
	}
	public void addCount(){
		this.count++;
	}
	public int getCount(){
		return this.count;
	}
	public void setLocation(Location newLocation){
		this.currentLocation = newLocation;
	}
	public Location getLocation(){
		return this.currentLocation;
	}
	public PublicKey getPublicKey(){
		return this.publicKey;
	}
	public void setTime(Time newTime){
		this.timeSent = newTime;
	}
	public Time getTimeSent(){
		return this.timeSent;
	}
	
	public void setBlockchain(ArrayList<Block> chain) {
		this.blockchainArr = chain;
	}
	public ArrayList<Block> getBlockchain() {
		return blockchainArr;
	}
}
