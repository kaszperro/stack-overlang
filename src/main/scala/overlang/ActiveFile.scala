package overlang

import java.io.{File, FileOutputStream, FileWriter}

import overlang.terminal.FilesManager

object ActiveFile {
  private var file: File = _

  def setFile(file: File) {
    ActiveFile.file = file
  }

  def setFile(filePath: String): Unit = {
    ActiveFile.file = new File(filePath)
  }

  def getFile: File = file

  def append(text: String): Unit = FilesManager.appendToFile(text, file)

  def save(text: String): Unit = FilesManager.writeFile(text, file)

  def readAll: String = FilesManager.readFile(file)


}
