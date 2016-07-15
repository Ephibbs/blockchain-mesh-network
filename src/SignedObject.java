
public class SignedObject extends Message{
	public byte[] byteArray = new byte[1024];
	public byte[] sig = new byte[1024];
	public String Author = "";
	
	public SignedObject(String author, byte[] bytearray, byte[] sig){
		super(author);
		this.byteArray = bytearray;
		this.sig = sig;
		this.Author = author;
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
	
	
}
