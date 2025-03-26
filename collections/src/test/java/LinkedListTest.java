import linkedlist.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.*;

class LinkedListTest {
    private LinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new LinkedList<>();
    }

    @Test
    void testAddAndGet() {
        list.add(10);
        list.add(20);
        list.add(30);

        assertThat(list.size()).isEqualTo(3);
        assertThat(list.get(0)).isEqualTo(10);
        assertThat(list.get(1)).isEqualTo(20);
        assertThat(list.get(2)).isEqualTo(30);
    }

    @Test
    void testAddAtIndex() {
        list.add(10);
        list.add(30);
        list.add(20, 1);

        assertThat(list.size()).isEqualTo(3);
        assertThat(list.get(0)).isEqualTo(10);
        assertThat(list.get(1)).isEqualTo(20);
        assertThat(list.get(2)).isEqualTo(30);
    }

    @Test
    void testAddAtIndexOutOfBounds() {
        assertThatThrownBy(() -> list.add(10, 1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testRemove() {
        list.add(10);
        list.add(20);
        list.add(30);

        list.remove(20);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(10);
        assertThat(list.get(1)).isEqualTo(30);
    }

    @Test
    void testRemoveNonExistingElement() {
        list.add(10);
        list.add(20);
        list.remove(30);

        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    void testClear() {
        list.add(10);
        list.add(20);
        list.clear();

        assertThat(list.size()).isEqualTo(0);
        assertThatThrownBy(() -> list.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testSort() {
        list.add(30);
        list.add(10);
        list.add(20);
        list.sort(Comparator.comparingInt(o -> o));

        assertThat(list.get(0)).isEqualTo(10);
        assertThat(list.get(1)).isEqualTo(20);
        assertThat(list.get(2)).isEqualTo(30);
    }
}