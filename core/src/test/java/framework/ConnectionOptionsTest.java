package framework;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionOptionsTest {

    @Test
    public void shouldBuildEncodedJdbcOptionsWithoutLeadingSeparator() {
        ConnectionOptions options = new ConnectionOptions(
                "analytics", "test user", "p@ss word", "&useSSL=false");

        assertEquals("user=test+user&password=p%40ss+word&useSSL=false",
                options.getJdbcOptions());
    }

    @Test
    public void shouldReturnEmptyOptionsWhenCredentialsAndOptionsAreAbsent() {
        assertEquals("", new ConnectionOptions().getJdbcOptions());
    }

    @Test
    public void shouldUseOneShotStatementOptionsBeforeGlobalOptions() {
        ConnectionOptions original = ConnectionOptionsManager.getGlobalOptions();
        try {
            ConnectionOptionsManager.setGlobalOptions(
                    new ConnectionOptions("default", "global", "secret", "timeout=10"));
            ConnectionOptionsManager.parserConnectionOptions(
                    "-- connection_options:useSSL=false\n");

            assertEquals("useSSL=false", ConnectionOptionsManager.peekConnectionOptions());
            assertEquals("user=global&password=secret&timeout=10",
                    ConnectionOptionsManager.peekConnectionOptions());
        } finally {
            // Consume any pending one-shot value before restoring the global object.
            ConnectionOptionsManager.peekConnectionOptions();
            ConnectionOptionsManager.setGlobalOptions(original);
        }
    }
}
