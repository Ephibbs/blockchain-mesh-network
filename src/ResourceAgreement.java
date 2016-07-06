
public class ResourceAgreement extends Message {
	int resourceBidID;
	int resourceRequestID;
	public ResourceAgreement(String auth, String rec) {
		super(auth, rec);
		// TODO Auto-generated constructor stub
		this.messageType = "ResourceAgreement";
		

	}
}