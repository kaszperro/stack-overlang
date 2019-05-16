object OverlangMain {
  def main(args: Array[String]): Unit = {

    //val snippet = StackOverflowSnippet(21991967)

    //println(snippet.getCode)
    /*
      Code(
         new StackOverflowSnippet(51015547))
         .save("~/Tmp/code.py")

        Code(
          new StackOverflowSnippet(51015547),
          new StackOverflowSnippet(45741489))
          .runWith("python3")

         */


    // val ans = StackOverflowAnswer(26853961)
    //println(ans.codeBlocks)
    // println(ans.codeBlocks(0))


    val res = StackOverflowConnection.getSearchResultAsString("python list comprehension")
    val ans = StackOverflowParser.parseSearchResponseToListOfAnswers(res)
    println(ans.head.codeBlocks)
  }
}