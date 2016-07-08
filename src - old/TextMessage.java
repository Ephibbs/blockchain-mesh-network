import java.io.Serializable;

/*
 * Class for text messages, inherits structure from Message class
 * Parameters: Data object, author node, (optional) receiver node
 */

public class TextMessage extends Message implements Serializable {
	private String data;
	// Constructors
	public TextMessage(String data, String auth, String rec) {
		super(auth, rec);
		this.data = data;
	}
}
