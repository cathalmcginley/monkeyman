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

package nl.flotsam.monkeyman.decorator.yaml

import nl.flotsam.monkeyman.{Resource, ResourceDecorator}
import nl.flotsam.monkeyman.decorator.markdown.MarkdownPage
import nl.flotsam.monkeyman.util.StringWithSuffix._
import org.fusesource.scalate.TemplateEngine
import org.apache.commons.io.FilenameUtils


class YamlFrontmatterDecorator(included: (Resource) => Boolean = { _.path.hasSuffix(MarkdownPage.Suffixes ++ TemplateEngine.templateTypes.map(ext => "." + ext))}) extends ResourceDecorator {
  
  def decorate(resource: Resource) =
    if (included(resource)) new YamlFrontmatterDecoration(resource)
    else resource
  
}
