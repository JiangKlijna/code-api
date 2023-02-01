package io.github.jiangklina.codeapi.executor.jdbc;

import com.github.freakchick.orange.SqlMeta;
import com.github.freakchick.orange.engine.DynamicSqlEngine;
import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.api.ParamException;
import io.github.jiangklina.codeapi.executor.Executor;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * CodeApi.Executor.JdbcExecutor
 * jdbc执行者,执行sql
 *
 * @author jiangklijna
 */
public class JdbcExecutor implements Executor<Object> {

    private static final DynamicSqlEngine engine = new DynamicSqlEngine();
    private static final Map<String, String> databaseAlias = new HashMap<>();

    static {
        databaseAlias.put("dameng", "oracle");
    }

    private final DataSource datasource;
    private final String databaseProductName;
    private final String databaseProductVersion;
    private final Map<String, Object> config;

    /**
     * 保存datasource同时获取数据库产品名及版本名
     *
     * @param datasource jdbc数据源
     */
    public JdbcExecutor(DataSource datasource) {
        this.datasource = datasource;
        try {
            Connection connection = datasource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            this.databaseProductVersion = metaData.getDatabaseProductVersion().toLowerCase();
            this.databaseProductName = metaData.getDatabaseProductName().toLowerCase();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> config = new HashMap<>();
        config.put("dbType", getDbType());
        this.config = Collections.unmodifiableMap(config);
    }

    /**
     * 获取dbType,数据库类型即sql语法类型
     * 目前只有这四种类型足够覆盖99.99%的数据库语法类型了
     * oracle: 达梦
     * mysql:
     * pgsql: 人大金仓
     * mssql:
     *
     * @return String
     */
    private String getDbType() {
        // 暂时这样写足够了,后续要根据适配的数据库做调整
        return databaseProductName;
    }

    @Override
    public Map<String, Object> execParams(Api api, Map<String, Object> params) throws ParamException {
        Map<String, Object> map = Executor.super.execParams(api, params);
        if (!map.containsKey("config")) {
            map.put("config", config);
        }
        return map;
    }

    /**
     * 根据数据库产品名及版本名以及数据库别名获取更详细对应的sql
     * 获取不到则返回commonCode
     *
     * @param api Api对象
     * @return sql
     */
    @Override
    public String getCode(Api api) {
        if (api.getOther().isEmpty()) {
            return api.getCommon();
        }
        String dataBaseAllTag = databaseProductName + databaseProductVersion;
        Map<String, String> otherCode = api.getOther();
        if (otherCode.containsKey(dataBaseAllTag)) {
            return otherCode.get(dataBaseAllTag);
        }
        if (otherCode.containsKey(databaseProductName)) {
            return otherCode.get(databaseProductName);
        }
        if (databaseAlias.containsKey(databaseProductName)) {
            String databaseProductAliasName = databaseAlias.get(databaseProductName);
            if (otherCode.containsKey(databaseProductAliasName)) {
                return otherCode.get(databaseProductAliasName);
            }
        }
        return api.getCommon();
    }

    /**
     * 1.通过DynamicSqlEngine对mybatis格式的code进行加工得到要执行的sql
     * 2.通过JdbcKit.executeSql以及执行参数 执行sql
     *
     * @param sql        code
     * @param execParams 执行参数
     * @return 执行结果
     * @throws Exception 执行异常
     */
    @Override
    public Object execCode(String sql, Map execParams) throws Exception {
        SqlMeta sqlMeta = engine.parse(sql, execParams);
        return executeSql(datasource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
    }


    public static Object executeSql(DataSource datasource, String sql, List<Object> jdbcParamValues) throws Exception {
        Connection connection = null;
        try {
            connection = datasource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            //参数注入
            for (int i = 1; i <= jdbcParamValues.size(); i++) {
                statement.setObject(i, jdbcParamValues.get(i - 1));
            }

            boolean hasResultSet = statement.execute();
            if (hasResultSet) {
                ResultSet rs = statement.getResultSet();
                int columnCount = rs.getMetaData().getColumnCount();

//                List<String> columns = new ArrayList<>();
                Map<String, String> fields = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnLabel(i);
                    fields.put(columnName, findPropertyName(columnName));
                }

                List<Map<String, Object>> list = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> jo = new HashMap<>();
                    fields.forEach((column, field) -> {
                        try {
                            Object value = rs.getObject(column);
                            jo.put(field, value);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });
                    list.add(jo);
                }
                return list;
            } else {
                int updateCount = statement.getUpdateCount();
                return updateCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // 字段明明转换为驼峰
    public static String findPropertyName(String name) {
        if (name.contains("_")) {
            return camelCase(name);
        } else if (StringUtils.isAllUpperCase(name)) {
            return name.toLowerCase();
        }
        return name;
    }

    public static String camelCase(String s) {
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;

        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '_') {
                upperCase = i != 1;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
