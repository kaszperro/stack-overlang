final class WrongBlockNumberException(private val message: String = "",
                                      private val cause: Throwable = None.orNull)
  extends Exception(message, cause)


class StackOverflowSnippet(val id: Int, val blockNumber: Int = 0) extends Snippet {
  private var codeBlocksList: Option[List[String]] = None

  override def getCode: String = {
    codeBlocksList = codeBlocksList match {
      case None =>
        val body = StackOverflowConnection.getAnswerAsString(id)
        Some(StackOverflowParser.parseAnswerBodyToListOfCode(body))
      case _ => codeBlocksList
    }

    if (codeBlocksList.size <= blockNumber)
      throw new WrongBlockNumberException("not enough code blocks in list")

    codeBlocksList.get(blockNumber)
  }
}

object StackOverflowSnippet {
  def apply(id: Int, blockNumber: Int): StackOverflowSnippet = {
    new StackOverflowSnippet(id, blockNumber)
  }


  def apply(stackOverflowAnswer: StackOverflowAnswer, blockNumber: Int): StackOverflowSnippet = {
    val mySnippet = new StackOverflowSnippet(stackOverflowAnswer.id, blockNumber)
    mySnippet.codeBlocksList = Some(stackOverflowAnswer.codeBlocks)
    mySnippet
  }


}


