import java.io.{File, PrintWriter}

import stackOverflowBackend.Snippet
import terminal.FilesManager

import scala.sys.process.{Process, ProcessLogger}

class Code private(content: String) {
  def runWith(command: String): Unit = {
    val file = FilesManager.writeToTempFile(content)

    val cmd = command + " " + file
    val procSeq = Seq("sh", "-c", cmd)

    val output = new StringBuilder
    val logger = ProcessLogger(
      (out: String) => output.append(out).append('\n'),
      (err: String) => output.append(err).append('\n'))

    Process(procSeq) ! logger

    println("Output:")
    println(output.toString())

    file.delete()
  }
}

object Code {
  def apply(snippets: Snippet*): Code = {
    val strBuilder = new StringBuilder()
    for (snippet <- snippets)
      strBuilder.append(snippet.getCode).append('\n')

    new Code(strBuilder.toString())
  }

}

