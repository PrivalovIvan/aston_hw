package arraylist;

import interfaces.List;

import java.util.Comparator;

public class ArrayList<T> implements List<T> {
    private T[] list;
    private int size = 0;
    private int capacity = 10;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        this.list = (T[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        this.list = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(T value) {
        if (size == capacity) {
            capacity *= 2;
            T[] temp = (T[]) new Object[capacity];
            System.arraycopy(list, 0, temp, 0, size);
            list = temp;
        }
        list[size++] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(T value, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == capacity) {
            capacity *= 2;
            T[] temp = (T[]) new Comparable[capacity];
            System.arraycopy(list, 0, temp, 0, size);
            list = temp;
        }
        for (int i = size; i > index; i--) {
            list[i] = list[i - 1];
        }
        list[index] = value;
        size++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return list[index];
    }

    @Override
    public void remove(T value) {
        for (int i = 0; i < size; i++) {
            if (list[i].equals(value)) {
                for (int j = i; j < size - 1; j++) {
                    list[j] = list[j + 1];
                }
                list[size - 1] = null;
                size--;
                return;
            }
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            list[i] = null;
        }
        size = 0;
    }

    @Override
    public void sort() {
        sort(null);
    }

    public void sort(Comparator<? super T> comparator) {
        if (comparator == null) {
            if (list[0] instanceof Comparable) {
                comparator = (Comparator<? super T>) Comparator.naturalOrder();
            } else {
                throw new IllegalArgumentException("Передайте Comparator для несравнимых объектов");
            }
        }
        quickSort(list, 0, size - 1, comparator);
    }

    @Override
    public int size() {
        return size;
    }

    //region quickSort
    private static <T> void quickSort(T[] arr, int low, int high, Comparator<? super T> comparator) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, comparator);
            quickSort(arr, low, pivotIndex - 1, comparator);
            quickSort(arr, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(T[] arr, int low, int high, Comparator<? super T> comparator) {
        T pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparator.compare(arr[j], (pivot)) <= 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    //endregion

    public static void printList(ArrayList<?> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
    }
}