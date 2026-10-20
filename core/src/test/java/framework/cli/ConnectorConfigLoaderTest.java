package framework.cli;

import framework.connection.GenericJdbcConnector;
import framework.connection.tchousexconnector.TchousexConnector;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConnectorConfigLoaderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldRejectMissingConfigFile() {
        File missing = new File(temporaryFolder.getRoot(), "missing.properties");

        assertInvalid(missing, "connector config file not found");
    }

    @Test
    public void shouldRejectDirectoryAsConfigFile() throws Exception {
        File directory = temporaryFolder.newFolder("connector.properties");

        assertInvalid(directory, "connector config file not found");
    }

    @Test
    public void shouldLoadAllConnectionSettings() throws Exception {
        File config = writeConfig("connector.properties",
                "connector.impl=framework.connection.tchousexconnector.TchousexConnector\n"
                        + "connection.endpoints=db-1:33060:hs2=25000,db-2:33061:hs2=25001\n"
                        + "connection.database=analytics\n"
                        + "connection.user=test user\n"
                        + "connection.password=密码 value\n"
                        + "connection.jdbcOptions=useSSL=false&connectTimeout=10000\n");

        ConnectorConfiguration configuration = ConnectorConfigLoader.load(config.getPath());

        assertTrue(configuration.getConnector() instanceof TchousexConnector);
        assertEquals(2, configuration.getEndpoints().size());
        assertEquals("db-1", configuration.getEndpoints().get(0).getHost());
        assertEquals("analytics", configuration.getConnectionOptions().getDbName());
        assertEquals("test user", configuration.getConnectionOptions().getUserName());
        assertEquals("密码 value", configuration.getConnectionOptions().getPassword());
        assertEquals("user=test+user&password=%E5%AF%86%E7%A0%81+value"
                        + "&useSSL=false&connectTimeout=10000",
                configuration.getConnectionOptions().getJdbcOptions());
    }

    @Test
    public void shouldLoadGenericConnectorAndDefaultOptionalSettings() throws Exception {
        File config = writeConfig("generic.properties",
                "connector.driver=org.example.Driver\n"
                        + "connector.url=jdbc:example://{host}:{port}/{db}{options}\n"
                        + "connector.extraPortNames=admin,http\n"
                        + "connection.endpoints=localhost:5432\n");

        ConnectorConfiguration configuration = ConnectorConfigLoader.load(config.getPath());

        assertTrue(configuration.getConnector() instanceof GenericJdbcConnector);
        assertEquals("org.example.Driver", configuration.getConnector().driverClass());
        assertEquals("default", configuration.getConnectionOptions().getDbName());
        assertNull(configuration.getConnectionOptions().getUserName());
        assertNull(configuration.getConnectionOptions().getPassword());
        assertEquals("", configuration.getConnectionOptions().getJdbcOptions());
    }

    @Test
    public void shouldRequireEndpoints() throws Exception {
        File config = writeConfig("missing-endpoints.properties",
                "connector.impl=framework.connection.tchousexconnector.TchousexConnector\n");

        assertInvalid(config, "missing required property: connection.endpoints");
    }

    @Test
    public void shouldIncludeConfigPathWhenConnectorPropertiesAreInvalid() throws Exception {
        File config = writeConfig("invalid.properties",
                "connector.driver=org.example.Driver\n"
                        + "connection.endpoints=localhost:5432\n");

        assertInvalid(config, "connector.driver");
    }

    private File writeConfig(String name, String content) throws Exception {
        File config = temporaryFolder.newFile(name);
        Files.write(config.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return config;
    }

    private void assertInvalid(File config, String expectedMessage) {
        try {
            ConnectorConfigLoader.load(config.getPath());
            fail("Expected invalid connector configuration to fail");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(config.getPath()));
            assertTrue(e.getMessage().contains(expectedMessage));
        }
    }
}
