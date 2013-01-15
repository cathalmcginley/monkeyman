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
package nl.flotsam.monkeyman.helper

import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.menu.MenuBuilder
import nl.flotsam.monkeyman.menu.MenuLinkNode

class NavigationMenuHelper(private val allResources: Seq[Resource], 
    current: Resource) {

  def menuRoot: Option[MenuLinkNode] = {
    MenuBuilder.build(allResources, current.path)
  }
  
//  def menuForPath(resources: Seq[Resource], currentPath: String): Option[MenuLinkNode] = {
//    MenuBuilder.build(resources, currentPath)
//  }
 
 
}