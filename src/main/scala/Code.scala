import java.io.{File, PrintWriter}
import scala.sys.process.{Process, ProcessLogger}

class Code private(content: String) {
  def runWith(command: String): Unit = {
    val file = File.createTempFile("code-", ".txt")
    save(file)

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

  def save(path: String): Unit = {
    val pathWithHome = path.replaceFirst("^~", System.getProperty("user.home"))
    save(new File(pathWithHome))
  }

  def save(file: File): Unit = {
    val printWriter = new PrintWriter(file)
    printWriter.print(content)
    printWriter.close()
  }
}

object Code {
  def build(snippets: Snippet*): Code = {
    val strBuilder = new StringBuilder()
    for (snippet <- snippets)
      strBuilder.append(snippet.getCode).append('\n')

    new Code(strBuilder.toString())
  }
}

