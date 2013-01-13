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

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.util.Logging
import org.apache.commons.io.FilenameUtils

object MenuBuilder extends Logging {

  /**
   * Orders menu links by menu depth, menu name and menu link order.
   * This means an order like:
   *   IndexLink<Menu<"main", 0>, "index.page", 0>
   *   IndexLink<Menu<"dev", 1>, "dev/index.page", 1>
   *   IndexLink<Menu<"dev/style", 2>, "dev/style/index.page", 1>
   *   IndexLink<Menu<"doc", 1>, "doc/index.page", 2> 
   *   IndexLink<Menu<"doc", 1>, "doc/isbn.page", 1>
   *   Link<Menu<"main", 0>, "about.page", 3>
   *   
   * From this, a correct menu tree can be built without re-ordering.
   */
  implicit object MenuLinkOrdering extends Ordering[MenuLink] {
    def compare(x: MenuLink, y: MenuLink) = {
      if (x.menu.depth != y.menu.depth) {
        x.menu.depth.compareTo(y.menu.depth)
      } else {
        if (x.menu.name != y.menu.name) {
          x.menu.name.compareTo(y.menu.name)
        } else {
          x.order.compareTo(y.order)
        }
      }
    }
  }

  def build(resources: Seq[Resource]): Option[MenuLinkNode] = {
    val map = new HashMap[String, MenuRoot]()
    buildTree(resources, map)
  }

  def build(resources: Seq[Resource], currentPath: String): Option[MenuLinkNode] = {
    val map = new HashMap[String, MenuRoot]()
    val mainMenu = buildTree(resources, map)

    // TODO cache this menu tree & create deep clones
    // this would also need us to create a new map
    // so that "dev/books" points to the right node etc.
    
    mainMenu match {
      case None => { None }
      case Some(menuNode) => {
        val coolUriPath = FilenameUtils.removeExtension(currentPath)      
    	menuNode.currentPageIs(coolUriPath, map)
    	mainMenu
      }
    }
  }

  private def buildTree(resources: Seq[Resource], map: HashMap[String, MenuRoot]): Option[MenuLinkNode] = {

    var mainMenu: Option[MenuRoot] = None
    for (res <- resources.filter(r => r.menuLink.isDefined && r.menuLink.get.display).sortBy(_.menuLink)) {
      val menuLink = res.menuLink.get
      Menu.forPath(res.path)
      if (menuLink.isIndex) {
        val root = new MenuRoot(res)
        if (root.isMainMenu) {
          mainMenu = Some(root)
        }
        if (map.contains(menuLink.menu.name)) {
          // attach this to old menu
          map(menuLink.menu.name).add(root)
        }
        for (newMenu <- menuLink.subMenu) {
          map += (newMenu.name -> root)
        }
      } else {
        if (map.contains(menuLink.menu.name)) {
          map(menuLink.menu.name).addLeaf(res)
        } else {
          warn("cannot find index for menu '" + menuLink.menu + "'")
        }
      }
    }    
    mainMenu
  }

}
