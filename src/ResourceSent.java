
public class ResourceSent extends Message {
	String resourceAgreementID;
	public ResourceSent(String agreeID, String auth) {
		super(auth);
		resourceAgreementID = agreeID;
		this.messageType = "ResourceSent";
	}
}
