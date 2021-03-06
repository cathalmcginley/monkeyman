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

package nl.flotsam.monkeyman.decorator.snippet

import org.fusesource.scalate.TemplateEngine
import nl.flotsam.monkeyman.{LayoutResolver, Resource, ResourceDecorator}


class SnippetDecorator(layoutResolver: LayoutResolver, engine: TemplateEngine, allResources: () => Seq[Resource])
  extends ResourceDecorator
{

  def decorate(resource: Resource) =
    if (resource.contentType == "text/x-html-fragment")
      new SnippetDecoration(resource, layoutResolver, engine, allResources)
    else resource

}
