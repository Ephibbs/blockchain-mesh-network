import java.io.*;

public class Sendable implements Serializable {
	private String sendType;
	Sendable(String t) {
		sendType = t;
	}
	String getType() {
		return sendType;
	}
}
