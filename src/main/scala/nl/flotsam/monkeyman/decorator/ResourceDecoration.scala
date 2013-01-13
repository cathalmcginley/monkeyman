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

package nl.flotsam.monkeyman.decorator

import nl.flotsam.monkeyman.Resource

class ResourceDecoration(resource: Resource) extends Resource {

  def title = resource.title

  def pubDateTime = resource.pubDateTime

  def contentType = resource.contentType

  def open = resource.open

  def path = resource.path

  def tags = resource.tags

  def published = resource.published

  def asHtmlFragment = resource.asHtmlFragment
    
  def options = resource.options
  
  def menuLink = resource.menuLink
  
  def info = resource.info
    
  def id = resource.id

}
