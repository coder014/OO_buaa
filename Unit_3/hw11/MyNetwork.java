import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import exceptions.MyAcquaintanceNotFoundException;
import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualEmojiIdException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyNetwork implements Network {
    private final Map<Integer, Person> people = new HashMap<>();
    private final Map<Integer, Message> messages = new HashMap<>();
    private final Map<Integer, Group> groups = new HashMap<>();
    private final Map<Integer, Integer> emojiHeat = new HashMap<>();
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
    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message instanceof EmojiMessage
                && !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
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
        if (message instanceof EmojiMessage) {
            int eid = ((EmojiMessage) message).getEmojiId();
            emojiHeat.put(eid, emojiHeat.get(eid) + 1);
        }
        if (message.getType() == 0) {
            message.getPerson1().addSocialValue(message.getSocialValue());
            if (message instanceof RedEnvelopeMessage) {
                message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
            }
            ((MyPerson) message.getPerson2()).addMessage(message);
        } else if (message.getType() == 1) {
            if (message instanceof RedEnvelopeMessage) {
                int tt = ((RedEnvelopeMessage) message).getMoney() / message.getGroup().getSize();
                message.getPerson1().addMoney(-tt * (message.getGroup().getSize() - 1));
            }
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
    public boolean containsEmojiId(int id) {
        return emojiHeat.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiHeat.put(id, 0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiHeat.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        Set<Integer> toDelete = new HashSet<>();
        for (Iterator<Map.Entry<Integer, Integer>> it = emojiHeat.entrySet().iterator();
             it.hasNext();) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getValue() < limit) {
                toDelete.add(entry.getKey());
                it.remove();
            }
        }
        for (Iterator<Map.Entry<Integer, Message>> it = messages.entrySet().iterator();
             it.hasNext();) {
            Message message = it.next().getValue();
            if (message instanceof EmojiMessage
                    && toDelete.contains(((EmojiMessage) message).getEmojiId())) {
                it.remove();
            }
        }
        return emojiHeat.size();
    }

    @Override
    public int deleteColdEmojiOKTest(int limit,
                                     ArrayList<HashMap<Integer, Integer>> beforeData,
                                     ArrayList<HashMap<Integer, Integer>> afterData,
                                     int result) {
        final HashMap<Integer, Integer> beforeEmojis = beforeData.get(0);
        final HashMap<Integer, Integer> beforeMessages = beforeData.get(1);
        final HashMap<Integer, Integer> afterEmojis = afterData.get(0);
        final HashMap<Integer, Integer> afterMessages = afterData.get(1);
        for (int eid : beforeEmojis.keySet()) {
            if (beforeEmojis.get(eid) >= limit && !afterEmojis.containsKey(eid)) {
                return 1;
            }
        }
        for (int eid : afterEmojis.keySet()) {
            if (!beforeEmojis.containsKey(eid)
                    || !beforeEmojis.get(eid).equals(afterEmojis.get(eid))) {
                return 2;
            }
        }
        int cnt = 0;
        for (Map.Entry<Integer, Integer> entry : beforeEmojis.entrySet()) {
            if (entry.getValue() >= limit) {
                ++cnt;
            }
        }
        if (cnt != afterEmojis.size()) {
            return 3;
        }
        for (Map.Entry<Integer, Integer> entry : beforeMessages.entrySet()) {
            if (entry.getValue() != null && afterEmojis.containsKey(entry.getValue())
                    && (!afterMessages.containsKey(entry.getKey())
                        || !entry.getValue().equals(afterMessages.get(entry.getKey())))) {
                return 5;
            }
        }
        for (Map.Entry<Integer, Integer> entry : beforeMessages.entrySet()) {
            if (entry.getValue() == null && (!afterMessages.containsKey(entry.getKey())
                    || afterMessages.get(entry.getKey()) != null)) {
                return 6;
            }
        }
        cnt = 0;
        for (Map.Entry<Integer, Integer> entry : beforeMessages.entrySet()) {
            if (entry.getValue() == null) {
                ++cnt;
            } else if (afterEmojis.containsKey(entry.getValue())) {
                ++cnt;
            }
        }
        if (cnt != afterMessages.size()) {
            return 7;
        }
        return result == afterEmojis.size() ? 0 : 8;
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        ((MyPerson) getPerson(personId)).clearNotices();
    }

    @Override
    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return GraphUtils.queryLeastMoments(people, getPerson(id));
    }
}
