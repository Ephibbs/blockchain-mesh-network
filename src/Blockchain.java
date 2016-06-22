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
	private ArrayList<Block> incBlks = new ArrayList<Block>();
	private Node node;
	private int difficulty;
	private PuzzleSolver puzzleSolver;
	private BlockChecker blockChecker;
	private Thread t;
	
    public Blockchain(Node node) {
    	System.out.println(node.nodeID);
    	this.node = node;
    	this.difficulty = 4;
    }
    
    public class PuzzleSolver implements Runnable {
    	Thread t;
    	public void run() {
    		while(node.isOnline()) {
    			ArrayList<Message> msgs = blockStore.getOrphanMessages();
    			if(!msgs.isEmpty()) {
	    			Block b = new Block(blockStore.getLastBlock().getMyHash(), msgs);
	    			String head = blockStore.getLastBlock().getMyHash();
	    			long nonce = -1;
	    			b.setNonce(String.valueOf(++nonce));
	    			boolean isSolved = Utils.checkHash(b, difficulty);
	    			while(!isSolved && node.isOnline()) {
	    				if(!head.equals(blockStore.getLastBlock().getMyHash())) {
	    					break;
	    				}
	    				b.setNonce(String.valueOf(++nonce));
	    				isSolved = Utils.checkHash(b, difficulty);
	    			}
	    			if(isSolved) {
	    				System.out.println("solved");
		    			blockStore.add(b);
		    			node.distributeBlock(b);
	    			}
	    			try {
	    				Thread.sleep(200);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
    			} else {
    				//System.out.println("no messages");
    			}
    			
    		}
    	}
    	public void start() {
    		t = new Thread(this, "puzzleSolver");
    		t.start();
    	}

    }

    public class BlockChecker implements Runnable {
    	Thread t;
    	public BlockChecker() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			for(int i = incBlks.size()-1; i >= 0; i--) {
    				System.out.println("block has arrived");
    				Block b = incBlks.get(i);
    				if(Utils.checkHash(b, difficulty)) {
    					System.out.println("block checked");
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
    }
    
    public void run() {
    	puzzleSolver = new PuzzleSolver();
    	blockChecker = new BlockChecker();
    	
    	puzzleSolver.start();
    	System.out.println("running puzzleSolver");
    	blockChecker.start();
    	System.out.println("running blockChecker");
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
    public void add(Message msg) {
    	blockStore.add(msg);
    	System.out.println("added message");
    }
    
    /**
     * block is added to incomingBlocks ArrayList, awaiting verification and addition to the blockchain
     * @param b
     */
    public void add(Block b) {
    	incBlks.add(b);
    }
    public Block getLastTreeNode() {
    	return blockStore.getLastBlock();
    }
}
