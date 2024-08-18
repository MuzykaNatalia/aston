package ru.aston;

import java.util.Comparator;

public interface Sorting<E> {
    Object[] sort(Comparator<? super E> c);
}
