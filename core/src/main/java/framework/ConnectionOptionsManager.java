package framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Holds the global connection settings loaded from connector.properties and a
// one-shot JDBC option override parsed from `-- connection_options:` comments.
// The override is consumed by the next parsed SQL statement.
public class ConnectionOptionsManager {
  private static Logger LOGGER = LogManager.getLogger(ConnectionOptionsManager.class);
  private static String IDENTIFIER = "-- connection_options";
  private static String connectionOptions = null;
  private static ConnectionOptions globalOptions = new ConnectionOptions();

  public static ConnectionOptions getGlobalOptions() {
    return globalOptions;
  }

  public static void setGlobalOptions(ConnectionOptions options) {
    if (options == null)
      throw new IllegalArgumentException("global connection options must not be null");
    globalOptions = options;
  }

  public static boolean isConnectionOptions(String comment) {
    if (comment == null) return false;
    return comment.startsWith(IDENTIFIER);
  }

  public static void parserConnectionOptions(String comment) {
    if (isConnectionOptions(comment)) {
      int pos = comment.indexOf(':');
      if (pos <= 0) return;
      connectionOptions = comment.substring(pos + 1).trim();
      if (connectionOptions.endsWith("\n"))
        connectionOptions = connectionOptions.substring(0, connectionOptions.length() - 1);
      LOGGER.trace(IDENTIFIER + ": " + connectionOptions);
    }
  }

  public static String peekConnectionOptions() {
    String options = connectionOptions;
    connectionOptions = null;
    return options != null ? options : globalOptions.getJdbcOptions();
  }
}
