package framework.connection;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EndpointTest {

    @Test
    public void shouldExposePrimaryAndExtraPorts() {
        Map<String, Integer> extraPorts = new LinkedHashMap<>();
        extraPorts.put("hs2", 25000);
        extraPorts.put("webserver", 25001);

        Endpoint endpoint = new Endpoint("127.0.0.1", 33060, extraPorts, null);

        assertEquals("127.0.0.1", endpoint.getHost());
        assertEquals(33060, endpoint.getPort());
        assertEquals(Integer.valueOf(25000), endpoint.getExtraPort("hs2"));
        assertNull(endpoint.getExtraPort("unknown"));
        assertEquals("127.0.0.1:33060:hs2=25000:webserver=25001", endpoint.format());
    }
}
