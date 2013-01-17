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

package nl.flotsam.monkeyman.util

import org.slf4j.LoggerFactory

trait Logging {

  lazy val logger = LoggerFactory.getLogger(getClass)

  def debug(message: => String, args: Any*) = logger.debug(message, args.toArray.map(_.asInstanceOf[AnyRef]).toArray[AnyRef])
  def info(message: => String, args: Any*)  = logger.info(message, args.map(_.asInstanceOf[AnyRef]).toArray[AnyRef])
  def warn(message: => String, args: Any*)  = logger.warn(message, args.map(_.asInstanceOf[AnyRef]).toArray[AnyRef])
  def warn(message: => String, e: Throwable)  = logger.warn(message, e)
  def error(message: => String) = logger.error(message)
  def error(message: => String, e : Throwable) = logger.error(message,e)

}
