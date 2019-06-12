package overlang.terminal

import net.team2xh.onions.Symbols
import net.team2xh.onions.Themes.ColorScheme
import net.team2xh.scurses.{Colors, Scurses}

class TestWidget(parent: FramePanel, color: Int,
                 widthFun: () => Int, heightFun: () => Int,
                 offsetXFun: () => Int, offsetYFun: () => Int)
                (implicit screen: Scurses) extends Widget(parent) {
  override def redraw(focus: Boolean, theme: ColorScheme): Unit = {
    val width = innerWidth
    val realColor = if (focus) Colors.BRIGHT_GREEN else color
    for (x <- 0 until width) {
      for (y <- 0 until innerHeight) {
        screen.put(x, y, Symbols.BLOCK_UPPER, realColor, realColor)
      }
    }
  }

  override def handleKeypress(keypress: Int): Unit = {}

  override def focusable: Boolean = true

  override def innerHeight: Int = heightFun()

  override def innerWidth: Int = widthFun()

  override def offsetX: Int = this.offsetXFun()

  override def offsetY: Int = this.offsetYFun()


}
