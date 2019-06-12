package terminal

import net.team2xh.scurses.Scurses
import stackOverflowBackend.StackOverflowAnswer


class ChooseAnswerFrame(array: Array[StackOverflowAnswer])(implicit screen: Scurses) extends Frame(Some("Choose answer")) {
  Labels(panel, array,
    () => panel.innerWidth, () => panel.innerHeight,
    () => 0, () => 0)
}
