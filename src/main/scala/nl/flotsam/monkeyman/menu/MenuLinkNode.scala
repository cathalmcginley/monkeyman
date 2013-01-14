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
import org.apache.commons.io.FilenameUtils

abstract class MenuLinkNode(val resource: Resource) {

  def link: MenuLink = {
    resource.menuLink match {
      case None => NonDisplayedMenuLink
      case Some(menuLink) => menuLink
    }
  }

  protected val idealizedPath = FilenameUtils.removeExtension(resource.path)

  def children: Seq[MenuLinkNode]

  def isIndex: Boolean

  def isOpen: Boolean

  def isMainMenu: Boolean

  def label = link.label

  def submenuName = link.subMenuName

  def submenuLabel = link.subMenuLabel

  def isCurrent = current

  def cssClasses: String = {
    val classes = new ArrayBuffer[String]
    classes += "monkeyman-menu-level" + (link.menu.depth + 1)
    if (isCurrent) {
      classes += "monkeyman-menu-item-selected"
    }
    if (isIndex && !isMainMenu) {
      classes += "monkeyman-menu-submenu"
    }
    classes.mkString(" ")
  }

  def href = resource.path

  protected var current = false

  def currentPageIs(path: String, map: HashMap[String, MenuRoot])

  def open(map: HashMap[String, MenuRoot]) {
    
  }
  
  def markCurrent(map: HashMap[String, MenuRoot]) {
    current = true
    open(map)
    if (map.contains(link.menu.name)) {
      val parent = map(link.menu.name)
      parent.open(map)
    }
  }
}

class MenuLeaf(res: Resource) extends MenuLinkNode(res) {

  override def children: Seq[MenuLinkNode] = Seq()
  override def isIndex = false

  override def isOpen = true
  override def isMainMenu = false

  def currentPageIs(path: String, map: HashMap[String, MenuRoot]) {
    if (idealizedPath == path) {
      markCurrent(map)
    }
  }

}

class MenuRoot(res: Resource) extends MenuLinkNode(res) {

  private var opened = false

  override def children: Seq[MenuLinkNode] = others.toSeq

  override def isIndex = true

  override def isOpen = opened

  override def isMainMenu = { link.subMenu.get.name == "main" && link.subMenu.get.depth == 0 }

  val others: ArrayBuffer[MenuLinkNode] = new ArrayBuffer[MenuLinkNode]()

  override def open(map: HashMap[String, MenuRoot]) {
    opened = true
    if (link.isIndex) {
      link.subMenu match {
        case None => {}
        case Some(newMenu) => {
          if (link.menu.name != newMenu.name) {
            // open up all parent menus
            if (map.contains(link.menu.name)) {
              val parent = map(link.menu.name)
              parent.open(map)
            }
          } // otherwise we are at the index of the main menu, and go no higher
        }
      }

    }
  }

  def add(tree: MenuLinkNode) {
    others += tree
  }

  def addLeaf(res: Resource) {
    others += new MenuLeaf(res)
  }

  def currentPageIs(path: String, map: HashMap[String, MenuRoot]) {
    if (idealizedPath == path) {
      markCurrent(map)
    } else {
      for (m <- others) {
        m.currentPageIs(path, map)
      }
    }
  }
}
