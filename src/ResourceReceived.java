
public class ResourceReceived extends Message{
	String resourceSentID;
	public ResourceReceived(String sentID, String auth) {
		super(auth);
		resourceSentID = sentID;
		// TODO Auto-generated constructor stub
		this.messageType = "ResourceReceived";
	}

}
