//Based on: https://github.com/Tenchi2xh/Scurses/blob/master/onions/src/main/scala/net/team2xh/onions/components/Frame.scala

package overlang.terminal

import net.team2xh.scurses.{Keys, Scurses}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class FrameManager(implicit screen: Scurses) {
  var frames: ArrayBuffer[Frame] = mutable.ArrayBuffer[Frame]()

  def add(frame: Frame): Unit = {
    frame.onAttach(this)
    frame.resize(size)
    frames += frame
  }

  def show(): Unit = {
    size = screen.size
    eventLoop()
  }

  var size: (Int, Int) = (0, 0)

  private def eventLoop(): Unit = {
    size = screen.size
    frames.foreach(a => a.resize(size))


    if (frames.nonEmpty) {
      frames.last.beforeDraw()
      frames.last.redraw()
    }

    while (true) {
      var k = -1
      if (System.in.available() > 0)
        k = screen.keypress()
      else
        Thread.sleep(20)

      if (k == Keys.RESIZE) {
        size = screen.size
        frames.foreach(a => {
          a.resize(size)
          a.clear()
          a.panel.markAllForRedraw()
        })
      } else if (k == Keys.CTRL_C) {
        return
      }

      if (frames.nonEmpty) {
        val frame = frames.last
        if (k != -1)
          frame.event(k)
        else
          frame.redraw()
        frames = frames.filter(a => !a.toDelete)
        if (frames.nonEmpty && frame != frames.last) {
          frames.last.beforeDraw()
          frames.last.redraw()
        }
      } else {
        return
      }
    }

  }


}
