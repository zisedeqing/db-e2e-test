package framework.cli;

import framework.connection.Endpoint;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EndpointParserTest {

    @Test
    public void shouldParsePrimaryAndExtraPorts() {
        Endpoint endpoint = EndpointParser.parse(
                "db.example.com:33060:hs2=25000:webserver=25001");

        assertEquals("db.example.com", endpoint.getHost());
        assertEquals(33060, endpoint.getPort());
        assertEquals(Integer.valueOf(25000), endpoint.getExtraPort("hs2"));
        assertEquals(Arrays.asList("hs2", "webserver"),
                new ArrayList<>(endpoint.getExtraPorts().keySet()));
    }

    @Test
    public void shouldParseEndpointWithoutExtraPorts() {
        Endpoint endpoint = EndpointParser.parse("localhost:5432");

        assertEquals("localhost", endpoint.getHost());
        assertEquals(5432, endpoint.getPort());
        assertEquals(0, endpoint.getExtraPorts().size());
    }

    @Test
    public void shouldRejectMissingHostOrPrimaryPort() {
        assertInvalid(null, "invalid endpoint format");
        assertInvalid("", "invalid endpoint format");
        assertInvalid("localhost", "invalid endpoint format");
        assertInvalid(":5432", "invalid endpoint host");
    }

    @Test
    public void shouldRejectInvalidPrimaryPort() {
        assertInvalid("localhost:not-a-port", "invalid endpoint port");
        assertInvalid("localhost:0", "invalid endpoint port");
        assertInvalid("localhost:-1", "invalid endpoint port");
        assertInvalid("localhost:70000", "invalid endpoint port");
    }

    @Test
    public void shouldRejectMalformedExtraPort() {
        assertInvalid("localhost:5432:", "invalid extra port segment");
        assertInvalid("localhost:5432:hs2=25000:", "invalid extra port segment");
        assertInvalid("localhost:5432:hs2", "invalid extra port segment");
        assertInvalid("localhost:5432:=25000", "invalid extra port segment");
        assertInvalid("localhost:5432:hs2=bad", "invalid extra port value");
    }

    private void assertInvalid(String spec, String expectedMessage) {
        try {
            EndpointParser.parse(spec);
            fail("Expected IllegalArgumentException for " + spec);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(expectedMessage));
        }
    }
}
