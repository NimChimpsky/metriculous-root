package au.com.metriculous.scanner.domain;

import java.util.Objects;

/**
 * Created by stephenbatty on 22/06/2018.
 */
public final class PersonWithCount {
    private final Person person;
    private final Long value;

    public PersonWithCount(Person person, Long value) {
        this.person = person;
        this.value = value;
    }

    public String getName() {
        return person.getName();
    }

    public String getEmail() {
        return person.getEmail();
    }

    public Long getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonWithCount that = (PersonWithCount) o;
        return person.equals(that.person) &&
                getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, getValue());
    }
}
