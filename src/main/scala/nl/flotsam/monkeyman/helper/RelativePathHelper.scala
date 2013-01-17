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

package nl.flotsam.monkeyman.helper

import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.util.Logging

class RelativePathHelper(private val resource: Resource) extends Logging {

  /**
   * Strip .html from links, and link to somepath/ rather than somepath/index.html.
   * Note, it should not strip all suffixes, such as for images and style sheets.
   */
  def coolUrls: Boolean = false

  def to(toPath: String): String = {
    pathFromTo(resource.path, toPath)
  } 
  
  private[helper] def pathFromTo(fromPath: String, toPath: String): String = {
    val (fromParts, toParts) = stripCommonPrefix(fromPath.split("/"), toPath.split("/"))
    if (fromParts.size == 1) {
      toParts.mkString("/")
    } else {
      val parentPathCount = fromParts.size - 1
      (Seq.fill(parentPathCount)("..") ++ toParts).mkString("/")
    }
  }
  
  private def stripCommonPrefix(fromParts: Seq[String], toParts: Seq[String]): Pair[Seq[String], Seq[String]] = {
    val common = commonPrefix(fromParts, toParts)
    val prefixLength = common.size

    Pair(fromParts.drop(prefixLength), toParts.drop(prefixLength))
  }

  private def commonPrefix(fromParts: Seq[String], toParts: Seq[String]): Seq[String] = {
    if (fromParts.head == toParts.head) {
      if (fromParts.size == 1 && toParts.size == 1) {
    	// happens in a link from any path to itself
        Seq()
      } else {
        Seq(fromParts.head) ++ commonPrefix(fromParts.tail, toParts.tail)
      }
    } else {
      Seq()
    }
  }

}

