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
import nl.flotsam.monkeyman.menu.Menu
import org.apache.commons.io.FilenameUtils
import nl.flotsam.monkeyman.menu._

class MenuBean extends Logging {

  @BeanProperty var label: String = null
  @BeanProperty var indexLabel: String = null
  @BeanProperty var level: Int = 0
  @BeanProperty var order: Int = 100
  @BeanProperty var display: Boolean = true
  @BeanProperty var menu: String = null

  def getMenuLink(resource: Resource) = {
    if (display) {

      val linkLabel: String = if (label != null)
        label
      else {
        resource.title match {
          case None => { warn(""); resource.path }
          case Some(title) => { title }
        }
      }
      val menu = Menu.forPath(resource.path)
      val resourceFile = FilenameUtils.getBaseName(resource.path)
      val resourceBaseName = FilenameUtils.removeExtension(resourceFile)
      val menuLink = if (resourceBaseName == "index") {
        val newMenuName = label // HACK
        val newLabel = if (indexLabel == null) label else indexLabel
        val parentMenu: Menu = if (menu.depth == 0 && menu.name == "main") {
          menu
        } else if (menu.depth == 1) {
          Menu.getMenu("main")
        } else {          
          val parentMenuName = menu.name.substring(0, menu.name.lastIndexOf("/"))
          Menu.getMenu(parentMenuName)
        }
        val indexMenu = menu
        new IndexLink(linkLabel, order, parentMenu,
          newMenuName, newLabel, indexMenu)

      } else {
        new Link(linkLabel, order, menu)
      }
      menuLink
    } else {
      NonDisplayedMenuLink
    }

  }

}

object NotInMenu extends MenuBean {
  display = false
}
