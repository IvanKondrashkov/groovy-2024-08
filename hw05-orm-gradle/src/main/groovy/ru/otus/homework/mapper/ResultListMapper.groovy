package ru.otus.homework.mapper

import java.sql.ResultSet
import java.sql.SQLException
import java.lang.reflect.Field
import java.util.function.Function
import ru.otus.homework.ReflectionUtility

class ResultListMapper<T> implements Function<ResultSet, List<T>> {
    private final EntityClassMetaData<T> entityClassMetaData

    ResultListMapper(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData
    }

    @Override
    List<T> apply(ResultSet resultSet) {
        try {
            List<T> tList = new ArrayList<>()
            while (resultSet.next()) {
                T object = ReflectionUtility.useConstructor(entityClassMetaData.getConstructor())
                for (Field f : entityClassMetaData.getAllFields()) {
                    ReflectionUtility.setValue(f, object, resultSet.getObject(f.getName()))
                }
                tList.add(object)
            }
            tList
        } catch (SQLException ex) {
            List.of(ex.printStackTrace())
        }
    }
}