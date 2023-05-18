import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private final Map<Integer, Person> people = new HashMap<>();
    private final UnionSet unionSet = new UnionSet();
    private int qbs = 0;
    private int qts = 0;

    @Override
    public boolean contains(int id) {
        return people.get(id) != null;
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        qbs++;
        people.put(person.getId(), person);
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        MyPerson p1 = (MyPerson)getPerson(id1);
        MyPerson p2 = (MyPerson)getPerson(id2);
        if (p1.getDegree() < p2.getDegree()) {
            addTripleSum(p1, p2);
        } else {
            addTripleSum(p2, p1);
        }
        p1.addRelation(p2, value);
        p2.addRelation(p1, value);
        if (unionSet.fa(id1) != unionSet.fa(id2)) {
            qbs--;
        }
        unionSet.union(id1, id2);
    }

    @Override
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return unionSet.isConnected(id1, id2);
    }

    @Override
    public int queryBlockSum() {
        return qbs;
    }

    private void addTripleSum(MyPerson p1, MyPerson p2) {
        for (Person person : p1.getAcquaintance()) {
            if (p2.isLinked(person)) {
                qts++;
            }
        }
    }

    @Override
    public int queryTripleSum() {
        return qts;
    }

    @Override
    public boolean queryTripleSumOKTest(
            HashMap<Integer, HashMap<Integer, Integer>> beforeData,
            HashMap<Integer, HashMap<Integer, Integer>> afterData,
            int result) {
        if (beforeData.size() != afterData.size()) {
            return false;
        }
        for (int id : beforeData.keySet()) {
            HashMap<Integer, Integer> beforePerson = beforeData.get(id);
            HashMap<Integer, Integer> afterPerson = afterData.get(id);
            if (afterPerson == null) {
                return false;
            }
            if (beforePerson.size() != afterPerson.size()) {
                return false;
            }
            for (int ac : beforePerson.keySet()) {
                int beforeValue = beforePerson.get(ac);
                Integer afterValue = afterPerson.get(ac);
                if (afterValue == null) {
                    return false;
                }
                if (!afterValue.equals(beforeValue)) {
                    return false;
                }
            }
        }
        int sum = 0;
        List<Integer> testPeople = new ArrayList<>(beforeData.keySet());
        int size = testPeople.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    int id1 = testPeople.get(i);
                    int id2 = testPeople.get(j);
                    int id3 = testPeople.get(k);
                    if (beforeData.get(id1).containsKey(id2)
                            && beforeData.get(id2).containsKey(id3)
                            && beforeData.get(id1).containsKey(id3)) {
                        sum++;
                    }
                }
            }
        }
        return result == sum;
    }
}
