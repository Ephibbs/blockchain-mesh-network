import java.io.Serializable;
import java.security.Signature;
import java.security.Timestamp;
import java.util.ArrayList;

/*
 * Abstract class used solely to provide structure for message objects
 * Message objects can contain different content (file, text, image, etc.)
 * Parameters: Data object, author node, (optional) receiver node
 */

public abstract class Message implements Serializable {

	// Variables
//	public Node author = new Node(null);
//	public Node recipient = new Node(null);
	public Node author = null;
	public Node recipient = null;
	public Object messageData = new Object();
	public String hash;

	// Constructors
	public Message(Object data, Node auth, Node rec) {
		this.messageData = data;
		this.author = auth;
		this.recipient = rec;
	}
	public Message(Object data, Node auth) {
		this.messageData = data;
		this.author = auth;
	}

	// Accessors
	public Object getMessageData() {
		return messageData;
	}
	public Node getAuthor() {
		return author;
	}
	public Node getRecipient() {
		return recipient;
	}
	public String getHash() {
		return hash;
	}

	// Printers
	public void printMessage() {
		System.out.println("The message is: " + messageData.toString() + " from: " +this.author.getNodeID()+ " to: " + this.recipient.getNodeID());
	}
}