import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

/**
 * Created by 585728 on 6/28/2016.
 */

/*
 * class to provide structure for the nodes in a network
 */
interface Node {

	// Accessors
	public HashMap<String, Integer> getResources();
	public String getNodeID();
	public ArrayList<Message> getOpenRequests();
	public ArrayList<Message> getBidsToMyRequests();
	public ArrayList<Message> getMyResourceAgreements();
	public ArrayList<Message> getMyResourceSents();
	public ArrayList<Message> getMyResourceReceives();

	// Mutators
	public void addResource(String type, int amount);
	public void addMessage(Message msg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException, ClassNotFoundException;

	public void setBlockChainDifficulty(int difficulty);


	//Block Request Protocol
	
	public void distributeBlock(Block b);
	
	public void receiveBlockDelivery(BlockDelivery bd);

	public void receiveBlockRequest(BlockRequest br);
	
	public void makeBlockRequest(String hash);

	// Utility
	public void start();

	public boolean isOnline();

	public void addBlock(Block b) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, ClassNotFoundException, IOException;
}
