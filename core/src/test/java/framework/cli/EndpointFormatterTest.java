package framework.cli;

import framework.connection.DbConnector;
import framework.connection.Endpoint;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EndpointFormatterTest {

    @Test
    public void shouldFormatExtraPortsInConnectorOrder() {
        Map<String, Integer> extraPorts = new LinkedHashMap<>();
        extraPorts.put("unknown", 9999);
        extraPorts.put("webserver", 25001);
        extraPorts.put("hs2", 25000);
        Endpoint endpoint = new Endpoint("db.example.com", 33060, extraPorts, null);
        DbConnector connector = connectorWithExtraPorts("hs2", "webserver");

        assertEquals(
                Collections.singletonList(
                        "db.example.com:33060:hs2=25000:webserver=25001"),
                EndpointFormatter.format(Collections.singletonList(endpoint), connector));
    }

    @Test
    public void shouldFormatMultipleEndpoints() {
        Endpoint first = new Endpoint("db-1", 33060, null, null);
        Endpoint second = new Endpoint("db-2", 33061, null, null);

        assertEquals(Arrays.asList("db-1:33060", "db-2:33061"),
                EndpointFormatter.format(Arrays.asList(first, second),
                        connectorWithExtraPorts()));
    }

    private DbConnector connectorWithExtraPorts(String... names) {
        return new DbConnector() {
            @Override
            public String buildJdbcUrl(Endpoint endpoint, String database, String connOptions) {
                return "";
            }

            @Override
            public String driverClass() {
                return "";
            }

            @Override
            public java.util.List<String> extraPortNames() {
                return Arrays.asList(names);
            }
        };
    }
}
