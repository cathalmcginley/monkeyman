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

package nl.flotsam.monkeyman.decorator.permalink

import nl.flotsam.monkeyman.decorator.ResourceDecoration
import nl.flotsam.monkeyman.Resource
import com.ibm.icu.text.Transliterator
import org.apache.commons.io.FilenameUtils._
import annotation.tailrec
import nl.flotsam.monkeyman.MonkeymanOptions

case class PermalinkDecoration(resource: Resource) extends ResourceDecoration(resource) {

  override def path = {
    if (resource.options.titleAsPath) {
      /* if resource has title "My New Page" return "my-new-page.html" */
      resource.title.map {
        str =>
          getPath(resource.path) +
            permalinkName(str, 60) +
            "." + getExtension(resource.path)
      }.getOrElse(resource.path)
    } else {
      /* default: base generated path on filename of resource source file */
      getPath(resource.path) +
        permalinkName(getBaseName(resource.id), 60) +
        "." + getExtension(resource.path)
    }
  }

  /**
   * Turns the String into something that wouldn't suck being part of a URI.
   *
   * Reduces whitespace to dashes, turns everything to lowercase, removes non letters or digits.
   */
  private def permalinkName(str: String, maxchars: Int = Int.MaxValue): String = {
    val cleaned = str.replace("&", " and ").replace("/", " or ")
    val parts = for {
      word <- cleaned.split("[- :;,.\t\n]+")
    } yield {
      withoutAccents(word).filter(_.isLetterOrDigit).toLowerCase
    }
    join(parts, "-", maxchars)
  }

  /**
   * Strips off the accents from characters in the String, etc.
   */
  private def withoutAccents(str: String) = {
    val transliterator = Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC")
    transliterator.transliterate(str)
  }

  /**
   * Joins the Strings in a [[scala.collection.immutable.Seq]] until a certain limit is reached.
   *
   * @param parts The Strings to be joined.
   * @param separator The separator to be used.
   * @param maxchars The maximum number of characters to be included.
   */
  private def join(parts: Seq[String], separator: String, maxchars: Int = Int.MaxValue) = {
    @tailrec
    def join(parts: Seq[String], separator: String, maxchars: Int, builder: StringBuilder): String =
      parts.headOption match {
        case Some(part) =>
          if (separator.size + part.size + builder.size > maxchars) builder.toString
          else {
            builder.append(separator).append(part)
            join(parts.tail, separator, maxchars, builder)
          }
        case None =>
          builder.toString
      }
    parts.headOption match {
      case Some(part) if (part.size <= maxchars) => join(parts.tail, separator, maxchars, new StringBuilder(part))
      case _ => ""
    }

  }

}


