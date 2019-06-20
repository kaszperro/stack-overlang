package overlang.terminal.commands

import overlang.terminal.{Frame, Labels}

abstract class Command(val rootFrame: Frame, val label: Labels) {
  def run(command: String): Boolean
  def help : String
}
