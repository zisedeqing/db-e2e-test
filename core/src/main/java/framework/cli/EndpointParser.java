package framework.cli;

import framework.connection.Endpoint;

import java.util.LinkedHashMap;
import java.util.Map;

// Parses the product-neutral endpoint syntax accepted by the CLI.
public final class EndpointParser {
    private EndpointParser() {}

    // Supported formats: host:port or host:port:name=port[:name=port...].
    public static Endpoint parse(String spec) {
        if (spec == null || spec.isEmpty())
            throw new IllegalArgumentException("invalid endpoint format: " + spec);
        String[] parts = spec.split(":", -1);
        if (parts.length < 2)
            throw new IllegalArgumentException("invalid endpoint format: " + spec);
        if (parts[0].isEmpty())
            throw new IllegalArgumentException("invalid endpoint host: " + spec);

        int port = parsePort(parts[1], "invalid endpoint port: " + spec);
        Map<String, Integer> extraPorts = new LinkedHashMap<>();
        for (int i = 2; i < parts.length; i++) {
            String[] pair = parts[i].split("=", -1);
            if (pair.length != 2 || pair[0].isEmpty())
                throw new IllegalArgumentException("invalid extra port segment: " + parts[i]);
            extraPorts.put(pair[0], parsePort(pair[1],
                    "invalid extra port value: " + parts[i]));
        }
        return new Endpoint(parts[0], port, extraPorts, null);
    }

    private static int parsePort(String value, String message) {
        try {
            int port = Integer.parseInt(value);
            if (port < 1 || port > 65535)
                throw new NumberFormatException("out of range");
            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(message, e);
        }
    }
}
