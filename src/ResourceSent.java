
public class ResourceSent extends Message {
	String resourceAgreementID;
	public String type;
	public int amount;
	//public ResourceSent(String agreeID, String auth) {
	public ResourceSent(String agreeID, String auth, String typ, int amt) {
		super(auth);
		resourceAgreementID = agreeID;
		this.messageType = "ResourceSent";
		this.type = typ;
		this.amount = amt;
	}
	/**
	 * @return the resourceAgreementID
	 */
	public String getResourceAgreementID() {
		return resourceAgreementID;
	}
	/**
	 * @param resourceAgreementID the resourceAgreementID to set
	 */
	public void setResourceAgreementID(String resourceAgreementID) {
		this.resourceAgreementID = resourceAgreementID;
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
