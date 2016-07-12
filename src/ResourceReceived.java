
public class ResourceReceived extends Message{
	String resourceSentID;
	public String resourceType;
	public int amount;
	//public ResourceReceived(String sentID, String auth) {
	public ResourceReceived(String sentID, String auth, String typ, int amt) {
		super(auth);
		resourceSentID = sentID;
		// TODO Auto-generated constructor stub
		this.messageType = "ResourceReceived";
		this.resourceType = typ;
		this.amount = amt;
	}
	/**
	 * @return the resourceSentID
	 */
	public String getResourceSentID() {
		return resourceSentID;
	}
	/**
	 * @param resourceSentID the resourceSentID to set
	 */
	public void setResourceSentID(String resourceSentID) {
		this.resourceSentID = resourceSentID;
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
