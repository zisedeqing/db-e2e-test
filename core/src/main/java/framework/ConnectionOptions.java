package framework;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConnectionOptions {
    private String dbName_ = "default";
    private String userName_ = "";
    private String password_ = "";

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
        if (userName_ != null) {
            builder.append("&user=").append(userName_);
        }
        if (password_ != null) {
            try{
                builder.append("&password=").append(URLEncoder.encode(password_, "UTF-8")).append("");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        if (jdbcOptions_ != null)
            builder.append("&").append(jdbcOptions_);
        return builder.toString();
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
