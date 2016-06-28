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

public abstract class Message implements Serializable {

	// Variables
//	public SimulationNode author = new SimulationNode(null);
//	public SimulationNode recipient = new SimulationNode(null);
	public SimulationNode author = null;
	public SimulationNode recipient = null;
	public Object messageData = new Object();
	public int id=0;

	// Constructors
	public Message(Object data, SimulationNode auth, SimulationNode rec) {
		this.messageData = data;
		this.author = auth;
		this.recipient = rec;
	}
	public Message(Object data, SimulationNode auth) {
		this.messageData = data;
		this.author = auth;
	}
	public Message(Object data) {
		this.messageData = data;
	}
	
	/**
	 * @return the messageData
	 */

	// Accessors
	public Object getMessageData() {
		return messageData;
	}
	public SimulationNode getAuthor() {
		return author;
	}
	public SimulationNode getRecipient() {
		return recipient;
	}
	public String getHash() {
		try {
			return Utils.sha256(messageData.toString() + Integer.toString(id));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	// Utility
	public void printMessage() {
		System.out.println("The message is: " + messageData.toString() + " from: " +this.author.getNodeID()+ " to: " + this.recipient.getNodeID());
	}
}