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

package nl.flotsam.monkeyman.helper

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import nl.flotsam.monkeyman.ClasspathResource

@RunWith(classOf[JUnitRunner])
class RelativePathHelperSpec extends Specification {

  def helperFor(path: String): RelativePathHelper = {
    new RelativePathHelper(new ClasspathResource(path))
  }
  
  val Path = helperFor(null)
  def relativePath(from: String, to: String) = Path.pathFromTo(from, to)

  "links to subdirectories" should {
    val from = "abc/index.html"
    val to = "abc/def/ghi/other.html"
    val expected = "def/ghi/other.html"
      
    "link down to subdirectory" in {
      relativePath(from, to) must be equalTo expected
    }
    "have no references to parent dirs" in {
      relativePath(from, to).indexOf("..") must be equalTo -1
    }
  }
  
  "links to the same directory" should {
    val from = "abc/index.html"
    val to = "abc/other.html"
    val expected = "other.html"
      
    "link directly to file" in {
      relativePath(from, to) must be equalTo expected
    }
    "have no references to parent dirs" in {
      relativePath(from, to).indexOf("..") must be equalTo -1
    }
  }
  
  "links to parent directories" should {
    val from = "abc/def/ghi/index.html"
    val to = "abc/other.html"    
    val expected = "../../other.html"
      
    "link up to parent directory" in {
      relativePath(from, to) must be equalTo expected
    }
    "refer to to parent dirs" in {
      relativePath(from, to).indexOf("..") must not equalTo -1
    }
  }
  
  "links to directories in different subtrees" should {
    val from = "abc/def/ghi/index.html"
    val to = "abc/jkl/mno/other.html"    
    val expected = "../../jkl/mno/other.html"
      
    "link up to parent directory and down again" in {
      relativePath(from, to) must be equalTo expected
    }
    "refer to to parent dirs" in {
      relativePath(from, to).indexOf("..") must not equalTo -1
    }
  }
  
  "links to self" should {
    val from = "abc/def/ghi/page.html"
    val to = from    
    val expected = "page.html"
      
    "not be empty" in {
      relativePath(from, to) must not beEmpty
    }
    "refer simply to file name" in {
      relativePath(from, to) must be equalTo expected
    }
  }
  
  "helper constructed from a real resource" should {
    val from = "abc/def/ghi/index.html"
    val to = "abc/jkl/mno/other.html"    
    val expected = "../../jkl/mno/other.html"
      
    val boundPath = helperFor(from)
      
    "link up to parent directory and down again" in {
      boundPath.to(to) must be equalTo expected
    }
    "refer to to parent dirs" in {
      boundPath.to(to).indexOf("..") must not equalTo -1
    }
  }
  
}