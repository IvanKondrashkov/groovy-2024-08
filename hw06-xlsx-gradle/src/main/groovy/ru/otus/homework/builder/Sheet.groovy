package ru.otus.homework.builder

import groovy.transform.ToString
import org.apache.poi.xssf.streaming.SXSSFSheet

@ToString(includeFields = true)
class Sheet {
    Integer id
    String name
    SXSSFSheet sheet

    Sheet(Integer id, String name) {
        this.id = id
        this.name = name ?: this.id
    }

    def row(Integer id = 0, @DelegatesTo(value = Row) Closure cl) {
        def delegate = new Row(id)
        def row = sheet.createRow(delegate.id)
        delegate.row = row
        cl.setDelegate(delegate)
        cl.setResolveStrategy(Closure.DELEGATE_FIRST)
        cl.call()
    }
}