package nl.flotsam.monkeyman.util

class StringWithSuffix(str: String) {

  def hasSuffix(suffixes: Seq[String]): Boolean = {
    val last = str.lastIndexOf(".")
    if (last < 0) {
      false
    } else {
      val suffix = str.substring(last)      
      suffixes.contains(suffix)
    }
  }
  
}

object StringWithSuffix {
  
  implicit def string2stringWithSuffix(str: String): StringWithSuffix = {
    new StringWithSuffix(str)
  }
  
}
