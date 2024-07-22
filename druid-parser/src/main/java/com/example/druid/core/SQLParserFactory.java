package com.example.druid.core;

public interface SQLParserFactory {
    CreateSQLParser createSQLParser();
    SelectSQLParser selectSQLParser();
    InsertSQLParser insertSQLParser();
    UpdateSQLParser updateSQLParser();
}
