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

package nl.flotsam.monkeyman.decorator.registry

import nl.flotsam.monkeyman.{ResourceListener, Resource, ResourceDecorator}
import nl.flotsam.monkeyman.util.Logging


class RegistryDecorator extends ResourceDecorator with ResourceListener with Logging {

  private val resourcesById = collection.mutable.Map.empty[String,  Resource]
  val resourceByPath = collection.mutable.Map.empty[String,  Resource]
  
  def allResources: List[Resource] = resourcesById.values.toList
  
  def decorate(resource: Resource) = {
    resourcesById += resource.id -> resource
    resourceByPath += resource.path -> resource
    resource
  }

  def deleted(id: String) {
    resourcesById.get(id) match {
      case Some(resource) => 
        info("Removed {}", resource.path)
        resourcesById -= id
        resourceByPath -= resource.path
      case None =>
        // TODO: Add warning here
    }
  }

  def added(resource: Resource) {
    info("Added {}", resource.path)
    resourcesById += resource.id -> resource
    resourceByPath += resource.path -> resource
  }

  def modified(resource: Resource) {
    info("Modified {}", resource.path)
    resourcesById += resource.id -> resource
    resourceByPath += resource.path -> resource
  }

}
