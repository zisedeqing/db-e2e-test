package framework.connection;

import framework.connection.tchousexconnector.TchousexConnector;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ConnectorFactoryTest {

    @Test
    public void shouldUseBuiltInTchousexConnectorByDefault() {
        assertSame(ConnectorFactory.DEFAULT, ConnectorFactory.createDefault());
        assertSame(ConnectorFactory.DEFAULT, ConnectorFactory.create(null));
        assertSame(ConnectorFactory.DEFAULT, ConnectorFactory.create(Collections.emptyMap()));
        assertTrue(ConnectorFactory.DEFAULT instanceof TchousexConnector);
    }

    @Test
    public void shouldInstantiateConfiguredConnectorImplementation() {
        Map<String, String> props = Collections.singletonMap(
                "connector.impl", TchousexConnector.class.getName());

        DbConnector connector = ConnectorFactory.create(props);

        assertTrue(connector instanceof TchousexConnector);
    }

    @Test
    public void shouldCreateGenericConnectorWhenImplementationIsAbsent() {
        Map<String, String> props = new LinkedHashMap<>();
        props.put("connector.driver", "org.example.Driver");
        props.put("connector.url", "jdbc:example://{host}:{port}/{db}{options}");

        DbConnector connector = ConnectorFactory.create(props);

        assertTrue(connector instanceof GenericJdbcConnector);
        assertEquals("org.example.Driver", connector.driverClass());
    }

    @Test
    public void shouldTreatBlankImplementationAsGenericConfiguration() {
        Map<String, String> props = new LinkedHashMap<>();
        props.put("connector.impl", "  ");
        props.put("connector.driver", "org.example.Driver");
        props.put("connector.url", "jdbc:example://{host}:{port}/{db}{options}");

        assertTrue(ConnectorFactory.create(props) instanceof GenericJdbcConnector);
    }

    @Test
    public void shouldExplainUnknownConnectorImplementation() {
        try {
            ConnectorFactory.create(Collections.singletonMap(
                    "connector.impl", "not.existing.Connector"));
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("cannot instantiate connector.impl"));
            assertTrue(e.getCause() instanceof ClassNotFoundException);
            return;
        }
        throw new AssertionError("Expected unknown connector implementation to fail");
    }

    @Test
    public void shouldKeepTchousexProductKnowledgeInsideImplementation() {
        TchousexConnector connector = new TchousexConnector();
        Endpoint endpoint = new Endpoint("db.host", 33060, null, null);

        assertEquals("com.mysql.cj.jdbc.Driver", connector.driverClass());
        assertEquals("jdbc:mysql://db.host:33060/default?allowPublicKeyRetrieval=true"
                        + "&useLocalSessionState=TRUE&useSSL=false",
                connector.buildJdbcUrl(endpoint, "default", "useSSL=false"));
        assertEquals(3, connector.extraPortNames().size());
        assertEquals("hs2", connector.extraPortNames().get(0));
    }
}
