



import java.util.ArrayList;
import java.io.Serializable;
import java.util.Queue;
import java.lang.Thread;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.lang.Thread;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * This class manages the order of messages that travel through the network, each node has its own copy of the blockchain
 * Blockchain has two threads: 
 * 		PuzzleSolver - to solve for the next block in the chain
 * 		BlockChecker - to verify that incoming blocks are valid, and adding to blockchain
 * 
 * Parameters: Node node
 */

public class Blockchain implements Runnable, Serializable {

	// Variables
	private Blockstore blockStore = new Blockstore();
	private ArrayList<Block> incBlks = new ArrayList<Block>();
	private Node node;
	private int difficulty;
	private PuzzleSolver puzzleSolver;
	private BlockChecker blockChecker;
	private Thread t;
	private boolean verbose = false;

	// Constructor
    public Blockchain(Node node) {
    	this.node = node;
    	this.difficulty = 4;
    }

	// Accessors
	public Block getLastBlock() {
		return blockStore.getLastBlock().getData();
	}
	
	public void makeVerbose() {
		verbose = true;
		blockStore.makeVerbose();
	}

	// Mutators
	public void add(Message msg) { // assume message is already verified, added to arraylist msgs
		blockStore.add(msg);
		if(verbose) System.out.println("added message");
	}
	public void add(Block b) { // block is added to incomingBlocks ArrayList, awaiting verification and addition to the blockchain
		if(blockStore.getBlock(b.getPrevHash()) == null) {
			node.makeBlockRequest(b.getPrevHash());
		}
		incBlks.add(b);
	}
	public boolean hasBlock(String hash) {
		return blockStore.getBlock(hash) != null;
	}
	Block getBlock(String hash) {
		TreeNode<Block> tnB = blockStore.getBlock(hash);
		if(tnB == null) {
			return null;
		}
		return tnB.getData();
	}

	// Utility
	public class PuzzleSolver implements Runnable { // try to solve puzzle
    	Thread t;
    	public void run() {
    		while(node.isOnline()) {
    			ArrayList<Message> msgs = blockStore.getOrphanMessages();
    			if(!msgs.isEmpty()) {
    				if(verbose) System.out.println("solving for block");
    				String head = blockStore.getLastBlock().getData().getMyHash();
	    			Block b = new Block(head, msgs);
	    			long nonce = -1;
	    			b.setNonce(String.valueOf(++nonce));
	    			boolean isSolved = Utils.checkHash(b, difficulty);
	    			while(!isSolved && node.isOnline()) {
	    				if(!head.equals(blockStore.getLastBlock().getData().getMyHash())) {
	    					break;
	    				}
	    				b.setNonce(String.valueOf(++nonce));
	    				isSolved = Utils.checkHash(b, difficulty);
	    			}
	    			if(isSolved) {
	    				if(verbose) System.out.println("solved");
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
    public class BlockChecker implements Runnable { // check incoming blockchain
    	Thread t;
    	public BlockChecker() {
    	}
    	public void run() {
    		while(node.isOnline()) {
    			for(int i = incBlks.size()-1; i >= 0; i--) {
    				if(verbose) System.out.println("block has arrived");
    				Block b = incBlks.get(i);
    				if(Utils.checkHash(b, difficulty)) {
    					if(verbose) System.out.println("block checked");
		    			if(blockStore.add(b)) {
		    				if(verbose) System.out.println("block added");
		    				node.distributeBlock(b);
		    			}
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
        	//System.out.println(node.getNodeID());
    		t = new Thread(this, "blockChecker");
    		t.start();
    	}
    }
    public void run() { // actually executing the code
    	puzzleSolver = new PuzzleSolver();
    	blockChecker = new BlockChecker();
    	
    	puzzleSolver.start();
    	if(verbose) System.out.println("running puzzleSolver");
    	blockChecker.start();
    	if(verbose) System.out.println("running blockChecker");
    }
    public void start() { // run each task independently
    	t= new Thread(this, "blockChain");
    	t.start();
    }
    public void setDifficulty(int difficulty) {
    	this.difficulty = difficulty;
    }
    public ArrayList<Block> getBlockchain() {
    	TreeNode<Block> tn = blockStore.getLastBlock();
    	ArrayList<Block> blocks = new ArrayList<Block>();
    	blocks.add(tn.getData());
    	while((tn = tn.getParent()) != null) {
    		blocks.add(tn.getData());
    	}
    	return blocks;
    }
    public ArrayList<String> getBlockchainHashes() {
    	ArrayList<Block> blocks = getBlockchain();
    	ArrayList<String> blockHashes = new ArrayList<String>();
    	for(Block b : blocks) {
    		blockHashes.add(b.getMyHash());
    	}
    	return blockHashes;
    }
}
