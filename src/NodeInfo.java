import java.security.PublicKey;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by 585728 on 7/8/2016.
 */
public class NodeInfo {

    // Variables
    private String nodeID = null;
    private PublicKey pubKey = null;
    private Location myLocation = null;
    private ArrayList<Message> resourceList = new ArrayList<Message>();
    private Time lastPingTime = null;

    // Constructor
    public NodeInfo(String nodeID, PublicKey pubKey, Location myLocation, ArrayList<Message> resourceList, Time latPingTime) {
        this.nodeID = nodeID;
        this.pubKey = pubKey;
        this.myLocation = myLocation;
        this.resourceList = resourceList;
        this.lastPingTime = latPingTime;
    }
    /**
	 * @return the nodeID
	 */
	public String getNodeID() {
		return nodeID;
	}

	/**
	 * @param nodeID the nodeID to set
	 */
	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	/**
	 * @return the myLocation
	 */
	public Location getMyLocation() {
		return myLocation;
	}

	/**
	 * @param myLocation the myLocation to set
	 */
	public void setMyLocation(Location myLocation) {
		this.myLocation = myLocation;
	}

	/**
	 * @return the resourceList
	 */
	public ArrayList<Message> getResourceList() {
		return resourceList;
	}

	/**
	 * @param resourceList the resourceList to set
	 */
	public void setResourceList(ArrayList<Message> resourceList) {
		this.resourceList = resourceList;
	}

	/**
	 * @return the lastPingTime
	 */
	public Time getLastPingTime() {
		return lastPingTime;
	}

	/**
	 * @param lastPingTime the lastPingTime to set
	 */
	public void setLastPingTime(Time lastPingTime) {
		this.lastPingTime = lastPingTime;
	}

	/**
	 * @return the pubKey
	 */
	public PublicKey getPubKey() {
		return pubKey;
	}
}