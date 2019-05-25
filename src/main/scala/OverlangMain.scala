import scala.collection.mutable.ListBuffer

object OverlangMain {
  def main(args: Array[String]): Unit = {

    val splitWidth = 4

    val lines: List[String] = List("abcdefgh", "abc", "aaaaaaaaaa")

    val newLines = lines.flatMap(l =>
      l.grouped(splitWidth).toList
    )
    println(newLines)
  }
}