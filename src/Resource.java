import javax.tools.DocumentationTool.Location;

public class Resource {
	
	public String type;
	public Location location = null;
	public double XLocation = 0.0;
	public double YLocation = 0.0;
	
	public Resource(int amt, String type, int x, int y){
		this.amount = amt;
		this.type = type;
		this.XLocation = x;
		this.YLocation = y;
	}
	
	public Resource(int amt, String type, Location l){
		this.amount = amt;
		this.type = type;
		this.location = l;
	}
	
	public int amount;
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
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
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
