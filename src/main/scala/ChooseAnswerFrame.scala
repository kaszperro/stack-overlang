import net.team2xh.scurses.Scurses
import terminal.{Frame, Labels}


class ChooseAnswerFrame(array: Array[StackOverflowAnswer])(implicit screen: Scurses) extends Frame(Some("Choose answer")) {
  val textExtractor: StackOverflowAnswer => String = s => {
    var string = s.codeBlocks.reduce((a, b) => a + b + "\n")
    string = string.substring(0, math.min(200, string.length))
    string = string.concat("\n[...]")
    string
  }
  new Labels[StackOverflowAnswer](panel, array, textExtractor,
    () => panel.innerWidth, () => panel.innerHeight,
    () => 0, () => 0, _ => Unit)
}
