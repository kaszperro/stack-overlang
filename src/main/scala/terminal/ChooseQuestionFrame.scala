package terminal

import net.team2xh.scurses.Scurses

import stackOverflowBackend.{StackOverflowConnection, StackOverflowParser, StackOverflowQuestion}


class ChooseQuestionFrame(array: Array[StackOverflowQuestion])(implicit screen: Scurses) extends Frame(Some("Choose question")) {
  Labels(panel, array,
    () => panel.innerWidth, () => panel.innerHeight,
    () => 0, () => 0)
}
