package overlang

import java.io.File

import net.team2xh.scurses.{Colors, Scurses}
import overlang.stackOverflowBackend.{StackOverflowAnswer, StackOverflowConnection}
import overlang.terminal.commands.{Command, Commands}
import overlang.terminal.{ExternalEditor, ExternalRunner, Frame, FrameManager, Input, Labels, OutputFrame, SearchResultsFrame}

import scala.collection.mutable

object Terminal {
  var errorLabel: Labels = _
  var infoLabel: Labels = _

  def saveAs(frame: Frame, str: String): Unit = {
    val text: String = ActiveFile.readAll
    ActiveFile.setFile(str)
    ActiveFile.save(text)
    updateTitle(frame)
  }

  def updateTitle(frame: Frame): Unit = {
    frame.setTitle(Some(ActiveFile.getFile.getName + " - Stack-Overlang"))

  }


  def main(args: Array[String]): Unit = {
    ActiveFile.setFile(File.createTempFile("overlang", "tmp"))

    Scurses { implicit screen =>
      val stack = new FrameManager
      val frame = Frame(Some("temporary file - Stack-Overlang"))

      if (args.length > 0) {
        ActiveFile.setFile(new File(args(0)))
        updateTitle(frame)
      }

      errorLabel = Labels(frame.panel, "",
        () => frame.panel.innerWidth, () => 1,
        () => 0, () => frame.panel.innerHeight - 2)
      errorLabel.setColor(Colors.BRIGHT_RED, Colors.DIM_BLACK)

      val commands: mutable.MutableList[Command] = Commands.getCommands(frame, errorLabel)

      infoLabel = Labels(frame.panel, commands.map(a => a.help).reduce((a, b) => a + " \n" + b),
        () => frame.panel.innerWidth, () => frame.panel.innerHeight - 2,
        () => 0, () => 0)



      Input(frame.panel, "text",
        text => {
          var matched = false
          commands.foreach(a => matched |= a.run(text))
          if (!matched)
            errorLabel.setText(Colors.BRIGHT_RED, "Cannot match any command")
          Unit
        },
        widthFun = () => frame.panel.innerWidth, heightFun = () => 1,
        offsetYFun = () => frame.panel.innerHeight - 1)

      stack.add(frame)
      stack.show()

    }
  }


}
