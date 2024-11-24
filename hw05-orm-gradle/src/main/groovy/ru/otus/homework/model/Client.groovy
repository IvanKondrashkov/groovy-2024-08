package ru.otus.homework.model

import groovy.transform.ToString
import ru.otus.homework.annotation.Id

@ToString(includeFields = true)
class Client {
    @Id
    Integer id
    String name
}