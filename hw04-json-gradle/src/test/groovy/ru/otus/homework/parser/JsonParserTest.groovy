package ru.otus.homework.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import groovy.xml.XmlSlurper

class JsonParserTest {
    private File html
    private File xml
    private final String url = 'https://raw.githubusercontent.com/IvanKondrashkov/groovy-2024-08/main/hw04-json-gradle/src/main/resources/test.json'

    @BeforeEach
    void init() {
        html = new File('result.html')
        xml = new File('result.xml')
        JsonParser.jsonToHtml(url, html)
        JsonParser.jsonToXml(url, xml)
    }

    @AfterEach
    void tearDown() {
        html.delete()
        xml.delete()
    }

    @Test
    void jsonToHtml() {
        def text = html.text
        def slurper = new XmlSlurper().parseText(text)

        assert slurper.name() == 'div'
        assert slurper.div.@id == 'employee'
        assert slurper.div.p[0] == 'name'
        assert slurper.div.p[1] == 'age'
        assert slurper.div.p[2] == 'secretIdentity'
        assert slurper.div.ul.@id == 'powers'
        assert slurper.div.ul.li[0] == 'power'
    }

    @Test
    void jsonToXml() {
        def text = xml.text
        def slurper = new XmlSlurper().parseText(text)

        assert slurper.name() == 'div'
        assert slurper.div.@id == 'employee'
        assert slurper.div.p[0] == 'name'
        assert slurper.div.p[1] == 'age'
        assert slurper.div.p[2] == 'secretIdentity'
        assert slurper.div.ul.@id == 'powers'
        assert slurper.div.ul.li[0] == 'power'
    }
}