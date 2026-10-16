package framework.connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

// Impala-compatible connector. All product-invariant knowledge (driver, port model,
// url template, timestamp dialect) is encapsulated here; no external config needed.
public class ImpalaConnector implements DbConnector {

    // Product-invariant constants.
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL_TEMPLATE =
        "jdbc:mysql://{host}:{port}/{db}?allowPublicKeyRetrieval=true&useLocalSessionState=TRUE{options}";
    private static final List<String> EXTRA_PORT_NAMES =
        Arrays.asList("hs2", "webserver", "http");

    @Override
    public String buildJdbcUrl(Endpoint endpoint, String database, String connOptions) {
        return JdbcUrls.render(URL_TEMPLATE, endpoint, database, connOptions);
    }

    @Override
    public String driverClass() {
        return DRIVER;
    }

    @Override
    public List<String> extraPortNames() {
        return EXTRA_PORT_NAMES;
    }

    // Impala-compatible timestamp nanosecond rendering (was Session.handleTimestampColumn).
    @Override
    public String renderColumn(ResultSet rs, ResultSetMetaData md, int i) throws SQLException {
        int colType = md.getColumnType(i);
        if (colType == Types.TIMESTAMP || colType == Types.TIMESTAMP_WITH_TIMEZONE) {
            return renderTimestamp(rs.getTimestamp(i));
        }
        return rs.getString(i);
    }

    private String renderTimestamp(Timestamp ts) {
        if (ts == null) return null;
        // resultSet.getString(i) only prints 4 digits of fractional seconds,
        // so render timestamps explicitly to keep up to 6 digits (microseconds).
        String base = ts.toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int nanos = ts.getNanos();
        if (nanos == 0) return base;
        int micros = nanos / 1000;                     // max 6 digits
        String fraction = String.format("%06d", micros).replaceFirst("0+$", "");
        return base + "." + fraction;
    }
}
