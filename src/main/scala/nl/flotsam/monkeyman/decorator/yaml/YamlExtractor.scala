/*
 * This file is part of Monkeyman, a static web site generator.
 *
 * Copyright (C) 2012 Wilfred Springer
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

package nl.flotsam.monkeyman.decorator.yaml

import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.util.Logging
import com.esotericsoftware.yamlbeans.YamlReader
import com.esotericsoftware.yamlbeans.YamlException

private[yaml] class YamlExtractor(resource: Resource, yamlString: String) extends Logging {
  lazy val reader = new YamlReader(yamlString)
  lazy val attributes: ResourceAttributes = extract(resource)

  private def extract(resource: Resource) = {
    try {
      val attributesBean = reader.read(classOf[ResourceAttributesBean])
      attributesBean.getAttributes(resource)
    } catch {
      case yamlEx: YamlException => {
        warn("could not parse YAML front-matter for " + resource.path +
          " : " + yamlEx.getMessage())
        new ResourceAttributes()
      }
    }
  }

}
  