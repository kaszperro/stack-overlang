import net.team2xh.scurses.Scurses
import terminal.{Frame, Labels}


class ChooseQuestionFrame(array: Array[StackOverflowQuestion])(implicit screen: Scurses) extends Frame(Some("Choose question")) {
  new Labels[StackOverflowQuestion](panel, array, a => a.title,
    () => panel.innerWidth, () => panel.innerHeight,
    () => 0, () => 0, e => {
      val res = StackOverflowConnection.getAnswersByQuestionId(e.id)
      val ans = StackOverflowParser.parseAnswersResponseToListOfAnswers(res)
      getStack.add(new ChooseAnswerFrame(ans.filter(a => a.codeBlocks.nonEmpty).toArray))
    })
}
