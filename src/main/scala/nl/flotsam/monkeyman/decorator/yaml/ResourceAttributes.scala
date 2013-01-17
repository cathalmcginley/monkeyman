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

package nl.flotsam.monkeyman.decorator.yaml

import org.joda.time.LocalDateTime
import nl.flotsam.monkeyman.menu.MenuLink
import nl.flotsam.monkeyman.MonkeymanOptions
import nl.flotsam.monkeyman.Minidoc

case class ResourceAttributes(var title: Option[String] = None,
  var published: Boolean = true,
  var tags: Set[String] = Set(),
  var pubDateTime: Option[LocalDateTime] = None,  
  var options: MonkeymanOptions = new MonkeymanOptions(),
  var menuLink: Option[MenuLink] = None,
  var info: Map[String, String] = Map.empty,
  var minidoc: Map[String, Minidoc] = Map.empty) {
    
 
  def withTitle(t: String) = copy(title = Some(t))
  def withPublished(b: Boolean) = copy(published = b)  
  def withTags(t: Set[String]) = copy(tags = tags ++ t)
  def withPubDateTime(pdt: LocalDateTime) = copy(pubDateTime = Some(pdt))
  
  def withOptions(o: MonkeymanOptions) = copy(options = o) // TODO combine options
  def withMenuLink(ml: MenuLink) = copy(menuLink = Some(ml))
  def withInfo(i: Map[String, String]) = copy(info = info ++ i)
  def withMinidoc(m: Map[String, Minidoc]) = copy(minidoc = minidoc ++ m)
}
