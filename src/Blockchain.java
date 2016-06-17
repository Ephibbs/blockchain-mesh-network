/*
* Blockchain.java
* An implementation of blockchain
 */

import java.util.ArrayList;
import java.util.Queue;
import java.lang.Thread;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Blockchain {
	private Block root = new Block();
	private Tree<Block> blockTree = new Tree<Block>(root);
	private ArrayList<Message> msgs = new ArrayList<Message>();
	private ArrayList<Block> incBlks = new ArrayList<Block>();
	private Node node;
	private int difficulty;
	
    public Blockchain(Node node) {
    	this.node = node;
    	this.run();
    }
    public class PuzzleSolver implements Runnable {
    	public PuzzleSolver() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			Block b = new Block(blockTree.getDeepestTreeNode().getData().getMyHash(), msgs);
    			TreeNode<Block> head = blockTree.getDeepestTreeNode();
    			long nonce = -1;
    			boolean isVerified = this.checkHashWithNonce(b, nonce++);
    			while(!isVerified && node.isOnline()) {
    				if(!head.equals(blockTree.getDeepestTreeNode())) {
    					break;
    				}
    				isVerified = this.checkHashWithNonce(b, nonce++);
    			}
    			if(isVerified) {
	    			b.setNonce(String.valueOf(nonce));
	    			this.removefromMsgsInBlock(b);
	    			blockTree.addTreeNode(blockTree.getDeepestTreeNode(), b);
	    			node.broadcast(b);
    			}
    		}
    	}
    	public boolean checkHashWithNonce(Block b, long nonce) {
    		int diff = b.getDifficulty();
        	if (diff < 1) {
        		diff = 1;
            } else if (diff > 32) {
            	diff = 32;
            }
            
            // Get hash
        	b.setNonce(String.valueOf(nonce));
            int hc = (b.toString()).hashCode();
            String hash = String.format("%32s", Integer.toBinaryString(hc)).replace(" ", "0");

            // Verified?
            for (int i = 0; i < hash.length(); i++) {
                if (hash.charAt(i) == '1') {
                    break;
                } else if (i == diff-1) {
                    return true;
                }
            }
            return false;
    	}
    	void removefromMsgsInBlock(Block block) {
    		for(Message m : block.getMsgs()) {
    			msgs.remove(m);
    		}
    	}
    }
    public class BlockChecker implements Runnable {
    	public BlockChecker() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			for(Block b : incBlks) {
    				TreeNode<Block> parent = this.findInLeaves(b.getPrevHash());
    				if(!parent.equals(null) && this.checkHash(b)) {
    					this.removefromMsgsInBlock(b);
    					blockTree.addTreeNode(parent, b);
    					node.broadcast(b);
    				}
    			}
    			incBlks = new ArrayList<Block>();
    			Thread.sleep(1000);
    		}
    	}
    	void removefromMsgsInBlock(Block block) {
    		for(Message m : block.getMsgs()) {
    			msgs.remove(m);
    		}
    	}
    	public boolean checkHash(Block b) {
    		int hc = (b.toString()).hashCode();
            String hash = String.format("%32s", Integer.toBinaryString(hc)).replace(" ", "0");

            // Verified?
            for (int i = 0; i < hash.length(); i++) {
                if (hash.charAt(i) == '1') {
                    break;
                } else if (i == difficulty-1) {
                    return true;
                }
            }
            return false;
        }
    	public TreeNode<Block> findInLeaves(String hash) {
    		for(TreeNode<Block> blockNode : blockTree.getLeaves()) {
    			if(blockNode.getData().getMyHash() == hash) {
    				return blockNode;
    			}
    		}
    		return null;
        }
    }
    
    public void run() {
    	Thread puzzleSolver = new Thread(new PuzzleSolver(), "puzzleSolver");
    	Thread blockChecker = new Thread(new BlockChecker(), "blockChecker");
    	
    	puzzleSolver.run();
    	blockChecker.run();
    }
    
    /**
     * msg is assumed to have been verified
     * msg is added to msgs ArrayList
     * @param msg
     */
    public void addMessage(Message msg) {
    	msgs.add(msg);
    }
    
    /**
     * block is added to incomingBlocks ArrayList, awaiting verification and addition to the blockchain
     * @param b
     */
    public void receiveBlock(Block b) {
    	incBlks.add(b);
    }
}
