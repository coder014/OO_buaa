import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.NoticeMessage;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    private final String str;

    public MyNoticeMessage(int messageId, String noticeString,
                                Person messagePerson1, Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        this.str = noticeString;
    }

    public MyNoticeMessage(int messageId, String noticeString,
                                Person messagePerson1, Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
        this.str = noticeString;
    }

    @Override
    public String getString() {
        return str;
    }
}
