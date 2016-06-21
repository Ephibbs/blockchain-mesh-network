import java.io.Serializable;

/*
 * Class for text messages, inherits structure from Message class
 * Parameters: Data object, author node, (optional) receiver node
 */

public class TextMessage extends Message implements Serializable {

	// Constructors
	public TextMessage(Object data, Node auth, Node rec) {
		super(data, auth, rec);
	} // use parent class constructor
	public TextMessage(Object data, Node auth) {
		super(data, auth);
	}
}