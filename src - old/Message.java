import java.awt.TrayIcon.MessageType;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Random;

/*
 * Abstract class used solely to provide structure for message objects
 * Message objects can contain different content (file, text, image, etc.)
 * Parameters: Data object, author node, (optional) receiver node
 */

public abstract class Message extends Sendable implements Serializable {

	// Variables
//	public Node author = new Node(null);
//	public Node recipient = new Node(null);
	
	public String messageType;
	public String author = null;
	public String recipient = null;
	public int id=0;
	public final long serialVersionUID = 2L;

	// Constructors
	public Message(String auth, String rec) {
		super("Message");
		this.author = auth;
		this.recipient = rec;
		id = Utils.getRandID(1000000000);
	}
	
	public Message() {
		super("Message");
		id = Utils.getRandID(1000000000);
	}
	
	/**
	 * @return the messageData
	 */

	// Accessors
	public String getMessageType() {
		return messageType;
	}
	public String getAuthor() {
		return author;
	}
	public String getRecipient() {
		return recipient;
	}
	public String getHash() {
		try {
			return Utils.sha256(author + recipient + Integer.toString(id));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public int getID() {
		return id;
	}

	// Utility
	public void printMessage() {
		System.out.println(" from: " + this.author + " to: " + this.recipient);
	}
}