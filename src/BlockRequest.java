
public class BlockRequest extends Message {
	public String blockHash;
	BlockRequest(String hash, String auth) {
		super(auth);
		this.messageType = "BlockRequest";
		this.blockHash = hash;
	}
	
	public String getBlockHash() {
		return blockHash;
	}
}