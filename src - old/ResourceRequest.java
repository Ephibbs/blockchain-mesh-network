import javax.tools.DocumentationTool.Location;

public class ResourceRequest extends Message{
	public String type;
	public int amount;
	public ResourceRequest(String auth, String rec) {
		super(auth, rec);
		this.messageType = "ResourceRequest";
	}
}
