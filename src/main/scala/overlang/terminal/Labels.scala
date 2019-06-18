package overlang.terminal

import net.team2xh.onions.Themes.ColorScheme
import net.team2xh.onions.utils.TextWrap
import net.team2xh.onions.utils.TextWrap.ALIGN_LEFT
import net.team2xh.scurses.{Colors, Keys, Scurses}
import overlang.ActiveFile
import overlang.stackOverflowBackend.{StackOverflowAnswer, StackOverflowConnection, StackOverflowParser, StackOverflowQuestion}

case class Labels(parent: FramePanel, text: String,
                  widthFun: () => Int, heightFun: () => Int,
                  offsetXFun: () => Int, offsetYFun: () => Int)
                 (implicit screen: Scurses) extends Widget(parent) {



  def wrapText(text: String, width: Int, alignment: Int = ALIGN_LEFT): Seq[String] = {
    text.split("\n")
      .map {
        case "" => " "
        case x => x
      }
      .flatMap(l => l.grouped(width).toList)
  }


  override def redraw(focus: Boolean, theme: ColorScheme): Unit = {
    val lines = wrapText(text, innerWidth - 1, TextWrap.ALIGN_LEFT)
    for ((line, i) <- lines.zipWithIndex) {
      screen.put(0, i, " " + line + " " * (innerWidth - line.length - 1), Colors.BRIGHT_WHITE, Colors.DIM_BLACK)
    }
  }


  override def focusable = false

  override def offsetX: Int = this.offsetXFun()

  override def offsetY: Int = this.offsetYFun()

  override def innerWidth: Int = widthFun()

  override def innerHeight: Int = heightFun()
}