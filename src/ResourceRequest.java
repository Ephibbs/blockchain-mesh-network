
public class ResourceRequest extends Message{

	public ResourceRequest(Object data, Node auth) {
		super(data, auth);
		this.messageType = "ResourceRequest";
	}
}
