package framework.connection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// Generic connector driven entirely by configuration. Zero code change to support
// a new product: just provide a new connector.properties.
public class GenericJdbcConnector implements DbConnector {
    private final String driverClass;
    private final String urlTemplate;          // supports {host} {port} {db} {options}
    private final List<String> extraPortNames;

    public GenericJdbcConnector(String driverClass, String urlTemplate,
                                List<String> extraPortNames) {
        this.driverClass = driverClass;
        this.urlTemplate = urlTemplate;
        this.extraPortNames = extraPortNames == null
                ? Collections.emptyList() : extraPortNames;
    }

    @Override
    public String buildJdbcUrl(Endpoint endpoint, String database, String connOptions) {
        return JdbcUrls.render(urlTemplate, endpoint, database, connOptions);
    }

    @Override
    public String driverClass() {
        return driverClass;
    }

    @Override
    public List<String> extraPortNames() {
        return extraPortNames;
    }

    // Factory: load from a properties map (parsed from connector.properties).
    public static GenericJdbcConnector fromProperties(Map<String, String> props) {
        String driver = props.get("connector.driver");
        String url = props.get("connector.url");
        if (driver == null || url == null) {
            throw new IllegalArgumentException(
                "generic connector config must define connector.driver and connector.url");
        }
        String names = props.get("connector.extraPortNames");
        List<String> extra = (names == null || names.trim().isEmpty())
                ? Collections.emptyList()
                : Arrays.asList(names.trim().split("\\s*,\\s*"));
        return new GenericJdbcConnector(driver, url, extra);
    }
}
