package framework.connection;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ConnectorFactoryTest {

    @Test
    public void shouldUseBuiltInImpalaConnectorByDefault() {
        assertSame(ConnectorFactory.DEFAULT, ConnectorFactory.createDefault());
        assertSame(ConnectorFactory.DEFAULT, ConnectorFactory.create(Collections.emptyMap()));
        assertTrue(ConnectorFactory.DEFAULT instanceof ImpalaConnector);
    }

    @Test
    public void shouldInstantiateConfiguredConnectorImplementation() {
        Map<String, String> props = Collections.singletonMap(
                "connector.impl", ImpalaConnector.class.getName());

        DbConnector connector = ConnectorFactory.create(props);

        assertTrue(connector instanceof ImpalaConnector);
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectUnknownConnectorImplementation() {
        ConnectorFactory.create(Collections.singletonMap(
                "connector.impl", "not.existing.Connector"));
    }

    @Test
    public void shouldKeepImpalaProductKnowledgeInsideImplementation() {
        ImpalaConnector connector = new ImpalaConnector();
        Endpoint endpoint = new Endpoint("db.host", 33060, null, null);

        assertEquals("com.mysql.cj.jdbc.Driver", connector.driverClass());
        assertEquals("jdbc:mysql://db.host:33060/default?allowPublicKeyRetrieval=true"
                        + "&useLocalSessionState=TRUE&useSSL=false",
                connector.buildJdbcUrl(endpoint, "default", "useSSL=false"));
        assertEquals(3, connector.extraPortNames().size());
        assertEquals("hs2", connector.extraPortNames().get(0));
    }
}
