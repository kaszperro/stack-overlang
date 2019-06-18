package overlang.terminal

import java.io.File

object ExternalRunner {

  def runWith(frame: Frame, labels: Labels, file: File, program: String) {
    val processBuilder = new ProcessBuilder("sh", "-c \"" + program +" " + file.getPath +"\"")
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)

    val p = processBuilder.start()
    p.waitFor()

    val scanner = new java.util.Scanner(System.in)
    val line = scanner.nextLine()

    frame.clear()
    frame.panel.markAllForRedraw()
  }
}
