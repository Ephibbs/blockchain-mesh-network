/*
* Blockchain.java
* An implementation of blockchain
 */

import java.util.ArrayList;
import java.util.Queue;
import java.lang.Thread;

public class Blockchain {
	private Block root = new Block();
	private Tree<Block> bt = new Tree<Block>(root);
	private ArrayList<Message> msgs = new ArrayList<Message>();
	private ArrayList<Block> incomingBlocks = new ArrayList<Block>();
	private int difficulty;
	private Node node;
	
    public Blockchain(Node node) {
    	this.node = node;
    	this.run();
    }
    public class PuzzleSolver implements Runnable {
    	public PuzzleSolver() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			Block b = new Block(bt.getDeepestTreeNode().getData().getMyHash(), msgs);
    			TreeNode head = bt.getDeepestTreeNode();
    			long nonce = -1;
    			while(!this.checkHashWithNonce(b, nonce++)) {
    				if(!head.equals(bt.getDeepestTreeNode())) {
    					break;
    				}
    			}
    			if(this.checkHashWithNonce(b, nonce)) {
	    			b.setNonce(String.valueOf(nonce));
	    			this.removefromMsgsInBlock(b);
	    			bt.addTreeNode(bt.getDeepestTreeNode(), new TreeNode<Block>(b));
    			}
    		}
    	}
    }
    public class BlockChecker implements Runnable {
    	public BlockChecker() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			
    		}
    	}
    }
    
    public void run() {
    	Thread puzzleSolver = new Thread(new PuzzleSolver(), "puzzleSolver");
    	Thread blockChecker = new Thread(new BlockChecker(), "blockChecker");
    	
    	puzzleSolver.run();
    	blockChecker.run();
    }
	void removeMsgsInBlock(Block block) {
		for(Message m : block.getMsgs()) {
			msgs.remove(m);
		}
	}
    public void addMessage(Message msg) {
    	msgs.add(msg);
    }
    public boolean checkHashWithNonce(Block b, long nonce) {
    	if
    }
    public boolean checkHash(Block b) {
    	if
    }
    public void receiveBlock(Block b) {
    	incomingBlocks.add(b);
    }
    public Tree<Block> getBt() {
    	return bt;
    }
    public ArrayList<Message> getMsgs() {
    	return msgs;
    }
    public boolean didRe
}
