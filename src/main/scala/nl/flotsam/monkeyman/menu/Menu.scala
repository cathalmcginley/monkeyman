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
package nl.flotsam.monkeyman.menu

import scala.collection.mutable.HashMap

/**
 * Represents the menu at a particular directory. The top-level
 * site menu is named "main" and has depth 0. Submenus are automatically
 * defined whenever a new IndexLink is defined in a subdirectory. Their
 * name is the directory path name, and their depth is the number of
 * subdirectories.
 * 
 * For example, the menu for development/index.page is 
 * Menu<"development", 1>. The menu for docs/guide/index.page
 * is Menu<"docs/guide", 2>.
 */
class Menu(val name: String, val depth: Int) {

  override def toString(): String = "Menu<\"%s\", %d>".format(name, depth)
  
}

/**
 * The top-level menu for the whole site.
 */
object MainMenu extends Menu("main", 0)

object Menu {
  
  private val menus = new HashMap[String, Menu]()
  menus += ("main" -> MainMenu)
  
  def getMenu(name: String) = {
    menus(name)
  }
  
  /**
   * Finds (or creates) the menu for the page at a given path.
   * Anything on the top-level (e.g. index.page, about.page, faq.page)
   * will return MainMenu. 
   */  
  def forPath(path: String): Menu = {
    val parts = path.split("/")
    val depth = parts.size - 1
    if (depth == 0) {
      MainMenu
    } else {     
      val menuName = path.substring(0, path.lastIndexOf("/"))
      if (menus.contains(menuName)) {
        menus(menuName)
      } else {
        val menu = new Menu(menuName, depth)
        menus += (menuName -> menu)
        menu
      }
    }
  }
}
