object OverlangMain {
  def main(args: Array[String]): Unit = {
    val snippet = new StackOverflowSnippet(21991967)
    println(snippet.getCode)

    /*Code(
      new StackOverflowSnippet(51015547))
      .save("~/Tmp/code.py")*/

    Code(
      new StackOverflowSnippet(51015547),
      new StackOverflowSnippet(45741489))
      .runWith("python3")

  }
}