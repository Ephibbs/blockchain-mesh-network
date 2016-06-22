import java.util.ArrayList;

/*
 * Class to track the blockchain with each node being a block
 * Branches occur when multiple blocks are added at the same time
 * Dead branches form when an initial split has been resolved
 * Parameters: Root data object (in this case it will be a block)
 */

public class Tree<T> {

    // Variables
    private TreeNode<T> root;
    private TreeNode<T> deepestNode;

    // Constructor
    public Tree(T rootData) {
        root = new TreeNode<T>(rootData);
        root.setDepth(0);
        deepestNode = root;
    }
    public Tree(TreeNode<T> root) {
        this.root = root;
        root.setDepth(0);
        deepestNode = root;
    }
    
    // Accessors
    public TreeNode<T> getDeepestTreeNode() {
        return deepestNode;
    }

    // Mutators
    public void addTreeNode(TreeNode<T> p, TreeNode<T> cn) {
    	long cDepth = p.getDepth()+1;
    	cn.setDepth(cDepth);
    	p.addChild(cn);
    	cn.setParent(p);
    	if(cDepth > deepestNode.getDepth()) {
    		deepestNode = cn;
    	}
    }
}