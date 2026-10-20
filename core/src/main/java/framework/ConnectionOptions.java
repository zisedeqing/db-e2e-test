package framework;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConnectionOptions {
    private String dbName_ = "default";
    private String userName_ = null;
    private String password_ = null;

    private String jdbcOptions_ = null;

    public ConnectionOptions() { }

    public ConnectionOptions(String dbName, String userName) {
        this(dbName, userName, null);
    }

    public ConnectionOptions(String dbName, String userName, String password) {
        this(dbName, userName, password, null);
    }

    public ConnectionOptions(String dbName, String userName, String password, String jdbcOptions) {
        this.dbName_ = dbName;
        this.userName_ = userName;
        this.password_ = password;
        this.jdbcOptions_ = jdbcOptions;
    }

    public String getJdbcOptions() {
        StringBuilder builder = new StringBuilder();
        appendOption(builder, "user", encode(userName_));
        appendOption(builder, "password", encode(password_));
        if (jdbcOptions_ != null && !jdbcOptions_.isEmpty()) {
            if (builder.length() > 0) builder.append("&");
            builder.append(trimOptionPrefix(jdbcOptions_));
        }
        return builder.toString();
    }

    private String encode(String value) {
        if (value == null) return null;
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendOption(StringBuilder builder, String name, String value) {
        if (value == null) return;
        if (builder.length() > 0) builder.append("&");
        builder.append(name).append("=").append(value);
    }

    private String trimOptionPrefix(String value) {
        int start = 0;
        while (start < value.length()
                && (value.charAt(start) == '?' || value.charAt(start) == '&')) {
            start++;
        }
        return value.substring(start);
    }

    public String getDbName() {
        return dbName_;
    }

    public String getPassword() {
        return password_;
    }

    public void setDbName(String dbName) {
        this.dbName_ = dbName;
    }

    public String getUserName() {
        return userName_;
    }

    public void setJdbcOptions(String jdbcOptions) {
        this.jdbcOptions_ = jdbcOptions;
    }

    public void setPassword(String password) {
        this.password_ = password;
    }

    public void setUserName(String userName) {
        this.userName_ = userName;
    }
}
