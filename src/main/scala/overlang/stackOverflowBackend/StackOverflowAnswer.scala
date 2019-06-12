package overlang.stackOverflowBackend

import net.team2xh.scurses.Scurses
import overlang.terminal.{ChooseAnswerFrame, Frame, FramePanel, SearchResult}

import scala.concurrent.duration.Duration

class StackOverflowAnswer(val id: Int,
                          val score: Int,
                          val codeBlocks: List[String],
                          val tags: List[String]) extends SearchResult {

  private var tempText: String = _
  /*private var begin: Long = 0L
  private var durtaion: Long = 0L
  private var framePanel: FramePanel = null*/

  override def extractText(): String = {
    if (tempText != null) {
      return tempText
    }


    if (codeBlocks.isEmpty) return "[A] score: " + score.toString + "\n\n"
    var string = codeBlocks.reduce((a, b) => a + b + "\n")
    string = string.substring(0, math.min(200, string.length))
    if (string.length == 200)
      string = string.concat("\n[...]")
    "[A] score: " + score + "\n" + string
  }

  override def setTempString(string: String): Unit = {
    tempText = string
  }

  override def removeTempString(): Unit = {
    tempText = null
  }
}


object StackOverflowAnswer {
  def apply(id: Int): StackOverflowAnswer = {
    val response = StackOverflowConnection.getAnswerAsString(id)
    StackOverflowParser.parseAnswerResponseToAnswer(response)
  }

}
