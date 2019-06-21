package overlang.stackOverflowBackend

import overlang.terminal.SearchResult

class StackOverflowQuestion(val id: Int,
                            val title: String, val score: Int) extends SearchResult {
  override def extractText(): String = "[Q] score: " + score.toString + "\n" + title

}



