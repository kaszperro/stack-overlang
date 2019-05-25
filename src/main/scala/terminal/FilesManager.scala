package terminal

import java.io.{File, PrintWriter}

import scala.io.Source

object FilesManager {

  def writeToTempFile(contents: String,
                      prefix: Option[String] = None,
                      suffix: Option[String] = None): File = {

    val tempFi = File.createTempFile(
      prefix.getOrElse("prefix-"),
      suffix.getOrElse("-suffix")
    )

    tempFi.deleteOnExit()
    new PrintWriter(tempFi) {
      try {
        write(contents)
      } finally {
        close()
      }
    }
    tempFi
  }

  def readFile(file: File): String = {
    Source.fromFile(file).getLines().mkString("\n")
  }


}
