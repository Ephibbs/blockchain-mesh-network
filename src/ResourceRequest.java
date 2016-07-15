import javax.tools.DocumentationTool.Location;

public class ResourceRequest extends Message{
	public String resourceType;
	public int amount;
	public String reason;
	public ResourceRequest(int amount, String type, String auth, String reason) {
		super(auth);
		this.amount = amount;
		this.resourceType = type;
		this.messageType = "ResourceRequest";
		this.reason = reason;
	}
	/**
	 * @return the type
	 */
	public String getResourceType() {
		return resourceType;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.resourceType = type;
	}
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
