package ru.otus.homework.builder

import groovy.transform.ToString
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.streaming.SXSSFWorkbook

@ToString(includeFields = true)
class Workbook {
    String fileName
    static SXSSFWorkbook workbook

    Workbook(String fileName, SXSSFWorkbook workbook) {
        this.fileName = fileName
        this.workbook = workbook
    }

    def build(@DelegatesTo(value = Workbook) Closure cl) {
        cl.setDelegate(this)
        cl.setResolveStrategy(Closure.DELEGATE_FIRST)
        cl.call()

        def file = new File(validateFileName(fileName))
        file.withOutputStream { stream ->
            workbook.write(stream)
        }
        return file
    }

    def sheet(Integer id = 0, String name = null, @DelegatesTo(value = Sheet) Closure cl) {
        def delegate = new Sheet(id, name)
        def sheet = workbook.createSheet(delegate.name)
        delegate.sheet = sheet
        cl.setDelegate(delegate)
        cl.setResolveStrategy(Closure.DELEGATE_FIRST)
        cl.call()
    }

    static def createCellStyle(String color, String backgroundColor) {
        CellStyle cellStyle = workbook.createCellStyle()

        Font font = workbook.createFont()
        font.setColor(IndexedColors.valueOf(color.toUpperCase()).getIndex())
        cellStyle.setFont(font)

        cellStyle.setFillBackgroundColor(IndexedColors.valueOf(backgroundColor.toUpperCase()).getIndex())
        cellStyle.setFillPattern(FillPatternType.FINE_DOTS)
        cellStyle.setAlignment(HorizontalAlignment.CENTER)
        cellStyle.setBorderTop(BorderStyle.THIN)
        cellStyle.setBorderBottom(BorderStyle.THIN)
        cellStyle.setBorderRight(BorderStyle.THIN)
        cellStyle.setBorderLeft(BorderStyle.THIN)
        return cellStyle
    }

    static def validateFileName(String fileName) {
        if (fileName.split('\\.')[-1] == 'xlsx') {
            return fileName
        }
        return "${fileName}.xlsx".toString()
    }
}