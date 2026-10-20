package framework.cli;

import framework.ConnectionOptions;
import framework.connection.ConnectorFactory;
import framework.connection.DbConnector;
import framework.connection.Endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

// Loads all database connection settings from one connector properties file.
public final class ConnectorConfigLoader {
    public static final String KEY_ENDPOINTS = "connection.endpoints";
    public static final String KEY_DATABASE = "connection.database";
    public static final String KEY_USER = "connection.user";
    public static final String KEY_PASSWORD = "connection.password";
    public static final String KEY_JDBC_OPTIONS = "connection.jdbcOptions";

    private ConnectorConfigLoader() {}

    public static ConnectorConfiguration load(String configFile) {
        File file = new File(configFile);
        if (!file.exists() || file.isDirectory())
            throw new IllegalArgumentException("connector config file not found: " + configFile);

        Map<String, String> properties = loadProperties(file, configFile);
        try {
            DbConnector connector = ConnectorFactory.create(properties);
            List<Endpoint> endpoints = parseEndpoints(properties.get(KEY_ENDPOINTS));
            ConnectionOptions options = new ConnectionOptions(
                    valueOrDefault(properties, KEY_DATABASE, "default"),
                    emptyToNullPreservingWhitespace(properties.get(KEY_USER)),
                    emptyToNullPreservingWhitespace(properties.get(KEY_PASSWORD)),
                    emptyToNull(properties.get(KEY_JDBC_OPTIONS)));
            return new ConnectorConfiguration(connector, endpoints, options);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("invalid connector config: " + configFile
                    + ": " + e.getMessage(), e);
        }
    }

    private static Map<String, String> loadProperties(File file, String configFile) {
        Map<String, String> properties = new LinkedHashMap<>();
        try (InputStream input = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            Properties raw = new Properties();
            raw.load(reader);
            for (Map.Entry<Object, Object> entry : raw.entrySet()) {
                properties.put((String) entry.getKey(), (String) entry.getValue());
            }
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot load connector config: " + configFile, e);
        }
    }

    private static List<Endpoint> parseEndpoints(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("missing required property: " + KEY_ENDPOINTS);

        List<Endpoint> endpoints = new ArrayList<>();
        for (String endpoint : value.split(",", -1)) {
            endpoints.add(EndpointParser.parse(endpoint.trim()));
        }
        return endpoints;
    }

    private static String valueOrDefault(Map<String, String> properties,
                                         String key, String defaultValue) {
        String value = emptyToNull(properties.get(key));
        return value == null ? defaultValue : value;
    }

    private static String emptyToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private static String emptyToNullPreservingWhitespace(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
