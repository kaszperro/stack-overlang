package overlang

import java.io.File

import overlang.stackOverflowBackend.{StackOverflowConnection, StackOverflowParser}
import overlang.terminal.FilesManager

object OverlangMain {
  def main(args: Array[String]): Unit = {
    print(System.in.read())
  }
}
