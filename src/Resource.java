import javax.tools.DocumentationTool.Location;

public class Resource {
	
	public String type;
	public double XLocation = 0.0;
	public double YLocation = 0.0;
	public int amount;
	public String category;
	public Location ownerLocation = null;
	public String ownerName = null;

	public Resource(int amt, String type, int x, int y, String cat, String ownerN){
		this.amount = amt;
		this.type = type;
		this.XLocation = x;
		this.YLocation = y;
		this.category = cat;
		this.ownerName = ownerN;
	}
	
	public Resource(int amt, String type, Location l, String cat, String ownerN){
		this.amount = amt;
		this.type = type;
		this.ownerLocation = l;
		this.category = cat;
		this.ownerName = ownerN;
	}
	
	/**
	 * @return the ownerLocation
	 */
	public Location getOwnerLocation() {
		return ownerLocation;
	}

	/**
	 * @param ownerLocation the ownerLocation to set
	 */
	public void setOwnerLocation(Location ownerLocation) {
		this.ownerLocation = ownerLocation;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the xLocation
	 */
	public double getXLocation() {
		return XLocation;
	}

	/**
	 * @param xLocation the xLocation to set
	 */
	public void setXLocation(double xLocation) {
		XLocation = xLocation;
	}

	/**
	 * @return the yLocation
	 */
	public double getYLocation() {
		return YLocation;
	}

	/**
	 * @param yLocation the yLocation to set
	 */
	public void setYLocation(double yLocation) {
		YLocation = yLocation;
	}
}
