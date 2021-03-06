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

package nl.flotsam.monkeyman.decorator.yaml

import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.util.Closeables._
import org.apache.commons.io.IOUtils
import nl.flotsam.monkeyman.decorator.ResourceDecoration
import nl.flotsam.monkeyman.util.Logging
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}
import org.joda.time.LocalDateTime
import scala.util.control.Exception._

/**
 * YAML front matter extraction.
 * Uses YamlBeans to extract data from the header of a page.
 * 
 * The structure corresponds to ResourceAttributeBean, and cannot
 * simply be free-form.
 */
class YamlFrontmatterDecoration(resource: Resource) extends ResourceDecoration(resource) with Logging {


  lazy val (attributes, content) = {
    allCatch.opt(using(resource.open)(IOUtils.toString(_, "UTF-8"))) match {
      case Some(str) =>
        extractAttributes(str)
      case None => 
        (new ResourceAttributes(), None)
    }
  }

  override def title = 
    attributes.title.orElse(resource.title)
  
  override def options =
    resource.options.add(attributes.options)
   
  override def menuLink =
    attributes.menuLink
    
  override def info = 
    resource.info ++ attributes.info  

  override def minidoc = 
    resource.minidoc ++ attributes.minidoc  
    
    
  override def published = attributes.published 

  override def tags =
    resource.tags ++ attributes.tags    

  override def pubDateTime = attributes.pubDateTime.getOrElse(resource.pubDateTime)

  override def open = {
    if (content.isDefined) IOUtils.toInputStream(content.get, "UTF-8")
    else resource.open
  }

  private def extractAttributes(str: String): (ResourceAttributes, Option[String]) = {
    val lines = str.lines.toList
    lines.headOption match {
      case Some(line) if line.trim == "---" =>
        val (settings, remainder) = lines.tail.span(_ != "---")
        val extractor = new YamlExtractor(resource, settings.mkString("\n"))        
        (extractor.attributes, Some(remainder.tail.mkString("\n")))
      case _ =>
        (new ResourceAttributes(), Some(str))
    }
  }

  override def toString = this.getClass.getName + "(" + resource.toString + ")"
  
}
