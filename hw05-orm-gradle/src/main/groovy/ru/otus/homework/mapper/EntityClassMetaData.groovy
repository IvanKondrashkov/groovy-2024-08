package ru.otus.homework.mapper

import java.lang.reflect.Field
import java.lang.reflect.Constructor

interface EntityClassMetaData<T> {
    String getName()
    Constructor<T> getConstructor()
    Field getIdField()
    List<Field> getAllFields()
    List<Field> getFieldsWithoutId()
}