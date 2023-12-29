import material.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * An implementation of the NAryTree interface using left-child, right-sibling representation.
 *
 * @param <E> the type of elements stored in the tree
 */
public class LCRSTree<E> implements NAryTree<E> {

    private class LCRSNode<T> implements Position<T> {

        private T element;
        
        private LCRSNode<T> parent;
        
        private LCRSNode<T> leftChild;

        private LCRSNode<T> sigSibling;

        public LCRSNode(T elem, LCRSNode<T> parent){
            element = elem;
            this.parent = parent;
        }
        public LCRSNode(T elem){
            this(elem,null);
        }

        @Override
        public T getElement() {
            return element;
        }

        public LCRSNode<T> getParent(){
            return parent;
        }

        public LCRSNode<T> getLeftChild(){
            return leftChild;
        }

        public LCRSNode<T> getSigSibling(){
            return sigSibling;
        }

    }

    private LCRSNode<E> root;
    private int size;

    public LCRSTree(LCRSNode<E> root, int size){
        this.root = root;
        this.size = size;
    }

    public LCRSTree(){
        root = null;
        size = 0;
    }

    @Override
    public Position<E> addRoot(E e) {
        if(!isEmpty()){
            throw new RuntimeException("El arbol no esta vacio, ya existe una raiz");
        }
        root = new LCRSNode<E>(e);
        size = 1;
        return root;
    }

    private LCRSNode<E> checkPosition(Position<E> p){
        if(!(p instanceof LCRSNode<E>)){
            throw new RuntimeException("invalid position");
        }
        return (LCRSNode<E>) p;
    }
    @Override
    public Position<E> add(E element, Position<E> p) {
        LCRSNode<E> nodeParent = checkPosition(p);
        LCRSNode<E> nodeAux = new LCRSNode<E>(element,nodeParent);
        if(nodeParent.getLeftChild() == null){
            nodeParent.leftChild = nodeAux;  
        }else{
            LCRSNode<E> nodeMove = nodeParent.getLeftChild();
            while(nodeMove.getSigSibling() != null){
                nodeMove = nodeMove.getSigSibling();
            }
            nodeMove.sigSibling = nodeAux;
        }
        size++;
        return nodeAux;
    }
    
    private void checkChildrenPosition(int n){
        if(n < 0){
            throw new RuntimeException("La posicion del hijo es invalida");
        }
    }

    @Override
    public Position<E> add(E element, Position<E> p, int n) {
        LCRSNode<E> nodeParent = checkPosition(p);
        LCRSNode<E> nodeAux = new LCRSNode<E>(element, nodeParent);
        checkChildrenPosition(n);
        if(n == 0){
            nodeAux.sigSibling = nodeParent.getLeftChild();
            nodeParent.leftChild = nodeAux;
        }else{
            LCRSNode<E> nodeMove = nodeParent.getLeftChild();
            int i = 1;
            while(i < n && nodeMove.getSigSibling() != null){
                nodeMove = nodeMove.getSigSibling();
                i++;
            }
            if(i != n){
                throw new RuntimeException("Esa posicion no es accesible");
            }
            nodeAux.sigSibling = nodeMove.getSigSibling();
            nodeMove.sigSibling = nodeAux;
        }
        size++;
        return nodeAux;        
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        LCRSNode<E> node1 = checkPosition(p1);
        LCRSNode<E> node2 = checkPosition(p2);
        E elementAux = node1.getElement();
        node1.element = node2.getElement();
        node2.element = elementAux;
    }

    @Override
    public E replace(Position<E> p, E e) {
        LCRSNode<E> node = checkPosition(p);
        E elementAux = node.getElement();
        node.element = e;
        return elementAux;
    }

    private int computeSize(LCRSNode<E> node){
        if(node == null){   //caso base
            return 0;
        }
        int size = 1;
        LCRSNode<E> child = node.getLeftChild();
        while(child.getLeftChild() != null){
            size += computeSize(node.getLeftChild());
            child = child.getSigSibling();
        }
        return size;
    }

    @Override
    public void remove(Position<E> p) {
        LCRSNode<E> node = checkPosition(p);
        if(node == root){
            root = null;
            size = 0;
        }else{
            if(node.getParent().getLeftChild() == node){
                node.getParent().leftChild = node.getSigSibling();
            }else{
                LCRSNode<E> nodeMove = node.getParent().getLeftChild();
                while(nodeMove.getSigSibling() != node){
                    nodeMove = nodeMove.getSigSibling();
                }
                nodeMove.sigSibling = node.getSigSibling(); 
            }
            size -= computeSize(node);
        }
    }

    @Override
    public NAryTree<E> subTree(Position<E> v) {
        LCRSNode<E> node = checkPosition(v);
        LCRSTree<E> tree = new LCRSTree<>(node,computeSize(node));
        return tree;
    }

    private LCRSTree<E> checkTree(NAryTree<E> t){
        if(!(t instanceof LCRSTree)){
            throw new RuntimeException("El arbol es invalido");
        }
        return (LCRSTree<E>) t;
    }

    @Override
    public void attach(Position<E> p, NAryTree<E> t) {
        LCRSNode<E> node = checkPosition(p);
        LCRSTree<E> subTree = checkTree(t);
        LCRSNode<E> nodeMove = node.getLeftChild();
        if(nodeMove != null){
            while(nodeMove.getSigSibling() != null){
                nodeMove = nodeMove.getSigSibling();
            }  
        }
        node.leftChild = subTree.root;
        size += subTree.size;
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
        LCRSNode<E> node = checkPosition(v);
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        LCRSNode<E> node = checkPosition(v);
        List<LCRSNode<E>> list = new ArrayList<LCRSNode<E>>();
        LCRSNode<E> nodeMove = node.getLeftChild();
        while(nodeMove != null){
            list.add(nodeMove);
            nodeMove = nodeMove.getSigSibling();
        }
        return list;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        LCRSNode<E> node = checkPosition(v);
        return node.getLeftChild() != null;
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        LCRSNode<E> node = checkPosition(v);
        return node.getLeftChild() == null;
    }

    @Override
    public boolean isRoot(Position<E> v) {
        LCRSNode<E> node = checkPosition(v);
        return root == node;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        if(isEmpty()){
            return new ArrayList<Position<E>>().iterator();
        }
        List<Position<E>> list = new ArrayList<>();
        breadthFirstTraversal(root, list);
        return list.iterator();
    }

    private void breadthFirstTraversal(LCRSNode<E> node, List<Position<E>> list){
        if(node != null){
            List<LCRSNode<E>> queue = new ArrayList<>();
            queue.add(node);
            while(!queue.isEmpty()){
                LCRSNode<E> nodeToVisit = queue.remove(0);
                list.add(nodeToVisit);
                LCRSNode<E> nodeMove = nodeToVisit;
                nodeMove = nodeMove.getLeftChild();
                while(nodeMove != null){
                    queue.add(nodeMove);
                    nodeMove = nodeMove.getSigSibling();
                } 
            }
            
        }
    }

    private void preOrdenTraversal(LCRSNode<E> node, List<Position<E>> list){
        if(node != null){
            list.add(node);
            List<LCRSNode<E>> childrenList = new ArrayList<>();
            LCRSNode<E> nodeMove = node.getLeftChild();
            while(nodeMove != null){
                childrenList.add(nodeMove);
                nodeMove = nodeMove.getSigSibling();
            }
            for(LCRSNode<E> child: childrenList){
                preOrdenTraversal(child, list);
            }
        }
    }

    private void postOrdenTraversal(LCRSNode<E> node, List<Position<E>> list){
        if(node != null){
            List<LCRSNode<E>> childrenList = new ArrayList<>();
            LCRSNode<E> nodeMove = node.getLeftChild();
            //Añadimos los nodos hijos a una lista q se recorrera
            while(nodeMove != null){
                childrenList.add(nodeMove);
                nodeMove = nodeMove.getSigSibling();
            }
            for(LCRSNode<E> child: childrenList){
                postOrdenTraversal(child, list);
            }
            list.add(node);
        }
    }
    public Iterator<Position<E>> iteratorPreOrden() {
        return null;
    }
    public Iterator<Position<E>> iteratorPostOrden() {
        return null;
    }


    public int size() {
        return size;
    }

}
