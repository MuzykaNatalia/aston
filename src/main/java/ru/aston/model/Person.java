package ru.aston.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Person {
    private String name;
    private int age;
}
