/*
 * This file is part of Monkeyman, a static web site generator.
 *
 * Copyright (C) 2012 Wilfred Springer
 * Copyright (C) 2013 Cathal Mc Ginley
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

package nl.flotsam.monkeyman.decorator.jsoup

import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.decorator.ResourceDecoration
import nl.flotsam.monkeyman.util.Closeables._
import java.io.StringWriter
import java.io.PrintWriter
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import nl.flotsam.monkeyman.decorator.yaml.ResourceAttributes

case class JsoupDecoration(resource: Resource) extends ResourceDecoration(resource) {

  val transformers: Seq[JsoupTransformer] = Seq(ArticleJumpTransformer)

  lazy val (modifiedAttributes: ResourceAttributes, html: String) = {
    using(resource.open) {
      in =>
        // do work of open() here, and cache it
        val writer = new StringWriter
        val printWriter = new PrintWriter(writer)

        val bodyFragment = scala.io.Source.fromInputStream(in).mkString("")

        val doc = Jsoup.parseBodyFragment(bodyFragment)
        val body = doc.body()

        val initialAttributes = new ResourceAttributes()
        // TODO fill initialAttributes from underlying resource attributes
        val newAttributes = transformers.foldLeft(initialAttributes)((attr, trans) => trans.transform(doc, resource, attr))

        (newAttributes, body.html())
    }
  }

  override def open = IOUtils.toInputStream(html)

  override def asHtmlFragment = Some(html)

  override def title = {
    modifiedAttributes.title match {
      case None => resource.title
      case Some(title) => modifiedAttributes.title
    }
  }
  
  override def info = {
    resource.info ++ modifiedAttributes.info
  }
  
  override def minidoc = {
    resource.minidoc ++ modifiedAttributes.minidoc
  }


}