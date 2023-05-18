import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import exceptions.MyAcquaintanceNotFoundException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private final Map<Integer, Person> people = new HashMap<>();
    private final Map<Integer, Message> messages = new HashMap<>();
    private final Map<Integer, Group> groups = new HashMap<>();
    private int qbs = 0;
    private int qts = 0;

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
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
        if (!GraphUtils.isConnected(p1, p2)) {
            qbs--;
        }
        p1.addRelation(p2, value);
        p2.addRelation(p1, value);
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
        return GraphUtils.isConnected((MyPerson) getPerson(id1), (MyPerson) getPerson(id2));
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

    private void delTripleSum(MyPerson p1, MyPerson p2) {
        for (Person person : p1.getAcquaintance()) {
            if (p2.isLinked(person)) {
                qts--;
            }
        }
    }

    @Override
    public int queryTripleSum() {
        return qts;
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        Group g = groups.get(id2);
        Person p = people.get(id1);
        if (g.hasPerson(p)) {
            throw new MyEqualPersonIdException(id1);
        }
        if (g.getSize() <= 1111) {
            g.addPerson(p);
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        Group g = groups.get(id2);
        Person p = people.get(id1);
        if (!g.hasPerson(p)) {
            throw new MyEqualPersonIdException(id1);
        }
        g.delPerson(p);
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getReceivedMessages();
    }

    @Override
    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        MyPerson person = (MyPerson) people.get(id);
        if (person.getDegree() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        }
        return person.getBestAcquaintance().getId();
    }

    @Override
    public int queryCoupleSum() {
        int result = 0;
        for (Person p : people.values()) {
            MyPerson person = (MyPerson) p;
            if (person.getDegree() == 0) {
                continue;
            }
            MyPerson best = (MyPerson) person.getBestAcquaintance();
            if (best.getDegree() > 0 && best.getBestAcquaintance().equals(person)) {
                result++;
            }
        }
        return result / 2;
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        if (message.getType() == 0 && !message.getPerson1().isLinked(message.getPerson2())) {
            throw new MyRelationNotFoundException(
                    message.getPerson1().getId(), message.getPerson2().getId());
        }
        if (message.getType() == 1 && !message.getGroup().hasPerson(message.getPerson1())) {
            throw new MyPersonIdNotFoundException(message.getPerson1().getId());
        }
        if (message.getType() == 0) {
            message.getPerson1().addSocialValue(message.getSocialValue());
            ((MyPerson) message.getPerson2()).addMessage(message);
        } else if (message.getType() == 1) {
            ((MyGroup) message.getGroup()).sendMessage(message);
        }
        messages.remove(message.getId());
    }

    @Override
    public void modifyRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        if (p1.queryValue(p2) + value > 0) {
            internalModifyRelation(p1, p2, p1.queryValue(p2) + value);
        } else {
            internalDeleteRelation(p1, p2);
        }
    }

    private void internalModifyRelation(Person p1, Person p2, int fvalue) {
        ((MyPerson) p1).modifyRelation(p2, fvalue);
        ((MyPerson) p2).modifyRelation(p1, fvalue);
    }

    private void internalDeleteRelation(Person p1, Person p2) {
        MyPerson mp1 = (MyPerson) p1;
        MyPerson mp2 = (MyPerson) p2;
        mp1.deleteRelation(mp2);
        mp2.deleteRelation(mp1);
        if (!GraphUtils.isConnected(mp1, mp2)) {
            qbs++;
        }
        if (mp1.getDegree() < mp2.getDegree()) {
            delTripleSum(mp1, mp2);
        } else {
            delTripleSum(mp2, mp1);
        }
    }

    @Override
    public int modifyRelationOKTest(int id1, int id2, int value,
                                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2)
                || id1 == id2 || !beforeData.get(id1).containsKey(id2)) {
            if (beforeData.equals(afterData)) {
                return 0;
            } else {
                return -1;
            }
        }
        if (beforeData.size() != afterData.size()) {
            return 1;
        } else if (!beforeData.keySet().equals(afterData.keySet())) {
            return 2;
        }
        for (int id : beforeData.keySet()) {
            if (id != id1 && id != id2 && !beforeData.get(id).equals(afterData.get(id))) {
                return 3;
            }
        }
        if (beforeData.get(id1).get(id2) + value > 0) {
            return modRelationTest(id1, id2, value, beforeData, afterData);
        } else {
            return delRelationTest(id1, id2, value, beforeData, afterData);
        }
    }

    private int modRelationTest(int id1, int id2, int value,
                                HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!afterData.get(id1).containsKey(id2) || !afterData.get(id2).containsKey(id1)) {
            return 4;
        } else if (!afterData.get(id1).get(id2).equals(beforeData.get(id1).get(id2) + value)) {
            return 5;
        } else if (!afterData.get(id2).get(id1).equals(beforeData.get(id2).get(id1) + value)) {
            return 6;
        } else if (afterData.get(id1).size() != beforeData.get(id1).size()) {
            return 7;
        } else if (afterData.get(id2).size() != beforeData.get(id2).size()) {
            return 8;
        } else if (!afterData.get(id1).keySet().equals(beforeData.get(id1).keySet())) {
            return 9;
        } else if (!afterData.get(id2).keySet().equals(beforeData.get(id2).keySet())) {
            return 10;
        }
        for (int id : beforeData.get(id1).keySet()) {
            if (id != id2 && !beforeData.get(id1).get(id).equals(afterData.get(id1).get(id))) {
                return 11;
            }
        }
        for (int id : beforeData.get(id2).keySet()) {
            if (id != id1 && !beforeData.get(id2).get(id).equals(afterData.get(id2).get(id))) {
                return 12;
            }
        }
        return 0;
    }

    private int delRelationTest(int id1, int id2, int value,
                                HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
            return 15;
        } else if (beforeData.get(id1).size() != afterData.get(id1).size() + 1) {
            return 16;
        } else if (beforeData.get(id2).size() != afterData.get(id2).size() + 1) {
            return 17;
        }
        for (int id : beforeData.get(id1).keySet()) {
            if (id != id2 && (!afterData.get(id1).containsKey(id)
                    || !beforeData.get(id1).get(id).equals(afterData.get(id1).get(id)))) {
                return 20;
            }
        }
        for (int id : beforeData.get(id2).keySet()) {
            if (id != id1 && (!afterData.get(id2).containsKey(id)
                    || !beforeData.get(id2).get(id).equals(afterData.get(id2).get(id)))) {
                return 21;
            }
        }
        return 0;
    }
}
