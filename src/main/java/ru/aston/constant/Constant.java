package ru.aston.constant;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String REASON_OK = "OK";
    public static final String REASON_CREATED = "Created";
    public static final String REASON_NOT_FOUND = "The required object was not found";
    public static final String REASON_INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String REASON_CONFLICT = "Integrity constraint has been violated";
    public static final String REASON_BAD_REQUEST = "Incorrectly made request";
    public static final String PAGE_FROM_DEFAULT = "0";
    public static final String PAGE_SIZE_DEFAULT = "10";
    public static final String SCHEMA_SQL_FILE = "schema.sql";
    public static final String HEADER_USER = "X-User-Id";
}
