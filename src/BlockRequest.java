
public class BlockRequest extends Message {
	public String blockHash;
	BlockRequest(String hash, String auth) {
		super(auth);
		this.messageType = "BlockRequest";
		this.blockHash = blockHash;
	}
	
	public String getBlockHash() {
		return blockHash;
	}
}