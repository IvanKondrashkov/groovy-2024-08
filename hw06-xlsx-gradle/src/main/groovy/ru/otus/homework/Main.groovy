package ru.otus.homework

import ru.otus.homework.builder.Row
import ru.otus.homework.builder.XlsxBuilder

static void main(String[] args) {
  def rows = (0..99).collect {
    new Row(it)
  }

  new XlsxBuilder("test.xlsx").builder {
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
}