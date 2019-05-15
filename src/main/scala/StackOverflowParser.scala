import io.circe.parser.parse
import io.circe.{Decoder, HCursor, Json}

import scala.util.matching.Regex

object StackOverflowParser {

  implicit val StackOverflowAnswerDecoder: Decoder[StackOverflowAnswer] = (c: HCursor) => for {
    id <- c.get[Int]("answer_id")
    score <- c.get[Int]("score")
    body <- c.get[String]("body")
    tags <- c.get[List[String]]("tags")
  } yield
    new StackOverflowAnswer(id, score, parseAnswerBodyToListOfCode(body), tags)

  def parseAnswerBodyToListOfCode(body: String): List[String] = {
    val codeRegex: Regex = "(?<=<pre><code>)(?s).*(?=</code></pre>)".r
    codeRegex.findAllMatchIn(body).map(b => b.toString).toList
  }


  def parseResponseToAnswer(response: String): StackOverflowAnswer = {
    val parsed = parse(response).getOrElse(Json.Null)
    val maybeAnswers: Option[Json] =
      parsed.hcursor.downField("items").focus

    maybeAnswers match {
      case None => throw new RuntimeException("error while parsing")
      case Some(answers) =>
        answers.hcursor.as[List[StackOverflowAnswer]] match {
          case Right(answersList) => if (answersList.isEmpty) throw new StackOverflowWrongIdException else answersList.head
          case Left(error) => throw new RuntimeException(error)
        }

    }
  }

}
