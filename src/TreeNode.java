import java.util.ArrayList;

/*
 * Leaves of the Tree class, stores blocks
 * Parameters: Data object or TreeNode object
 */

public class TreeNode<T> {
	
    // Variables
    private T data;
    private long depth;
    private TreeNode<T> parent = null;
    private ArrayList<TreeNode<T>> children = new ArrayList<TreeNode<T>>();
    private Tree<T> myTree = null; // reference of tree that this node belongs to

    // Constructors
    public TreeNode(TreeNode<T> tn) {
        this.parent = tn.getParent();
        this.depth = tn.getDepth();
        this.data = tn.getData();
        this.children = tn.getChildren();
    }
    public TreeNode(T data) {
        this.data = data;
    }

    // Accessors
    public Tree<T> getMyTree() {
        return myTree;
    }
    public long getDepth() {
        return depth;
    }
    public T getData() {
        return data;
    }
    public ArrayList<TreeNode<T>> getChildren() {
        return children;
    }
    public TreeNode<T> getParent() {
        return parent;
    }

    // Mutators -- don't call these, let the Tree class do it for you
    public void setMyTree(Tree<T> t) {
        this.myTree = t;
    }
    public void addChild(TreeNode<T> c) {
        children.add(c);
    }
    public void addChild(T c) {
        children.add(new TreeNode<T>(c));
    }
    public void setDepth(long depth) {
        this.depth = depth;
    }
    public void setParent(TreeNode<T> p) {
        this.parent = p;
    }
}