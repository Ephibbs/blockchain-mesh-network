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
    private ArrayList<TreeNode<T>> leaves = new ArrayList<TreeNode<T>>();

    // Constructor
    public Tree(T rootData) {
        root = new TreeNode<T>(rootData);
        root.setDepth(0);
        leaves.add(root);
    }

    // Accessors
    public ArrayList<TreeNode<T>> getLeaves(){
        return leaves;
    }
    public TreeNode<T> getDeepestTreeNode() {
        return leaves.get(0);
    }

    // Mutators
    public void addTreeNode(TreeNode p, T c) {
    	TreeNode<T> cn = new TreeNode<T>(c);
    	long cDepth = p.getDepth()+1;
    	cn.setDepth(cDepth);
    	p.addChild(cn);
    	if (leaves.contains(p)) {
    		leaves.remove(p);
    	}
    	int i = 0;
    	while(leaves.size() > i && cn.getDepth() > leaves.get(i).getDepth()) {i++;}
    	leaves.add(i--, cn);
    }
}