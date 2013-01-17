/*
 * This file is part of Monkeyman, a static web site generator.
 *
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

package nl.flotsam.monkeyman

/*
 * Monkeyman static web site generator
 * Copyright (C) 2012  Wilfred Springer
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

import util.Logging

case class Registry(loader: ResourceLoader)
  extends ResourceListener
  with Logging
{

  val allResources = loader.load.toList
  private val resourceById = collection.mutable.Map(allResources.map(resource => resource.id -> resource): _*)
  val resourceByPath = collection.mutable.Map(allResources.map(resource => resource.path -> resource): _*)

  loader.register(this)

  def deleted(id: String) {
    resourceById.get(id) match {
      case Some(resource) =>
        info("Removed {} ({})", resource.id, resource.path)
        resourceById -= id
        resourceByPath -= resource.path
      case None =>
      // TODO: Add warning here
    }
  }

  def added(resource: Resource) {
    info("Added {} ({})", resource.id, resource.path)
    resourceById += resource.id -> resource
    resourceByPath += resource.path -> resource
  }

  def modified(resource: Resource) {
    info("Modified {} ({})", resource.id, resource.path)
    resourceById.get(resource.id).map(previous => if (previous.path != resource.path) {
      info("Changing path of {} to {}", previous.id, resource.path)
      resourceByPath -= previous.path
    })
    resourceById += resource.id -> resource
    resourceByPath += resource.path -> resource
  }

}
