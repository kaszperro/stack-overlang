import net.team2xh.scurses.Scurses
import terminal.Frame

object ChooseSnippetView {
  def run() {
    Scurses { implicit screen =>
      val frame = Frame(Some("Stack-Overlang"))
      frame.show()
    }
  }
}
