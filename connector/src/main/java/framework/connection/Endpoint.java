package framework.connection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Product-neutral service endpoint. Replaces the Impala-specific ImpaladService.
public class Endpoint {
    private final String host;
    private final int port;                        // primary port used to build JDBC url
    private final Map<String, Integer> extraPorts; // optional named ports (e.g. hs2/webserver)
    private final Map<String, String> attributes;  // arbitrary extension attributes

    public Endpoint(String host, int port,
                    Map<String, Integer> extraPorts,
                    Map<String, String> attributes) {
        this.host = host;
        this.port = port;
        this.extraPorts = extraPorts == null ? new HashMap<>() : extraPorts;
        this.attributes = attributes == null ? new HashMap<>() : attributes;
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public Integer getExtraPort(String name) { return extraPorts.get(name); }
    public Map<String, Integer> getExtraPorts() { return Collections.unmodifiableMap(extraPorts); }
    public String getAttribute(String key) { return attributes.get(key); }

    // Serialize back to CLI/env string, e.g. host:port:hs2=25000:webserver=25001
    public String format() {
        StringBuilder sb = new StringBuilder(host).append(":").append(port);
        for (Map.Entry<String, Integer> e : extraPorts.entrySet()) {
            sb.append(":").append(e.getKey()).append("=").append(e.getValue());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Endpoint(host=" + host + ", port=" + port + ", extraPorts=" + extraPorts + ")";
    }
}
