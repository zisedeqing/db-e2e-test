package framework.connection;

// Shared helper to render a JDBC url template with endpoint / db / options.
// Supported placeholders: {host} {port} {db} {options}
final class JdbcUrls {
    private JdbcUrls() {}

    static String render(String template, Endpoint endpoint, String database, String connOptions) {
        String options = normalizeOptions(template, connOptions);
        return template
                .replace("{host}", endpoint.getHost())
                .replace("{port}", String.valueOf(endpoint.getPort()))
                .replace("{db}", database == null ? "" : database)
                .replace("{options}", options);
    }

    // The connector owns URL syntax. Core code passes a raw query fragment and
    // this helper selects '?' or '&' according to the template before {options}.
    private static String normalizeOptions(String template, String connOptions) {
        if (connOptions == null) return "";
        String options = connOptions.trim();
        while (options.startsWith("?") || options.startsWith("&")) {
            options = options.substring(1);
        }
        if (options.isEmpty()) return "";

        int placeholder = template.indexOf("{options}");
        String prefix = placeholder >= 0 ? template.substring(0, placeholder) : template;
        return (prefix.contains("?") ? "&" : "?") + options;
    }
}
