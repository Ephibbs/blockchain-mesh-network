import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by 585728 on 6/28/2016.
 */

/*
 * class to provide structure for the nodes in a network
 */
public class Node implements Serializable {

    // Variables
    public String nodeID = null;
    public ArrayList<Node> networkNodes = new ArrayList<Node>();
    public ArrayList<Message> localMSG = new ArrayList<Message>();
    public ArrayList<PublicKey> publicKeySet = new ArrayList<PublicKey>();
    public KeyPairGenerator keyGen = null;
    public SecureRandom random = null;
    public Signature dsa = null;
    public KeyPair pair = null;
    public PrivateKey privKey = null;
    public PublicKey pubKey = null;
    public byte[] byteArray = new byte[1024];
    public Blockchain blockChain;
    public Hashtable<Node, Node> routeTable = new Hashtable<Node, Node>();
    public Random rand = new Random();
    private Ping ping;
    public ArrayList<String> blockRequestIDs = new ArrayList<String>();
    public HashMap<String,Integer> myResources = new HashMap<String,Integer>();

    // Constructor
    public Node(String id) throws NoSuchAlgorithmException, NoSuchProviderException {

        this.nodeID = id;
        this.keyGen = KeyPairGenerator.getInstance("DSA", "SUN"); // create key
        // generator
        // object
        this.random = SecureRandom.getInstance("SHA1PRNG", "SUN"); // random var
        // for
        // random
        // generation
        this.dsa = Signature.getInstance("SHA1withDSA", "SUN"); // create
        // signature
        // object
        keyGen.initialize(1024, random); // initialize and generate random key
        // pair
        this.pair = keyGen.generateKeyPair();
        this.privKey = pair.getPrivate();
        this.pubKey = pair.getPublic();
        this.blockChain = new Blockchain(this);
        this.blockChain.makeVerbose();
    }
    
    public void addResource(String type, int amount){
    	String resourceType = type.toLowerCase();
    	if(myResources.containsKey(resourceType)){
    		myResources.replace(resourceType, amount + myResources.get(resourceType));
    	}
    	else{
    		myResources.put(resourceType, amount);
    	}
    }
    
    public HashMap<String, Integer> getResources(){
    	return this.myResources;
    }

    // Accessors
    public String getNodeID() {
        return nodeID;
    }
    public PublicKey getPublicKey() {
        return this.pubKey;
    }
    public ArrayList<Node> getFriends() {
        return this.networkNodes;
    }
    public ArrayList<Message> getMessages() {
        // TODO Auto-generated method stub
        return this.localMSG;
    }


    // Mutators
    public void createMessage(Message text) {

        distributePublicKey(this.getPublicKey()); // distribute public key to
        // friend nodes (they will
        // propagate it to their
        // friends)

        this.blockChain.add(text);

        localMSG.add(text);
        this.distributeMessage(text); // distribute message to friend nodes

    }
    public void createMessage(Object data) {
    	
        distributePublicKey(this.getPublicKey()); // distribute public key to
        // friend nodes (they will
        // propagate it to their
        // friends)
        // System.out.println("I distributed my public key");

        Random rand = new Random(); // create a message with a random friend
        // node as the recipient
        if (networkNodes.size() > 0) {
            int nodesSize = networkNodes.size();
            int receiverNum = rand.nextInt(nodesSize);
            Message text = new TextMessage(data, this, networkNodes.get(receiverNum));

            this.blockChain.add(text);

            localMSG.add(text);
            this.distributeMessage(text); // distribute message to friend nodes
        } // (they will propagate to their
        // friends)
    }
    public void createMessageWithSignature(Object data)
            throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {

        distributePublicKey(this.getPublicKey());
        //System.out.println("I distributed my public key");

        Random rand = new Random(); // create a message with a random friend
        // node as the recipient
        int receiverNum = rand.nextInt(networkNodes.size());
        Message text = new TextMessage(data, this, networkNodes.get(receiverNum));

        byteArray = text.getMessageData().toString().getBytes(); // convert
        // message
        // to series
        // of bytes
        byte[] realSig = new byte[1024];

        dsa.initSign(this.privKey); // sign message with private key
        dsa.update(byteArray);
        realSig = dsa.sign();

        localMSG.add(text);
        this.distributeSignedMessage(realSig, byteArray, text); // distribute
        // message to
        // friend nodes
        // (they will
        // propagate to
        // their
        // friends)
    }
    public void addNodes(ArrayList<Node> newNodes) { // add a group of friend
        // nodes
        for (int i = 0; i < newNodes.size(); i++) {
            networkNodes.add(newNodes.get(i));
        }
    }
    public void addFriend(Node node) { // add a single friend node
        networkNodes.add(node);
    }
    public void addMessage(Message text) {
    	// if message is unique, add and
        // distribute
        // do nothing
        if (text != null && !this.localMSG.contains(text)) {
            this.blockChain.add(text);
            this.localMSG.add(text);
            this.distributeMessage(text);
        }
    }
    public void addSignedMessage(byte[] realSig, byte[] byteArray2, Message text)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        for (int i = 0; i < publicKeySet.size(); i++) { // find public key that
            // can verify message
            sig.initVerify(publicKeySet.get(i));
            sig.update(byteArray2);
            boolean verifies = sig.verify(realSig);

            if (verifies == true) { // if verified and unique, add to localMSG
                // and distribute friend nodes
                if (!this.localMSG.contains(text)) {
                    localMSG.add(text);
                    this.distributeSignedMessage(realSig, byteArray2, text);
                    return;
                }
            } else {
            }
        }
    }
    public void addPublicKey(PublicKey publicKey) {
        // TODO Auto-generated method stub
        if (this.publicKeySet.contains(publicKey)) { // if public key is unique,
            // add and distribute to
            // friend nodes
            // do nothing
        } else {
            this.publicKeySet.add(publicKey);
            this.distributePublicKey(publicKey);
        }
    }
    public void setBlockChainDifficulty(int difficulty) {
        blockChain.setDifficulty(difficulty);
    }
    public void createPing() {
        this.ping = new Ping(this);
        distributePing(this.ping);
    }
    private void receivePing(Ping ping) {
        // TODO Auto-generated method stub
        if (this.routeTable.containsKey(ping.getOriginator())) {
            // do nothing
        } else {
            routeTable.put(ping.originator, ping.relayer);
            ping.setRelayer(this);
            distributePing(ping);
        }
    }
    public void receiveBlock(Block b, String nodeID) {
        if (blockRequestIDs.contains(b.getMyHash() + nodeID)) {
        	blockRequestIDs.remove(b.getMyHash() + nodeID);
            if (nodeID == this.nodeID) {
                blockChain.add(b);
            } else {
                broadcastBlock(b, nodeID);
            }
        }
    }
    public void requestBlock(String hash, String id) {
        if (!blockRequestIDs.contains(hash + id)) {
        	blockRequestIDs.add(hash + id);
            Block b = blockChain.getBlock(hash);
            if (b != null) {
                broadcastBlock(b, id);
            } else {
                makeBlockRequest(hash, id);
            }
        }
    }
    public void makeBlockRequest(String hash, String id) {
        for (int i = 0; i < networkNodes.size(); i++) { // distribute
            // blockrequest to
            // friend nodes (they
            // will propagate to
            // their friends if they
            // cannot resolve)
            networkNodes.get(i).requestBlock(hash, id);
        }
    }
    public void broadcastBlock(Block b, String nodeID) {
        for (int i = 0; i < networkNodes.size(); i++) { // distribute
            // blockrequest to
            // friend nodes (they
            // will propagate to
            // their friends if they
            // cannot resolve)
            networkNodes.get(i).receiveBlock(b, nodeID);
        }
    }

    // Utility
    public void start() {
        blockChain.start();
    }
    public void printNodes() {
        for (int i = 0; i < networkNodes.size(); i++) { // print out friend
            // nodes
            System.out.println(networkNodes.get(i).getNodeID());
        }
    }
    public boolean isOnline() {
        return true;
    }
    public void distributeMessage(Message text) {
        for (int i = 0; i < networkNodes.size(); i++) { // distribute message to
            // friend nodes (they
            // will propagate to
            // their friends)
            networkNodes.get(i).addMessage(text);
        }
    }
    public void distributeSignedMessage(byte[] realSig, byte[] byteArray2, Message text)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        // TODO Auto-generated method stub
        for (int i = 0; i < networkNodes.size(); i++) { // give each friend node
            // the signed message,
            // saved in their
            // localMSG
            networkNodes.get(i).addSignedMessage(realSig, byteArray2, text);
        }
    }
    public void distributePublicKey(PublicKey publicKey) {
        for (int i = 0; i < networkNodes.size(); i++) { // add public key to all
            // friend nodes (they
            // will propagate to
            // their friends)
            networkNodes.get(i).addPublicKey(publicKey);
        }
    }
    public void distributeBlock(Block b) {
        for (int i = 0; i < networkNodes.size(); i++) { // add block to friend
            // node's blockchain
            networkNodes.get(i).blockChain.add(b);
        }
    }
    public void addBlock(Block b) {
    	blockChain.add(b);
    }
    public void distributePing(Ping receivedPing) {
        for (int i = 0; i < this.networkNodes.size(); i++) {
            networkNodes.get(i).receivePing(receivedPing);
        }
    }
}
