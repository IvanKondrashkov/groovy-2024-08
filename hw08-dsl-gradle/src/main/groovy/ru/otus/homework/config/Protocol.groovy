package ru.otus.homework.config

import groovy.transform.ToString

@ToString(includeFields = true)
class Protocol {
    Integer port
    Boolean secure
}