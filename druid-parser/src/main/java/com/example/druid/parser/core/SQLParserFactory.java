package com.example.druid.parser.core;

public interface SQLParserFactory {
    CreateSQLParser createSQLParser();
    SelectSQLParser selectSQLParser();
    InsertSQLParser insertSQLParser();
    UpdateSQLParser updateSQLParser();
}
