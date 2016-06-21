import java.io.Serializable;

/*
 * A class for text messages, inherits structure from Message class
 * Parameters: Data object, author node, (optional) receiver node
 */

public class TextMessage extends Message implements Serializable {
	
	// Constructors
	public TextMessage(Object data, Node auth, Node rec) {
		super(data, auth, rec);
	}

	public TextMessage(Object data, Node auth) {
		super(data, auth);
	}
}