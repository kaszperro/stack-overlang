package terminal

import scala.concurrent.duration.Duration

trait SearchResult {
  def extractText(): String

  def setTempString(string: String): Unit = {}

  def removeTempString(): Unit = {}
}
