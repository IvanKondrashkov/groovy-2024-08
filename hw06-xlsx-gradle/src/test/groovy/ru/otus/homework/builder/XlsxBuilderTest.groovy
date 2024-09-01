package ru.otus.homework.builder

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class XlsxBuilderTest {
    private File file
    private XSSFWorkbook workbook

    @BeforeEach
    void init() {
        def rows = (0..99).collect {
            new Row(it)
        }

        file = new XlsxBuilder("test.xlsx").builder {
            sheet(0) {
                rows.each {
                    row(it.id) {
                        cell(0) {
                            value = new Random().nextInt(100)
                            style {
                                color = "white"
                                backgroundColor = "red"
                            }
                        }
                        cell() {
                            id = 1
                            value = "test"
                            style {
                                color = "white"
                                backgroundColor = "red"
                            }
                        }
                        cell() {
                            id = 2
                            value = new Random().nextBoolean()
                            style {
                                color = "white"
                                backgroundColor = "red"
                            }
                        }
                    }
                }
            }
        }.build {}
        workbook = new XSSFWorkbook(file)
    }

    @AfterEach
    void tearDown() {
        file.delete()
    }

    @Test
    void checkXlsxBuilder() {
        def sheet = workbook.getSheet('0')
        def rowNum = sheet.getLastRowNum()

        assert sheet != null
        assert rowNum == 99
    }
}