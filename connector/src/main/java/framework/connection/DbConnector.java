package framework.connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

// Strategy for how to connect to and read from a specific database product.
public interface DbConnector {

    // Build the full JDBC url for the given endpoint / database / extra options.
    String buildJdbcUrl(Endpoint endpoint, String database, String connOptions);

    // Fully-qualified JDBC driver class name to load.
    String driverClass();

    // Names of extra ports carried by each endpoint (exposed to shell scripts).
    // Product-invariant knowledge: default empty, product connectors override it.
    default List<String> extraPortNames() {
        return Collections.emptyList();
    }

    // Render one result column to string. Default keeps ResultSet.getString() behavior.
    // Dialect-specific quirks (e.g. timestamp nanosecond precision) are overridden here.
    default String renderColumn(ResultSet rs, ResultSetMetaData md, int columnIndex)
            throws SQLException {
        return rs.getString(columnIndex);
    }
}
