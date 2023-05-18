import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person sender;
    private final Person receiver;
    private final Group toGroup;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.type = 0;
        this.toGroup = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.sender = messagePerson1;
        this.receiver = messagePerson2;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.receiver = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.sender = messagePerson1;
        this.toGroup = messageGroup;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return sender;
    }

    @Override
    public Person getPerson2() {
        return receiver;
    }

    @Override
    public Group getGroup() {
        return toGroup;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)) {
            return false;
        } else {
            return ((Message) obj).getId() == id;
        }
    }
}
