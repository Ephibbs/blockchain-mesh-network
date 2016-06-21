import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Random;

import java.lang.Thread;

public class BlockChainTesting {
	Node node1;
	public Blockchain blockChain = null;
	Thread thread;
	//public Blockchain chain1,chain2,chain3,chain4,chain5,chain6,chain7,chain8,chain9;
	
	/*
	 * This function is to basically just give each node a name and add the
	 * nodes to an arraylist to similar the network
	 */
	public BlockChainTesting() throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException {
		node1 = new Node("A");
		System.out.println("after A");
	}
	
	public void runWithBlockChain() throws InterruptedException {
		blockChain = new Blockchain(node1);
		blockChain.start();
		
		System.out.println("look at me");
		
		Message text = new TextMessage("help", node1, node1);
		blockChain.addMessage(text);
		Thread.sleep(10000);
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.add(text);
		
		System.out.println("look at me now");
		
		Block newBlock = new Block("whatishappening", messages);
		blockChain.receiveBlock(newBlock);
		
		
		System.out.println(node1.getNodeID());
	}
}
