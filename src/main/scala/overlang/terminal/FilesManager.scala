package overlang.terminal

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

  def writeToFile(content: String, filePath: String): Unit = {
    val file = new File(filePath)
    file.createNewFile()
    writeToFile(content, file)
  }

  def writeToFile(content: String, file: File): Unit = {
    new PrintWriter(file) {
      try {
        print(content)
      } finally {
        close()
      }
    }
  }

  def readFile(file: File): String = {
    val fromFile = Source.fromFile(file)
    val ret = fromFile.getLines().mkString("\n")
    fromFile.close()
    ret
  }


}
