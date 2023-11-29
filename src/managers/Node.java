package managers;

public class Node<E> {
    E cur;
    Node<E> prev;
    Node<E> next;

    public Node(Node<E> prev, E cur, Node<E> next) {
        this.prev = prev;
        this.cur = cur;
        this.next = next;
    }
}