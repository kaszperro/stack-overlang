//Based on: https://github.com/Tenchi2xh/Scurses/blob/master/onions/src/main/scala/net/team2xh/onions/components/Widget.scala

package overlang.terminal

import net.team2xh.onions.Component
import net.team2xh.onions.Themes.ColorScheme
import net.team2xh.onions.utils.Varying
import net.team2xh.scurses.Scurses

abstract class Widget(parent: FramePanel, values: Varying[_]*)
                     (implicit screen: Scurses) extends Component(Some(parent)) {


  parent.addWidget(this)

  for (value <- values)
    value.subscribe(() => {
      needsRedraw = true
    })

  def focusable: Boolean

  var needsRedraw = true

  def draw(focus: Boolean, theme: ColorScheme): Unit = {
    redraw(focus, theme)
    needsRedraw = false
  }

  def redraw(focus: Boolean, theme: ColorScheme): Unit

  def handleKeypress(keypress: Int): Unit = Unit

  def handleArrowPress(k: Int): Boolean = false


  override def innerWidth: Int = parent.innerWidth

  override def innerHeight: Int = parent.innerHeight

  def offsetX: Int = 0

  def offsetY: Int = 0

  override def toString: String = this.getClass.getSimpleName + " @ " + Integer.toHexString(System.identityHashCode(this))
}