import java.io.*;

public class Sendable implements Serializable {
	private String type;
	Sendable(String t) {
		type = t;
	}
	String getType() {
		return type;
	}
}