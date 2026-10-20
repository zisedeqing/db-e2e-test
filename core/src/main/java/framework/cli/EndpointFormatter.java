package framework.cli;

import framework.connection.DbConnector;
import framework.connection.Endpoint;

import java.util.ArrayList;
import java.util.List;

// Formats endpoints in the stable extra-port order declared by the connector.
public final class EndpointFormatter {
    private EndpointFormatter() {}

    public static List<String> format(List<Endpoint> endpoints, DbConnector connector) {
        List<String> formatted = new ArrayList<>();
        for (Endpoint endpoint : endpoints) {
            StringBuilder builder = new StringBuilder(endpoint.getHost())
                    .append(":").append(endpoint.getPort());
            for (String name : connector.extraPortNames()) {
                Integer port = endpoint.getExtraPort(name);
                if (port != null)
                    builder.append(":").append(name).append("=").append(port);
            }
            formatted.add(builder.toString());
        }
        return formatted;
    }
}
