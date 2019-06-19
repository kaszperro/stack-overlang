package overlang.terminal

import java.io.File

import scala.sys.process.{Process, ProcessLogger}

object ExternalRunner {


  def runWith(frame: Frame, labels: Labels, file: File, program: String) {

    val procSeq = Seq(program, file.getPath)

    val output = new StringBuilder
    val logger = ProcessLogger(
      (out: String) => output.append(out).append('\n'),
      (err: String) => output.append(err).append('\n'))

    Process(procSeq) ! logger

    labels.setText(output.toString)
    frame.clear()
    frame.panel.markAllForRedraw()
  }

  def runWith(file: File, program: String) = {

    val procSeq = Seq(program, file.getPath)

    val output = new StringBuilder
    val logger = ProcessLogger(
      (out: String) => output.append(out).append('\n'),
      (err: String) => output.append(err).append('\n'))

    Process(procSeq) ! logger

    output.toString()
  }
}
