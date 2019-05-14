import scala.util.matching.Regex

final class WrongBlockNumberException(private val message: String = "",
                                      private val cause: Throwable = None.orNull)
  extends Exception(message, cause)


class StackOverflowSnippet(val id: Int, val blockNumber: Int = 0) extends Snippet {
  override def getCode: String = {
    val codeRegex: Regex = "<pre><code>(.*?)</code></pre>".r
    val body = StackOverflowConnection.getAnswerBody(id)

    val codeBlocksList = codeRegex.findAllMatchIn(body).toList

    if (codeBlocksList.length <= blockNumber)
      throw new WrongBlockNumberException("not enough code blocks in list")

    val blockString = codeBlocksList(blockNumber).toString

    blockString
      .substring(11, blockString.length - 13) //need to remove <pre><code> and </pre></code>
      .replace("\\n", "\n")
  }
}




