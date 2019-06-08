import scala.collection.mutable.ListBuffer

object OverlangMain {
  def main(args: Array[String]): Unit = {
    val res = StackOverflowConnection.getSearchResultAsString("java sort")
    val ans = StackOverflowParser.parseSearchResponseToListOfAnswers(res)
  println(ans)
  }
}