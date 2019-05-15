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
    val maybeAnswer: Option[Json] = parsed.hcursor.downField("items").downArray.focus

    maybeAnswer match {
      case None => throw new StackOverflowWrongIdException
      case Some(answer) => answer.as[StackOverflowAnswer].getOrElse(throw new RuntimeException)
    }
  }

  def parseResponseToListOfCode(response: String): List[String] = {
    val parsed = parse(response).getOrElse(Json.Null)

    parseAnswerBodyToListOfCode(
      parsed.hcursor
        .downField("items").downArray.get[String]("body")
        .getOrElse(throw new StackOverflowWrongIdException())
    )
  }


}
