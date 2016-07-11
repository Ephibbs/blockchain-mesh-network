
public class DirectMessage extends Message {
	
	public String messageContents = "";

	public DirectMessage(String auth, String messageText) {
		super(auth);
		this.messageContents = messageText;
		this.messageType = "DirectMessage";
	}
	
	public DirectMessage(String auth, String rec, String messageText) {
		super(auth, rec);
		this.messageContents = messageText;
		this.messageType = "DirectMessage";
	}

}
