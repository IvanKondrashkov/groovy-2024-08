package ru.otus.homework.builder

import groovy.transform.ToString
import org.apache.poi.xssf.streaming.SXSSFWorkbook

@ToString(includeFields = true)
class XlsxBuilder {
    String fileName

    XlsxBuilder(String fileName) {
        this.fileName = fileName
    }

    def builder(@DelegatesTo(value = Workbook) Closure cl) {
        def delegate = new Workbook(fileName, new SXSSFWorkbook(-1))
        cl.setDelegate(delegate)
        cl.setResolveStrategy(Closure.DELEGATE_FIRST)
        cl.call()
        return delegate
    }
}