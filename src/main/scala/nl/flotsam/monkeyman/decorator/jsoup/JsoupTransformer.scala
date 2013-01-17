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

package nl.flotsam.monkeyman.decorator.jsoup

import org.jsoup.nodes.Document
import nl.flotsam.monkeyman.Resource
import nl.flotsam.monkeyman.decorator.yaml.ResourceAttributes

abstract class JsoupTransformer(val name: String) {

  /**
   * Whether this transformation should be applied to the document
   * of this resource. It is possible to use options to turn
   * transformations on and off for specific documents, e.g. with-article-jump
   */
  def isApplicableTo(resource: Resource): Boolean
  
  /**
   * Modify the document for this resource. This method
   * will change the document directly, it doesn't make a copy.
   * It returns the resource, or a decorated resource if any
   * attributes have been added.
   */
  def transform(doc: Document, resource: Resource, attr: ResourceAttributes): ResourceAttributes
  
}