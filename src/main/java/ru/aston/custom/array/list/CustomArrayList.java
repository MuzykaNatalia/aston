package ru.aston.custom.array.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aston.sorting.Sorting;

import java.util.*;

@Component
public class CustomArrayList<E> implements CustomList<E>, Iterable<E> {
    private static final int DEFAULT_SIZE = 10;
    private final Sorting<E> sorting;
    private Object[] elementData;
    private int size = 0;

    @Autowired
    public CustomArrayList(Sorting<E> sorting) {
        this.sorting = sorting;
        this.elementData = new Object[DEFAULT_SIZE];
    }

    public CustomArrayList(int initialSize, Sorting<E> sorting) {
        if (initialSize < 1) {
            throw new IllegalArgumentException("Illegal Size: "+ initialSize);
        } else {
            this.sorting = sorting;
            this.elementData = new Object[initialSize];
        }
    }

    @Override
    public void add(E element) {
        expandLengthIfNeededForOne();
        elementData[size] = element;
        size++;
    }

    @Override
    public void add(int index, E element) {
        if (index != 0) {
            getExceptionIfIndexInvalid(index);
        }
        expandLengthIfNeededForOne();

        if (elementData.length != 0) {
            System.arraycopy(elementData, index, elementData, index + 1, size - index);
        }

        elementData[index] = element;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        Object[] incomingArray = c.toArray();
        int incomingArrayLength = incomingArray.length;
        expandLengthIfNeededForMultiple(incomingArrayLength);

        System.arraycopy(incomingArray, 0, elementData, size, incomingArrayLength);
        size += incomingArrayLength;
        return true;
    }

    @Override
    public void clear() {
        Arrays.fill(elementData, 0, size, null);
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        getExceptionIfIndexInvalid(index);
        return (E) elementData[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        getExceptionIfIndexInvalid(index);
        E oldElement = (E) elementData[index];

        shiftElementsLeft(index);
        return oldElement;
    }

    @Override
    public boolean remove(Object o) {
        final Object[] copyElementData = elementData;
        final int size = this.size;

        int index = -1;
        for (int i = 0; i < size; i++) {
            if (equalsWithNullCheck(o, copyElementData[i])) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            shiftElementsLeft(index);
        }
        return index > -1;
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        try {
            elementData = sorting.sort(elementData, comparator);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot sort list with mixed types: " + e.getMessage());
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("There are no more elements");
                }
                return (E) elementData[currentIndex++];
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomArrayList<?> that = (CustomArrayList<?>) o;
        return size == that.size && Arrays.equals(elementData, that.elementData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(elementData);
        return result;
    }

    public int getSize() {
        return size;
    }

    private void getExceptionIfIndexInvalid(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void expandLengthIfNeededForOne() {
        if (size + 1 > elementData.length) {
            int sizeNewArray = (int) (elementData.length * 1.5 + 1);
            elementData = Arrays.copyOf(elementData, sizeNewArray);
        }
    }

    private void expandLengthIfNeededForMultiple(int incomingArrayLength) {
        int requiredArrayLength = size + incomingArrayLength;
        if (requiredArrayLength > elementData.length) {
            int newArrayLength = Math.max((int) (elementData.length * 1.5 + 1), requiredArrayLength);
            elementData = Arrays.copyOf(elementData, newArrayLength);
        }
    }

    private void shiftElementsLeft(int index) {
        boolean notEndElement = index + 1 <= elementData.length - 1;
        if (notEndElement) {
            System.arraycopy(elementData, index + 1, elementData, index, size - index - 1);
        }
        elementData[--size] = null;
    }

    private boolean equalsWithNullCheck(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }
}
