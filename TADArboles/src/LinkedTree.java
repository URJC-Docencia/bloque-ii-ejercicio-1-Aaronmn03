import material.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.basic.BasicTreeUI.TreeCancelEditingAction;


/**
 * This class represents a tree data structure using a linked implementation.
 * It implements the NAryTree interface.
 *
 * @param <E> the type of element stored in the tree
 */
public class LinkedTree<E> implements NAryTree<E> {

    /**
     * This class represents a node in a tree data structure.
     * It implements the Position interface.
     *
     * @param <T> the type of element stored in the node
     */
    private class TreeNode<T> implements Position<T> {
        private T element;
        private List<TreeNode<T>> children = new ArrayList<>();
        private TreeNode<T> parent;

        public TreeNode(T element,TreeNode parent){
            this.element = element;
            this.parent = parent;
        }

        public TreeNode(T element){
            this(element,null);
        }

        @Override
        public T getElement() {
            return element;
        }

        public TreeNode<T> getParent(){
            return parent;
        }

        public List<TreeNode<T>> getChildren(){
            return children;
        }

        public void setElement(T elem){
            element = elem;
        }

        public void setParent(TreeNode<T> parent){
            this.parent = parent;
        }

        public void setChildren(List<TreeNode<T>> children){
            this.children = children;
        }
    }

    private TreeNode<E> root;
    private int size;
    public LinkedTree(){
        size = 0;
    }

    @Override
    public Position<E> addRoot(E e) {
        if(!isEmpty()){
            throw new RuntimeException("El arbol no es vacio, ya tiene una raiz");
        }
        root = new TreeNode<E>(e);
        size ++;
        return root;
    }

    public TreeNode<E> checkPosition(Position<E> p){
        if(!(p instanceof TreeNode<E>)){
            throw new RuntimeException("The Position is invalid");
        }
        return (TreeNode<E>) p;
    }

    @Override
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode node = new TreeNode<>(element,parent);
        parent.getChildren().add(node);
        size++;
        return node;
    }

    public void checkPositionOfChildrenList(int n,TreeNode<E> parent){
        if(n < 0 || n > parent.getChildren().size()){
            throw new RuntimeException("The position of the children is invalid");
        }
    }

    @Override
    public Position<E> add(E element, Position<E> p, int n) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode node = new TreeNode<>(element, parent);
        checkPositionOfChildrenList(n, parent);
        parent.getChildren().add(n, node);
        size++;
        return node;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = checkPosition(p1);
        TreeNode<E> node2 = checkPosition(p2);
        E elementAux = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(elementAux);;
    }

    @Override
    public E replace(Position<E> p, E e) {
        TreeNode<E> node = checkPosition(p);
        E elementAux = node.getElement();
        node.setElement(e);
        return elementAux;
    }

    @Override
    public void remove(Position<E> p) {
        TreeNode<E> node = checkPosition(p);
        if(node == root){
            root = null;
            size = 0;
        }else{
            node.getParent().getChildren().remove(node);
            size -= computeSize(node);
        }
    }

    private int computeSize(TreeNode<E> node){
        size = 1;
        for(TreeNode<E> child : node.getChildren()){
            size += computeSize(child);
        }
        return size;
    }

    @Override
    public NAryTree<E> subTree(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        LinkedTree<E> subTree;
        subTree = new LinkedTree<E>();
        subTree.root = node;
        subTree.size = computeSize(node);
        return subTree;
    }

    private LinkedTree<E> checkTree(NAryTree<E> tree){
        if(!(tree instanceof TreeNode)){
            throw new RuntimeException("El arbol no es un arbol");
        }
        return (LinkedTree<E>) tree;
    }

    @Override
    public void attach(Position<E> p, NAryTree<E> t) {
        TreeNode<E> node = checkPosition(p);
        LinkedTree<E> SubTree = checkTree(t);
        node.getChildren().addAll(SubTree.root.getChildren());
        size += SubTree.size;
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
        TreeNode<E> node = checkPosition(v);
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getChildren();
    }

    @Override
    public boolean isInternal(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getChildren() != null;
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getChildren() == null;
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getParent() == null;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        if(isEmpty()){
            return new ArrayList<Position<E>>().iterator();
        }
        List<Position<E>> positions = new ArrayList<>();
        breadthFirstTraversal(root,positions);
        return positions.iterator();
    }

    private void breadthFirstTraversal(TreeNode<E> node, List<Position<E>> lista){
        if(node != null){
            List<TreeNode<E>> queue = new ArrayList<>();
            queue.add(node);
            while(!queue.isEmpty()){
                TreeNode<E> nodeToVisit = queue.remove(0);
                lista.add(nodeToVisit);
                queue.addAll(nodeToVisit.getChildren());
            }
        }
    }


    public Iterator<Position<E>> iteratorPreOrden() {
        if(isEmpty()){
            return new ArrayList<Position<E>>().iterator();
        }
        List<Position<E>> list = new ArrayList<>();
        preOrdenTraversal(root, list);
        return list.iterator();

    }

    private void preOrdenTraversal(TreeNode<E> node, List<Position<E>> list){
        if(node != null){
            list.add(node);
            for(TreeNode<E> child: node.getChildren()){
                preOrdenTraversal(child, list);
            } 
        }
    }


    public Iterator<Position<E>> iteratorPostOrden() {
        if(isEmpty()){
            return new ArrayList<Position<E>>().iterator();
        }
        List<Position<E>> list = new ArrayList<>();
        postOrdenTraversal(root, list);
        return list.iterator();
        
    }

    private void postOrdenTraversal(TreeNode<E> node, List<Position<E>> list){
        if(node != null){
            for(TreeNode<E> child : node.getChildren()){
                postOrdenTraversal(child, list);
            }
            list.add(node);
        }
    }

    public int size() {
        return size;
    }
}