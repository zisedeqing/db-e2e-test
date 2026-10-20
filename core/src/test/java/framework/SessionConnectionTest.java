package framework;

import framework.connection.DbConnector;
import framework.connection.Endpoint;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SessionConnectionTest {

    @Test
    public void shouldRedactPasswordFromJdbcUrlLogs() {
        assertEquals(
                "jdbc:mysql://localhost/default?user=test&password=***&useSSL=false",
                Session.sanitizeJdbcUrl(
                        "jdbc:mysql://localhost/default?user=test&password=secret&useSSL=false"));
        assertEquals(
                "jdbc:example://localhost/default?pwd=***",
                Session.sanitizeJdbcUrl("jdbc:example://localhost/default?pwd=secret"));
    }

    @Test
    public void shouldPassRawConnectionOptionsAndGlobalDatabaseToConnector() throws Exception {
        RecordingDriver driver = new RecordingDriver();
        DriverManager.registerDriver(driver);
        String originalDatabase = ConnectionOptionsManager.getGlobalOptions().getDbName();
        ConnectionOptionsManager.getGlobalOptions().setDbName("analytics");
        RecordingConnector connector = new RecordingConnector();
        Endpoint endpoint = new Endpoint("db.example.com", 5432, null, null);

        try {
            Session session = new Session("session-1", 0, endpoint, connector,
                    "ssl=true&timeout=10");
            session.close();

            assertSame(endpoint, connector.endpoint);
            assertEquals("analytics", connector.database);
            assertEquals("ssl=true&timeout=10", connector.options);
            assertEquals("jdbc:recording:test", driver.connectedUrl);
        } finally {
            ConnectionOptionsManager.getGlobalOptions().setDbName(originalDatabase);
            DriverManager.deregisterDriver(driver);
        }
    }

    private static class RecordingConnector implements DbConnector {
        private Endpoint endpoint;
        private String database;
        private String options;

        @Override
        public String buildJdbcUrl(Endpoint endpoint, String database, String connOptions) {
            this.endpoint = endpoint;
            this.database = database;
            this.options = connOptions;
            return "jdbc:recording:test";
        }

        @Override
        public String driverClass() {
            return RecordingDriver.class.getName();
        }

        @Override
        public String renderColumn(ResultSet rs, ResultSetMetaData md, int columnIndex)
                throws SQLException {
            return rs.getString(columnIndex);
        }
    }

    private static class RecordingDriver implements Driver {
        private String connectedUrl;

        @Override
        public Connection connect(String url, Properties info) {
            if (!acceptsURL(url)) return null;
            connectedUrl = url;
            return (Connection) Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> defaultValue(method.getReturnType()));
        }

        @Override
        public boolean acceptsURL(String url) {
            return url != null && url.startsWith("jdbc:recording:");
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
            return new DriverPropertyInfo[0];
        }

        @Override
        public int getMajorVersion() {
            return 1;
        }

        @Override
        public int getMinorVersion() {
            return 0;
        }

        @Override
        public boolean jdbcCompliant() {
            return false;
        }

        @Override
        public Logger getParentLogger() {
            return Logger.getGlobal();
        }

        private Object defaultValue(Class<?> type) {
            if (!type.isPrimitive()) return null;
            if (type == boolean.class) return false;
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0F;
            if (type == double.class) return 0D;
            if (type == char.class) return '\0';
            return null;
        }
    }
}
