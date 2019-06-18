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


    Input(frame.panel, "text",
      (text) => {
        val addPattern = """add (\d+)""".r
        val searchPattern = """search (.+)""".r
        val saveAsPattern = """saveas (.+)""".r
        val editPattern = """edit""".r
        text match {
          case addPattern(_) =>
          case saveAsPattern(filePath) => saveAs(filePath)
          case searchPattern(text) => stack.add(SearchResultsFrame(text))
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
