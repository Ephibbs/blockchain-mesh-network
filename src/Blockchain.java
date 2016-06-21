/*
* Blockchain.java
* An implementation of blockchain
 */

import java.util.ArrayList;
import java.util.Queue;
import java.lang.Thread;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.lang.Thread;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Blockchain implements Runnable {
	private BlockStore blockStore = new BlockStore();
	private ArrayList<Message> msgs = new ArrayList<Message>();
	private ArrayList<Block> incBlks = new ArrayList<Block>();
	private Node node;
	private int difficulty;
	private PuzzleSolver puzzleSolver;
	private BlockChecker blockChecker;
	private Thread t;
	
    public Blockchain(Node node) {
    	System.out.println(node.nodeID);
    	this.node = node;
    	this.difficulty = 2;
    }
    
    public class PuzzleSolver implements Runnable {
    	Thread t;
    	public PuzzleSolver() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			if(!msgs.isEmpty()) {
	    			Block b = new Block(blockStore.getLastBlock().getData().getMyHash(), msgs);
	    			TreeNode<Block> head = blockStore.getLastBlock();
	    			long nonce = -1;
	    			boolean isVerified = Utils.checkHashWithNonce(b, nonce++, difficulty);
	    			while(!isVerified && node.isOnline()) {
	    				if(!head.equals(blockStore.getLastBlock())) {
	    					break;
	    				}
	    				isVerified = Utils.checkHashWithNonce(b, nonce++, difficulty);
	    			}
	    			if(isVerified) {
	    				System.out.print("verified block:");
	    				System.out.println(b.getID());
	    				System.out.println(nonce);
		    			b.setNonce(String.valueOf(nonce));
		    			this.removefromMsgsInBlock(b);
		    			blockStore.addTreeNode(blockStore.getLastBlock(), b);
		    			node.distributeBlock(b);
	    			}
	    			try {
	    				Thread.sleep(1000);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
    			}
    			
    		}
    	}
    	public void start() {
    		t = new Thread(this, "puzzleSolver");
    		t.start();
    	}
    	public void removefromMsgsInBlock(Block block) {
    		for(Message m : block.getMsgs()) {
    			msgs.remove(m);
    		}
    	}

    }

    public class BlockChecker implements Runnable {
    	Thread t;
    	public BlockChecker() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			for(int i = incBlks.size()-1; i >= 0; i--) {
    				Block b = incBlks.get(i);
    				if(Utils.checkHash(b)) {
    					this.removefromMsgsInBlock(b);
    					blockStore.add(b);
    					node.distributeBlock(b);
    				}
    				incBlks.remove(b);
    			}
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    	public void start() {
    		t = new Thread(this, "blockChecker");
    		t.start();
    	}
    	public void removefromMsgsInBlock(Block block) {
    		for(Message m : block.getMsgs()) {
    			msgs.remove(m);
    		}
    	}
    }
    
    public void run() {
    	puzzleSolver = new PuzzleSolver();
    	blockChecker = new BlockChecker();
    	
    	puzzleSolver.start();
    	System.out.println("running puzzleSolver");
    	blockChecker.start();
    }
    
    public void start() {
    	t= new Thread(this, "blockChain");
    	t.start();
    }
    
    /**
     * msg is assumed to have been verified
     * msg is added to msgs ArrayList
     * @param msg
     */
    public void addMessage(Message msg) {
    	msgs.add(msg);
    	System.out.println("added message");
    }
    
    /**
     * block is added to incomingBlocks ArrayList, awaiting verification and addition to the blockchain
     * @param b
     */
    public void receiveBlock(Block b) {
    	incBlks.add(b);
    }
}
