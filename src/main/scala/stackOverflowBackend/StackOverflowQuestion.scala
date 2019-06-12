package stackOverflowBackend

import net.team2xh.scurses.Scurses
import terminal.{ChooseAnswerFrame, Frame, SearchResult}

class StackOverflowQuestion(val id: Int,
                            val title: String, val score: Int) extends SearchResult {
  override def extractText(): String = "[Q] score: " + score.toString + "\n" + title

}



