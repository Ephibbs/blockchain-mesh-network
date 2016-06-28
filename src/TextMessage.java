import java.io.Serializable;

/*
 * Class for text messages, inherits structure from Message class
 * Parameters: Data object, author node, (optional) receiver node
 */

public class TextMessage extends Message implements Serializable {

	// Constructors
	public TextMessage(Object data, SimulationNode auth, SimulationNode rec) {
		super(data, auth, rec);
	}
	public TextMessage(Object data, SimulationNode auth) {
		super(data, auth);
	}
}
