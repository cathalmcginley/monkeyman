/*
 * This file is part of Monkeyman, a static web site generator.
 *
 * Copyright (C) 2012 Wilfred Springer
 *
 * Monkeyman is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Monkeyman is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Monkeyman; see the file COPYING. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package nl.flotsam.monkeyman.decorator.markdown

import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.decorator.ResourceDecoration
import nl.flotsam.monkeyman.util.Closeables._
import org.apache.commons.io.{FilenameUtils, IOUtils}
import org.pegdown.ast.{TextNode, HeaderNode}
import org.pegdown.{LinkRenderer, ToHtmlSerializer, PegDownProcessor}

case class MarkdownDecoration(resource: Resource)
  extends ResourceDecoration(resource) {

  lazy val (extractedTitle, html) = {
    using(resource.open) {
      in =>
        val markdown = IOUtils.toString(in, "UTF-8")
        val processor = new PegDownProcessor
        val rootNode = processor.parseMarkdown(markdown.toCharArray)
        val visitor = new TitleExtractingToHtmlSerializer(new LinkRenderer)
        val html = visitor.toHtml(rootNode)
        val title = visitor.title
        (title, html)
    }
  }

  override def title = resource.title.orElse(extractedTitle)

  override val contentType = "text/x-html-fragment"

  override lazy val path = FilenameUtils.removeExtension(resource.path) + ".frag"

  override def open = IOUtils.toInputStream(html)

  override def asHtmlFragment = Some(html)

  class TitleExtractingToHtmlSerializer(linkRenderer: LinkRenderer) extends ToHtmlSerializer(linkRenderer) {
    var inheader = false
    var title: Option[String] = None

    override def visit(node: HeaderNode) {
      if (title.isEmpty) {
        inheader = true
        visitChildren(node)
        inheader = false
      } else {
        super.visit(node)
      }
    }

    override def visit(node: TextNode) {
      if (inheader && title.isEmpty) {
        title = Some(node.getText)
      } else {
        super.visit(node)
      }
    }
  }

}
