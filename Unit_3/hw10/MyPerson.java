import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final Set<Person> acquaintance = new HashSet<>();
    private final Map<Integer, Integer> value = new HashMap<>();
    private final SortedSet<Person> internalAc;
    private int socialValue = 0;
    private final List<Message> messages = new LinkedList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.internalAc = new TreeSet<>((p1, p2) -> {
            int r = this.value.get(p2.getId()).compareTo(this.value.get(p1.getId()));
            if (r == 0) {
                return Integer.compare(p1.getId(), p2.getId());
            }
            return r;
        });
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

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    public void addMessage(Message message) {
        addSocialValue(message.getSocialValue());
        messages.add(0, message);
    }

    @Override
    public List<Message> getMessages() {
        return new LinkedList<>(messages);
    }

    @Override
    public List<Message> getReceivedMessages() {
        if (messages.size() < 5) {
            return new LinkedList<>(messages);
        } else {
            return new LinkedList<>(messages.subList(0, 5));
        }
    }

    public void addRelation(Person person, int value) {
        this.acquaintance.add(person);
        this.value.put(person.getId(), value);
        this.internalAc.add(person);
    }

    public void deleteRelation(Person acq) {
        internalAc.remove(acq);
        acquaintance.remove(acq);
        value.remove(acq.getId());
    }

    public int getDegree() {
        return acquaintance.size();
    }

    public Collection<Person> getAcquaintance() {
        return acquaintance;
    }

    public Person getBestAcquaintance() {
        return internalAc.first();
    }

    public void modifyRelation(Person acq, int fvalue) {
        internalAc.remove(acq);
        value.put(acq.getId(), fvalue);
        internalAc.add(acq);
    }
}
