
public class BlockRequest extends Message {
	public String hash;
	BlockRequest(String hash, String auth) {
		super(auth);
		this.hash = hash;
	}
}
