import net.team2xh.scurses.{Colors, Scurses}
import terminal.{Frame, FrameStack, Input, Labels, TestWidget}
import sun.misc.Regexp

import scala.collection.mutable.ListBuffer


object Terminal extends App {
  Scurses { implicit screen =>
    val stack = new FrameStack
    val frame = Frame(Some("Stack-Overlang"))

//
//    var testString = "Siemanko testowy tekst"
//    var testString2 = "Siemanko testowy tekst długi bardzo ijdsiadjaoisdjoisajdoiasjdoiajsoidaisodhasoiudhoiashdioashdoiasi"
//
//    var listBuff = new ListBuffer[String]
//    for (i <- 1 to 20) {
//      listBuff += i + ". " + testString
//      listBuff += i + ". " + testString2
//    }
//
//    val array = listBuff.toArray

//    new Labels[String](frame.panel, array, s => s, s => s,
//      () => frame.panel.innerWidth, () => frame.panel.innerHeight - 1,
//      () => 0, () => 0)

    Input(frame.panel, "text",
      (text) => {
        val addPattern = """add (\d+)""".r
        val searchPattern = """search (.)+""".r
        text match {
          case addPattern(_) =>
            //todo
          case searchPattern(word) =>
            val res = StackOverflowConnection.getSearchQuestionsAsString(text.substring("search".length+1))
            val ans = StackOverflowParser.parseSearchResponseToListOfQuestions(res)
            stack.add(new ChooseQuestionFrame(ans.toArray))
          case _ => //todo
        }
        Unit
      },
      widthFun = () => frame.panel.innerWidth, heightFun = () => 1,
      offsetYFun = () => frame.panel.innerHeight - 1)


    stack.add(frame)
    stack.show()

  }
}
