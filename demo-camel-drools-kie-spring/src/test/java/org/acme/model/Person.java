package org.acme.model;

public class Person {

    private String name;

    public Person() {

    }
    public Person(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
