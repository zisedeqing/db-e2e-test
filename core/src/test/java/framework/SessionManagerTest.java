package framework;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SessionManagerTest {

    @Test
    public void shouldReportEndpointIndexOutOfRange() {
        SessionManager manager = new SessionManager();

        try {
            manager.getOrCreateSession("session-1", 1, null);
            fail("Expected endpoint index validation to fail");
        } catch (ExecException e) {
            assertTrue(e.getMessage().contains("Endpoint index 1 is out of range"));
            assertTrue(e.getMessage().contains("session-1"));
        }
    }
}
