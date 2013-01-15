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
import java.util.{ HashMap => JMap }
import scala.collection.JavaConversions._
import nl.flotsam.monkeyman.MonkeymanOptions

private[yaml] class ResourceAttributesBean extends Logging {

  @BeanProperty var title: String = null
  @BeanProperty var published: Boolean = true // LocalDateTime
  @BeanProperty var options: String = null // Array[String] = null
  @BeanProperty var tags: String = null // Array[String] = null
  @BeanProperty var pubDateTime: String = null // LocalDateTime
  @BeanProperty var menu: MenuBean = NotInMenu
  @BeanProperty var info: JMap[String, String] = new JMap[String, String]()

  private val DateAndTimePattern = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm")
  private val DatePattern = DateTimeFormat.forPattern("yyyy-MM-dd")

  private def parseDateTime(str: String): Option[LocalDateTime] = {
    val patterns = Stream(DateAndTimePattern, DatePattern)
    def tryParse(pattern: DateTimeFormatter) =
      allCatch.opt(pattern.parseLocalDateTime(str))

    patterns.flatMap(tryParse).headOption
  }

  def getAttributes(resource: Resource) = {

    val attributeBuilders: Seq[(ResourceAttributes => ResourceAttributes)] = Seq(
      (attr) => 
        if (title == null) { attr }
        else { attr.withTitle(title) },
      (attr) => 
        attr.withPublished(published),       
      (attr) =>
        if (pubDateTime == null) { attr }
        else {
          parseDateTime(pubDateTime) match {
            case None => {
              warn("Failed to parse pubDateTime in %s".format(resource.path))
              attr
            }
            case Some(dateTime) => {
              attr.withPubDateTime(dateTime)            
            }
          }          
        },          
      (attr) => 
        if (tags == null) { attr }
        else { attr.withTags(tags.split(",").map(_.trim).toSet) },
      (attr) => 
        if (options == null) { attr }
        else { attr.withOptions(MonkeymanOptions.parse(options.split(",").map(_.trim))) },
      (attr) => 
        if (!menu.display) { attr }
        else { attr.withMenuLink(menu.getMenuLink(resource)) },
      (attr) => 
        if (!info.isEmpty) { attr }
        else { attr.withInfo(info.toMap) }        
    )
    
     attributeBuilders.foldLeft(ResourceAttributes())((attr, build) => build(attr))
  }
//
//    println("attr " + attr.title + " | " + attr.tags + " | " + attr.options)

    //if (title != null) attribs.title = Some(title)
    //attribs.published = published

//    if (options != null) {
//      attribs.options = MonkeymanOptions.parse(options.split(",").map(_.trim))
//    }

//    if (tags != null) {
//      attribs.tags = tags.split(",").map(_.trim).toSet
//    }
//
//    if (pubDateTime != null) {
//      val dateTime = parseDateTime(pubDateTime)
//      if (dateTime.isDefined) {
//        attribs.pubDateTime = dateTime
//      } else {
//        warn("Failed to parse pubDateTime in %s".format(resource.path))
//      }
//    }

//    if (menu.display) {
//      attribs.menuLink = Some(menu.getMenuLink(resource))
//    }
//    if (!info.isEmpty()) {
//      attribs.info = info.toMap
//    }

    
//  }

}
