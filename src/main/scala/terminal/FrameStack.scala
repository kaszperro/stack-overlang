package terminal

import net.team2xh.scurses.{Keys, Scurses}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class FrameStack(implicit screen: Scurses) {
  var frames: ArrayBuffer[Frame] = mutable.ArrayBuffer[Frame]()

  def add(frame: Frame): Unit = {
    frames += frame
  }

  def show(): Unit = {
    eventLoop()
  }

  private def eventLoop(): Unit = {
    if (frames.nonEmpty) {
      frames.last.beforeDraw()
      frames.last.redraw()
    }
    var running = true
    while (running) {
      val k = screen.keypress()
      if (frames.nonEmpty) {
        val frame = frames.last
        frames.last.event(k)
        frames = frames.filter(a => !a.toDelete)
        if (frames.nonEmpty && frame != frames.last) {
          frames.last.beforeDraw()
          frames.last.redraw()
        }
      } else {
        running = false
      }
    }

  }


}
