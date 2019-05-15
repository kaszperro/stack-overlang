import StackOverflowConnection._
import StackOverflowParser._

final class WrongBlockNumberException(private val message: String = "",
                                      private val cause: Throwable = None.orNull)
  extends Exception(message, cause)


class StackOverflowSnippet(private var codeBlock: Either[String, List[String]]) extends Snippet {


  override def getCode: String = {

    codeBlock match {
      case Left(str) => str
      case Right(list) => list.foldLeft("") { (acc, str) => acc + "\n" + str }
    }

  }

  def block(blocksNumber: Range): StackOverflowSnippet = {
    codeBlock match {
      case Left(_) => throw new RuntimeException("cat get range from string")
      case Right(list) => new StackOverflowSnippet(Right(
        for {i <- blocksNumber.toList} yield list(i)
      ))
    }
  }

  def block(blockIndex: Int): StackOverflowSnippet = {
    codeBlock match {
      case Left(_) => throw new RuntimeException("cant get block of code")
      case Right(list) => new StackOverflowSnippet(Left(list(blockIndex)))
    }
  }
}

object StackOverflowSnippet {
  def apply(id: Int): StackOverflowSnippet = {
    new StackOverflowSnippet(Right(
      parseResponseToListOfCode(getAnswerAsString(id))
    ))
  }

  def apply(id: Int, blockNumber: Int): StackOverflowSnippet = {
    val codeList = parseResponseToListOfCode(getAnswerAsString(id))
    new StackOverflowSnippet(Left(codeList(blockNumber)))
  }

  def apply(id: Int, blocksNumbers: Range): StackOverflowSnippet = {
    val codeList = parseResponseToListOfCode(getAnswerAsString(id))
    val newCodeList = for {i <- blocksNumbers.toList} yield codeList(i)
    new StackOverflowSnippet(Right(newCodeList))
  }


  /*

    def apply(stackOverflowAnswer: StackOverflowAnswer, blockNumber: Int): StackOverflowSnippet = {
      val mySnippet = new StackOverflowSnippet(stackOverflowAnswer.id, blockNumber)
      mySnippet.codeBlocksList = Some(stackOverflowAnswer.codeBlocks)
      mySnippet
    }

  */
}


