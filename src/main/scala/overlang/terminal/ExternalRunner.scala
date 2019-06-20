package overlang.terminal

import java.io.File

import scala.sys.process.{Process, ProcessLogger}

object ExternalRunner {
  def runWith(file: File, program: String): String = {

    val procSeq = Seq(program, file.getPath)

    val output = new StringBuilder
    val logger = ProcessLogger(
      (out: String) => output.append(out).append('\n'),
      (err: String) => output.append(err).append('\n'))

    Process(procSeq) ! logger

    output.toString()
  }
}
