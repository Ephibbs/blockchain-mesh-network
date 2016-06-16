import java.util.ArrayList;

public class BlockTree extends Tree<Block> {
	private Node<Block> root;
	public BlockTree(Block rootBlock) {
		super(rootBlock);
		root = new Node<Block>(rootBlock);
	}
}
