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

						// Check if any orphan trees can be added ot the blocktree
						for (Tree<Block> t : orphanTrees) {
							if (bn.getData().getMyHash() == t.getRootTreeNode().getData().getPrevHash()) {
								// TODO call add() method to add new node to blocktree 
							}
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
				TreeNode<Block> bNode = new TreeNode<Block>(b);

				if (orphanBlockIDs.contains(b.getPrevHash())) { // if orphan's parent exists, add orphan to parent's tree
					System.out.println("into orphan tree");

					// Find parent node and add block as its child
					TreeNode<Block> pNode;
					for (Tree<Block> t : orphanTrees) {
						// TODO iterate through all nodes, find parent and add b as child
						if (false) {// replace with condition that is true when parent is found
							t.addTreeNode(pNode, bNode));
							break;
						}
					}

					// Look for orphan tree roots that can join the added treenode
					for (Tree<Block> t : orphanTrees) {
						if (t.getRootTreeNode().getData().getPrevHash() == b.getMyHash()) { // if orphan and parent matches
							t.addTreeNode(bNode, t.getRootTreeNode());
//							orphanTrees.remove(t); remove tree
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
	Block getBlock(String hash) {
		return blockMap.get(hash).getData();
	}
 }
