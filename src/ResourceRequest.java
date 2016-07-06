import javax.tools.DocumentationTool.Location;

public class ResourceRequest extends Message{
	public String type;
	public int amount;
	public ResourceRequest(int amount, String type, String auth) {
		super(auth);
		this.amount = amount;
		this.type = type;
		this.messageType = "ResourceRequest";
	}
}
