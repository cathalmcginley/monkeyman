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

package nl.flotsam.monkeyman.menu

/**
 * A link to a page in a menu. A link may also be the index for
 * a submenu. When the submenu is selected, the subMenuLabel
 * is used, so the link "Development" can be displayed with the label
 * "Resources" within a submenu. The submenu can also have a title. 
 */
abstract class MenuLink(val label: String, val order: Int = 100, val menu: Menu = MainMenu) {

  def isIndex = false
  var display = true
  
  def subMenu: Option[Menu] = None
  def subMenuName: Option[String] = None
  def subMenuLabel: Option[String] = None 
  
}

/**
 * A link to a leaf page (i.e. one that is not the index for a submenu).
 */
class Link(label: String, order: Int = 100, menu: Menu = MainMenu) extends MenuLink(label, order, menu)

/**
 * A link to a page which is the index page of a submenu.
 */
class IndexLink(label: String, order: Int, menu: Menu,
  val newMenuName: String, val newLabel: String, val newMenu: Menu) extends MenuLink(label, order, menu) {

  override def subMenu = Some(newMenu)
  override def subMenuName = Some(newMenuName)
  override def subMenuLabel = Some(newLabel)
  
  override val isIndex = true
}

object NonDisplayedMenuLink extends MenuLink("#", -1, MainMenu) {
  display = false
}
