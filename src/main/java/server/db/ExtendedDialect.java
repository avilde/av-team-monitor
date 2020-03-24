package server.db;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

public class ExtendedDialect extends MySQL5Dialect {
    public ExtendedDialect() {
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StringType.INSTANCE));
        registerFunction("concat_ws", new StandardSQLFunction("concat_ws", StringType.INSTANCE));
    }
}
