package nl.jjkester.crt.html

import nl.jjkester.crt.api.annotations.InternalFactoryApi
import nl.jjkester.crt.api.factory.NodeFactory
import nl.jjkester.crt.api.model.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.nodes.Node as JSoupNode

@OptIn(InternalFactoryApi::class)
public class HtmlParser(private val nodeFactory: NodeFactory) {

    public fun parse(htmlNode: String): Container {
        val html = Jsoup.parse(htmlNode)
        val elements = html.body().childNodes()

        val result = elements.mapNotNull { parseNode(it) }.map {
            when (it) {
                is Node.Span -> nodeFactory.paragraph(listOf(it))
                is Node.Block -> it
            }
        }

        return Container(
            children = result,
            metadata = null
        )
    }

    private fun parseNode(
        node: JSoupNode,
    ): Node? {
        return when (node) {
            is TextNode -> parseTextNode(node)
            is Element -> parseElement(node)
            else -> parseRaw(node)
        }
    }

    private fun parseRaw(node: JSoupNode): Node.Span {
        return nodeFactory.text(node.outerHtml())
    }

    private fun parseTextNode(textNode: TextNode): Node.Block? {
        if (textNode.wholeText.isNullOrBlank()) {
            return null
        }

        return nodeFactory.paragraph(listOf(nodeFactory.text(textNode.wholeText)))
    }

    private fun parseElement(element: Element): Node {
        if (element.tagName().startsWith("h")) {
            return parseHeader(element)
        }

        return when (element.tagName()) {
            "ul" -> parseUnorderedList(element)
            "li" -> parseListItem(element)
            "p" -> parseParagraph(element)
            "a" -> parseLink(element)
            else -> parseRaw(element)
        }
    }

    private fun parseParagraph(element: Element): Node.Block {
        val spans = mutableListOf<Node.Span>()

        element.childNodes().forEach {
            if (it is TextNode && !it.wholeText.isNullOrBlank()) {
                spans.add(nodeFactory.text(it.wholeText))
            }
        }

        return nodeFactory.paragraph(spans, null)
    }

    private fun parseUnorderedList(element: Element): UnorderedList {
        val listItems = mutableListOf<ListItem>()

        element.childNodes().filterIsInstance<Element>().forEach {
            listItems.add(ListItem(listOf(parseElement(it)).filterIsInstance<Node.Block>(), null))
        }

        return UnorderedList(listItems, null)
    }

    private fun parseListItem(element: Element): Container {
        val children = mutableListOf<Node.Block>()

        element.childNodes().filterIsInstance<Element>().forEach {
            val result = parseElement(it)

            if (result is Node.Block) {
                children.add(result)
            } else if (result is Node.Span) {
                children.add(nodeFactory.paragraph(listOf(result), null))
            }
        }

        return nodeFactory.container(children.toList())
    }

    private fun parseLink(element: Element): Node.Span {
        val href = element.attr("href")

        val children = mutableListOf<Node.Span>()

        element.childNodes().filterIsInstance<Element>().forEach {
            val result = parseElement(it)

            if (result is Node.Span) {
                children.add(result)
            }
        }

        children.add(nodeFactory.text(element.text()))

        return nodeFactory.link(
            Link.Destination(href),
            children.toList(),
            null
        )
    }

    private fun parseHeader(element: Element): Node.Block {
        val spans = mutableListOf<Node.Span>()

        element.childNodes().forEach {
            if (it is TextNode) {
                spans.add(nodeFactory.text(it.wholeText))
                return@forEach
            }

            if (it !is Element) return@forEach

            val childElement = parseElement(it)

            if (childElement is Node.Span) spans.add(childElement)
        }


        return nodeFactory.heading(
            headingMap.get(element.tagName())!!,
            spans.toList()
        )
    }

    private companion object {
        val headingMap = mapOf(
            Pair("h1", Heading.Level.One),
            Pair("h2", Heading.Level.Two),
            Pair("h3", Heading.Level.Three),
            Pair("h4", Heading.Level.Four),
            Pair("h5", Heading.Level.Five),
            Pair("h6", Heading.Level.Six),
        )
    }
}
