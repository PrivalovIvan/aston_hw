package linkedlist;

public class Main {
    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.add(2);
        linkedList.add(1);
        linkedList.add(3);
        linkedList.add(7);
        linkedList.add(4);
        linkedList.add(9);
        linkedList.add(0);
        linkedList.add(4,1);
        linkedList.printLinkedList();
        linkedList.remove(2);
        linkedList.printLinkedList();

        linkedList.sort();
        linkedList.printLinkedList();


        linkedList.clear();
        linkedList.printLinkedList();
    }
}
