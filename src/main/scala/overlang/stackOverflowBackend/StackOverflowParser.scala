package overlang.stackOverflowBackend

import io.circe.parser.parse
import io.circe.{Decoder, DecodingFailure, HCursor, Json}
import overlang.terminal.SearchResult

import scala.util.matching.Regex

object StackOverflowParser {
  implicit val StackOverflowAnswerDecoder: Decoder[StackOverflowAnswer] = (c: HCursor) => for {
    id <- c.get[Int]("answer_id")
    score <- c.get[Int]("score")
    body <- c.get[String]("body")
    tags <- c.get[List[String]]("tags")

  } yield
    new StackOverflowAnswer(id, score, parseAnswerBodyToListOfCode(body), tags)


  implicit val StackOverflowQuestionDecoder: Decoder[StackOverflowQuestion] = (c: HCursor) =>
    for {
      id <- c.get[Int]("question_id")
      title <- c.get[String]("title")
      score <- c.get[Int]("question_score")
    } yield
      new StackOverflowQuestion(id, title, score)


  implicit val SearchResultDecoder: Decoder[SearchResult] = (c: HCursor) => {
    for {
      res_type <- c.get[String]("item_type")
    } yield res_type match {
      case "answer" => parseAnswerResponseToAnswer(
        StackOverflowConnection.getAnswerAsString(c.get[Int]("answer_id").right.get)
      )
      case "question" => c.as[StackOverflowQuestion].right.get
    }
  }


  def parseAnswerBodyToListOfCode(body: String): List[String] = {
    val codeRegex: Regex = "(?<=<pre.*><code>)(?s).*?(?=</code></pre>)".r
    val preHTML = codeRegex.findAllMatchIn(body).map(b => b.toString).toList

    preHTML.map(s => s.replaceAll("&lt;", "<").replaceAll("&gt;", ">"))
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

  def parseSearchResponseToListOfResults(response: String): List[SearchResult] = {
    val decodeSearchResults = Decoder[List[SearchResult]].prepare(
      _.downField("items")
    )
    io.circe.parser.decode(response)(decodeSearchResults).right.get
  }

  def parseSearchResponseToListOfQuestions(response: String): List[StackOverflowQuestion] = {
    val parsed = parse(response).getOrElse(Json.Null)

    val answersFiltered = parsed.hcursor.downField("items").downArray.rights.get
    answersFiltered.map(
      j => j.as[StackOverflowQuestion].right.get
      //new StackOverflowQuestion(j.hcursor.get[Int]("question_id").right.get, j.hcursor.get[String]("title").right.get)

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
