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

package nl.flotsam.monkeyman.decorator.zuss

import nl.flotsam.monkeyman.decorator.ResourceDecoration
import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.util.Closeables
import org.zkoss.zuss.{Locator, Zuss}
import java.io.{StringWriter, FileNotFoundException}
import org.zkoss.zuss.impl.out.BuiltinResolver
import org.apache.commons.io.{IOUtils, FilenameUtils}

class ZussDecoration(resource: Resource) extends ResourceDecoration(resource) {
  
  override def contentType = "text/css"

  override def path = FilenameUtils.removeExtension(resource.path) + ".css"

  override def open = {
    Closeables.using(resource.open) {
      in =>
        val definition = Zuss.parse(in, "UTF-8", NullLocator, FilenameUtils.getName(resource.path))
        val writer = new StringWriter
        Zuss.translate(definition, writer, new BuiltinResolver)
        IOUtils.toInputStream(writer.toString)
    }
  }

  // Need to do something with relative locations
  object NullLocator extends Locator {
    def getResource(name: String) = throw new FileNotFoundException("Failed to find file called " + name)
  }
  
}
