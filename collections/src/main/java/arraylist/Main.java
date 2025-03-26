package arraylist;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        ArrayList<Integer> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(r.nextInt(20));
        }

        System.out.println("Исходный массив:");
        ArrayList.printList(list);

        list.add(123, 3);
        System.out.println("После добавления 123 в индекс 3:");
        ArrayList.printList(list);

        list.remove(123);
        System.out.println("После удаления 123:");
        ArrayList.printList(list);

        list.sort();
        System.out.println("После сортировки:");
        ArrayList.printList(list);

        list.clear();
        System.out.println("После очистки:");
        ArrayList.printList(list);
    }

}
