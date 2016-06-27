import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * BlockStore class to store the blocks, each blockchain has this
 */

public class BlockStore {

	// Variables
	private TreeNode<Block> root = new TreeNode<Block>(new Block("1", new ArrayList<Message>()));
	private Tree<Block> blockTree = new Tree<Block>(root);
	private ArrayList<String> treeBlockIDs = new ArrayList<String>(); // stores the hashes of the blocks (their ID)
	private ArrayList<String> orphanBlockIDs = new ArrayList<String>();
	//private ArrayList<TreeNode<Block>> allBlocks = new ArrayList<Block>();
	private HashMap<String, TreeNode<Block>> blockMap = new HashMap<String, TreeNode<Block>>(); // input: hash, output: TreeNode<block> with that hash
	private ArrayList<Tree<Block>> orphanTrees = new ArrayList<Tree<Block>>(); // list of all the orphan trees
	private ArrayList<String> messageIDs = new ArrayList<String>();
	private HashMap<String, Message> allMessages = new HashMap<String, Message>();
	private String blockFlag = null;

	// Constructor
	BlockStore() {
		treeBlockIDs.add(root.getData().getMyHash());
		blockMap.put(root.getData().getMyHash(), root);
	}

	// Accessors
	Block getLastBlock() {
		return blockTree.getDeepestTreeNode().getData();
	}
	ArrayList<Message> getOrphanMessages() {
		ArrayList<Message> orphanMessages = new ArrayList<Message>();
		ArrayList<String> orphanMessageIDs = new ArrayList<String>(messageIDs);
		TreeNode<Block> p = blockTree.getDeepestTreeNode();
		while(p != null) {
			for(Message m : p.getData().getMsgs()) {
				orphanMessageIDs.remove(m.getHash());
			}
			p = p.getParent();
		}
		for(String s : orphanMessageIDs) {
			orphanMessages.add(allMessages.get(s));
		}
		return orphanMessages;
	}

	// Mutators
	boolean add(Block b) {
		TreeNode<Block> c = blockMap.get(b.getMyHash()); // check if block is in blocktree
		if(c == null) { // if block is not in blocktree (is unique)
			if(treeBlockIDs.contains(b.getPrevHash())) { // check if parent is in blocktree
				TreeNode<Block> p = blockMap.get(b.getPrevHash()); // get previous block (parent block)

				//verify all messages in the block have been received
				boolean messagesVerified = true;
				for(Message m : b.getMsgs()) { // check if messageIDs list has all messages in block
					if(!messageIDs.contains(m.getHash())) {
						messagesVerified = false;
						break;
					}
				}
				
				//verify messages aren't in any previous block
				if(messagesVerified) {
					ArrayList<Message> blockMsgs = b.getMsgs();
					TreeNode<Block> tempP = new TreeNode<Block>(p); // copy of parent block
					while(tempP.getDepth() != 0) { // go through parent blocks
						for(Message m : tempP.getData().getMsgs()) {
							if(blockMsgs.contains(m.getHash())) {
								messagesVerified = false;
							}
						}
						tempP = tempP.getParent();
					}
					
					if(messagesVerified) {
						//add block to blocktree
						System.out.println("block added to tree");
						TreeNode<Block> bn = new TreeNode<Block>(b);
						blockTree.addTreeNode(p, bn);
						treeBlockIDs.add(b.getMyHash());
						blockMap.put(b.getMyHash(), bn);

						if (blockFlag == null) { // flag to know when you've exited the recursive call
							blockFlag = b.getMyHash();
						}

						// Check if any orphan trees can be added to the blocktree
						for (String orphanID : orphanBlockIDs) {
							TreeNode<Block> orphanBlock = blockMap.get(orphanID);
							if (treeBlockIDs.contains(orphanBlock.getData().getPrevHash())) { // if orphan can be put under a tree
								orphanBlockIDs.remove(orphanID);
								if (!add(orphanBlock.getData())) { // if add failed
									for (TreeNode<Block> cn : orphanBlock.getChildren()) {
										orphanTrees.add(new Tree<Block>(cn)); // create new tree node for each child
									}
								}
							}
						}

						// Remove orphan tree when out
						if (b.getMyHash() == blockFlag) {
							blockFlag = null;
							orphanTrees.remove(blockMap.get(b.getMyHash()).getMyTree());
						}

						tempP = new TreeNode<Block>(bn); // show tree
						String s = "";
						while(tempP.getDepth() != 0) {
							s += tempP.getData().getMyHash();
							s += " <- ";
							tempP = tempP.getParent();
						}
						s += "0";
						System.out.println(s);
						return true;
					}
				}
			} else if (!orphanBlockIDs.contains(b.getMyHash())) { // is a unique orphan block
				orphanBlockIDs.add(b.getMyHash()); // add hash to list of orphans
				TreeNode<Block> bn = new TreeNode<Block>(b); // save block in blockmap
				blockMap.put(b.getMyHash(), bn);

				if (orphanBlockIDs.contains(b.getPrevHash())) { // if orphan's parent exists, add orphan to parent's tree
					System.out.println("into orphan tree");

					// Find parent node and add block as its child
					Tree<Block> pTree = blockMap.get(b.getPrevHash()).getMyTree();
					pTree.addTreeNode(blockMap.get(b.getPrevHash()), bn);

					// Check if any orphan trees can be added under the newly added block
					for (Tree<Block> t : orphanTrees) {
						if (b.getMyHash() == t.getRootTreeNode().getData().getPrevHash()) { // if there is a match
							t.addTreeNode(bn, t.getRootTreeNode()); // set orphan tree root as child to new blck
							orphanTrees.remove(t); // remove added orphan tree
						}
					}
				} else { // if orphan has no parent, make a new tree
					System.out.println("into new orphan tree");
					orphanTrees.add(new Tree<Block>(b));
				}
			}
		}
		return false;
	}
	void add(Message m) {
		if(!messageIDs.contains(m.getHash())) {
			messageIDs.add(m.getHash());
			allMessages.put(m.getHash(), m);
		}
	}
	TreeNode<Block> getBlock(String hash) {
		return blockMap.get(hash);
	}
 }
