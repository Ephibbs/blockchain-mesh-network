/**
 * Created by evan on 6/15/16.
 */
import java.util.ArrayList;

/*
 * The blocks that make up the blockchain
 * Parameters: Previous hash string, list of messages
 */

public class Block {

    // Variables
    private String myHash = "";
    private String prevHash;
    private ArrayList<Message> msgs;
    private String nonce = "";
    private int difficulty;
    private int id;

    // Constructors
    Block() {
    	
    }
    Block(String prevHash, ArrayList<Message> msgs){
    	this.msgs = msgs;
    }

    // Accessors
    String getMyHash() {
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
        msgs.add(msg);
    }

    // Utility
    public String toString() {
    	String out = "";
    	out += prevHash;
    	for (Message m : this.msgs) {
    		out += m.toString();
    	}
    	out += String.valueOf(this.difficulty);
    	out += this.nonce;
    	return out;
    }
}
