package overlang

import java.io.File

import overlang.stackOverflowBackend.{StackOverflowConnection, StackOverflowParser}

object OverlangMain {
  def main(args: Array[String]): Unit = {
    ActiveFile.setFile(File.createTempFile("overlang", "tmp"))


    val myText = StackOverflowConnection.getAnswerAsString(466376)

    //val myText: String = "<pre class='cos tam'><code> eloooo </code></pre>"

    val l = StackOverflowParser.parseAnswerBodyToListOfCode(myText) //codeRegex.findAllMatchIn(myText).map(b => b.toString).toList
    l.foreach(s => println(s))
  }
}
