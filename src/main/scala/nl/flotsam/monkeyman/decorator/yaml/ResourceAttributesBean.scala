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
package nl.flotsam.monkeyman.decorator.yaml

import scala.reflect.BeanProperty
import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.util.Logging
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import scala.util.control.Exception._

private[yaml] class ResourceAttributesBean extends Logging {

  @BeanProperty var title: String = null
  @BeanProperty var published: Boolean = true // LocalDateTime
  @BeanProperty var options: String = null // Array[String] = null
  @BeanProperty var tags: String = null // Array[String] = null
  @BeanProperty var pubDateTime: String = null // LocalDateTime
  @BeanProperty var menu: MenuBean = NotInMenu

  private val DateAndTimePattern = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm")
  private val DatePattern = DateTimeFormat.forPattern("yyyy-MM-dd")

  private def parseDateTime(str: String): Option[LocalDateTime] = {
    val patterns = Stream(DateAndTimePattern, DatePattern)
    def tryParse(pattern: DateTimeFormatter) =
      allCatch.opt(pattern.parseLocalDateTime(str))

    patterns.flatMap(tryParse).headOption
  }

  def getAttributes(resource: Resource) = {
    val attribs = new ResourceAttributes()
    if (title != null) attribs.title = Some(title)
    attribs.published = published

    if (options != null) {
      attribs.options = options.split(",").map(_.trim).toSet
    }

    if (tags != null) {
      attribs.tags = tags.split(",").map(_.trim).toSet
    }

    if (pubDateTime != null) {
      val dateTime = parseDateTime(pubDateTime)
      if (dateTime.isDefined) {
        attribs.pubDateTime = dateTime
      } else {
          warn("Failed to parse pubDateTime in %s".format(resource.path))
      }
    }

    if (menu.display) {
      attribs.menuLink = Some(menu.getMenuLink(resource))
    }
    
    attribs
  }

}
