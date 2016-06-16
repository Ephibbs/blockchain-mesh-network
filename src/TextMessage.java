
public class TextMessage extends Message {

	public TextMessage(Object data, Node auth, Node rec) {
		super(data, auth, rec);
	}

	public TextMessage(Object data, Node auth) {
		super(data, auth);
	}

}
