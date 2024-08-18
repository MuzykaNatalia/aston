package ru.aston.sorting;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@Component
public final class CustomQuickSort<E> implements Sorting<E> {
    @Override
    public Object[] sort(Object[] elementData, Comparator<? super E> comparator) {
        if (elementData == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        int sizeElementData = (int) Arrays.stream(elementData)
                .filter(Objects::nonNull)
                .count();

        if (sizeElementData < 2) {
            return elementData;
        }

        quickSort(elementData, 0, sizeElementData - 1, comparator);
        return elementData;
    }

    private void quickSort(Object[] elementData, int low, int high, Comparator<? super E> comparator) {
        if (low < high) {
            int pivotIndex = partition(elementData, low, high, comparator);
            quickSort(elementData, low, pivotIndex - 1, comparator);
            quickSort(elementData, pivotIndex + 1, high, comparator);
        }
    }

    private int partition(Object[] array, int low, int high, Comparator<? super E> comparator) {
        int mid = low + (high - low) / 2;
        Object pivot = medianOfThree(array, low, mid, high, comparator);

        while (true) {
            while (low <= high && comparator.compare((E) array[low], (E) pivot) < 0) {
                low++;
            }

            while (high >= low && comparator.compare((E) array[high], (E) pivot) > 0) {
                high--;
            }

            if (low >= high) {
                return high;
            }

            swap(array, low, high);
            low++;
            high--;
        }
    }

    private Object medianOfThree(Object[] array, int low, int mid, int high, Comparator<? super E> c) {
        if (c.compare((E) array[low], (E) array[mid]) > 0) swap(array, low, mid);
        if (c.compare((E) array[low], (E) array[high]) > 0) swap(array, low, high);
        if (c.compare((E) array[mid], (E) array[high]) > 0) swap(array, mid, high);
        return array[mid];
    }

    private void swap(Object[] array, int i, int j) {
        Object temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
