package overlang.stackOverflowBackend

import overlang.stackOverflowBackend.StackOverflowParser.parseAnswerResponseToListOfCode

final class WrongBlockNumberException(private val message: String = "",
                                      private val cause: Throwable = None.orNull)
  extends Exception(message, cause)

import StackOverflowConnection.getAnswerAsString

class StackOverflowSnippet(private var codeBlocks: List[String]) extends Snippet {

  override def getCode: String = {
    codeBlocks.foldLeft("") { (acc, str) => acc + "\n" + str }
  }

  def block(blocksNumber: Range): StackOverflowSnippet =
    new StackOverflowSnippet(
      for {i <- blocksNumber.toList} yield codeBlocks(i)
    )


  def block(blockIndex: Int): StackOverflowSnippet = {
    new StackOverflowSnippet(List(codeBlocks(blockIndex)))
  }

}

object StackOverflowSnippet {
  def apply(id: Int): StackOverflowSnippet = {
    new StackOverflowSnippet(
      parseAnswerResponseToListOfCode(getAnswerAsString(id))
    )
  }

  def apply(id: Int, blockNumber: Int): StackOverflowSnippet = {
    val codeList = parseAnswerResponseToListOfCode(getAnswerAsString(id))
    new StackOverflowSnippet(List(codeList(blockNumber)))
  }

  def apply(id: Int, blocksNumbers: Range): StackOverflowSnippet = {
    val codeList = parseAnswerResponseToListOfCode(getAnswerAsString(id))
    new StackOverflowSnippet(for {i <- blocksNumbers.toList} yield codeList(i))
  }


  def apply(stackOverflowAnswer: StackOverflowAnswer): StackOverflowSnippet =
    new StackOverflowSnippet(stackOverflowAnswer.codeBlocks)

}


