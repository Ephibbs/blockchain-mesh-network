import javax.tools.DocumentationTool.Location;

public class ResourceRequest extends Message{
	public String resourceType;
	public int amount;
	public ResourceRequest(int amount, String type, String auth) {
		super(auth);
		this.amount = amount;
		this.resourceType = resourceType;
		this.messageType = "ResourceRequest";
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
