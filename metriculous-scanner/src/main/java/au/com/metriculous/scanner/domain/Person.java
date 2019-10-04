package au.com.metriculous.scanner.domain;

import org.eclipse.jgit.lib.PersonIdent;

import java.util.Objects;

public class Person implements Comparable<Person> {
    private final String name;
    private final String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static Person fromIdent(PersonIdent personIdent) {
        return new Person(personIdent.getName().trim(), personIdent.getEmailAddress().trim());
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getName(), person.getName()) &&
                Objects.equals(getEmail(), person.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail());
    }

    @Override
    public int compareTo(Person other) {
        return getName().compareTo(other.getName()) == 0 ? getEmail().compareTo(other.getEmail()) : getName().compareTo(other
                .getName());
    }

}
