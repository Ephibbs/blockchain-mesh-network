import java.util.ArrayList;

/*
 * Class to track the blockchain with each node being a block
 * Branches occur when multiple blocks are added at the same time
 * Dead branches form when an initial split has been resolved
 * Parameters: Root data object or TreeNode object
 */

public class Tree<T> {

    // Variables
    private TreeNode<T> root;
    private TreeNode<T> deepestNode;

    // Constructors
    public Tree(T rootData) {
        root = new TreeNode<T>(rootData);
        root.setDepth(0);
        deepestNode = root;
        root.setMyTree(this); // reference to this tree
    }
    public Tree(TreeNode<T> root) {
        this.root = root;
        root.setDepth(0);
        deepestNode = root;
        root.setMyTree(this); // reference to this tree
    }
    
    // Accessors
    public TreeNode<T> getDeepestTreeNode() {
        return deepestNode;
    }
    public TreeNode<T> getRootTreeNode() {
        return root;
    }

    // Mutators
    public void addTreeNode(TreeNode<T> p, TreeNode<T> cn) {
        cn.setMyTree(p.getMyTree()); // child and parent share same tree
    	long cDepth = p.getDepth()+1;
    	cn.setDepth(cDepth);
    	p.addChild(cn);
    	cn.setParent(p);
    	if(cDepth > deepestNode.getDepth()) {
    		deepestNode = cn;
    	}

        // Recursively update the rest of the tree
        if (!cn.getChildren().isEmpty()) {
            for (TreeNode<T> c : cn.getChildren()) {
                addTreeNode(cn, c);
            }
        }
    }
}