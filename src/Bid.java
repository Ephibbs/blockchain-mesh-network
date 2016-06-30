
public class Bid {
	
	public int BidNumber = 0;
	public Node Bidder = null;
	public Node Requester = null;
	public int ETA = 0;
	public int amount = 0;
	
	public Bid(Node bidder, Node requester, int eta, int amt){
		//this.BidNumber = bidnum;
		this.Bidder = bidder;
		this.Requester = requester;
		this.ETA = eta;
		this.amount = amt;
	}
	
	/**
	 * @return the bidNumber
	 */
	public int getBidNumber() {
		return BidNumber;
	}
	/**
	 * @param bidNumber the bidNumber to set
	 */
	public void setBidNumber(int bidNumber) {
		BidNumber = bidNumber;
	}
	/**
	 * @return the bidder
	 */
	public Node getBidder() {
		return Bidder;
	}
	/**
	 * @param bidder the bidder to set
	 */
	public void setBidder(Node bidder) {
		Bidder = bidder;
	}
	/**
	 * @return the requester
	 */
	public Node getRequester() {
		return Requester;
	}
	/**
	 * @param requester the requester to set
	 */
	public void setRequester(Node requester) {
		Requester = requester;
	}
	/**
	 * @return the eTA
	 */
	public int getETA() {
		return ETA;
	}
	/**
	 * @param eTA the eTA to set
	 */
	public void setETA(int eTA) {
		ETA = eTA;
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
	
	

}
