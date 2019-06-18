package overlang

import java.io.File

import net.team2xh.scurses.{Colors, Scurses}
import overlang.terminal.{ClickableLabels, ExternalEditor, Frame, FrameStack, Input, Labels, SearchResultsFrame}

object Terminal {
  var errorLabel: Labels = _

  def saveAs(frame: Frame, str: String): Unit = {
    val text: String = ActiveFile.readAll
    ActiveFile.setFile(new File(str))
    ActiveFile.save(text)
    updateTitle(frame)
  }

  def updateTitle(frame: Frame): Unit = {
    frame.setTitle(Some(ActiveFile.getFile.getName + " - Stack-Overlang"))

  }


  def main(args: Array[String]): Unit = {
    ActiveFile.setFile(File.createTempFile("overlang", "tmp"))

    Scurses { implicit screen =>
      val stack = new FrameStack
      val frame = Frame(Some("temporary file - Stack-Overlang"))

      if (args.length > 0) {
        ActiveFile.setFile(new File(args(0)))
        updateTitle(frame)
      }

      Labels(frame.panel, Help.text,
        () => frame.panel.innerWidth, () => frame.panel.innerHeight - 2,
        () => 0, () => 0)

      errorLabel = Labels(frame.panel, "",
        () => frame.panel.innerWidth, () => 1,
        () => 0, () => frame.panel.innerHeight - 2)
      errorLabel.setColor(Colors.BRIGHT_RED, Colors.DIM_BLACK)

      Input(frame.panel, "text",
        text => {
          val addPattern = """add (\d+)""".r
          val searchPattern = """search (.+)""".r
          val saveAsPattern = """saveas (.+)""".r
          val editPattern = """edit""".r
          errorLabel.setText("")
          text match {
            case addPattern(_) =>
            case saveAsPattern(filePath) => saveAs(frame, filePath)
            case searchPattern(query) => stack.add(SearchResultsFrame(query))
            case editPattern() => ExternalEditor.editFile(frame, ActiveFile.getFile)
            case _ => errorLabel.setText("Syntax error")
          }
          Unit
        },
        widthFun = () => frame.panel.innerWidth, heightFun = () => 1,
        offsetYFun = () => frame.panel.innerHeight - 1)

      stack.add(frame)
      stack.show()

    }
  }


}
