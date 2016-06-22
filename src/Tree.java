import java.util.ArrayList;

public class Tree<T> {
    private TreeNode<T> root;
    private ArrayList<TreeNode<T>> leaves = new ArrayList<TreeNode<T>>();
    public Tree(T rootData) {
        root = new TreeNode<T>(rootData);
        root.setDepth(0);
        leaves.add(root);
    }
    public Tree(TreeNode<T> root) {
        this.root = root;
        root.setDepth(0);
        leaves.add(root);
    }
    public void addTreeNode(TreeNode<T> p, TreeNode<T> cn) {
    	long cDepth = p.getDepth()+1;
    	cn.setDepth(cDepth);
    	p.addChild(cn);
    	cn.setParent(p);
    	if (leaves.contains(p)) {
    		leaves.remove(p);
    	}
    	int i = 0;
    	while(leaves.size() > i && cn.getDepth() > leaves.get(i).getDepth()) {i++;}
    	leaves.add(i--, cn);
    }
    public ArrayList<TreeNode<T>> getLeaves(){
    	return leaves;
    }
    public TreeNode<T> getDeepestTreeNode() {
    	return leaves.get(0);
    }
}