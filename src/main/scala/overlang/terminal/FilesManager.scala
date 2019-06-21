package overlang.terminal

import java.io.{File, FileWriter}

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
    writeFile(contents, tempFi)
    tempFi
  }

  private def writeHelper(content: String, file: File, append: Boolean): Unit = {
    val fw = new FileWriter(file, append)

    try {
      fw.write(content)
    }
    finally fw.close()
  }

  def writeFile(content: String, filePath: String): Unit = {
    val file = new File(filePath)
    file.createNewFile()
    writeHelper(content, file, append = false)
  }

  def writeFile(content: String, file: File): Unit = {
    writeHelper(content, file, append = false)
  }

  def appendToFile(content: String, filePath: String): Unit = {
    val file = new File(filePath)
    file.createNewFile()
    writeHelper(content, file, append = true)
  }

  def appendToFile(content: String, file: File): Unit = {
    writeHelper(content, file, append = true)
  }


  def readFile(file: File): String = {
    val fromFile = Source.fromFile(file)
    val ret = fromFile.getLines().mkString("\n")
    fromFile.close()
    ret
  }


}
