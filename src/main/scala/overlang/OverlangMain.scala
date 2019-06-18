package overlang

import java.io.File

import overlang.stackOverflowBackend.{StackOverflowConnection, StackOverflowParser}
import overlang.terminal.FilesManager

object OverlangMain {
  def main(args: Array[String]): Unit = {
    ActiveFile.setFile("test.txt")
    ActiveFile.append("elo wariaty\n")
    val myFile = new File("test.txt")
    println(FilesManager.readFile(myFile))
  }
}
