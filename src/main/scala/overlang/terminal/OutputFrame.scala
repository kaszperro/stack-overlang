package overlang.terminal

import net.team2xh.scurses.Scurses


class OutputFrame(output: String)(implicit screen: Scurses) extends Frame(Some("Program output")) {
  ScrollableLabel(panel, output,
    () => panel.innerWidth, () => panel.innerHeight,
    () => 0, () => 0)
}
