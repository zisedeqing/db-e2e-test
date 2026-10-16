package framework;

public class ImpaladService {
    private String host_;
    private int mysql_port_;

    private int hs2_port_;
    private int webserver_port_;
    private int hs2_http_port_;

    public ImpaladService(String host, int mysql_port, int hs2_port, int webserver_port,int hs2_http_port) {
        host_ = host;
        mysql_port_ = mysql_port;
        hs2_port_ = hs2_port;
        webserver_port_ = webserver_port;
        hs2_http_port_ = hs2_http_port;
    }

    public String getHost() {
        return host_;
    }

    public int getMysqlPort() {
        return mysql_port_;
    }

    public int getHs2_port() {
        return hs2_port_;
    }

    public int getWebserver_port() {
        return webserver_port_;
    }

    public int getHs2_http_port() {
        return hs2_http_port_;
    }

    @Override
    public String toString() {
        return String.format("ImpalaService(host:%s, port:%d, webserver_port=%d, hs2_http_port=%d)",
                host_, mysql_port_, webserver_port_, hs2_http_port_);
    }

    public String format() {
        return String.format("%s:%s:%s:%s:%s",
                host_, mysql_port_, hs2_port_, webserver_port_, hs2_http_port_);
    }
}
