package overlang

import java.io.{File, FileWriter}

object ActiveFile {
  private var file: File = _

  def setFile(file: File) {
    ActiveFile.file = file
  }

  def getFile: File = file

  private def save(text: String, append: Boolean): Unit = {
    val fw = new FileWriter(file, append)
    try {
      fw.write(text)
    }
    finally fw.close()
  }

  def append(text: String): Unit = save(text, append = true)

  def save(text: String): Unit = save(text, append = false)

  def readAll: String = {
    scala.io.Source.fromFile(file).getLines.mkString
  }


}
