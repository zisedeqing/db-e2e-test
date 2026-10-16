package framework.connection;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GenericJdbcConnectorTest {

    @Test
    public void shouldRenderAllJdbcUrlPlaceholders() {
        GenericJdbcConnector connector = new GenericJdbcConnector(
                "org.example.Driver",
                "jdbc:example://{host}:{port}/{db}?mode=test{options}",
                Arrays.asList("admin", "http"));
        Endpoint endpoint = new Endpoint("db.example.com", 5432, null, null);

        assertEquals(
                "jdbc:example://db.example.com:5432/analytics?mode=test&ssl=true",
                connector.buildJdbcUrl(endpoint, "analytics", "ssl=true"));
        assertEquals("org.example.Driver", connector.driverClass());
        assertEquals(Arrays.asList("admin", "http"), connector.extraPortNames());
    }

    @Test
    public void shouldAddQuestionMarkWhenTemplateHasNoQueryString() {
        GenericJdbcConnector connector = new GenericJdbcConnector(
                "org.example.Driver",
                "jdbc:example://{host}:{port}/{db}{options}",
                Collections.emptyList());
        Endpoint endpoint = new Endpoint("db.example.com", 5432, null, null);

        assertEquals(
                "jdbc:example://db.example.com:5432/analytics?ssl=true",
                connector.buildJdbcUrl(endpoint, "analytics", "ssl=true"));
        assertEquals(
                "jdbc:example://db.example.com:5432/analytics?ssl=true",
                connector.buildJdbcUrl(endpoint, "analytics", "&ssl=true"));
    }

    @Test
    public void shouldCreateConnectorFromProperties() {
        Map<String, String> props = new LinkedHashMap<>();
        props.put("connector.driver", "org.example.Driver");
        props.put("connector.url", "jdbc:example://{host}:{port}/{db}{options}");
        props.put("connector.extraPortNames", "admin, http");

        GenericJdbcConnector connector = GenericJdbcConnector.fromProperties(props);

        assertEquals("org.example.Driver", connector.driverClass());
        assertEquals(Arrays.asList("admin", "http"), connector.extraPortNames());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectIncompleteProperties() {
        GenericJdbcConnector.fromProperties(Collections.singletonMap(
                "connector.driver", "org.example.Driver"));
    }
}
