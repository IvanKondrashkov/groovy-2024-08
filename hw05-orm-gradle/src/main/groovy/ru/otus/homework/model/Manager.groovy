package ru.otus.homework.model

import groovy.transform.ToString
import ru.otus.homework.annotation.Id

@ToString(includeFields = true)
class Manager {
    @Id
    Integer no
    String label
    String param1
}