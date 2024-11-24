package ru.otus.homework.repository

interface DataTemplate<T> {
    def findById(connection, id)
    def findAll(connection)
    int insert(connection, object)
    void update(connection, object)
}