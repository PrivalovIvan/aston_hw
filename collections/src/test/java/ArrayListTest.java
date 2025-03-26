import static org.assertj.core.api.Assertions.*;

import arraylist.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrayListTest {
    private ArrayList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
    }

    @Test
    void testAdd() {
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
    void testAddAtIndex_OutOfBounds() {
        assertThatThrownBy(() -> list.add(10, 1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testGet() {
        list.add(10);
        list.add(20);

        assertThat(list.get(0)).isEqualTo(10);
        assertThat(list.get(1)).isEqualTo(20);
    }

    @Test
    void testGet_OutOfBounds() {
        assertThatThrownBy(() -> list.get(0))
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
    void testRemove_NonExistent() {
        list.add(10);
        list.remove(20);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(10);
    }

    @Test
    void testClear() {
        list.add(10);
        list.add(20);

        list.clear();

        assertThat(list.size()).isEqualTo(0);
        assertThatThrownBy(() -> list.get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testSort() {
        list.add(5);
        list.add(2);
        list.add(8);
        list.add(1);

        list.sort();

        assertThat(list.get(0)).isEqualTo(1);
        assertThat(list.get(1)).isEqualTo(2);
        assertThat(list.get(2)).isEqualTo(5);
        assertThat(list.get(3)).isEqualTo(8);
    }

    @Test
    void testSize() {
        assertThat(list.size()).isEqualTo(0);

        list.add(10);
        list.add(20);

        assertThat(list.size()).isEqualTo(2);
    }
}