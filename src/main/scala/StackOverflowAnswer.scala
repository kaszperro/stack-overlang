class StackOverflowAnswer(val id: Int,
                          val score: Int,
                          val codeBlocks: List[String],
                          val tags: List[String]) {

}


object StackOverflowAnswer {
  def apply(id: Int): StackOverflowAnswer = {
    val response = StackOverflowConnection.getAnswerAsString(id)
    StackOverflowParser.parseResponseToAnswer(response)
  }

}
