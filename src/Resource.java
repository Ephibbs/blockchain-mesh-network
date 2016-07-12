import javax.tools.DocumentationTool.Location;

public class Resource {
	
	public String type;
	public int amount;
	public String category;

	public Resource(int amt, String type, String cat){
		this.amount = amt;
		this.type = type;
		this.category = cat;
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

	public void modifyResource(int amount2) {
		// TODO Auto-generated method stub
		this.amount = this.amount + amount2;
	}
}
