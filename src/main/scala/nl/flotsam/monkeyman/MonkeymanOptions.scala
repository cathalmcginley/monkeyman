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

package nl.flotsam.monkeyman

class MonkeymanOption(val name: String, val on: Boolean) {

  override def toString(): String = {
    if (on) {
      "with[" + name + "]"
    } else {
      "without[" + name + "]"
    }
  }

  def isValid = true
}

class InvalidOption(name: String) extends MonkeymanOption(name, false) {
  override def isValid = false
}

class MonkeymanOptions(protected val options: Seq[MonkeymanOption] = Seq()) {

  private var _titleAsPath = new MonkeymanOption(MonkeymanOptions.TitleAsPath, false)
  private var _articleJump = new MonkeymanOption(MonkeymanOptions.ArticleJump, false)

  def titleAsPath = _titleAsPath.on
  def articleJump = _articleJump.on

  if (!options.isEmpty) {
    for (o <- options) {
      if (o.isValid) {
        o.name match {
          case MonkeymanOptions.TitleAsPath => _titleAsPath = o
          case MonkeymanOptions.ArticleJump => _articleJump = o
        }
      }
    }
  }

  def add(moreOptions: Seq[MonkeymanOption]): MonkeymanOptions = {
    new MonkeymanOptions(options ++ moreOptions)
  }

  def add(moreOptions: MonkeymanOptions): MonkeymanOptions = {
    new MonkeymanOptions(options ++ moreOptions.options)
  }

}

object MonkeymanOptions {

  val TitleAsPath = "title-as-path"
  val ArticleJump = "article-jump"

  def parse(optionStrings: Seq[String]): MonkeymanOptions = {
    new MonkeymanOptions(optionStrings.map(s => parse(s)))
  }

  def parse(optionString: String): MonkeymanOption = {
    val parts = optionString.split("-")
    val withOrWithout = parts.head

    val optionName = parts.tail.mkString("-")
    if (withOrWithout.equals("with")) {
      new MonkeymanOption(optionName, true)
    } else if (withOrWithout.equals("without")) {
      new MonkeymanOption(optionName, false)
    } else {
      new InvalidOption(optionString)
    }
  }

}