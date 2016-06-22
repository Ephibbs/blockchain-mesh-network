/**
 * Created by evan on 6/15/16.
 */
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Block {
    private String myHash = "";
    private String prevHash;
    private ArrayList<Message> msgs;
    private String nonce = "";
    private int difficulty;
    Block() {
    	
    }
    Block(String prevHash, ArrayList<Message> msgs){
    	this.prevHash = prevHash;
    	this.msgs = msgs;
    }
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
    void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    void addMessage(Message msg) {
        msgs.add(msg);
    }
    void setNonce(String nonce) {
    	this.nonce = nonce;
    }
    ArrayList<Message> getMsgs() {
    	return msgs;
    }
    String getNonce() {
    	return nonce;
    }
    int getDifficulty(){
    	return difficulty;
    }
    public String toString() {
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
