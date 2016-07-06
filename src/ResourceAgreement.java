
public class ResourceAgreement extends Message {
	int resourceBidID;
	int resourceRequestID;
	public ResourceAgreement(String auth, String rec) {
		super(auth, rec);
		this.messageType = "ResourceAgreement";
		

	}
}