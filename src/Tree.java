import java.util.ArrayList;

public abstract class Tree<T> {
    private Node<T> root;
    private ArrayList<Node<T>> leaves;
    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private ArrayList<Node<T>> children;
    }
    
    public ArrayList getLeaves(){
    	return leaves;
    }
}