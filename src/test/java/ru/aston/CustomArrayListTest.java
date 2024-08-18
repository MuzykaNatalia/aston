package ru.aston;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.custom.array.list.CustomArrayList;
import ru.aston.model.Person;
import ru.aston.sorting.CustomQuickSort;
import ru.aston.sorting.Sorting;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {
    private Sorting<Integer> sortingNumber;
    private Sorting<String> sortingString;
    private Sorting<Person> sortingPerson;

    @BeforeEach
    public void setUp() {
        sortingNumber = new CustomQuickSort<>();
        sortingString = new CustomQuickSort<>();
        sortingPerson = new CustomQuickSort<>();
    }

    @Test
    public void create() {
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomArrayList<>(-1, sortingNumber)
        );
        assertEquals("Illegal Size: -1", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomArrayList<>(0, sortingNumber)
        );
        assertEquals("Illegal Size: 0", exception2.getMessage());

        CustomArrayList<Integer> list1 = new CustomArrayList<>(sortingNumber);
        assertEquals(0, list1.getSize());

        CustomArrayList<Integer> list2 = new CustomArrayList<>(1, sortingNumber);
        assertEquals(0, list2.getSize());
    }

    @Test
    public void add() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(sortingNumber);
        assertEquals(0, list1.getSize());

        list1.add(5);
        assertEquals(1, list1.getSize());

        list1.add(7);
        assertEquals(2, list1.getSize());

        assertIterableEquals(Arrays.asList(5, 7), list1);

        CustomArrayList<Integer> list2 = new CustomArrayList<>(1, sortingNumber);
        assertEquals(0, list2.getSize());

        list2.add(9);
        assertEquals(1, list2.getSize());

        list2.add(6);
        assertEquals(2, list2.getSize());

        list2.add(7);
        assertEquals(3, list2.getSize());

        assertIterableEquals(Arrays.asList(9, 6, 7), list2);
    }

    @Test
    public void addingByIndex() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(sortingNumber);
        assertEquals(0, list1.getSize());

        IndexOutOfBoundsException exception1 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.add(-1, 10)
        );
        assertEquals("Index: -1, Size: 0", exception1.getMessage());

        IndexOutOfBoundsException exception2 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.add(1, 10)
        );
        assertEquals("Index: 1, Size: 0", exception2.getMessage());

        list1.add(0, 10);
        assertEquals(1, list1.getSize());

        list1.add(0, 7);
        assertEquals(2, list1.getSize());

        list1.add(1, 1);
        assertEquals(3, list1.getSize());

        IndexOutOfBoundsException exception3 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.add(3, 500)
        );
        assertEquals("Index: 3, Size: 3", exception3.getMessage());

        assertIterableEquals(Arrays.asList(7, 1, 10), list1);

        CustomArrayList<Integer> list2 = new CustomArrayList<>(1, sortingNumber);
        assertEquals(0, list2.getSize());

        list2.add(0, 5);
        assertEquals(1, list2.getSize());

        list2.add(0, 8);
        assertEquals(2, list2.getSize());

        list2.add(1, 4);
        assertEquals(3, list2.getSize());

        assertIterableEquals(Arrays.asList(8, 4, 5), list2);
    }

    @Test
    public void addAll() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(sortingNumber);
        assertEquals(0, list1.getSize());

        Collection<Integer> collectionToAdd1 = Arrays.asList(1, 2, 3, 4, 5);
        boolean result1 = list1.addAll(collectionToAdd1);

        assertTrue(result1);
        assertEquals(5, list1.getSize());
        assertIterableEquals(Arrays.asList(1, 2, 3, 4, 5), list1);

        CustomArrayList<Integer> list2 = new CustomArrayList<>(1, sortingNumber);
        assertEquals(0, list2.getSize());

        Collection<Integer> collectionToAdd2 = Arrays.asList(7, -2, 5, -9, 3);
        boolean result2 = list2.addAll(collectionToAdd2);

        assertTrue(result2);
        assertEquals(5, list2.getSize());
        assertIterableEquals(Arrays.asList(7, -2, 5, -9, 3), list2);
    }

    @Test
    public void clear() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(sortingNumber);
        list1.add(5);
        list1.add(7);

        assertEquals(2, list1.getSize());

        list1.clear();

        assertEquals(0, list1.getSize());
        assertIterableEquals(List.of(), list1);

        CustomArrayList<Integer> list2 = new CustomArrayList<>(1, sortingNumber);
        assertEquals(0, list2.getSize());

        list2.clear();
        assertIterableEquals(List.of(), list2);
    }

    @Test
    public void get() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(1, sortingNumber);
        list1.add(5);
        assertEquals(1, list1.getSize());

        Integer number = list1.get(0);
        assertEquals(5, number);

        IndexOutOfBoundsException exception1 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.get(1)
        );
        assertEquals("Index: 1, Size: 1", exception1.getMessage());

        IndexOutOfBoundsException exception2 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.get(-1)
        );
        assertEquals("Index: -1, Size: 1", exception2.getMessage());
    }

    @Test
    public void isEmpty() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(2, sortingNumber);
        boolean isEmpty1 = list1.isEmpty();
        assertTrue(isEmpty1);

        list1.add(5);
        boolean isEmpty2 = list1.isEmpty();
        assertFalse(isEmpty2);

        CustomArrayList<Integer> list2 = new CustomArrayList<>(sortingNumber);
        boolean isEmpty3 = list2.isEmpty();
        assertTrue(isEmpty3);

        list2.add(5);
        boolean isEmpty4 = list2.isEmpty();
        assertFalse(isEmpty4);
    }

    @Test
    public void remove() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(1, sortingNumber);

        IndexOutOfBoundsException exception1 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.remove(-1)
        );
        assertEquals("Index: -1, Size: 0", exception1.getMessage());

        IndexOutOfBoundsException exception2 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.remove(500)
        );
        assertEquals("Index: 500, Size: 0", exception2.getMessage());

        list1.add(5);
        list1.add(8);
        list1.add(7);
        assertEquals(3, list1.getSize());

        Integer removed1 = list1.remove(0);
        assertEquals(5, removed1);
        assertEquals(2, list1.getSize());
        assertIterableEquals(List.of(8, 7), list1);

        Integer removed2 = list1.remove(1);
        assertEquals(7, removed2);
        assertEquals(1, list1.getSize());
        assertIterableEquals(List.of(8), list1);

        Integer removed3 = list1.remove(0);
        assertEquals(8, removed3);
        assertEquals(0, list1.getSize());
        assertIterableEquals(List.of(), list1);

        IndexOutOfBoundsException exception3 = assertThrows(
                IndexOutOfBoundsException.class,
                () -> list1.remove(0)
        );
        assertEquals("Index: 0, Size: 0", exception3.getMessage());
    }

    @Test
    public void removeObject() {
        CustomArrayList<Integer> list1 = new CustomArrayList<>(sortingNumber);

        Integer number = 6;
        boolean isRemoved = list1.remove(number);
        assertFalse(isRemoved);
        assertIterableEquals(List.of(), list1);

        list1.add(-5);
        list1.add(8);
        list1.add(0);
        list1.add(1);
        list1.add(8);
        assertEquals(5, list1.getSize());

        Integer number1 = 9;
        boolean isRemoved1 = list1.remove(number1);
        assertFalse(isRemoved1);
        assertIterableEquals(Arrays.asList(-5, 8, 0, 1, 8), list1);

        Integer number2 = 8;
        boolean isRemoved2 = list1.remove(number2);
        assertTrue(isRemoved2);
        assertIterableEquals(Arrays.asList(-5, 0, 1, 8), list1);

        Integer number3 = -5;
        boolean isRemoved3 = list1.remove(number3);
        assertTrue(isRemoved3);
        assertIterableEquals(Arrays.asList(0, 1, 8), list1);

        Integer number4 = 8;
        boolean isRemoved4 = list1.remove(number4);
        assertTrue(isRemoved4);
        assertIterableEquals(Arrays.asList(0, 1), list1);

        Integer number5 = null;
        boolean isRemoved5 = list1.remove(number5);
        assertFalse(isRemoved5);
        assertIterableEquals(Arrays.asList(0, 1), list1);
    }

    @Test
    public void sortInteger() {
        CustomArrayList<Integer> list = new CustomArrayList<>(sortingNumber);
        list.add(5);
        list.add(8);
        list.add(0);
        list.add(1);
        list.add(3);
        list.add(1);
        list.add(-8);

        list.sort(Comparator.naturalOrder());
        assertIterableEquals(Arrays.asList(-8, 0, 1, 1, 3, 5, 8), list);
    }

    @Test
    public void sortString() {
        CustomArrayList<String> list = new CustomArrayList<>(sortingString);
        list.add("d");
        list.add("c");
        list.add("A");
        list.add("b");
        list.add("B");
        list.add("AA");
        list.add("a");

        list.sort(Comparator.naturalOrder());
        assertIterableEquals(Arrays.asList("A", "AA", "a", "B", "b", "c", "d"), list);
    }

    @Test
    public void sortPerson() {
        CustomArrayList<Person> list = new CustomArrayList<>(sortingPerson);
        list.add(new Person("Ivan", 35));
        list.add(new Person("Maria", 1));
        list.add(new Person("Sveta", 56));
        list.add(new Person("Ira", 68));
        list.add(new Person("Sasha", 20));

        list.sort(Comparator.comparing(Person::getAge));
        List<Person> result = Arrays.asList(
                new Person("Maria", 1),
                new Person("Sasha", 20),
                new Person("Ivan", 35),
                new Person("Sveta", 56),
                new Person("Ira", 68));
        assertIterableEquals(result, list);

        list.sort(Comparator.comparing(Person::getName));
        List<Person> result2 = Arrays.asList(
                new Person("Ira", 68),
                new Person("Ivan", 35),
                new Person("Maria", 1),
                new Person("Sasha", 20),
                new Person("Sveta", 56));
        assertIterableEquals(result2, list);
    }
}