import scala.collection.mutable.ListBuffer

object OverlangMain {
  def main(args: Array[String]): Unit = {
    val test = "abcd\n\n\nabcd"
    val splited = test.split("\n")
    splited.foreach(s => println(s))
    val gr = splited.grouped(30)
    gr.foreach(
      f => f.foreach(s => println(s))
    )

    splited.grouped(100).foreach(a => println(a.length))


    val te = test.split("\n")
      .flatMap(l => l.grouped(100).map {
        case "" => "\n"
        case f => print("|" + f + "|"); f
      })
    te.foreach(t => println(t))


    val myList = List[String]()


    test.split("\n").foreach(
      s => s.grouped(10)
    )


    val tesste = "".grouped(10)

    println(tesste.length)

  }
}