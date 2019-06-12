package overlang

import java.io.File

import net.team2xh.scurses.Scurses
import overlang.terminal.{ExternalEditor, Frame, FrameStack, Input, SearchResultsFrame}

object Terminal extends App {
  def saveAs(str: String): Unit = {
    val text: String = ActiveFile.readAll
    ActiveFile.setFile(new File(str))
    ActiveFile.save(text)
  }

  ActiveFile.setFile(File.createTempFile("overlang", "tmp"))


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
        val saveAsPattern = """saveas (.)+""".r
        val editPattern = """edit""".r
        text match {
          case addPattern(_) =>
          case saveAsPattern(_) => saveAs(text.substring("saveas".length + 1))
          case searchPattern(word) => stack.add(SearchResultsFrame(text.substring("search".length + 1)))
          case editPattern() => ExternalEditor.editFile(frame, ActiveFile.getFile)
          case _ =>
        }
        Unit
      },
      widthFun = () => frame.panel.innerWidth, heightFun = () => 1,
      offsetYFun = () => frame.panel.innerHeight - 1)


    stack.add(frame)
    stack.show()

  }
}
