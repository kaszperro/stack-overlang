import stackOverflowBackend.{StackOverflowConnection, StackOverflowParser}

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

object OverlangMain {
  def main(args: Array[String]): Unit = {


    val myText = StackOverflowConnection.getAnswerAsString(466376)

    //val myText: String = "<pre class='cos tam'><code> eloooo </code></pre>"

    val l = StackOverflowParser.parseAnswerBodyToListOfCode(myText) //codeRegex.findAllMatchIn(myText).map(b => b.toString).toList
    l.foreach(s => println(s))
  }
}