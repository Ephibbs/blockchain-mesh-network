import java.util.ArrayList;

public class TreeNode<T> {
        private T data;
        private long depth;
        private TreeNode<T> parent = null;
        private ArrayList<TreeNode<T>> children = new ArrayList<TreeNode<T>>();;
        public TreeNode(T data) {
        	this.data = data;
        }
        public void addChild(TreeNode<T> c) {
        	children.add(c);
        }
        public void addChild(T c) {
        	children.add(new TreeNode<T>(c));
        }
        public long getDepth() {
        	return depth;
        }
        public void setDepth(long depth) {
        	this.depth = depth;
        }
        public T getData() {
        	return data;
        }
    }
