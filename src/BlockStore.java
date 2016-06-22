import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockStore {

	// Variables
	private TreeNode<Block> root = new TreeNode<Block>(new Block("1", new ArrayList<Message>()));
	private Tree<Block> blockTree = new Tree<Block>(root);
	private ArrayList<String> treeBlockIDs = new ArrayList<String>();
	private ArrayList<String> orphanBlockIDs = new ArrayList<String>();
	//private ArrayList<TreeNode<Block>> allBlocks = new ArrayList<Block>();
	private HashMap<String, TreeNode<Block>> blockMap = new HashMap<String, TreeNode<Block>>();
	private ArrayList<Tree<Block>> orphanTrees = new ArrayList<Tree<Block>>();
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

	//
	void add(Block b) {
		//check that parent exists
		TreeNode<Block> c = blockMap.get(b.getMyHash());
		//if not in tree
		if(c == null) {
			if(treeBlockIDs.contains(b.getPrevHash())) {
				//check if block is in blockstore already
				TreeNode<Block> p = blockMap.get(b.getPrevHash());
					
				//verify all messages in the block have been received
				boolean messagesVerified = true;
				for(Message m : b.getMsgs()) {
					if(!messageIDs.contains(m.getHash())) {
						messagesVerified = false;
						break;
					}
				}
				
				//verify messages aren't in any previous block
				if(messagesVerified) {
					ArrayList<Message> blockMsgs = b.getMsgs();
					TreeNode<Block> tempP = new TreeNode<Block>(p);
					while(tempP.getDepth() != 0) {
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
						
						tempP = new TreeNode<Block>(bn);
						String s = "";
						while(tempP.getDepth() != 0) {
							s += tempP.getData().getMyHash();
							s += " <- ";
							tempP = tempP.getParent();
						}
						s += "0";
						System.out.println(s);
					}
				}
			
			} else if(orphanBlockIDs.contains(b.getPrevHash())) {
				System.out.println("into orphan chain");
				//TreeNode<Block> p = blockMap.get(b.getPrevHash());
				//blocks came out of order
			} else {
				//create new tree in orphanTrees
				System.out.println("into new orphan chain");
				orphanTrees.add(new Tree<Block>(b));
			}
		}
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
	
	void add(Message m) {
		if(!messageIDs.contains(m.getHash())) {
			messageIDs.add(m.getHash());
			allMessages.put(m.getHash(), m);
		}
	}
}
