//Based on: https://github.com/Tenchi2xh/Scurses/blob/master/onions/src/main/scala/net/team2xh/onions/components/widgets/Label.scala

package overlang.terminal

import net.team2xh.onions.Themes.ColorScheme
import net.team2xh.scurses.{Colors, Keys, Scurses}

case class ScrollableLabel(parent: FramePanel, text: String,
                           widthFun: () => Int, heightFun: () => Int,
                           offsetXFun: () => Int, offsetYFun: () => Int)
                          (implicit screen: Scurses) extends Widget(parent) {

  private var firstLine = 0
  private var lines: Seq[String] = _


  def wrapText(text: String, width: Int): Seq[String] = {
    text.split("\n")
      .map {
        case "" => " "
        case x => x
      }
      .flatMap(l => l.grouped(width).toList)
  }


  private def checkFirstLine(): Unit = {
    if (firstLine >= lines.size - innerHeight)
      firstLine = lines.size - innerHeight
    if (firstLine < 0) firstLine = 0
  }

  override def redraw(focus: Boolean, theme: ColorScheme): Unit = {
    var wrappedLines = 0
    lines = wrapText(text, innerWidth - 1)
    checkFirstLine()
    for (line <- lines) {
      val draw = wrappedLines >= firstLine
      if (draw) {
        screen.put(0, wrappedLines - firstLine, " " + line + " " * (innerWidth - line.length - 1),
          Colors.BRIGHT_WHITE, Colors.DIM_BLACK)
      }
      wrappedLines = wrappedLines + 1
      if (wrappedLines - firstLine >= innerHeight)
        return

    }
  }

  override def handleArrowPress(keypress: Int): Boolean = {
    var handled = false
    if (keypress == Keys.UP) {
      if (firstLine > 0) firstLine -= 1
    }
    if (keypress == Keys.DOWN) {
      if (firstLine < lines.size - innerHeight)
        firstLine += 1
    }
    if (keypress == Keys.UP || keypress == Keys.DOWN) {
      handled = true
      needsRedraw = true
    }
    handled
  }


  override def focusable = true

  override def offsetX: Int = this.offsetXFun()

  override def offsetY: Int = this.offsetYFun()

  override def innerWidth: Int = widthFun()

  override def innerHeight: Int = heightFun()
}