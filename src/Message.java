import java.security.Signature;
import java.security.Timestamp;
import java.util.ArrayList;

/*
 * This class is the main class for handling a message object. A message object
 * is defined as being constructed by the content of the message (file, text,
 * string, image,etc.) and the author of the message. This is a simple class
 * used solely for constructing a message.
 */

public abstract class Message {

	public Node author = new Node(null);
	public Node recipient = new Node(null);
	public Object messageData = new Object();
	
	public Message(Object data, Node auth, Node rec) {
		this.messageData = data;
		this.author = auth;
		this.recipient = rec;
	}
	
	public Message(Object data, Node auth) {
		this.messageData = data;
		this.author = auth;
	}
	
	/**
	 * @return the messageData
	 */
	public Object getMessageData() {
		return messageData;
	}

	/**
	 * @return the author
	 */
	public Node getAuthor() {
		return author;
	}
	
	public void printMessage() {
		System.out.println("The message is: " + messageData.toString() + " from: " +this.author.getNodeID()+ " to: " + this.recipient.getNodeID());
	}
}
