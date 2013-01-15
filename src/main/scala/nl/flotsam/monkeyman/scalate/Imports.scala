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

package nl.flotsam.monkeyman.scalate

import org.joda.time.LocalDateTime
import nl.flotsam.monkeyman.helper.HyperLinkHelper
import nl.flotsam.monkeyman.helper.DateSpanHelper
import nl.flotsam.monkeyman.helper.NavigationMenuHelper
import nl.flotsam.monkeyman.helper.RelativePathHelper


object Imports {
  
  implicit object LocalDateTimeOrdering extends Ordering[LocalDateTime] {
    def compare(x: LocalDateTime, y: LocalDateTime) = x.compareTo(y)
  }

  val Link = new HyperLinkHelper
  
  val Date = new DateSpanHelper
  
  val Navigation = new NavigationMenuHelper
  
}
