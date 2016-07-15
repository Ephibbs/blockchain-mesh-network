


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * BlockStore class to store the blocks, each blockchain has this
 */

public class Blockstore implements Serializable {

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
	private ArrayList<TreeNode<Block>> rootNodesToAdd = new ArrayList<TreeNode<Block>>(); // store nodes to add to blocktree
	private boolean verbose = false;
	
	// Constructor
	Blockstore() {
		treeBlockIDs.add(root.getData().getMyHash());
		blockMap.put(root.getData().getMyHash(), root);
	}

	// Accessors
	TreeNode<Block> getLastBlock() {
		return blockTree.getDeepestTreeNode();
	}
	
	void makeVerbose() {
		verbose = true;
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
			Message m = allMessages.get(s);
			if(m != null) {
				orphanMessages.add(m);
			}
		}
		return orphanMessages;
	}
	
	ArrayList<Message> getMessages() { // get all messages up the tree
		TreeNode<Block> n = blockTree.getDeepestTreeNode();
		ArrayList<Message> msgList = new ArrayList<Message>(n.getData().getMsgs());
		while ((n = n.getParent()) != null) {
			msgList.addAll(n.getData().getMsgs());
		}
		return msgList;
	}

	// Mutators
	boolean add(Block b) {

		// Check if block is unique
		if(blockMap.get(b.getMyHash()) == null) {
			if(treeBlockIDs.contains(b.getPrevHash())) { // check if parent is in blocktree
				TreeNode<Block> p = blockMap.get(b.getPrevHash()); // get previous block (parent block)

				//verify all messages in the block have been received
				boolean messagesVerified = true;
				//The below code is commented out until we get the signature code up and running
				//instead of checking if all the messages have been received
				//we should verify all the messages signatures
//				for(Message m : b.getMsgs()) { // check if messageIDs list has all messages in block
//					if(!messageIDs.contains(m.getHash())) {
//						messagesVerified = false;
//						break;
//					}
//				}
				
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
						//System.out.println("block added to tree");
						TreeNode<Block> bn = new TreeNode<Block>(b);
						blockTree.addTreeNode(p, bn);
						treeBlockIDs.add(b.getMyHash());
						blockMap.put(b.getMyHash(), bn);

						// Display changes
						tempP = new TreeNode<Block>(bn);
						String s = "";
						while(tempP.getDepth() != 0) {
							s += tempP.getData().getMyHash();
							s += " <- ";
							tempP = tempP.getParent();
						}
						s += "0";
						if(verbose) System.out.println(s);
						if(verbose) System.out.println("Messages:");
						if(verbose) {
							for(Message m : b.getMsgs()) {
								System.out.println(m.toString());
							}
						}

						// Check for matching orphan trees
						if (rootNodesToAdd.isEmpty()) {
							for (Tree<Block> t : orphanTrees) { // find matching roots
								if (treeBlockIDs.contains(t.getRootTreeNode().getData().getPrevHash())) {
									rootNodesToAdd.add(t.getRootTreeNode());
								}
							}
							for (TreeNode<Block> tn : rootNodesToAdd) { // add root nodes first
								orphanBlockIDs.remove(tn.getData().getMyHash());
								orphanTrees.remove(tn.getMyTree());
								if (!add(tn.getData())) { // if add failed ,make each child a new tree
									for (TreeNode<Block> nodeToTree : tn.getChildren()) {
										orphanTrees.add(new Tree<Block>(nodeToTree));
									}
								}
							}
							rootNodesToAdd.clear(); // reset root nodes
						} else { // add contents of orphan tree
							for (TreeNode<Block> cn : bn.getChildren()) {
								orphanBlockIDs.remove(cn.getData().getMyHash());
								if (!add(cn.getData())) { // if add failed, make each child a new tree
									for (TreeNode<Block> nodeToTree : cn.getChildren()) {
										orphanTrees.add(new Tree<Block>(nodeToTree));
									}
								}
							}
						}

						return true;
					}
				}
			} else { // is an orphan block
				orphanBlockIDs.add(b.getMyHash()); // add hash to list of orphans
				TreeNode<Block> bn = new TreeNode<Block>(b); // save block in blockmap
				blockMap.put(b.getMyHash(), bn);

				if (orphanBlockIDs.contains(b.getPrevHash())) { // if orphan's parent exists, add orphan to parent's tree
					//System.out.println("into orphan tree");

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
					//System.out.println("into new orphan tree");
					orphanTrees.add(new Tree<Block>(bn));
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
