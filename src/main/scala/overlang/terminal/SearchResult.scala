package overlang.terminal

trait SearchResult {
  def extractText(): String

  def setTempString(string: String): Unit = {}

  def removeTempString(): Unit = {}
}
