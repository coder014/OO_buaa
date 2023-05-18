import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyGroup implements Group {
    private final int id;
    private final Set<Person> people = new HashSet<>();
    private int ageSum = 0;
    private int ageSqrSum = 0;

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getSize() {
        return people.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Group)) {
            return false;
        } else {
            return ((Group) obj).getId() == id;
        }
    }

    @Override
    public void addPerson(Person person) {
        ageSum += person.getAge();
        ageSqrSum += person.getAge() * person.getAge();
        people.add(person);
    }

    public void sendMessage(Message message) {
        for (Person p : people) {
            p.addSocialValue(message.getSocialValue());
        }
        if (message instanceof RedEnvelopeMessage) {
            int tt = ((RedEnvelopeMessage) message).getMoney() / getSize();
            for (Person p : people) {
                if (!p.equals(message.getPerson1())) {
                    p.addMoney(tt);
                }
            }
        }
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
        ageSum -= person.getAge();
        ageSqrSum -= person.getAge() * person.getAge();
    }

    @Override
    public int getValueSum() {
        List<Person> list = new ArrayList<>(people);
        int size = people.size();
        int result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (list.get(i).isLinked(list.get(j))) {
                    result += list.get(i).queryValue(list.get(j)) * 2;
                }
            }
        }
        return result;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int mean = getAgeMean();
        int n = people.size();
        return (ageSqrSum - 2 * mean * ageSum + n * mean * mean) / n;
    }
}
