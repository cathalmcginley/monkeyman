/*
 * Monkeyman static web site generator
 * Copyright (C) 2012  Wilfred Springer	
 * Copyright (C) 2013  Cathal Mc Ginley
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package nl.flotsam.monkeyman.decorator.jsoup

import org.jsoup.nodes.Document
import org.jsoup.nodes.Comment
import org.jsoup.nodes.Node
import org.jsoup.parser.Tag
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.decorator.yaml.ResourceAttributes
import scala.collection.JavaConversions._
import nl.flotsam.monkeyman.Minidoc

object ArticleJumpTransformer extends JsoupTransformer("ArticleJump") {

  def isApplicableTo(resource: Resource): Boolean = {
    resource.options.articleJump
  }

  def transform(doc: Document, resource: Resource, attr: ResourceAttributes) = {
    val body = doc.body()
    val strongs: Elements = body.select("p > strong")

    val jumpElements = for {
      strong <- strongs
      parentParagraph = strong.parent()
      if (strong.text() == "JUMP" && parentParagraph.children().size() == 1)
    } yield { parentParagraph }
    

    jumpElements.headOption match {
      case None => attr
      case Some(jumpElement) => {
        jumpElement.previousElementSibling().attr("id", "before-the-jump")
        jumpElement.nextElementSibling().attr("id", "after-the-jump")
        
        val contents: Elements = doc.select("body > *")        
        val nodesBefore = contents.iterator().takeWhile(node => !node.equals(jumpElement))
        
        val beforeDiv = new Element(Tag.valueOf("div"), "")
        beforeDiv.addClass("before-jump")
        for (node <- nodesBefore) {
          beforeDiv.appendChild(node)
        }
        jumpElement.replaceWith(new Comment("jump", ""))
        // TODO attr.withMinidoc(minidocJMap.toMap)
        attr.withMinidoc(Map("before-the-jump" -> Minidoc.fromHtml("before-the-jump", beforeDiv.html())))
        }
      }
  }

}