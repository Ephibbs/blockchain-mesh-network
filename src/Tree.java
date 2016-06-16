import java.util.ArrayList;

public class Tree<T> {
    private TreeNode<T> root;
    private ArrayList<TreeNode<T>> leaves;
    public Tree(T rootData) {
        root = new TreeNode<T>(rootData);
    }
    public void addTreeNode(TreeNode p, TreeNode c) {
    	long cDepth = p.getDepth()+1;
    	c.setDepth(cDepth);
    	p.addChild(c);
    	if (leaves.contains(p)) {
    		leaves.remove(p);
    	}
    	int i = 0;
    	while(c.getDepth() > leaves.get(i).getDepth()) {i++;}
    	leaves.add(i--, c);
    	leaves.add(c);
    }
    public ArrayList<TreeNode<T>> getLeaves(){
    	return leaves;
    }
    public TreeNode<T> getDeepestTreeNode() {
    	return leaves.get(0);
    }
}