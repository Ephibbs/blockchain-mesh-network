
public class BlockDelivery extends Message {
	public Block block;
	BlockDelivery(Block block, String auth) {
		super(auth);
		this.block = block;
	}
}