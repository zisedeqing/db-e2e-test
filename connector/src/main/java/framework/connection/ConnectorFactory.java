package framework.connection;

import java.util.Map;

// Assembles a DbConnector from configuration, falling back to the built-in default.
public class ConnectorFactory {

    // Built-in default connector when no config is provided.
    public static final DbConnector DEFAULT = new ImpalaConnector();

    // No connector.properties found at all -> use the built-in default.
    public static DbConnector createDefault() {
        return DEFAULT;
    }

    // Build connector from parsed connector.properties.
    public static DbConnector create(Map<String, String> props) {
        if (props == null || props.isEmpty()) {
            return DEFAULT;                      // zero-config -> built-in ImpalaConnector
        }

        String impl = props.get("connector.impl");

        // Path 1: a product connector class -> product-invariant knowledge is
        // encapsulated inside; driver/extraPortNames from config are ignored.
        if (impl != null && !impl.trim().isEmpty()) {
            try {
                return (DbConnector) Class.forName(impl.trim())
                        .getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("cannot instantiate connector.impl: " + impl, e);
            }
        }

        // Path 2: has config but no impl -> generic connector driven by config.
        return GenericJdbcConnector.fromProperties(props);
    }
}
