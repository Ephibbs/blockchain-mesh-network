import java.util.ArrayList;

public class Tree<T> {
    private TreeNode<T> root;
    private ArrayList<TreeNode<T>> leaves;
    public Tree(T rootData) {
        root = new TreeNode<T>(rootData);
    }
    public void addTreeNode(TreeNode p, T c) {
    	TreeNode<T> cn = new TreeNode<T>(c);
    	long cDepth = p.getDepth()+1;
    	cn.setDepth(cDepth);
    	p.addChild(cn);
    	if (leaves.contains(p)) {
    		leaves.remove(p);
    	}
    	int i = 0;
    	while(cn.getDepth() > leaves.get(i).getDepth()) {i++;}
    	leaves.add(i--, cn);
    	leaves.add(cn);
    }
    public ArrayList<TreeNode<T>> getLeaves(){
    	return leaves;
    }
    public TreeNode<T> getDeepestTreeNode() {
    	return leaves.get(0);
    }
}