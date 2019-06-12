package overlang.terminal

import java.io.{File, PrintWriter}

object ExternalEditor {

  def editFile(frame: Frame, file: File, editorPath: String = "vi") {
    val processBuilder = new ProcessBuilder(editorPath, file.getAbsolutePath)
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)

    val p = processBuilder.start()
    p.waitFor()

    frame.clear()
    frame.panel.markAllForRedraw()
  }

  def editString(framePanel: FramePanel, stringToEdit: String, editorPath: String = "vi"): String = {


    val tmpFile = FilesManager.writeToTempFile(
      stringToEdit
    )


    val processBuilder = new ProcessBuilder(editorPath, tmpFile.getAbsolutePath)
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)

    val p = processBuilder.start()
    p.waitFor()

    framePanel.markAllForRedraw()

    FilesManager.readFile(tmpFile)
  }
}
