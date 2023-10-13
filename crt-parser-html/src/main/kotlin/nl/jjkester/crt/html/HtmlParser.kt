/*
package nl.jjkester.crt.html

import nl.jjkester.crt.api.factory.NodeFactory
import nl.jjkester.crt.api.model.Container
import nl.jjkester.crt.api.model.Node
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

internal class HtmlParser {
    fun parse(htmlNode: String, nodeFactory: NodeFactory): Container
    {
        val html = Jsoup.parse(htmlNode)
        val elements = html.childNodes()
        return Container(
            children = parseChildren(elements, nodeFactory),
            metadata = null
        )
    }

    private fun parseChildren(
        elements: Elements,
        nodeFactory: NodeFactory
    ): MutableList<Node> {
        val mutList: MutableList<Node.Block> = mutableListOf()
        for (elem: Element in elements) {
            val tag = elem.tagName()
            val node = when (tag) {
                "li" -> nodeFactory.listItem(childParser.parseBlockChildren(node))
                "ul" -> nodeFactory.orderedList(childParser.parseListItemChildren(node))
                "p" -> nodeFactory.paragraph(childParser.parseSpanChildren(node))
                else -> throw (IllegalArgumentException("Unknown tag: $x"))
            }
            mutList.add(node)
        }
    }
}*/

package nl.jjkester.crt.markdown

import nl.jjkester.crt.api.annotations.InternalFactoryApi
import nl.jjkester.crt.api.factory.NodeFactory
import nl.jjkester.crt.api.model.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

@OptIn(InternalFactoryApi::class)
public class HtmlParser {
    public fun parse(htmlNode: String, nodeFactory: NodeFactory): Container {
        val html = Jsoup.parse(htmlNode)
        val elements = html.body().childNodes().filterIsInstance<Element>()
        return Container(
            children = elements.map { parseBlock(it, nodeFactory) },
            metadata = null
        )
    }

    private fun parseBlock(
        element: Element,
        nodeFactory: NodeFactory
    ): Node.Block {
        val tag = element.tagName()

        return when (tag) {
            "ul" -> parseUnorderedList(element, nodeFactory)
            "li" -> parseListItem(element, nodeFactory)
            "h" -> nodeFactory.heading(Heading.Level.One, listOf(nodeFactory.text(element.text())), null)
            "h2" -> nodeFactory.heading(Heading.Level.Two, listOf(nodeFactory.text(element.text())), null)
            "h3" -> nodeFactory.heading(Heading.Level.Three, listOf(nodeFactory.text(element.text())), null)
            "h4" -> nodeFactory.heading(Heading.Level.Four, listOf(nodeFactory.text(element.text())), null)
            "h5" -> nodeFactory.heading(Heading.Level.Five, listOf(nodeFactory.text(element.text())), null)
            "h6" -> nodeFactory.heading(Heading.Level.Six, listOf(nodeFactory.text(element.text())), null)
            else -> throw IllegalArgumentException("Unknown tag: $tag")
        }
    }

    private fun parseSpan(
        element: Element,
        nodeFactory: NodeFactory
    ): Node.Span {
        val tag = element.tagName()

        return when (tag) {
            "p" -> nodeFactory.text(element.text(), null)
            "a" -> parseLink(element, nodeFactory)
//            "li" -> parseListItem(element, nodeFactory)
            else -> throw IllegalArgumentException("Unknown tag: $tag")
        }
    }

    private fun parseUnorderedList(element: Element, nodeFactory: NodeFactory): UnorderedList {
        val listItems = mutableListOf<ListItem>()

        element.childNodes().filterIsInstance<Element>().forEach {
            listItems.add(ListItem(listOf(parseBlock(it, nodeFactory)), null))
        }

        return UnorderedList(listItems, null)
    }

    private fun parseListItem(element: Element, nodeFactory: NodeFactory): Container {
        val children = mutableListOf<Node.Block>()

        element.childNodes().filterIsInstance<Element>().forEach {
            children.add(nodeFactory.paragraph(listOf(parseSpan(it, nodeFactory)), null))
        }

        return nodeFactory.container(children.toList())
    }

    private fun parseLink(element: Element, nodeFactory: NodeFactory): Node.Span {
        val href = element.attr("href")

        val children = mutableListOf<Node.Span>()

        element.childNodes().filterIsInstance<Element>().forEach {
            children.add(parseSpan(it, nodeFactory))
        }

        children.add(nodeFactory.text(element.text()))

        return nodeFactory.link(
            Link.Destination(href),
            children.toList(),
            null
        )
    }
}
