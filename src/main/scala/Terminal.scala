import net.team2xh.onions.components.Frame
import net.team2xh.onions.components.widgets.Input
import net.team2xh.scurses.Scurses


object Terminal extends App {
  Scurses { implicit screen =>
    val frame = Frame(Some("Stack-Overlang"))


    val input = Input(frame.panel)




    // Display and launch event loop
    frame.show()
  }
}
