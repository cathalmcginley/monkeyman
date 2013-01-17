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

import org.joda.time.LocalDateTime
import collection.JavaConversions._
import eu.medsea.mimeutil.detector.ExtensionMimeDetector
import eu.medsea.mimeutil.{MimeType, MimeUtil}


class ClasspathResourceLoader(paths: Seq[String], loader: ResourceLoader) extends ResourceLoader {
  
  private val resources = paths.map(path => ClasspathResource(path))
  
  def load = {
    val loaded = loader.load
    val ids = loaded.map(_.id)
    resources.filter(resource => !ids.contains(resource.id)) ++ loaded
  } 

  def register(listener: ResourceListener) {
    loader.register(new ResourceListener {
      def deleted(id: String) {
        resources.find(_.id == id) match {
          case Some(resource) => listener.modified(resource)
          case None => listener.deleted(id)
        }
      }

      def modified(resource: Resource) {
        listener.modified(resource)
      }

      def added(resource: Resource) {
        resources.find(_.id == resource.id) match {
          case Some(resource) => listener.modified(resource)
          case None => listener.added(resource)
        }
      }
    })
  }
  


}
