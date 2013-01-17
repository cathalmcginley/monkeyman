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

class DecoratingResourceLoader(loader: ResourceLoader, decorators: ResourceDecorator*) extends ResourceLoader {

  def load = loader.load.map(decorate)
  
  def decorate(resource: Resource): Resource =
    decorators.foldLeft(resource)((resource, decorator) => decorator.decorate(resource))

  def register(listener: ResourceListener) {
    loader.register(new ResourceListener {
      def deleted(id: String) {
        listener.deleted(id)
      }

      def added(resource: Resource) {
        listener.added(decorate(resource))
      }

      def modified(resource: Resource) {
        listener.modified(decorate(resource))
      }

    })
  }

}
