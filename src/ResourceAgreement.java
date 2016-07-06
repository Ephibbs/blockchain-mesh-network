


public class ResourceAgreement extends Message {
	String resourceBidID;
	public ResourceAgreement(String bidID, String auth) {
		super(auth);
		resourceBidID = bidID;
		this.messageType = "ResourceAgreement";
		

	}
}