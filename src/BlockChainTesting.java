

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import java.lang.Thread;

public class BlockChainTesting {
	Node node1, node2;
	public Blockchain blockChain1, blockChain2;
	Thread thread;
	public HashMap<String, TreeNode<Block>> blockMap = new HashMap<String, TreeNode<Block>>();
	
	//public Blockchain chain1,chain2,chain3,chain4,chain5,chain6,chain7,chain8,chain9;
	
	/*
	 * This function is to basically just give each node a name and add the
	 * nodes to an arraylist to similar the network
	 */
	public BlockChainTesting() throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException {
		node1 = new Node("A");
		node2 = new Node("B");
		System.out.println("after A");
	}
	
	public void runWithBlockChain() throws InterruptedException {
		/*TreeNode<Block> b1 = new TreeNode<Block>(new Block("123", new ArrayList<Message>()));
		TreeNode<Block> b2 = new TreeNode<Block>(new Block("4", new ArrayList<Message>()));
		TreeNode<Block> b3 = new TreeNode<Block>(new Block("5", new ArrayList<Message>()));
		blockMap.put("1", b1);
		blockMap.put("2", b2);
		b2.setParent(b3);
		System.out.print(b3);
		System.out.print(blockMap.get("2").getParent());
		*/
		blockChain1 = new Blockchain(node1);
		blockChain1.start();
		
		blockChain2 = new Blockchain(node2);
		blockChain2.start();
		
		Message text = new TextMessage("help1", node1, node1);
		Message text2 = new TextMessage("help2", node1, node1);
		blockChain1.add(text);
		Thread.sleep(8000);
		
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.add(text);
		
		Block newBlock = new Block(blockChain1.getLastTreeNode().getPrevHash(), messages);
		newBlock.setNonce(blockChain1.getLastTreeNode().getNonce());
		blockChain2.add(text);
		blockChain2.add(newBlock);
		
		Thread.sleep(8000);
		
		blockChain2.add(text2);
		
		Thread.sleep(8000);
		
		messages = new ArrayList<Message>();
		messages.add(text2);
		
		newBlock = new Block(blockChain2.getLastTreeNode().getPrevHash(), messages);
		newBlock.setNonce(blockChain2.getLastTreeNode().getNonce());
		blockChain1.add(text2);
		blockChain1.add(newBlock);
	}
}
