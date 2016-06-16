
public class PuzzleSolver implements Runnable {
	private Blockchain bc;
	private Node node;
	public PuzzleSolver(Node node, Blockchain bc) {
		this.bc = bc;
		this.node = node;
	}
	public void run() {
		while(node.isOnline()) {
			Block b = new Block(bc.getBt().getDeepestTreeNode().getData().getMyHash(), bc.getMsgs());
			long nonce = -1;
			while(!this.checkHashWithNonce(b, nonce++)) {
				
			}
			b.setNonce(String.valueOf(nonce));
			bc.removeMsgsInBlock(b)
			bc.getBt().addTreeNode(bc.getBt().getDeepestTreeNode(), new TreeNode<Block>(b));
		}
	}
}
