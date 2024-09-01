package ru.otus.homework.builder

import groovy.transform.ToString
import org.apache.poi.xssf.streaming.SXSSFRow

@ToString(includeFields = true)
class Row {
    Integer id
    List<Cell> cells = []
    SXSSFRow row

    Row(Integer id) {
        this.id = id
    }

    def cell(Integer id = 0, @DelegatesTo(value = Cell) Closure cl) {
        def delegate = new Cell(id)
        cl.setDelegate(delegate)
        cl.setResolveStrategy(Closure.DELEGATE_FIRST)
        def style = cl.call()

        def cell = row.createCell(delegate.id)
        delegate.cell = cell
        cell.setCellStyle(style)
        cell.setCellValue(delegate.value)
        cells << delegate
    }
}