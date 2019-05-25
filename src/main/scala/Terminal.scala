import net.team2xh.scurses.{Colors, Scurses}
import terminal.{Frame, Input, Labels, TestWidget}

import scala.collection.mutable.ListBuffer


object Terminal extends App {
  Scurses { implicit screen =>
    val frame = Frame(Some("Stack-Overlang"))


    //    new TestWidget(frame.panel, Colors.DIM_RED,
    //      () => frame.panel.innerWidth / 2, () => frame.panel.innerHeight / 2,
    //      () => 0, () => 0)
    //
    //    new TestWidget(frame.panel, Colors.DIM_BLUE,
    //      () => frame.panel.innerWidth / 2, () => frame.panel.innerHeight / 2,
    //      () => frame.panel.innerWidth / 4, () => frame.panel.innerHeight / 4)
    //
    //    Input(frame.panel, "text",
    //      widthFun = () => frame.panel.innerWidth, heightFun = () => 1,
    //      offsetYFun = () => frame.panel.innerHeight - 1)

    var testString = "Siemanko testowy tekst"
    var testString2 = "Siemanko testowy tekst długi bardzo ijdsiadjaoisdjoisajdoiasjdoiajsoidaisodhasoiudhoiashdioashdoiasi"

    var listBuff = new ListBuffer[String]
    for (i <- 1 to 20) {
      listBuff += i + ". " + testString
      listBuff += i + ". " + testString2
    }

    val array = listBuff.toArray

    new Labels[String](frame.panel, array, s => s, s => s,
      () => frame.panel.innerWidth, () => frame.panel.innerHeight - 1,
      () => 0, () => 0)

    Input(frame.panel, "text",
      widthFun = () => frame.panel.innerWidth, heightFun = () => 1,
      offsetYFun = () => frame.panel.innerHeight - 1)


    frame.show()

  }
}
