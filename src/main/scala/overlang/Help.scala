package overlang

object Help {
  def text: String =
    "add <answer_id> - adds answer by id\n" +
      "search <question> searches questions and answers\n" +
      "saveas <filename> - saves actual file\n" +
      "runwith <program> - runs with selected program\n" +
      "edit - launches actual file in vi text editor"

}
