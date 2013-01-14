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

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RelativePathHelperSpec extends Specification {

  val Path = new RelativePathHelper()
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
  
  "helper.from(_).to(_)" should {
    val from = "abc/def/ghi/index.html"
    val to = "abc/jkl/mno/other.html"    
    val expected = "../../jkl/mno/other.html"
      
    "link up to parent directory and down again" in {
      Path.from(from).to(to) must be equalTo expected
    }
    "refer to to parent dirs" in {
      Path.from(from).to(to).indexOf("..") must not equalTo -1
    }
  }
  
}