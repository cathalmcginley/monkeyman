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

package nl.flotsam.monkeyman.decorator.markdown

import nl.flotsam.monkeyman.{Resource, ResourceDecorator}
import nl.flotsam.monkeyman.util.StringWithSuffix._

class MarkdownDecorator extends ResourceDecorator {
  
  def decorate(resource: Resource) = {
    if (resource.contentType == "text/x-web-markdown" || resource.path.hasSuffix(MarkdownPage.Suffixes))
      new MarkdownDecoration(resource)
    else resource
  }
  
}
