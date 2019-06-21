package overlang.terminal.commands

import net.team2xh.scurses.{Colors, Scurses}
import overlang.{ActiveFile, Terminal}
import overlang.stackOverflowBackend.StackOverflowAnswer
import overlang.terminal.{ExternalEditor, ExternalRunner, Frame, Labels, OutputFrame, SearchResultsFrame}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import ExecutionContext.Implicits.global
import scala.collection.mutable

object Commands {
  def getCommands(rootFrame: Frame, label: Labels)
                 (implicit screen: Scurses): mutable.MutableList[Command] = {
    var list: mutable.MutableList[Command] = new mutable.MutableList[Command]
    list += new SearchCommand(rootFrame, label)
    list += new AddCommand(rootFrame, label)
    list += new EditCommand(rootFrame, label)
    list += new EditWithCommand(rootFrame, label)
    list += new RunWithCommand(rootFrame, label)
    list += new SaveAsCommand(rootFrame, label)
    list += new ExitCommand(rootFrame, label)

    list
  }

  class AddCommand(override val rootFrame: Frame, override val label: Labels)
    extends Command(rootFrame, label) {
    override def run(command: String): Boolean = {
      val pattern = """add (\d+)""".r
      command match {
        case pattern(id) => {
          label.setText(Colors.BRIGHT_CYAN, "Fetching...")
          val task = Future[Unit] {
            ActiveFile.append(StackOverflowAnswer(Integer.parseInt(id)).codeBlocks.mkString)
          }
          task onComplete {
            case Success(_) => label.setText(Colors.BRIGHT_GREEN, "Success")
            case Failure(t) => label.setText(Colors.BRIGHT_RED, "Error")
          }
        }
        case _ => return false
      }
      true
    }

    override def help: String = "add <answer_id> - adds answer by id"
  }

  class SearchCommand(override val rootFrame: Frame, override val label: Labels)
                     (implicit screen: Scurses)
    extends Command(rootFrame, label) {
    override def run(command: String): Boolean = {
      val pattern = """search (.+)""".r
      command match {
        case pattern(query) => {
          label.setText(Colors.BRIGHT_CYAN, "Fetching...")
          val task = Future[Unit] {
            rootFrame.getFrameManager.add(SearchResultsFrame(query))
          }
          task onComplete {
            case Success(_) => label.setText(Colors.BRIGHT_GREEN, "Success")
            case Failure(t) => label.setText(Colors.BRIGHT_RED, "Error")
          }
        }
        case _ => return false
      }
      true
    }

    override def help: String = "search <question> - searches questions and answers"

  }

  class RunWithCommand(override val rootFrame: Frame, override val label: Labels)
                      (implicit screen: Scurses)
    extends Command(rootFrame, label) {
    override def run(command: String): Boolean = {
      val pattern = """runwith (.+)""".r
      command match {
        case pattern(program) => {
          label.setText(Colors.BRIGHT_CYAN, "Running program...")
          val task = Future[Unit] {
            rootFrame.getFrameManager.add(new OutputFrame(ExternalRunner.runWith(ActiveFile.getFile, program)))
          }
          task onComplete {
            case Success(_) => label.setText(Colors.BRIGHT_GREEN, "Success")
            case Failure(t) => label.setText(Colors.BRIGHT_RED, "Error")
          }
        }
        case _ => return false
      }
      true
    }

    override def help: String = "runwith <program> - runs with selected program"

  }

  class SaveAsCommand(override val rootFrame: Frame, override val label: Labels)
                     (implicit screen: Scurses)
    extends Command(rootFrame, label) {
    override def run(command: String): Boolean = {
      val pattern = """saveas (.+)""".r
      command match {
        case pattern(filePath) => {
          label.setText(Colors.BRIGHT_CYAN, "Saving...")
          val task = Future[Unit] {
            val text: String = ActiveFile.readAll
            ActiveFile.setFile(filePath)
            ActiveFile.save(text)
            Terminal.updateTitle(rootFrame)
          }
          task onComplete {
            case Success(_) => label.setText(Colors.BRIGHT_GREEN, "Success")
            case Failure(t) => label.setText(Colors.BRIGHT_RED, "Error")
          }
        }
        case _ => return false
      }
      true
    }

    override def help: String = "saveas <filename> - saves actual file"

  }

  class EditCommand(override val rootFrame: Frame, override val label: Labels)
                   (implicit screen: Scurses)
    extends Command(rootFrame, label) {
    override def run(command: String): Boolean = {
      val pattern = """edit""".r
      command match {
        case pattern() => {
          ExternalEditor.editFile(rootFrame, ActiveFile.getFile)
          label.setText(Colors.BRIGHT_GREEN, "Success")
        }
        case _ => return false
      }
      true
    }

    override def help: String = "edit - launches actual file in vi text editor"

  }

  class EditWithCommand(override val rootFrame: Frame, override val label: Labels)
                       (implicit screen: Scurses)
    extends Command(rootFrame, label) {
    override def run(command: String): Boolean = {
      val pattern = """editwith (.+)""".r
      command match {
        case pattern(editor) => {
          try {
            ExternalEditor.editFile(rootFrame, ActiveFile.getFile, editorPath = editor)
            label.setText(Colors.BRIGHT_GREEN, "Success")
          } catch {
            case e: Throwable => label.setText(Colors.BRIGHT_RED, "Error")
          }

        }
        case _ => return false
      }
      true
    }

    override def help: String = "editwith <program> - launches actual file in selected text editor"

  }

  class ExitCommand(override val rootFrame: Frame, override val label: Labels)
    extends Command(rootFrame, label) {
    override def run(command: String): Boolean = {
      val pattern = """exit""".r
      command match {
        case pattern() => {
          System.exit(0)
        }
        case _ => return false
      }
      true
    }

    override def help: String = "exit - exits stack-overlang"
  }

}
