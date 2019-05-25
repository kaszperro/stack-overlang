package terminal


import net.team2xh.onions.Component
import net.team2xh.onions.Themes.ColorScheme
import net.team2xh.onions.components.{FramePanel, Widget}
import net.team2xh.onions.utils.TextWrap.ALIGN_LEFT
import net.team2xh.onions.utils.{TextWrap, Varying}
import net.team2xh.scurses.{Colors, Keys, Scurses}

case class Labels[A](parent: FramePanel, elements: Array[A], textExtractor: A => String, textPacker: String => A,
                     widthFun: () => Int, heightFun: () => Int,
                     offsetXFun: () => Int, offsetYFun: () => Int)
                    (implicit screen: Scurses) extends Widget(parent) {

  private var actualFocusedElement = 0
  private var firstElementIndex = 0
  private var lastElementIndex = -1
  private var firstLine = 0


  def wrapText(text: String, width: Int, alignment: Int = ALIGN_LEFT): Seq[String] = {
    TextWrap.wrapText(text, width, alignment)
      .flatMap(l => l.grouped(width).toList)
  }


  override def redraw(focus: Boolean, theme: ColorScheme): Unit = {
    var wrappedLines = 0
    for (i <- elements.indices) {
      val lines = wrapText(textExtractor(elements(i)), innerWidth - 1, TextWrap.ALIGN_LEFT)
      for (line <- lines) {
        if (wrappedLines == firstLine) firstElementIndex = i
        val draw = wrappedLines >= firstLine
        if (draw) {
          val color = if (i == actualFocusedElement) Colors.BRIGHT_GREEN else Colors.BRIGHT_RED
          val color2 = if (focus) Colors.DIM_BLUE else Colors.DIM_MAGENTA
          screen.put(0, wrappedLines - firstLine, " " + line + " " * (innerWidth - line.length - 1), color, color2)
        }
        wrappedLines = wrappedLines + 1
        lastElementIndex = i
        if (wrappedLines - firstLine >= innerHeight)
          return
      }
    }
  }

  private def getFirstLineOfElement(index: Int): Int = {
    var wrappedLines = 0
    for (i <- elements.indices) {
      val lines = wrapText(textExtractor(elements(i)), innerWidth - 1, TextWrap.ALIGN_LEFT)
      if (i == index) return wrappedLines
      for (_ <- lines) {
        wrappedLines = wrappedLines + 1
      }
    }
    -1
  }

  private def getLinesSize(index: Int): Int = {
    var wrappedLines = 0
    val lines = wrapText(textExtractor(elements(index)), innerWidth - 1, TextWrap.ALIGN_LEFT)
    for (_ <- lines) {
      wrappedLines = wrappedLines + 1
    }
    wrappedLines
  }

  override def handleArrowPress(keypress: Int): Boolean = {
    var handled = false
    if (keypress == Keys.UP) {
      if (actualFocusedElement > 0) {
        actualFocusedElement = actualFocusedElement - 1
        handled = true
      }
      if (actualFocusedElement <= firstElementIndex) {
        firstLine = getFirstLineOfElement(actualFocusedElement)
      }
    }
    if (keypress == Keys.DOWN) {
      if (actualFocusedElement < elements.length - 1) {
        actualFocusedElement = actualFocusedElement + 1
        handled = true
      }
      if (actualFocusedElement >= lastElementIndex) {
        firstLine = getFirstLineOfElement(actualFocusedElement) - (innerHeight - getLinesSize(actualFocusedElement))
        if (firstLine < 0) firstLine = 0
      }
    }
    if (keypress == Keys.UP || keypress == Keys.DOWN)
      needsRedraw = true

    handled
  }


  override def handleKeypress(keypress: Int): Unit = {
    if (keypress == Keys.ENTER) {

      val myString = ExternalEditor.editString(
        parent,
        textExtractor(elements(actualFocusedElement))
      )

      elements.update(actualFocusedElement, textPacker(myString))
      parent.needsClear = true
    }

  }

  override def focusable = true

  override def offsetX: Int = this.offsetXFun()

  override def offsetY: Int = this.offsetYFun()

  override def innerWidth: Int = widthFun()

  override def innerHeight: Int = heightFun()
}