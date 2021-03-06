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

import java.io.InputStream
import org.joda.time.LocalDateTime
import nl.flotsam.monkeyman.menu.MenuLink
import nl.flotsam.monkeyman.decorator.ResourceDecoration

trait Resource {

  /**
   * A human readable String to refer to this file. Can contain spaces.
   */
  def title: Option[String]

  /**
   * The date from which on this file should be considered published. (Note: that doesn't mean it will stay there
   * forever, it just allows you to hold off publication if you don't want to and have a publication date if that's
   * what you want to display.
   */
  def pubDateTime: LocalDateTime

  /**
   * The type of resource.
   */
  def contentType: String

  /**
   * The bytes.
   */
  def open: InputStream

  /**
   * The path to this resource.
   */
  def path: String

  /**
   * A number of tags associated to this resource. Eases lookups. Can be used for whatever you need.
   */
  def tags: Set[String]

  /**
   * If this resource should be included in the output.
   */
  def published: Boolean

  /**
   * Get an HTML fragment to be included somewhere else.
   */
  def asHtmlFragment: Option[String]
  
  /**
   * Change the way this resource is rendered, based on presence
   * of option name. All considered to be off by default. 
   * 
   * Works like tags. But they're not free-form, they have to be defined
   * in MonkeymanOptions. So you might include:
   * 
   * options: use-title-as-path
   */
  def options: MonkeymanOptions
  
  /**
   * An optional menu link to this resource. Need only be defined
   * if this resource is going to appear as an item in some
   * navigation menu or other.
   */
  def menuLink: Option[MenuLink]
  
  /**
   * Page information of various types. This allows you to
   * define ad-hoc data which can be rendered in certain templates.
   * 
   * For example, if you have a page for each new software release, you
   * could define "version", "codeName", "sourceLink" etc. and the
   * "release" template could automatically lay out this information.
   */
  def info: Map[String, String]  
  
  /**
   * Embedded miniature documents connected to a resource.
   * 
   * These are usually defined as Markdown in the YAML front-matter
   * and rendered into HTML. They may be used to provide abstracts,
   * overviews, tweet-sized summaries etc. A Minidoc is also used
   * to hold the "before-the-jump" HTML fragment of a blog post.
   */
  def minidoc: Map[String, Minidoc]
  
  /**
   * The unique identifier of this resource. Doesn't change during its lifetime.
   */
  def id: String 
  
  private var decorator: Option[ResourceDecoration] = None
  def decoratedBy(dec: ResourceDecoration) {
    decorator = Some(dec)
  }
  
  /**
   * This follows decorators in the opposite direction
   * (assuming ResourceDecoration correctly calls resource.decoratedBy(this)
   * every time it wraps a resource in a decorator).
   * 
   * Use sparingly, as it breaks the abstraction.
   * 
   * Use with care, since it's just possible that infinite loops may result.
   */
  def eventual: Resource = {
    decorator match {
      case None => this
      case Some(decorator) => decorator.eventual
    }
  }

  

}
