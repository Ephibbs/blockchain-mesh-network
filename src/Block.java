/**
 * Created by evan on 6/15/16.
 */
import java.util.ArrayList;

public class Block {
    private String myHash = "";
    private String prevHash;
    private ArrayList<Message> msgs;
    private String nonce;
    Block(){

    }
    void setMyHash(String myHash) {
        this.myHash = myHash;
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
}
