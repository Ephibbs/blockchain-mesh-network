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
        this.lastPingTime = lastPingTime;
    }
}
