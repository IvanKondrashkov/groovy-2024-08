package ru.otus.homework.mapper

interface EntitySQLMetaData<T> {
    String getSelectAllSql()
    String getSelectByIdSql(Long id)
    String getInsertSql(Object object)
    String getUpdateSql(Object object)
}