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

package nl.flotsam.monkeyman

/**
 * Resource decorates can adjust some of the properties of the resource passed in. Note that - even though the
 * underlying resource *can* be considered immutable - it might be a good idea to implement the updated properties as
 * lazy values, just for sake of optimization. If possible, use the content type as a trigger rather than the path
 * name. Even though both the content type and the path name could change dynamically,
 * the changes being made to the path name sometimes involve looking into the actual content,
 * which might not be something you want to do for every resource you ever come across.
 */
trait ResourceDecorator {

  /**
   * Either turns the resource in an altered resource, or returns the original resource.
   */
  def decorate(resource: Resource): Resource

}
