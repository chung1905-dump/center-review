package com.reviewtrungtam.webapp.general.database;

import org.hibernate.dialect.MariaDB10Dialect;

public class MySQLDialectUtf8mb4 extends MariaDB10Dialect {
    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_520_ci";
    }
}
