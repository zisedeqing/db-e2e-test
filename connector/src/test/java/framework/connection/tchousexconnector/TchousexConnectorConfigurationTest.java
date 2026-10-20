package framework.connection.tchousexconnector;

import framework.connection.ConnectorFactory;
import framework.connection.DbConnector;
import org.junit.Test;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TchousexConnectorConfigurationTest {

    @Test
    public void shouldInstantiateConnectorFromPackagedTemplate() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("connectors/tchousex.properties")) {
            assertNotNull("TCHouse-X connector template must be packaged", input);
            properties.load(input);
        }

        Map<String, String> config = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            config.put((String) entry.getKey(), (String) entry.getValue());
        }

        DbConnector connector = ConnectorFactory.create(config);

        assertTrue(connector instanceof TchousexConnector);
        assertEquals("127.0.0.1:33060:hs2=25000:webserver=25010:http=28000",
                properties.getProperty("connection.endpoints"));
        assertEquals("default", properties.getProperty("connection.database"));
        assertTrue(properties.containsKey("connection.user"));
        assertTrue(properties.containsKey("connection.password"));
        assertTrue(properties.containsKey("connection.jdbcOptions"));
    }
}
