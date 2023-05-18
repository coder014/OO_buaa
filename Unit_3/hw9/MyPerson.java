import com.oocourse.spec1.main.Person;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final Set<Person> acquaintance = new HashSet<>();
    private final Map<Integer, Integer> value = new HashMap<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        } else {
            return ((Person) obj).getId() == id;
        }
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public boolean isLinked(Person person) {
        return person.getId() == id || acquaintance.contains(person);
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.contains(person)) {
            return value.get(person.getId());
        } else {
            return 0;
        }
    }

    public void addRelation(Person person, int value) {
        this.acquaintance.add(person);
        this.value.put(person.getId(), value);
    }

    public int getDegree() {
        return acquaintance.size();
    }

    public Collection<Person> getAcquaintance() {
        return acquaintance;
    }
}
