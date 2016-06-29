
public class ResourceRequestAccept extends Message {

	public ResourceRequestAccept(Object data, Node auth) {
		super(data, auth);
		this.messageType = "ResourceRequestAccept";
	}

}
