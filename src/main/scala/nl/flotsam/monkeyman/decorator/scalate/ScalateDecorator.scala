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

package nl.flotsam.monkeyman.decorator.scalate

import org.fusesource.scalate.TemplateEngine
import nl.flotsam.monkeyman.{ Resource, ResourceDecorator }
import org.apache.commons.io.FilenameUtils
import nl.flotsam.monkeyman.util.StringWithSuffix._
import nl.flotsam.monkeyman.util.Logging
import org.fusesource.scalate.support.StringTemplateSource

class ScalateDecorator(engine: TemplateEngine, allResources: () => Seq[Resource]) extends ResourceDecorator with Logging {

  def decorate(resource: Resource) = {
    if (resource.path.hasSuffix(TemplateEngine.templateTypes.map(ext => "." + ext))) {
      try {
        val sourceString = scala.io.Source.fromInputStream(resource.open).mkString("")
        val source = new StringTemplateSource(resource.path, sourceString)
        new ScalateDecoration(resource, engine.load(source), engine, allResources)
      } catch {
        case t: Throwable => {
          error(t.getMessage())
          resource
        }
      }
    } else {
      resource
    }
  }

}
