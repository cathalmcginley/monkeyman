/*
 * Monkeyman static web site generator
 * Copyright (C) 2012  Wilfred Springer	
 * Copyright (C) 2013  Cathal Mc Ginley
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
package nl.flotsam.monkeyman.helper

/**
 * This helper allows you to create formatted span elements with
 * date information.
 */
class DateSpanHelper {

  val Months = List("",
    "January", "February", "March",
    "April", "May", "June",
    "July", "August", "September",
    "October", "November", "December")

  val OrdinalSuffixes = List("",
    "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th",
    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
    "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th",
    "st")

  def span(year: Int, month: Int, day: Int) = {
    assert(month > 0 && month < 13, "month " + month +  " out of range 1..12; order is span(Year,Month,Date)")
    assert(day > 0 && day < 32, "day " + day +  " out of range 1..31; order is span(Year,Month,Date)")
    val dateClass = "date"
    val isoDate = "%4d-%02d-%02d".format(year, month, day)

    <span class={ dateClass } title={ isoDate }>{ day }<sup>{ OrdinalSuffixes(day) }</sup> { Months(month) } { year }</span>
  }

}