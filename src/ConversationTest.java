import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversationTest {

    @Test
    void tryParseCity() {
        assertEquals("Dublin", Conversation.tryParseCity("Dublin"));
        assertEquals("", Conversation.tryParseCity(""));
        assertEquals("", Conversation.tryParseCity("I don't know"));
        assertEquals("Rome", Conversation.tryParseCity("I am going to Rome"));
        assertEquals("Cork", Conversation.tryParseCity("I would like to go to Cork"));
    }
}