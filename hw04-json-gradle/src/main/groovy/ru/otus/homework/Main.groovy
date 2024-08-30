package ru.otus.homework

import ru.otus.homework.parser.JsonParser

static void main(String[] args) {
  final String url = 'https://raw.githubusercontent.com/IvanKondrashkov/groovy-2024-08/main/hw04-json-gradle/src/main/resources/test.json'
  JsonParser.jsonToHtml(url, new File('result.html'))
  JsonParser.jsonToXml(url, new File('result.xml'))
}