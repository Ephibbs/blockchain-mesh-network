import javax.tools.DocumentationTool.Location;

public class ResourceRequestBid extends Message {
	public String type;
	public int amount;
	public int eta;
	public String requestID;
	public ResourceRequestBid(String requestID, int eta, int amount, String auth) {
		super(auth);
		this.requestID = requestID;
		this.eta = eta;
		this.amount = amount;
		this.messageType = "ResourceRequestBid";
	}
	
}
