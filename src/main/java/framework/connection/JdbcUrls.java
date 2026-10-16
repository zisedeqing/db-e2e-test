package framework.connection;

// Shared helper to render a JDBC url template with endpoint / db / options.
// Supported placeholders: {host} {port} {db} {options}
final class JdbcUrls {
    private JdbcUrls() {}

    static String render(String template, Endpoint endpoint, String database, String connOptions) {
        String options = (connOptions != null && !connOptions.isEmpty()) ? connOptions : "";
        return template
                .replace("{host}", endpoint.getHost())
                .replace("{port}", String.valueOf(endpoint.getPort()))
                .replace("{db}", database == null ? "" : database)
                .replace("{options}", options);
    }
}
