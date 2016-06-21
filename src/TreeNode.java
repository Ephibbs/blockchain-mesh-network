import java.util.ArrayList;

/*
 * Leaves of the Tree class, stores blocks
 * Parameters: Data object (in this case it will be a block)
 */

public class TreeNode<T> {

    // Variables
    private T data;
    private long depth;
    private TreeNode<T> parent = null;
    private ArrayList<TreeNode<T>> children = new ArrayList<TreeNode<T>>();

    // Constructor
    public TreeNode(T data) {
        this.data = data;
    }

    // Accessors
    public T getData() {
        return data;
    }
    public long getDepth() {
        return depth;
    }
    public ArrayList<TreeNode<T>> getChildren() {
        return children;
    }
    public TreeNode<T> getParent() {
        return parent;
    }

    // Mutators
    public void addChild(TreeNode<T> c) {
        children.add(c);
    }
    public void addChild(T c) {
        children.add(new TreeNode<T>(c));
    }
    public void setDepth(long depth) {
        this.depth = depth;
    }
}