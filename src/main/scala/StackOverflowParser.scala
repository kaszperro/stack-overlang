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
    val codeRegex: Regex = "(?<=<pre><code>)(?s).*?(?=</code></pre>)".r
    codeRegex.findAllMatchIn(body).map(b => b.toString).toList
  }


  def parseAnswerResponseToAnswer(response: String): StackOverflowAnswer = {
    val parsed = parse(response).getOrElse(Json.Null)
    val maybeAnswer: Option[Json] = parsed.hcursor.downField("items").downArray.focus

    maybeAnswer match {
      case None => throw new StackOverflowWrongIdException
      case Some(answer) => answer.as[StackOverflowAnswer].right.get
    }
  }

  def parseAnswerResponseToListOfCode(response: String): List[String] = {
    val parsed = parse(response).getOrElse(Json.Null)

    parseAnswerBodyToListOfCode(
      parsed.hcursor
        .downField("items").downArray.get[String]("body")
        .getOrElse(throw new StackOverflowWrongIdException())
    )
  }

  def parseSearchResponseToListOfAnswers(response: String): List[StackOverflowAnswer] = {
    val parsed = parse(response).getOrElse(Json.Null)

    val answersFiltered = parsed.hcursor.downField("items").downArray.rights.get.filter(
      j => j.hcursor.get[String]("item_type").right.get == "answer"
    )

    answersFiltered.map(
      j => {
        parseAnswerResponseToAnswer(
          StackOverflowConnection.getAnswerAsString(j.hcursor.get[Int]("answer_id").right.get)
        )
      }
    ).toList

  }

  def parseSearchResponseToListOfQuestions(response: String): List[StackOverflowQuestion] = {
    val parsed = parse(response).getOrElse(Json.Null)

    val answersFiltered = parsed.hcursor.downField("items").downArray.rights.get
    answersFiltered.map(
      j => {
        new StackOverflowQuestion(j.hcursor.get[Int]("question_id").right.get, j.hcursor.get[String]("title").right.get)
      }
    ).toList

  }

  def parseAnswersResponseToListOfAnswers(response: String): List[StackOverflowAnswer] = {
    val parsed = parse(response).getOrElse(Json.Null)

    val answersFiltered = parsed.hcursor.downField("items").downArray.rights.get

    answersFiltered.map(
      j => {
        parseAnswerResponseToAnswer(
          StackOverflowConnection.getAnswerAsString(j.hcursor.get[Int]("answer_id").right.get)
        )
      }
    ).toList
  }

}
