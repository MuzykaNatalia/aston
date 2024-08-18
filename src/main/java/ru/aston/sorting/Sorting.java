package ru.aston.sorting;

import java.util.Comparator;

public interface Sorting<E> {
    Object[] sort(Object[] elementData, Comparator<? super E> c);
}
