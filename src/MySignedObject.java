
public class MySignedObject extends Message{
	public byte[] byteArray = new byte[1024];
	public byte[] sig = new byte[1024];
	public String Author = "";
	public String typeToCast = "";
	
	public MySignedObject(String author, byte[] bytearray, byte[] sig){
		super(author);
		this.byteArray = bytearray;
		this.sig = sig;
		this.Author = author;
		//this.typeToCast = classToCast;
		this.messageType = "MySignedObject";
	}

	/**
	 * @return the byteArray
	 */
	public byte[] getByteArray() {
		return byteArray;
	}

	/**
	 * @return the sig
	 */
	public byte[] getSig() {
		return sig;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return Author;
	}
		
}
