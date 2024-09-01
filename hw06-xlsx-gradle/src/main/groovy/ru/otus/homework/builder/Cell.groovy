package ru.otus.homework.builder

import groovy.transform.ToString
import org.apache.poi.xssf.streaming.SXSSFCell

@ToString(includeFields = true)
class Cell {
    Integer id
    Object value
    SXSSFCell cell

    Cell(Integer id) {
        this.id = id
    }

    def style(@DelegatesTo(value = Style) Closure cl) {
        def delegate = new Style()
        cl.setDelegate(delegate)
        cl.setResolveStrategy(Closure.DELEGATE_FIRST)
        cl.call()
        return Workbook.createCellStyle(delegate.color, delegate.backgroundColor)
    }
}