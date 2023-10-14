package nl.jjkester.crt.html

import nl.jjkester.crt.api.factory.DefaultNodeFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HtmlParserTest {

    /*    @Test
        fun parseHtml() {
            val doc: Document = Jsoup.parse(
                "        <ul id=\"ProjectSubmenu\">\n" +
                "            <li><a href=\"/projects/markdown/\" title=\"Markdown Project Page\">Main</a></li>\n" +
                "            <li><a href=\"/projects/markdown/basics\" title=\"Markdown Basics\">Basics</a></li>\n" +
                "            <li><a class=\"selected\" title=\"Markdown Syntax Documentation\">Syntax</a></li>\n" +
                "            <li><a href=\"/projects/markdown/license\" title=\"Pricing and License Information\">License</a></li>\n" +
                "            <li><a href=\"/projects/markdown/dingus\" title=\"Online Markdown Web Form\">Dingus</a></li>\n" +
                "        </ul>"
            )
            val elements: Elements = doc.childNodes()
            for (elem: Element in elements) {
                val x = elem.tagName()
                val text = elem.text()
                val href = elem.attr("href")

                if (elem.hasClass("selected")) {
                    println("- $text")
                } else {
                    println("- [$text]($href)")
                }
            }
        }*/

    @Test
    fun `wat een test`() {
//        val doc = """
//            <ul>
//                <li>
//                    Foo
//                    <p>hoi</p>
//                    <p>hoi2</p>
//                </li>
//                <li>
//                    <a href="https://chat.bing.com">hoi3</a>
//                    <p>hoi4</p>
//                </li>
//                <li>
//                    <a href="https://infosupport.com">hoi5</a>
//                    <a href="https://google.com">hoi6</a>
//                </li>
//            </ul>
//        """.trimIndent()

        val doc = """
            
        """.trimIndent()

        val htmlParser = HtmlParser(nodeFactory = DefaultNodeFactory)

        val result = htmlParser.parse(doc)

        println(result)

        assertEquals(1, 1)
    }
}