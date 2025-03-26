package linkedlist;

import interfaces.List;

import java.util.Comparator;

public class LinkedList<T> implements List<T> {
    private Node<T> head;
    private int size = 0;

    @Override
    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    @Override
    public void add(T value, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            Node<T> newNode = new Node<>(value);
            newNode.next = head;
            head = newNode;
            size++;
            return;
        }

        Node<T> newNode = new Node<>(value);
        Node<T> current = head;
        for (int i = 1; i < index; i++) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;

        size++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    @Override
    public void remove(T value) {
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.value.equals(value)) {
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }


    @Override
    public void sort() {
        throw new UnsupportedOperationException("Для сортировки передайте Comparator в sort(Comparator<T>)");
    }

    public void sort(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator не может быть null");
        }
        head = mergeSort(head, comparator);
    }

    @Override
    public int size() {
        return size;
    }


    //region MergeSort
    private Node<T> mergeSort(Node<T> node, Comparator<T> comparator) {
        if (node == null || node.next == null) {
            return node;
        }

        Node<T> middle = getMiddle(node);
        Node<T> secondHalf = middle.next;
        middle.next = null;

        Node<T> left = mergeSort(node, comparator);
        Node<T> right = mergeSort(secondHalf, comparator);

        return merge(left, right, comparator);
    }

    private Node<T> getMiddle(Node<T> node) {
        if (node == null) return null;
        Node<T> slow = node, fast = node;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private Node<T> merge(Node<T> left, Node<T> right, Comparator<T> comparator) {
        Node<T> dummy = new Node<>(null); // Временный узел
        Node<T> current = dummy;

        while (left != null && right != null) {
            if (comparator.compare(left.value, (right.value)) <= 0) {
                current.next = left;
                left = left.next;
            } else {
                current.next = right;
                right = right.next;
            }
            current = current.next;
        }

        current.next = (left != null) ? left : right;
        return dummy.next;
    }
    //endregion

    public void printLinkedList() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }

    private static class Node<T> {
        private T value;
        private Node<T> next;

        public Node(T value) {
            this.value = value;
            next = null;
        }
    }
}
