import javax.tools.DocumentationTool.Location;

public class ResourceRequestBid extends Message {
	public String type;
	public int amount;
	public ResourceRequestBid(String auth, String rec) {
		super(auth, rec);
		this.messageType = "ResourceRequestBid";
	}
	
}
