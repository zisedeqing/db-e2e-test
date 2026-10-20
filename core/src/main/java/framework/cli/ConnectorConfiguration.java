package framework.cli;

import framework.ConnectionOptions;
import framework.connection.DbConnector;
import framework.connection.Endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Complete runtime connection configuration loaded from one connector properties file.
public final class ConnectorConfiguration {
    private final DbConnector connector;
    private final List<Endpoint> endpoints;
    private final ConnectionOptions connectionOptions;

    public ConnectorConfiguration(DbConnector connector, List<Endpoint> endpoints,
                                  ConnectionOptions connectionOptions) {
        this.connector = connector;
        this.endpoints = Collections.unmodifiableList(new ArrayList<>(endpoints));
        this.connectionOptions = connectionOptions;
    }

    public DbConnector getConnector() {
        return connector;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public ConnectionOptions getConnectionOptions() {
        return connectionOptions;
    }
}
