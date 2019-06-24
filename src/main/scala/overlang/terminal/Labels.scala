//Based on: https://github.com/Tenchi2xh/Scurses/blob/master/onions/src/main/scala/net/team2xh/onions/components/widgets/Label.scala

package overlang.terminal

import net.team2xh.onions.Themes.ColorScheme
import net.team2xh.onions.utils.TextWrap
import net.team2xh.onions.utils.TextWrap.ALIGN_LEFT
import net.team2xh.scurses.{Colors, Keys, Scurses}
import overlang.ActiveFile
import overlang.stackOverflowBackend.{StackOverflowAnswer, StackOverflowConnection, StackOverflowParser, StackOverflowQuestion}

case class Labels(parent: FramePanel, var text: String,
                  widthFun: () => Int, heightFun: () => Int,
                  offsetXFun: () => Int, offsetYFun: () => Int)
                 (implicit screen: Scurses) extends Widget(parent) {

  private var color1: Int = Colors.BRIGHT_WHITE
  private var color2: Int = Colors.DIM_BLACK


  def setColor(color1: Int, color2: Int): Unit = {
    this.color1 = color1
    this.color2 = color2
    needsRedraw = true
  }

  def setText(color: Int, text: String): Unit = {
    this.synchronized {
      this.color1 = color
      this.text = text
      needsRedraw = true
    }
  }

  private def wrapText(text: String, width: Int, alignment: Int = ALIGN_LEFT)= {
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
      screen.put(0, i, " " + line + " " * (innerWidth - line.length - 1), color1, color2)
    }
  }


  override def focusable = false

  override def offsetX: Int = this.offsetXFun()

  override def offsetY: Int = this.offsetYFun()

  override def innerWidth: Int = widthFun()

  override def innerHeight: Int = heightFun()
}