import material.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This class represents a tree data structure using a linked implementation.
 * It implements the NAryTree interface.
 *
 * @param <E> the type of element stored in the tree
 */
public class LinkedTree<E> implements NAryTree<E> {

    private  TreeNode<E> root;

    private int size;
    /**
     * This class represents a node in a tree data structure.
     * It implements the Position interface.
     *
     * @param <T> the type of element stored in the node
     */
    private class TreeNode<T> implements Position<T> {

        private T element;
        private List<TreeNode<T>> children = new ArrayList<>();
        private TreeNode<E> parent;


        public TreeNode(T element) {
            this.element = element;
        }

        public TreeNode(T element, LinkedTree<E>.TreeNode<E> parent) {
            this.element = element;
            this.parent = parent;
        }


        public void setElement(T element) {
            this.element = element;
        }

        public List<TreeNode<T>> getChildren() {
            return children;
        }

        public void setChildren(List<TreeNode<T>> children) {
            this.children = children;
        }

        public TreeNode<E> getParent() {
            return parent;
        }

        public void setParent(TreeNode<E> parent) {
            this.parent = parent;
        }

        @Override
        public T getElement() {
            return element;
        }

    }

    @Override
    public Position<E> addRoot(E e) {
        if (!isEmpty()){
            throw new RuntimeException("The tree has already a root node");
        }
        root = new TreeNode<>(e);
        size++;
        return root;
    }

    private TreeNode<E> checkPosition(Position<E> p){
        if(!(p instanceof TreeNode)){
            throw new RuntimeException("The position is invalid");
        }
        return (TreeNode<E>)p; 
    }
    @Override
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode<E> newNode = new TreeNode<>(element,parent);

        parent.getChildren().add(newNode);
        size++;
        return newNode;
    }

    /**
     * Check if a given position is valid for the children list of a TreeNode.
     *
     * @param n      The position to check
     * @param parent The parent TreeNode
     * @throws RuntimeException If the position is invalid
     */

    private static <E> void checkPositionOfChildrenList(int n, LinkedTree<E>.TreeNode<E> parent) {
        if(n<0 || n > parent.getChildren().size()){
            throw new RuntimeException("The position is invalid");
        }
    }
    @Override
    public Position<E> add(E element, Position<E> p, int n) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode<E> newNode = new TreeNode<>(element, parent);

        checkPositionOfChildrenList(n, parent);
        parent.getChildren().add(n, newNode);

        size++;
        return newNode;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = checkPosition(p1);
        TreeNode<E> node2 = checkPosition(p2);
        E aux = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(aux);
    }

    @Override
    public E replace(Position<E> p, E e) {
        TreeNode<E> node = checkPosition(p);
        E aux = node.getElement();
        node.setElement(e);
        return aux;
    }

    @Override
    public void remove(Position<E> p) {
        TreeNode<E> node = checkPosition(p);
        if (node == root){
            node = null;
            size = 0;
        }else{
            TreeNode<E> parent = node.getParent();
            parent.getChildren().remove(node);
            size -= computeSize(node);
        }

    }

    private int computeSize(TreeNode<E> node){ // metodo que dice el tamaño del subarbol.
        int size = 1;
        for (TreeNode<E> children : node.getChildren()){
            size += computeSize(children);
        }
        return size;
    }
    @Override
    public NAryTree<E> subTree(Position<E> v) {
        TreeNode<E> vertex = checkPosition(v);
        LinkedTree<E> tree = new LinkedTree<>();
        tree.root = vertex;
        tree.size = computeSize(vertex);
        return tree;
    }

    @Override
    public void attach(Position<E> p, NAryTree<E> t) {
        TreeNode<E> vertex = checkPosition(p);
        LinkedTree<E> tree = checkTree(t);
        vertex.getChildren().addAll(tree.root.getChildren());
        size += tree.size;
    }

    private LinkedTree<E> checkTree(NAryTree<E> t){
        if(!(t instanceof LinkedTree)){
            throw new RuntimeException("No es un LinkedTree");
        }
        return (LinkedTree<E>) t;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Position<E> root() {
        return root;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInternal(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRoot(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // 2 parte
    @Override
    public Iterator<Position<E>> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Iterator<Position<E>> iteratorPreOrder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Iterator<Position<E>> iteratorPostOrder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void breadthFirstTraversal(TreeNode<E> root, List<Position<E>> positions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void postOrderTraversal(TreeNode<E> root, List<Position<E>> positions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void preOrderTraversal(TreeNode<E> node, List<Position<E>> positions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public int size() {
        return size;
    }
}