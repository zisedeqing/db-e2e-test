package framework.connection;

import org.junit.Test;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.sql.Types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ImpalaConnectorTest {

    @Test
    public void shouldRenderTimestampWithMicrosecondPrecision() throws Exception {
        Timestamp timestamp = Timestamp.valueOf("2026-10-16 12:34:56");
        timestamp.setNanos(123456000);
        ResultSet resultSet = resultSetReturning(timestamp);
        ResultSetMetaData metadata = metadataReturning(Types.TIMESTAMP);

        String rendered = new ImpalaConnector().renderColumn(resultSet, metadata, 1);

        assertEquals("2026-10-16 12:34:56.123456", rendered);
    }

    @Test
    public void shouldPreserveNullTimestamp() throws Exception {
        ResultSet resultSet = resultSetReturning(null);
        ResultSetMetaData metadata = metadataReturning(Types.TIMESTAMP);

        assertNull(new ImpalaConnector().renderColumn(resultSet, metadata, 1));
    }

    private ResultSet resultSetReturning(Timestamp timestamp) {
        return (ResultSet) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[]{ResultSet.class},
                (proxy, method, args) -> {
                    if ("getTimestamp".equals(method.getName())) return timestamp;
                    if ("getString".equals(method.getName())) return null;
                    throw new UnsupportedOperationException(method.getName());
                });
    }

    private ResultSetMetaData metadataReturning(int columnType) {
        return (ResultSetMetaData) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[]{ResultSetMetaData.class},
                (proxy, method, args) -> {
                    if ("getColumnType".equals(method.getName())) return columnType;
                    throw new UnsupportedOperationException(method.getName());
                });
    }
}
