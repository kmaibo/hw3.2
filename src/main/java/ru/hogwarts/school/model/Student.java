package ru.hogwarts.school.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DialectOverride;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;

    private String name;
    private int age;

    @Version
    private Long version;

    public Student (String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student() {

    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
