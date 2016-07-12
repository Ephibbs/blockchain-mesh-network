
public class ResourceReceived extends Message{
	String resourceSentID;
	public String type;
	public int amount;
	//public ResourceReceived(String sentID, String auth) {
	public ResourceReceived(String sentID, String auth, String typ, int amt) {
		super(auth);
		resourceSentID = sentID;
		// TODO Auto-generated constructor stub
		this.messageType = "ResourceReceived";
		this.type = typ;
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
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
