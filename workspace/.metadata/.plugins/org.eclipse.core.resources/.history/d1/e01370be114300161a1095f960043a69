


/**
 * Created by evan on 6/15/16.
 */
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.io.Serializable;

/*
 * A block that make up the blockchain
 * Parameters: Previous hash string, list of messages
 * 
 * a block's hash 
 */

public class Block extends Sendable implements Serializable {

    // Variables
    private String myHash = "";
    private String prevHash;
    private ArrayList<Message> msgs = new ArrayList<Message>();
    private String nonce = "";
    private int difficulty;
    private int id;
    private final long serialVersionUID = 1L;

    // Constructors
    Block() {
    	super("Block");
    }
    Block(String prevHash, ArrayList<Message> msgs){
    	super("Block");
    	this.prevHash = prevHash;
    	this.msgs = msgs;
    }
    
    // Accessors
    String getMyHash() {
		try {
			myHash = Utils.sha256(this.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	return myHash;
    }
    String getPrevHash() {
    	return prevHash;
    }
    String getNonce() {
        return nonce;
    }
    ArrayList<Message> getMsgs() {
        return msgs;
    }
    int getDifficulty(){
        return difficulty;
    }
    int getID() {
        return id;
    }

    // Mutators
    void setMyHash(String myHash) {
        this.myHash = myHash;
    }
    void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }
    void setNonce(String nonce) {
        this.nonce = nonce;
    }
    void addMessage(Message msg) {
    	if(msg != null) {
        	msgs.add(msg);
    	}
    }

    // Utility
    public String toString() { // encode previous hash, messages, difficulty, and nonce to string
    	String out = "";
    	out += prevHash;
    	for (Message m : this.msgs) {
    		out += m.getHash();
    	}
    	out += String.valueOf(this.difficulty);
    	out += this.nonce;
    	return out;
    }
}
