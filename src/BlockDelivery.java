public class BlockDelivery extends Message {
	public Block block;
	BlockDelivery(Block block, String auth, String rec) {
		super(auth, rec);
		this.messageType = "BlockRequest";
		this.block = block;
	}
	public String getBlockHash() {
		return block.getMyHash();
	}
	public Block getBlock() {
		return block;
	}
}