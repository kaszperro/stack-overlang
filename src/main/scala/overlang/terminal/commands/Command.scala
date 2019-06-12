package overlang.terminal.commands

trait Command {
  def run(): Unit

  def canRun(string: String): Boolean
}
